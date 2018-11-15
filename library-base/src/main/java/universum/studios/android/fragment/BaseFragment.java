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
package universum.studios.android.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.CheckResult;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.BaseAnnotationHandlers;
import universum.studios.android.fragment.annotation.handler.FragmentAnnotationHandler;
import universum.studios.android.fragment.util.FragmentUtils;

/**
 * A {@link Fragment} implementation designed to provide extended API and logic that is useful almost
 * every time you need to implement your desired fragment.
 * <p>
 * BaseFragment class provides lifecycle state check methods that may be used to check whether a
 * particular instance of fragment is in a specific lifecycle state. All provided methods are listed
 * below:
 * <ul>
 * <li>{@link #isAttached()}</li>
 * <li>{@link #isCreated()}</li>
 * <li>{@link #isStarted()}</li>
 * <li>{@link #isPaused()}</li>
 * <li>{@link #isStopped()}</li>
 * <li>{@link #isDestroyed()}</li>
 * </ul>
 * as addition to the Android framework's lifecycle methods:
 * <ul>
 * <li>{@link #isResumed()}</li>
 * <li>{@link #isDetached()}</li>
 * </ul>
 * <p>
 * You can also easily dispatch view click events to your specific implementation of BaseFragment
 * via {@link #dispatchViewClick(View)} or back press events via {@link #dispatchBackPress()}
 * from activity's context in which such fragment presented.
 *
 * <h3>Accepted annotations</h3>
 * <ul>
 * <li>
 * {@link universum.studios.android.fragment.annotation.ContentView @ContentView} <b>[class - inherited]</b>
 * <p>
 * If this annotation is presented, the layout resource specified via this annotation will be used
 * to inflate root view for an instance of annotated BaseFragment sub-class when
 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)} is called.
 * </li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public abstract class BaseFragment extends Fragment implements BackPressWatcher, ViewClickWatcher {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "BaseFragment";

	/**
	 * Lifecycle flag used to indicate that fragment is <b>attached</b> to the parent context.
	 */
	static final int LIFECYCLE_ATTACHED = 0x00000001;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>created</b>.
	 */
	static final int LIFECYCLE_CREATED = 0x00000001 << 1;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>started</b>.
	 */
	static final int LIFECYCLE_STARTED = 0x00000001 << 2;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>resumed</b>.
	 */
	static final int LIFECYCLE_RESUMED = 0x00000001 << 3;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>paused</b>.
	 */
	static final int LIFECYCLE_PAUSED = 0x00000001 << 4;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>stopped</b>.
	 */
	static final int LIFECYCLE_STOPPED = 0x00000001 << 5;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>destroyed</b>.
	 */
	static final int LIFECYCLE_DESTROYED = 0x00000001 << 6;

	/**
	 * Lifecycle flag used to indicate that fragment is <b>detached</b>.
	 */
	static final int LIFECYCLE_DETACHED = 0x00000001 << 7;

	/**
	 * Defines an annotation for determining set of available lifecycle flags.
	 */
	@IntDef(flag = true, value = {
			LIFECYCLE_ATTACHED,
			LIFECYCLE_CREATED,
			LIFECYCLE_STARTED,
			LIFECYCLE_RESUMED,
			LIFECYCLE_PAUSED,
			LIFECYCLE_STOPPED,
			LIFECYCLE_DESTROYED,
			LIFECYCLE_DETACHED
	})
	@Retention(RetentionPolicy.SOURCE)
	private @interface LifecycleFlag {}

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Handler responsible for processing of all annotations of this class and also for handling all
	 * annotations related operations for this class.
	 */
	final FragmentAnnotationHandler annotationHandler;

	/**
	 * Delegate for activity to which is this instance of fragment currently attached. This delegate
	 * is available between calls to {@link #onAttach(Context)} and {@link #onDetach()}.
	 */
	ActivityDelegate activityDelegate;

	/**
	 * Stores all lifecycle related flags for this fragment.
	 */
	private int lifecycleFlags;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of BaseFragment.
	 * <p>
	 * If annotations processing is enabled via {@link FragmentAnnotations} all annotations supported
	 * by this class will be processed/obtained here so they can be later used.
	 */
	public BaseFragment() {
		super();
		this.annotationHandler = onCreateAnnotationHandler();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of the specified <var>classOfFragment</var> with the given <var>args</var>.
	 *
	 * @param classOfFragment Class of the desired fragment to instantiate.
	 * @param args            Arguments to set to new instance of fragment by {@link Fragment#setArguments(Bundle)}.
	 * @param <F>             Type of the desired fragment.
	 * @return New instance of fragment with the given arguments or {@code null} if some instantiation
	 * error occurs.
	 */
	@SuppressWarnings("TryWithIdenticalCatches")
	@Nullable public static <F extends Fragment> F newInstanceWithArguments(@NonNull final Class<F> classOfFragment, @Nullable final Bundle args) {
		try {
			final F fragment = classOfFragment.newInstance();
			fragment.setArguments(args);
			return fragment;
		} catch (java.lang.InstantiationException e) {
			Log.e(TAG, "Failed to instantiate instance of " + classOfFragment + " with arguments!", e);
		} catch (IllegalAccessException e) {
			Log.e(TAG, "Failed to instantiate instance of " + classOfFragment + " with arguments!", e);
		}
		return null;
	}

	/**
	 * Invoked to create annotations handler for this instance.
	 *
	 * @return Annotations handler specific for this class.
	 */
	FragmentAnnotationHandler onCreateAnnotationHandler() {
		return BaseAnnotationHandlers.obtainFragmentHandler(getClass());
	}

	/**
	 * Returns handler that is responsible for annotations processing of this class and also for
	 * handling all annotations related operations for this class.
	 *
	 * @return Annotations handler specific for this class.
	 * @throws IllegalStateException If annotations processing is not enabled for the Fragments library.
	 */
	@NonNull protected FragmentAnnotationHandler getAnnotationHandler() {
		FragmentAnnotations.checkIfEnabledOrThrow();
		return annotationHandler;
	}

	/**
	 * Updates the current private flags.
	 *
	 * @param flag Value of the desired flag to add/remove to/from the current private flags.
	 * @param add  Boolean flag indicating whether to add or remove the specified <var>flag</var>.
	 *
	 * @see #hasLifecycleFlag(int)
	 */
	private void updateLifecycleFlags(@LifecycleFlag final int flag, final boolean add) {
		if (add) this.lifecycleFlags |= flag;
		else this.lifecycleFlags &= ~flag;
	}

	/**
	 * Returns a boolean flag indicating whether the specified <var>flag</var> is contained within
	 * the current private flags or not.
	 *
	 * @param flag Value of the flag to check.
	 * @return {@code True} if the requested flag is contained, {@code false} otherwise.
	 *
	 * @see #updateLifecycleFlags(int, boolean)
	 */
	@VisibleForTesting boolean hasLifecycleFlag(@LifecycleFlag final int flag) {
		return (lifecycleFlags & flag) != 0;
	}

	/**
	 */
	@SuppressWarnings("deprecation")
	@Override public void onAttach(@NonNull final Activity activity) {
		super.onAttach(activity);
		this.activityDelegate = ActivityDelegate.create(activity);
		this.updateLifecycleFlags(LIFECYCLE_DETACHED, false);
		this.updateLifecycleFlags(LIFECYCLE_ATTACHED, true);
	}

	/**
	 * Checks whether this fragment is attached to its parent context. This is {@code true} for
	 * duration of {@link #onAttach(Context)} and {@link #onDetach()}.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isDetached()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment is attached, {@code false} otherwise.
	 *
	 * @see #isDetached()
	 */
	public final boolean isAttached() {
		return hasLifecycleFlag(LIFECYCLE_ATTACHED);
	}

	/**
	 * Returns the theme of the context to which is this fragment attached.
	 *
	 * @return Parent context's theme.
	 * @throws IllegalStateException If this fragment is not attached to any context.
	 */
	@NonNull protected Resources.Theme getContextTheme() {
		final Activity activity = getActivity();
		if (activity == null) {
			throw new IllegalStateException("Fragment is not attached to parent context.");
		}
		return activity.getTheme();
	}

	/**
	 * Delegate method for {@link Activity#runOnUiThread(Runnable)} of the parent activity.
	 *
	 * @return {@code True} if parent activity is available and action was posted, {@code false}
	 * otherwise.
	 */
	public final boolean runOnUiThread(@NonNull final Runnable action) {
		final Activity activity = getActivity();
		if (activity != null) {
			activity.runOnUiThread(action);
			return true;
		}
		return false;
	}

	/**
	 */
	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.updateLifecycleFlags(LIFECYCLE_DESTROYED, false);
		this.updateLifecycleFlags(LIFECYCLE_CREATED, true);
	}

	/**
	 * Checks whether this fragment is created. This is {@code true} for duration of {@link #onCreate(Bundle)}
	 * and {@link #onDestroy()}.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isDestroyed()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment is created, {@code false} otherwise.
	 *
	 * @see #isDestroyed()
	 */
	public final boolean isCreated() {
		return hasLifecycleFlag(LIFECYCLE_CREATED);
	}

	/**
	 */
	@Override public void onStart() {
		super.onStart();
		this.updateLifecycleFlags(LIFECYCLE_STOPPED, false);
		this.updateLifecycleFlags(LIFECYCLE_STARTED, true);
	}

	/**
	 * Checks whether this fragment is in the started lifecycle state. This is {@code true} for
	 * duration of {@link #onStart()} and {@link #onStop()}.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isStopped()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment has been started, {@code false} otherwise.
	 *
	 * @see #isStopped()
	 */
	public final boolean isStarted() {
		return hasLifecycleFlag(LIFECYCLE_STARTED);
	}

	/**
	 */
	@Override public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		if (annotationHandler != null) {
			final int viewResource = annotationHandler.getContentViewResource(-1);
			if (viewResource != -1) {
				if (annotationHandler.shouldAttachContentViewToContainer()) {
					inflater.inflate(viewResource, container, true);
					return null;
				}
				return inflater.inflate(viewResource, container, false);
			}
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 */
	@Override public void onViewCreated(@NonNull final View view, @Nullable final Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (annotationHandler != null) {
			final int backgroundResId = annotationHandler.getContentViewBackgroundResId(-1);
			if (backgroundResId != -1) {
				view.setBackgroundResource(backgroundResId);
			}
		}
	}

	/**
	 * Returns a boolean flag indicating whether the view is already created or not.
	 *
	 * @return {@code True} if the view of this fragment is already created, {@code false} otherwise.
	 */
	public final boolean isViewCreated() {
		return getView() != null;
	}

	/**
	 * Inflates a desired Transition from the specified <var>resource</var>.
	 *
	 * @param resource Resource id of the desired transition to inflate.
	 * @return Inflated transition or {@code null} if the current API level does not support transitions
	 * or this fragment is not attached to its parent context.
	 *
	 * @see TransitionInflater#inflateTransition(int)
	 */
	@Nullable protected Transition inflateTransition(@TransitionRes int resource) {
		final Context context = getActivity();
		return context == null ? null : FragmentUtils.inflateTransition(context, resource);
	}

	/**
	 */
	@Override public void onResume() {
		super.onResume();
		this.updateLifecycleFlags(LIFECYCLE_PAUSED, false);
		this.updateLifecycleFlags(LIFECYCLE_RESUMED, true);
	}

	// This lifecycle state method is already available for the parent Fragment class.
	/*public final boolean isResumed();*/

	/**
	 * Invalidates the attached activity's options menu as necessary.
	 * <p>
	 * Note that the invalidation request is ignored if this fragment instance is not added or it is
	 * hidden at this time.
	 *
	 * @return {@code True} if menu has been invalidated, {@code false} otherwise.
	 */
	public boolean invalidateOptionsMenu() {
		if (isAdded() && !isHidden()) {
			final Activity activity = getActivity();
			if (activity == null) {
				return false;
			}
			activity.invalidateOptionsMenu();
			return true;
		}
		return false;
	}

	/**
	 * Dispatches to {@link #onViewClick(View)}.
	 * <p>
	 * This implementation by default returns {@code false} for all passed views.
	 */
	@Override public boolean dispatchViewClick(@NonNull final View view) {
		onViewClick(view);
		return false;
	}

	/**
	 * Invoked immediately after {@link #dispatchViewClick(View)} was called to process
	 * click event on the given <var>view</var>.
	 *
	 * @param view The clicked view dispatched to this fragment.
	 */
	protected void onViewClick(@NonNull View view) {
		// Inheritance hierarchies may handle here click event for the passed view.
	}

	/**
	 */
	@Override public void onPause() {
		super.onPause();
		this.updateLifecycleFlags(LIFECYCLE_RESUMED, false);
		this.updateLifecycleFlags(LIFECYCLE_PAUSED, true);
	}

	/**
	 * Checks whether this fragment is in the paused lifecycle state. This is {@code true} for
	 * duration of {@link #onPause()} until {@link #onResume()} is called again.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isResumed()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment has been paused, {@code false} otherwise.
	 *
	 * @see #isResumed()
	 */
	public final boolean isPaused() {
		return hasLifecycleFlag(LIFECYCLE_PAUSED);
	}

	/**
	 */
	@Override public void onStop() {
		super.onStop();
		this.updateLifecycleFlags(LIFECYCLE_STARTED, false);
		this.updateLifecycleFlags(LIFECYCLE_STOPPED, true);
	}

	/**
	 * Checks whether this fragment is in the stopped lifecycle state. This is {@code true} for
	 * duration of {@link #onStop()} until {@link #onStart()} is called again.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isStarted()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment has been stopped, {@code false} otherwise.
	 *
	 * @see #isStarted()
	 */
	public final boolean isStopped() {
		return hasLifecycleFlag(LIFECYCLE_STOPPED);
	}

	/**
	 */
	@Override @CheckResult public boolean dispatchBackPress() {
		return onBackPress();
	}

	/**
	 * Invoked immediately after {@link #dispatchBackPress()} was called to process back press event.
	 *
	 * @return {@code True} if this instance of fragment has processed the back press event,
	 * {@code false} otherwise.
	 */
	protected boolean onBackPress() {
		return false;
	}

	/**
	 */
	@Override public void onDestroy() {
		super.onDestroy();
		this.updateLifecycleFlags(LIFECYCLE_CREATED, false);
		this.updateLifecycleFlags(LIFECYCLE_DESTROYED, true);
	}

	/**
	 * Checks whether this fragment is destroyed. This is {@code true} whenever {@link #onDestroy()}
	 * has been called.
	 * <p>
	 * When this method returns {@code true} the opposite lifecycle state method {@link #isCreated()}
	 * returns {@code false} and vise versa.
	 *
	 * @return {@code True} if fragment is destroyed, {@code false} otherwise.
	 *
	 * @see #isCreated()
	 */
	public final boolean isDestroyed() {
		return hasLifecycleFlag(LIFECYCLE_DESTROYED);
	}

	/**
	 */
	@Override public void onDetach() {
		super.onDetach();
		this.updateLifecycleFlags(LIFECYCLE_ATTACHED, false);
		this.updateLifecycleFlags(LIFECYCLE_DETACHED, true);
		this.activityDelegate = null;
	}

	// This lifecycle state method is already available for the parent Fragment class.
	/*public final boolean isDetached();*/

	/*
	 * Inner classes ===============================================================================
	 */
}