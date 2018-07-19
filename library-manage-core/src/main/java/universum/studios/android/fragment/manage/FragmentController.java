/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.fragment.manage;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;
import android.transition.Transition;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import universum.studios.android.fragment.FragmentPolicies;
import universum.studios.android.fragment.FragmentsLogging;
import universum.studios.android.fragment.util.FragmentUtils;

/**
 * FragmentController class is designed primarily to simplify {@link Fragment Fragments} management
 * within an Android application.
 * <p>
 * Whether it is desired to replace|add|remove|show|hide|attach|detach a specific {@link Fragment},
 * this may be requested via {@link FragmentRequest}. A new request may be created via {@link #newRequest(Fragment)}.
 * Each fragment request is associated with the controller trough which it has been created and that
 * controller is responsible for execution of fragment request when {@link FragmentRequest#execute()}
 * is called.
 *
 * <h3>Factory</h3>
 * The best advantage of the FragmentController and globally of this library may be accomplished by
 * using of {@link FragmentFactory} attached to the desired fragment controller. Basically in an
 * Android application you will use directly instances of the FragmentController to replace|add|hide...
 * fragments and for each of that application screens (Activities) a single FragmentFactory may be
 * defined which will provide fragment instances for that activity screen. For example, one fragment
 * factory for main activity with <b>navigation drawer</b> where that factory will provide fragments
 * for each of the items within the navigation menu. Than another fragment factory for profile activity
 * and that factory will provide all fragments used within that activity, like fragment for displaying
 * of user's info an another one for editing that info.
 * <p>
 * The desired factory for FragmentController may be specified via {@link #setFactory(FragmentFactory)}.
 * Fragment requests for fragments provided by the attached factory may be than created via {@link #newRequest(int)}.
 * <b>Note, that it is required that factory is attached to the controller before call to this method,
 * otherwise an exception will be thrown.</b>
 * <p>
 * Fragments that are provided by factory attached to the fragment controller may be found via
 * {@link #findFragmentByFactoryId(int)} (when already displayed) by theirs corresponding id defined
 * in the related factory.
 *
 * <h3>Callbacks</h3>
 * If you want to listen for fragment request executions, a desired {@link OnRequestListener} may
 * be attached to the FragmentController via {@link #registerOnRequestListener(OnRequestListener)}.
 * <p>
 * If you want to listen for changes in the fragments back stack whenever a new fragment is added to
 * the stack or an old one removed from the stack, a desired {@link OnBackStackChangeListener} may
 * be attached to the FragmentController via {@link #registerOnBackStackChangeListener(OnBackStackChangeListener)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see FragmentFactory
 * @see FragmentRequest
 */
public class FragmentController {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "FragmentController";

	/**
	 * Default TAG used for fragments.
	 */
	public static final String FRAGMENT_TAG = FragmentPolicies.class.getPackage().getName() + ".TAG.Fragment";

	/**
	 * Constant used to determine that no view container id is specified.
	 */
	public static final int NO_CONTAINER_ID = -1;

	/**
	 * Flag indicating whether we can attach transitions to a fragment instance at the current Android
	 * API level or not.
	 */
	private static final boolean CAN_ATTACH_TRANSITIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Listener that may be used to receive callback about executed {@link FragmentRequest}.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 *
	 * @see #registerOnRequestListener(OnRequestListener)
	 */
	public interface OnRequestListener {

		/**
		 * Invoked whenever the specified <var>request</var> has been executed.
		 *
		 * @param request The executed fragment request.
		 *
		 * @see FragmentRequest#execute()
		 */
		void onRequestExecuted(@NonNull FragmentRequest request);
	}

	/**
	 * Listener that may be used to receive a callback about changes in the fragments back stack.
	 * The callback is fired whenever a new fragment is added into the back stack or an old fragment
	 * is removed from the back stack.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 *
	 * @see #registerOnBackStackChangeListener(OnBackStackChangeListener)
	 */
	public interface OnBackStackChangeListener {

		/**
		 * Invoked whenever fragments back stack change occur.
		 *
		 * @param backStackEntry The back stack entry that was added into back stack or removed from it.
		 * @param added          {@code True} if the entry has been added, {@code false} if removed.
		 */
		void onFragmentsBackStackChanged(@NonNull FragmentManager.BackStackEntry backStackEntry, boolean added);
	}

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Listener used to listen for changes in fragments back stack.
	 */
	private final FragmentManager.OnBackStackChangedListener backStackChangeListener;

	/**
	 * Fragment manager used to perform fragments related operations.
	 */
	@SuppressWarnings("WeakerAccess")
	final FragmentManager manager;

	/**
	 * Context used to check whether custom fragment animations will be played by the Android framework
	 * or not. This context may be {@code null} if {@link #FragmentController(Fragment)} constructor
	 * is used.
	 *
	 * @see #createTransaction(FragmentRequest)
	 */
	private final Context context;

	/**
	 * Id of a view container where to place view hierarchies of the desired fragments.
	 */
	private int viewContainerId = NO_CONTAINER_ID;

	/**
	 * Factory that provides fragment instances for this controller.
	 */
	private FragmentFactory factory;

	/**
	 * Interceptor that may be used to intercept an execution of a particular {@link FragmentRequest}
	 * when its execution has been requested via {@link #executeRequest(FragmentRequest)}.
	 */
	private FragmentRequestInterceptor requestInterceptor;

	/**
	 * List of listener callbacks registered for fragment requests.
	 */
	private List<OnRequestListener> requestListeners;

	/**
	 * List of listener callbacks registered for back stack changes.
	 */
	private List<OnBackStackChangeListener> backStackChangeListeners;

	/**
	 * Entry that is at the top of the fragments back stack.
	 */
	private FragmentManager.BackStackEntry topBackStackEntry;

	/**
	 * Boolean flag indicating whether this controller has been destroyed or not.
	 */
	private boolean destroyed;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of FragmentController for the given <var>parentActivity</var>.
	 * <p>
	 * Passed activity will be used to obtain an instance of {@link FragmentManager} for the new
	 * controller.
	 * <p>
	 * This constructor attaches the given activity to the new controller as one of interfaces
	 * listed below if the activity implements listed interfaces respectively:
	 * <ul>
	 * <li>{@link FragmentRequestInterceptor} -&gt; {@link #setRequestInterceptor(FragmentRequestInterceptor)}</li>
	 * <li>{@link OnRequestListener} -&gt; {@link #registerOnRequestListener(OnRequestListener)}</li>
	 * <li>{@link OnBackStackChangeListener} -&gt; {@link #registerOnBackStackChangeListener(OnBackStackChangeListener)}</li>
	 * </ul>
	 *
	 * @param parentActivity The activity that wants to use the new fragment controller.
	 *
	 * @see #FragmentController(Context, FragmentManager)
	 * @see #FragmentController(Fragment)
	 */
	public FragmentController(@NonNull final Activity parentActivity) {
		this(parentActivity, parentActivity.getFragmentManager());
		if (parentActivity instanceof FragmentRequestInterceptor) {
			setRequestInterceptor((FragmentRequestInterceptor) parentActivity);
		}
		if (parentActivity instanceof OnRequestListener) {
			registerOnRequestListener((OnRequestListener) parentActivity);
		}
		if (parentActivity instanceof OnBackStackChangeListener) {
			registerOnBackStackChangeListener((OnBackStackChangeListener) parentActivity);
		}
	}

	/**
	 * Creates a new instance of FragmentController for the given <var>parentFragment</var>.
	 * <p>
	 * Passed fragment will be used to obtain an instance of {@link FragmentManager} for the new
	 * controller.
	 * <p>
	 * This constructor attaches the given fragment to the new controller as one of interfaces
	 * listed below if the fragment implements listed interfaces respectively:
	 * <ul>
	 * <li>{@link FragmentRequestInterceptor} -&gt; {@link #setRequestInterceptor(FragmentRequestInterceptor)}</li>
	 * <li>{@link OnRequestListener} -&gt; {@link #registerOnRequestListener(OnRequestListener)}</li>
	 * <li>{@link OnBackStackChangeListener} -&gt; {@link #registerOnBackStackChangeListener(OnBackStackChangeListener)}</li>
	 * </ul>
	 * <p>
	 * <b>Do not forget to destroy the new controller via {@link #destroy()} when the fragment is
	 * also destroyed.</b>
	 *
	 * @param parentFragment The fragment that wants to use the new fragment controller.
	 *
	 * @see #FragmentController(Context, FragmentManager)
	 * @see #FragmentController(Activity)
	 */
	public FragmentController(@NonNull final Fragment parentFragment) {
		this(parentFragment.getActivity(), parentFragment.getFragmentManager());
		if (parentFragment instanceof FragmentRequestInterceptor) {
			setRequestInterceptor((FragmentRequestInterceptor) parentFragment);
		}
		if (parentFragment instanceof OnRequestListener) {
			registerOnRequestListener((OnRequestListener) parentFragment);
		}
		if (parentFragment instanceof OnBackStackChangeListener) {
			registerOnBackStackChangeListener((OnBackStackChangeListener) parentFragment);
		}
	}

	/**
	 * Creates a new instance of FragmentController with the given <var>fragmentManager</var>.
	 *
	 * @param fragmentManager Fragment manager that will be used to perform fragments related operations.
	 *
	 * @see #FragmentController(Activity)
	 * @see #FragmentController(Fragment)
	 * @see #FragmentController(Context, FragmentManager)
	 */
	public FragmentController(@NonNull final FragmentManager fragmentManager) {
		this(null, fragmentManager);
	}

	/**
	 * Creates a new instance of FragmentController with the given <var>context</var> and <var>fragmentManager</var>.
	 *
	 * @param context         Context used to resolve whether custom fragment animations (if specified)
	 *                        will be played or not. {@link FragmentUtils#willBeCustomAnimationsPlayed(Context)}.
	 * @param fragmentManager Fragment manager that will be used to perform fragments related operations.
	 *
	 * @see #FragmentController(Activity)
	 * @see #FragmentController(Fragment)
	 */
	public FragmentController(@Nullable final Context context, @NonNull final FragmentManager fragmentManager) {
		this.context = context;
		this.manager = fragmentManager;
		this.manager.addOnBackStackChangedListener(backStackChangeListener = new BackStackListener(this));
		final int n = manager.getBackStackEntryCount();
		if (n > 0) {
			this.topBackStackEntry = manager.getBackStackEntryAt(n - 1);
		}
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the fragment manager specified for this controller during its initialization.
	 *
	 * @return FragmentManager instance.
	 *
	 * @see #FragmentController(FragmentManager)
	 */
	@NonNull public FragmentManager getFragmentManager() {
		return manager;
	}

	/**
	 * Sets an id of a view container where to place view hierarchies of the desired fragments.
	 * <p>
	 * <b>Note</b>, that this container id is used to specify initial/default container id for all
	 * {@link FragmentRequest FragmentRequests} created via {@link #newRequest(Fragment)}
	 *
	 * @param containerId The desired view container id.
	 *
	 * @see #getViewContainerId()
	 */
	public void setViewContainerId(@IdRes final int containerId) {
		this.viewContainerId = containerId;
	}

	/**
	 * Returns id of the view container for fragment views.
	 *
	 * @return View container id or {@link #NO_CONTAINER_ID} if no id has been specified yet.
	 *
	 * @see #setViewContainerId(int)
	 */
	@IdRes public int getViewContainerId() {
		return viewContainerId;
	}

	/**
	 * Sets a fragment factory that should provide fragment instances for {@link FragmentRequest FragmentRequests}
	 * created via {@link #newRequest(int)}.
	 *
	 * @param factory The desired factory. May {@code null} to clear the current one.
	 *
	 * @see #getFactory()
	 * @see #hasFactory()
	 */
	public void setFactory(@Nullable final FragmentFactory factory) {
		this.factory = factory;
	}

	/**
	 * Checks whether this controller has fragment factory attached or not.
	 *
	 * @return {@code True} if factory is attached, {@code false} otherwise.
	 *
	 * @see #setFactory(FragmentFactory)
	 * @see #getFactory()
	 */
	public boolean hasFactory() {
		return factory != null;
	}

	/**
	 * Asserts that the factory has been attached to this controller. If no factory is attached,
	 * an exception is thrown.
	 */
	private void assertHasFactory() {
		if (factory == null) throw new IllegalStateException("No factory attached!");
	}

	/**
	 * Returns the current fragment factory attached to this controller.
	 *
	 * @return This controller's factory or {@code null} if there is no factory attached yet.
	 *
	 * @see #setFactory(FragmentFactory)
	 * @see #hasFactory()
	 */
	@Nullable public FragmentFactory getFactory() {
		return factory;
	}

	/**
	 * Sets an interceptor that may be used to intercept an execution of a {@link FragmentRequest}
	 * created via {@link #newRequest(Fragment)} when execution of that request has been requested
	 * via {@link FragmentRequest#execute()}.
	 *
	 * @param interceptor The desired interceptor. May be {@code null} to clear the current one.
	 */
	public void setRequestInterceptor(@Nullable final FragmentRequestInterceptor interceptor) {
		this.requestInterceptor = interceptor;
	}

	/**
	 * Registers a callback to be invoked when a {@link FragmentRequest} is executed via this
	 * controller.
	 * <p>
	 * Fragment request created via {@link #newRequest(Fragment)} is executed whenever its
	 * {@link FragmentRequest#execute()} is called and the associated controller does not have
	 * {@link FragmentRequestInterceptor} attached or the attached interceptor did not intercept
	 * execution of that particular request.
	 *
	 * @param listener The desired listener callback to be registered.
	 *
	 * @see #unregisterOnRequestListener(OnRequestListener)
	 */
	public void registerOnRequestListener(@NonNull final OnRequestListener listener) {
		if (requestListeners == null) this.requestListeners = new ArrayList<>(1);
		if (!requestListeners.contains(listener)) requestListeners.add(listener);
	}

	/**
	 * Notifies all registered {@link OnRequestListener OnRequestListeners} that the given
	 * <var>request</var> has been executed.
	 *
	 * @param request The request that has been just executed via {@link #executeRequest(FragmentRequest)}.
	 */
	@VisibleForTesting void notifyRequestExecuted(final FragmentRequest request) {
		if (requestListeners != null && !requestListeners.isEmpty()) {
			for (final OnRequestListener listener : requestListeners) {
				listener.onRequestExecuted(request);
			}
		}
	}

	/**
	 * Un-registers the given callback from the registered request listeners.
	 *
	 * @param listener The desired listener callback to be un-registered.
	 *
	 * @see #registerOnRequestListener(OnRequestListener)
	 */
	public void unregisterOnRequestListener(@NonNull final OnRequestListener listener) {
		if (requestListeners != null) requestListeners.remove(listener);
	}

	/**
	 * Registers a callback to be invoked when some change occurs in the fragments back stack.
	 *
	 * @param listener The desired listener callback to be registered.
	 *
	 * @see #unregisterOnBackStackChangeListener(OnBackStackChangeListener)
	 * @see FragmentManager#addOnBackStackChangedListener(FragmentManager.OnBackStackChangedListener)
	 */
	public void registerOnBackStackChangeListener(@NonNull final OnBackStackChangeListener listener) {
		if (backStackChangeListeners == null) this.backStackChangeListeners = new ArrayList<>(1);
		if (!backStackChangeListeners.contains(listener)) backStackChangeListeners.add(listener);
	}

	/**
	 * Notifies all registered {@link OnBackStackChangeListener OnBackStackChangeListeners} that the
	 * given <var>changedEntry</var> was added or removed from the back stack.
	 *
	 * @param changedEntry The back stack entry that was changed.
	 * @param added        {@code True} if the specified entry was added to the back stack,
	 *                     {@code false} if it was removed.
	 */
	@VisibleForTesting void notifyBackStackEntryChange(final FragmentManager.BackStackEntry changedEntry, final boolean added) {
		if (backStackChangeListeners != null && !backStackChangeListeners.isEmpty()) {
			for (final OnBackStackChangeListener listener : backStackChangeListeners) {
				listener.onFragmentsBackStackChanged(changedEntry, added);
			}
		}
	}

	/**
	 * Un-registers the given callback from the registered back stack change listeners.
	 *
	 * @param listener The desired listener callback to be un-registered.
	 *
	 * @see #registerOnBackStackChangeListener(OnBackStackChangeListener)
	 */
	public void unregisterOnBackStackChangeListener(@NonNull final OnBackStackChangeListener listener) {
		if (backStackChangeListeners != null) backStackChangeListeners.remove(listener);
	}

	/**
	 * Creates a new FragmentRequest for already committed fragment. The new request will have
	 * specified view container id via {@link FragmentRequest#viewContainerId(int)} that has been
	 * specified for this controller via {@link #setViewContainerId(int)}.
	 *
	 * @return New fragment request with view container id specified for this controller.
	 *
	 * @see FragmentRequest#tag(String)
	 * @see FragmentRequest#viewContainerId(int)
	 */
	@NonNull public final FragmentRequest newRequest() {
		this.assertNotDestroyed("NEW REQUEST");
		return new FragmentRequest(this, null).viewContainerId(viewContainerId);
	}

	/**
	 * Creates a new FragmentRequest for the given <var>fragment</var>. The new request will have
	 * the given fragment attached along with this controller which will be responsible for execution
	 * of the new request when its {@link FragmentRequest#execute()} is called.
	 * <p>
	 * The returned request will have default {@link #FRAGMENT_TAG} specified via {@link FragmentRequest#tag(String)}
	 * and also view container id via {@link FragmentRequest#viewContainerId(int)} that has been
	 * specified for this controller via {@link #setViewContainerId(int)}.
	 *
	 * @param fragment The fragment for which to create the new request.
	 * @return New fragment request with default {@link #FRAGMENT_TAG} and view container id specified
	 * for this controller.
	 *
	 * @see FragmentRequest#tag(String)
	 * @see FragmentRequest#viewContainerId(int)
	 */
	@NonNull public final FragmentRequest newRequest(@NonNull final Fragment fragment) {
		this.assertNotDestroyed("NEW REQUEST");
		return new FragmentRequest(this, fragment).tag(FRAGMENT_TAG).viewContainerId(viewContainerId);
	}

	/**
	 * Creates a new instance of FragmentRequest for the given <var>fragmentId</var>. The new request
	 * will have the given fragment id attached along with this controller which will be responsible
	 * for execution of the new request when its {@link FragmentRequest#execute()} is called.
	 * <p>
	 * <b>Note</b>, that execution of the created request assumes that there is factory attached and
	 * that factory provides fragment that is associated with the specified <var>fragmentId</var>
	 * otherwise an exception will be thrown.
	 *
	 * @param fragmentId Id of the desired factory fragment for which to crate the new request.
	 * @return New fragment request with view container id specified for this controller.
	 */
	@NonNull public final FragmentRequest newRequest(final int fragmentId) {
		this.assertNotDestroyed("NEW REQUEST");
		return new FragmentRequest(this, fragmentId).viewContainerId(viewContainerId);
	}

	/**
	 * Performs execution of the given fragment <var>request</var>.
	 * <p>
	 * This method also notifies all registered {@link OnRequestListener OnRequestListeners} about
	 * the request execution.
	 * <p>
	 * <b>Note</b>, that this method does not check if the request has been already executed or not.
	 *
	 * @param request The fragment request to be executed.
	 * @return The fragment that has been associated with the request either during its initialization
	 * or as result of this execution. May be {@code null} if the execution has failed.
	 * @throws IllegalStateException    If there is no factory attached.
	 * @throws IllegalArgumentException If the attached factory does not provide fragment for the
	 *                                  fragment id specified for the request.
	 *
	 * @see FragmentRequestInterceptor#interceptFragmentRequest(FragmentRequest)
	 */
	@Nullable Fragment executeRequest(final FragmentRequest request) {
		this.assertNotDestroyed("EXECUTE REQUEST");
		Fragment fragment = request.fragment;
		if (fragment == null) {
			String fragmentTag = request.tag;
			final int fragmentId = request.fragmentId;
			if (fragmentId == FragmentRequest.NO_ID) {
				switch (request.transaction) {
					case FragmentRequest.REMOVE:
					case FragmentRequest.SHOW:
					case FragmentRequest.HIDE:
					case FragmentRequest.ATTACH:
					case FragmentRequest.DETACH:
						fragment = manager.findFragmentByTag(fragmentTag);
						break;
					case FragmentRequest.ADD:
					case FragmentRequest.REPLACE:
					default:
						// Fragment should be provided for these transaction types.
						break;
				}
			} else {
				this.assertHasFactory();
				if (!factory.isFragmentProvided(fragmentId)) {
					throw new IllegalArgumentException(
							"Cannot execute request for factory fragment. Current factory(" + factory.getClass() + ") " +
									"does not provide fragment for the requested id(" + fragmentId + ")!");
				}
				if (fragmentTag == null) {
					fragmentTag = factory.createFragmentTag(fragmentId);
				}
				switch (request.transaction) {
					case FragmentRequest.REMOVE:
					case FragmentRequest.SHOW:
					case FragmentRequest.HIDE:
					case FragmentRequest.ATTACH:
					case FragmentRequest.DETACH:
						fragment = manager.findFragmentByTag(fragmentTag);
						break;
					case FragmentRequest.REPLACE:
					case FragmentRequest.ADD:
					default:
						fragment = factory.createFragment(fragmentId);
						if (fragment == null) {
							throw new IllegalArgumentException(
									"Cannot execute request for factory fragment. Current factory(" + factory.getClass() + ") is cheating. " +
											"FragmentFactory.isFragmentProvided(...) returned true, but FragmentFactory.createFragment(...) returned null!"
							);
						}
						break;
				}
				request.tag = fragmentTag;
			}
		}
		if ((request.fragment = fragment) == null) {
			return null;
		}
		fragment = requestInterceptor == null ? null : requestInterceptor.interceptFragmentRequest(request);
		if (fragment == null) {
			fragment = onExecuteRequest(request);
		}
		notifyRequestExecuted(request);
		return fragment;
	}

	/**
	 * Called to perform execution of the given fragment <var>request</var>.
	 * <p>
	 * This implementation creates a new {@link FragmentTransaction} via {@link #createTransaction(FragmentRequest)}
	 * for the request and commits it via appropriate commit method and returns the associated fragment.
	 *
	 * @param request The fragment request of which execution has been requested via call to
	 *                {@link FragmentRequest#execute()}
	 * @return The fragment associated with the request.
	 */
	@SuppressWarnings("ConstantConditions")
	@NonNull protected Fragment onExecuteRequest(@NonNull final FragmentRequest request) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && manager.isDestroyed()) {
			throw new IllegalStateException("Cannot execute fragment request in context of activity that has been already destroyed!");
		}
		if (request.transaction == FragmentRequest.REPLACE && !request.hasFlag(FragmentRequest.REPLACE_SAME)) {
			// Do not replace same fragment if there is already displayed fragment with the same tag.
			final Fragment existingFragment = manager.findFragmentByTag(request.tag);
			if (existingFragment != null) {
				FragmentsLogging.d(TAG, "Fragment with tag(" + request.tag + ") is already displayed or it is in the back-stack.");
				return existingFragment;
			}
		}
		// Crate transaction for the fragment request.
		final Fragment fragment = request.fragment;
		final FragmentTransaction transaction = createTransaction(request);
		if (request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK)) {
			FragmentsLogging.d(TAG, "Fragment(" + fragment + ") will be added into back-stack under the tag(" + fragment.getTag() + ").");
		}
		// Commit the transaction either normally or allowing state loss.
		if (request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS)) {
			transaction.commitAllowingStateLoss();
		} else {
			transaction.commit();
		}
		if (request.hasFlag(FragmentRequest.IMMEDIATE)) {
			manager.executePendingTransactions();
		}
		return fragment;
	}

	/**
	 * Begins and configures a new FragmentTransaction for the given fragment <var>request</var>.
	 * <p>
	 * The created transaction will be configured via methods listed below depending on configuration
	 * parameters supplied via the specified request:
	 * <ul>
	 * <li>{@link FragmentTransaction#replace(int, Fragment, String)}</li>
	 * <li>{@link FragmentTransaction#add(int, Fragment, String)}</li>
	 * <li>{@link FragmentTransaction#remove(Fragment)}</li>
	 * <li>{@link FragmentTransaction#show(Fragment)}</li>
	 * <li>{@link FragmentTransaction#hide(Fragment)}</li>
	 * <li>{@link FragmentTransaction#attach(Fragment)}</li>
	 * <li>{@link FragmentTransaction#detach(Fragment)}</li>
	 * <li>{@link FragmentTransaction#setCustomAnimations(int, int, int, int)}</li>
	 * <li>{@link FragmentTransaction#setTransitionStyle(int)}</li>
	 * <li>{@link FragmentTransaction#addSharedElement(View, String)}</li>
	 * <li>{@link FragmentTransaction#addToBackStack(String)}</li>
	 * </ul>
	 * Also transitions related configuration will be performed for the fragment that is attached to
	 * the request via methods listed below:
	 * <ul>
	 * <li>{@link Fragment#setEnterTransition(Transition)}
	 * <li>{@link Fragment#setExitTransition(Transition)}</li>
	 * <li>{@link Fragment#setReenterTransition(Transition)}</li>
	 * <li>{@link Fragment#setReturnTransition(Transition)}</li>
	 * <li>{@link Fragment#setSharedElementEnterTransition(Transition)}</li>
	 * <li>{@link Fragment#setSharedElementReturnTransition(Transition)}</li>
	 * <li>{@link Fragment#setAllowEnterTransitionOverlap(boolean)}</li>
	 * <li>{@link Fragment#setAllowReturnTransitionOverlap(boolean)}</li>
	 * </ul>
	 * <p>
	 * <b>Do not forget to commit the transaction.</b>
	 *
	 * @param request The request specifying configuration parameters for the transaction to be
	 *                created.
	 * @return The desired fragment transaction that may be immediately committed via one of
	 * {@code FragmentTransaction.commit...()} methods.
	 * @throws IllegalArgumentException If the request has specified transaction type of {@link FragmentRequest#REPLACE}
	 *                                  or {@link FragmentRequest#ADD} but it has no container id
	 *                                  specified via {@link FragmentRequest#viewContainerId(int)}
	 */
	@SuppressWarnings("NewApi")
	@NonNull public FragmentTransaction createTransaction(@NonNull final FragmentRequest request) {
		this.assertNotDestroyed("CREATE TRANSACTION");
		final FragmentTransaction transaction = manager.beginTransaction();
		final Fragment fragment = request.fragment;
		if (request.arguments != null) {
			fragment.setArguments(request.arguments);
		}
		// Attach animations to the transaction from the FragmentTransition parameter.
		if (request.transition != null) {
			if (context == null || FragmentUtils.willBeCustomAnimationsPlayed(context)) {
				transaction.setCustomAnimations(
						request.transition.getIncomingAnimation(),
						request.transition.getOutgoingAnimation(),
						request.transition.getIncomingBackStackAnimation(),
						request.transition.getOutgoingBackStackAnimation()
				);
			}
		} else if (request.transitionStyle != FragmentRequest.NO_STYLE) {
			transaction.setTransitionStyle(request.transitionStyle);
		}
		// Resolve transaction type.
		switch (request.transaction) {
			case FragmentRequest.REPLACE:
				if (request.viewContainerId == NO_CONTAINER_ID) {
					throw new IllegalArgumentException("Cannot create REPLACE transaction. No view container id specified!");
				}
				transaction.replace(request.viewContainerId, fragment, request.tag);
				break;
			case FragmentRequest.ADD:
				if (request.viewContainerId == NO_CONTAINER_ID) {
					throw new IllegalArgumentException("Cannot create ADD transaction. No view container id specified!");
				}
				transaction.add(request.viewContainerId, fragment, request.tag);
				break;
			case FragmentRequest.REMOVE:
				transaction.remove(fragment);
				break;
			case FragmentRequest.SHOW:
				transaction.show(fragment);
				break;
			case FragmentRequest.HIDE:
				transaction.hide(fragment);
				break;
			case FragmentRequest.ATTACH:
				transaction.attach(fragment);
				break;
			case FragmentRequest.DETACH:
				transaction.detach(fragment);
				break;
			default:
				throw new IllegalArgumentException("Unsupported transaction type(" + request.transaction + ") specified for the fragment request!");
		}
		// Attach transitions with shared elements, if specified and supported.
		if (CAN_ATTACH_TRANSITIONS) {
			attachTransitionsToFragment(request, fragment);
			if (request.sharedElements != null && !request.sharedElements.isEmpty()) {
				final List<Pair<View, String>> elements = request.sharedElements;
				for (final Pair<View, String> pair : elements) {
					if (pair.first == null || pair.second == null) {
						FragmentsLogging.i(TAG, "Skipping invalid shared element pair(view: " + pair.first + ", name: " + pair.second + ").");
						continue;
					}
					transaction.addSharedElement(pair.first, pair.second);
				}
			}
		}
		// Add fragment to back stack if requested.
		if (request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK)) {
			transaction.addToBackStack(fragment.getTag());
		}
		return transaction;
	}

	/**
	 * Attaches all transitions specified via the given <var>request</var> to the given <var>fragment</var>.
	 *
	 * @param request  Request caring the specified transitions for the fragment.
	 * @param fragment The fragment instance to which to attach the transitions.
	 */
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	static void attachTransitionsToFragment(final FragmentRequest request, final Fragment fragment) {
		if (request.hasTransition(FragmentRequest.TRANSITION_ENTER)) {
			fragment.setEnterTransition(request.enterTransition);
		}
		if (request.hasTransition(FragmentRequest.TRANSITION_EXIT)) {
			fragment.setExitTransition(request.exitTransition);
		}
		if (request.hasTransition(FragmentRequest.TRANSITION_REENTER)) {
			fragment.setReenterTransition(request.reenterTransition);
		}
		if (request.hasTransition(FragmentRequest.TRANSITION_RETURN)) {
			fragment.setReturnTransition(request.returnTransition);
		}
		if (request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_ENTER)) {
			fragment.setSharedElementEnterTransition(request.sharedElementEnterTransition);
		}
		if (request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_RETURN)) {
			fragment.setSharedElementReturnTransition(request.sharedElementReturnTransition);
		}
		if (request.allowEnterTransitionOverlap != null) {
			fragment.setAllowEnterTransitionOverlap(request.allowEnterTransitionOverlap);
		}
		if (request.allowReturnTransitionOverlap != null) {
			fragment.setAllowReturnTransitionOverlap(request.allowReturnTransitionOverlap);
		}
	}

	/**
	 * Finds a fragment that is currently visible in the layout container with id specified via
	 * {@link #setViewContainerId(int)}.
	 *
	 * @return Currently visible fragment or {@code null} if there is no fragment displayed in the
	 * fragment layout container.
	 * @throws UnsupportedOperationException If there is no fragment container id specified.
	 *
	 * @see FragmentManager#findFragmentById(int)
	 */
	@Nullable public Fragment findCurrentFragment() {
		this.assertNotDestroyed("FIND CURRENT FRAGMENT");
		if (viewContainerId == NO_CONTAINER_ID) {
			throw new UnsupportedOperationException("Cannot find current fragment. No fragment container id specified!");
		}
		return manager.findFragmentById(viewContainerId);
	}

	/**
	 * Delegates to {@link FragmentManager#findFragmentByTag(String)} with the TAG obtained via
	 * {@link FragmentFactory#createFragmentTag(int)} from the current factory.
	 * <p>
	 * <b>Note</b>, that this method assumes that there is factory attached and that factory provides
	 * fragment that is associated with the specified <var>factoryFragmentId</var> otherwise an
	 * exception is thrown.
	 *
	 * @param factoryFragmentId Id of the desired factory fragment to find.
	 * @return The requested fragment if found, {@code null} otherwise.
	 * @throws IllegalStateException    If there is no factory attached.
	 * @throws IllegalArgumentException If the attached factory does not provide fragment for the
	 *                                  specified id.
	 */
	@Nullable public Fragment findFragmentByFactoryId(final int factoryFragmentId) {
		this.assertNotDestroyed("FIND FRAGMENT BY FACTORY ID");
		this.assertHasFactory();
		if (!factory.isFragmentProvided(factoryFragmentId)) {
			throw new IllegalArgumentException(
					"Cannot find fragment by factory id. Current factory(" + factory.getClass() + ") " +
							"does not provide fragment for the requested id(" + factoryFragmentId + ")!");
		}
		return manager.findFragmentByTag(factory.createFragmentTag(factoryFragmentId));
	}

	/**
	 * Checks whether there are some fragments within the back stack or not.
	 *
	 * @return {@code True} if fragments back stack contains at least one entry, {@code false} otherwise.
	 *
	 * @see FragmentManager#getBackStackEntryCount()
	 */
	public boolean hasBackStackEntries() {
		return manager.getBackStackEntryCount() > 0;
	}

	/**
	 * Returns the top entry from the fragments back stack.
	 *
	 * @return The top back stack entry or {@code null} if there are no back stack entries.
	 *
	 * @see #hasBackStackEntries()
	 */
	@Nullable public FragmentManager.BackStackEntry getTopBackStackEntry() {
		return topBackStackEntry;
	}

	/**
	 * Clears fragments back stack by calling {@link FragmentManager#popBackStack()} in loop for
	 * current back stack size obtained via {@link FragmentManager#getBackStackEntryCount()}.
	 * <p>
	 * <b>Note</b>, that {@link FragmentManager#popBackStack()} is an asynchronous call, so the
	 * fragments back stack may be cleared in a feature, not immediately.
	 *
	 * @see #clearBackStackImmediate()
	 */
	public void clearBackStack() {
		this.assertNotDestroyed("CLEAR BACK STACK");
		final int n = manager.getBackStackEntryCount();
		for (int i = 0; i < n; i++) {
			manager.popBackStack();
		}
	}

	/**
	 * Like {@link #clearBackStack()} but this will call {@link FragmentManager#popBackStackImmediate()}
	 * instead of {@link FragmentManager#popBackStack()}.
	 * <p>
	 * <b>Note</b>, that {@link FragmentManager#popBackStackImmediate()} is a synchronous call, so
	 * the fragments back stack will be popped immediately within this call. If there are too many
	 * fragments, this may take some time.
	 *
	 * @return {@code True} if there was at least one fragment popped, {@code false} otherwise.
	 */
	public boolean clearBackStackImmediate() {
		this.assertNotDestroyed("CLEAR BACK STACK IMMEDIATE");
		boolean popped = false;
		final int n = manager.getBackStackEntryCount();
		for (int i = 0; i < n; i++) {
			if (manager.popBackStackImmediate() && !popped) {
				popped = true;
			}
		}
		return popped;
	}

	/**
	 * Destroys this fragment controller instance, mainly un-registering its internal <b>back-stack</b>
	 * listener from the attached {@link FragmentManager}.
	 * <p>
	 * Fragment controller should be destroyed whenever it is used in application component that has
	 * 'shorter' lifecycle (like fragment) as its parent application component (activity).
	 * <p>
	 * <b>Note</b>, that already destroyed controller should not be used further as such usage will
	 * result in an exception to be thrown.
	 */
	public final void destroy() {
		if (!destroyed) {
			this.destroyed = true;
			this.manager.removeOnBackStackChangedListener(backStackChangeListener);
			this.requestListeners = null;
			this.backStackChangeListeners = null;
		}
	}

	/**
	 * Asserts that this controller is not destroyed yet. If it is already destroyed, an exception
	 * is thrown.
	 *
	 * @param forAction Action for which the check should be performed. The action will be placed
	 *                  into exception if it will be thrown.
	 */
	private void assertNotDestroyed(final String forAction) {
		if (destroyed) {
			throw new IllegalStateException("Cannot perform " + forAction + " action. Controller is already destroyed!");
		}
	}

	/**
	 * Called to dispatch change in the fragments back stack.
	 *
	 * @param backStackSize Current size of the fragments back stack.
	 * @param change        Identifier determining the occurred change. One of {@link BackStackListener#ADDED}
	 *                      or {@link BackStackListener#REMOVED}.
	 */
	void handleBackStackChange(final int backStackSize, final int change) {
		switch (change) {
			case BackStackListener.ADDED: {
				final FragmentManager.BackStackEntry entry = manager.getBackStackEntryAt(backStackSize - 1);
				if (entry != null) {
					this.notifyBackStackEntryChange(topBackStackEntry = entry, true);
				}
				break;
			}
			case BackStackListener.REMOVED:
			default:
				if (topBackStackEntry != null) {
					this.notifyBackStackEntryChange(topBackStackEntry, false);
				}
				this.topBackStackEntry = backStackSize > 0 ? manager.getBackStackEntryAt(backStackSize - 1) : null;
				break;
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link FragmentManager.OnBackStackChangedListener} implementation used to listen for changes
	 * in fragments back stack.
	 */
	static final class BackStackListener implements FragmentManager.OnBackStackChangedListener {

		/**
		 * Flag to indicate, that fragment was added to the back stack.
		 */
		static final int ADDED = 0x00;

		/**
		 * Flag to indicate, that fragment was removed from the back stack.
		 */
		static final int REMOVED = 0x01;

		/**
		 * Parent controller that uses this listener to listener for changes in fragments back stack.
		 */
		final FragmentController controller;

		/**
		 * Current size of the fragments back stack.
		 */
		int backStackSize;

		/**
		 * Creates a new instance of BackStackListener for the given fragment <var>controller</var>.
		 *
		 * @param controller Instance of the parent controller that will use the new listener to
		 *                   listen for changes in fragments back stack.
		 */
		BackStackListener(FragmentController controller) {
			this.controller = controller;
		}

		/**
		 */
		@Override public void onBackStackChanged() {
			final int n = controller.getFragmentManager().getBackStackEntryCount();
			if (n >= 0 && n != backStackSize) {
				this.controller.handleBackStackChange(n, n > backStackSize ? ADDED : REMOVED);
				this.backStackSize = n;
			}
		}
	}
}