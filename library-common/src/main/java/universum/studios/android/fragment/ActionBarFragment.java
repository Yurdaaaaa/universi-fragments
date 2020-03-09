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

import android.app.ActionBar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.MenuOptions;
import universum.studios.android.fragment.annotation.handler.ActionBarAnnotationHandlers;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;

/**
 * A {@link BaseFragment} implementation that provides API allowing to access an instance of
 * {@link ActionBar} directly from an implementation of this class. ActionBar is accessed through
 * the parent activity to which is a particular instance of ActionBarFragment attached. ActionBar may
 * be obtained via {@link #getActionBar()} after the parent activity has been created, so fragment's
 * {@link #onActivityCreated(Bundle)} has been called.
 *
 * <h3>Action mode</h3>
 * This fragment implementation also provides logic allowing to start an action mode via
 * {@link #startActionMode()} or via {@link #startActionMode(ActionMode.Callback)}. Whenever a new
 * action mode is started, {@link #onActionModeStarted(ActionMode)} is invoked. To check whether
 * fragment is currently in action mode, call {@link #isInActionMode()}. The currently active action
 * mode may be obtained via {@link #getActionMode()}. Finishing of the active action mode may be done
 * via {@link #finishActionMode()} and {@link #onActionModeFinished()} will be invoked immediately.
 *
 * <h3>Accepted annotations</h3>
 * <ul>
 * <li>
 * {@link ActionBarOptions @ActionBarOptions} <b>[class - inherited]</b>
 * <p>
 * If this annotation is presented, all options specified via this annotation will be used to set
 * up an instance of ActionBar accessible from within context of a sub-class of ActionBarFragment.
 * Such a set up is accomplished in {@link #onViewCreated(android.view.View, Bundle)}.
 * </li>
 * <li>
 * {@link MenuOptions @MenuOptions} <b>[class - inherited]</b>
 * <p>
 * If this annotation is presented, options menu will be requested in {@link #onCreate(Bundle)}
 * via {@link #setHasOptionsMenu(boolean)} and menu will be created in {@link #onCreateOptionsMenu(Menu, MenuInflater)}
 * according to the options specified via this annotation.
 * </li>
 * <li>
 * {@link universum.studios.android.fragment.annotation.ActionModeOptions @ActionModeOptions} <b>[class - inherited]</b>
 * <p>
 * If this annotation is presented, the {@link ActionMode} started via {@link #startActionMode()}
 * will be configured with options menu specified via this annotation using an instance of
 * {@link ActionModeCallback}.
 * </li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
public class ActionBarFragment extends BaseFragment {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ActionBarFragment";

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
	 * Current action mode started via {@link #startActionMode(ActionMode.Callback)}. May be {@code null}
	 * if no action mode has been started yet or has been already finished.
	 */
	private ActionMode actionMode;

	/**
	 * Delegate for ActionBar obtained from the parent activity of this fragment. This delegate is
	 * available between calls to {@link #onActivityCreated(Bundle)} and {@link #onDetach()}.
	 */
	@VisibleForTesting ActionBarDelegate actionBarDelegate;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ActionBarFragment.
	 *
	 * @see #ActionBarFragment(int)
	 */
	public ActionBarFragment() {
		this(0);
	}

	/**
	 * Alternate constructor that may be used to provide a default layout that will be inflated by
	 * {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
	 *
	 * @param contentLayoutId The desired layout resource id.
	 */
	public ActionBarFragment(@LayoutRes final int contentLayoutId) {
		super(contentLayoutId);
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override ActionBarFragmentAnnotationHandler onCreateAnnotationHandler() {
		return ActionBarAnnotationHandlers.obtainActionBarFragmentHandler(getClass());
	}

	/**
	 */
	@Override @NonNull protected ActionBarFragmentAnnotationHandler getAnnotationHandler() {
		FragmentAnnotations.checkIfEnabledOrThrow();
		return (ActionBarFragmentAnnotationHandler) annotationHandler;
	}

	/**
	 */
	@Override public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (annotationHandler != null) {
			final ActionBarFragmentAnnotationHandler annotationHandler = (ActionBarFragmentAnnotationHandler) this.annotationHandler;
			if (annotationHandler.hasOptionsMenu()) {
				setHasOptionsMenu(true);
			}
		}
	}

	/**
	 */
	@Override public void onCreateOptionsMenu(@NonNull final Menu menu, @NonNull final MenuInflater inflater) {
		if (annotationHandler == null) {
			super.onCreateOptionsMenu(menu, inflater);
			return;
		}
		final ActionBarFragmentAnnotationHandler annotationHandler = (ActionBarFragmentAnnotationHandler) this.annotationHandler;
		if (annotationHandler.hasOptionsMenu()) {
			if (annotationHandler.shouldClearOptionsMenu()) {
				menu.clear();
			}
			final int menuResource = annotationHandler.getOptionsMenuResource(0);
			if (menuResource == 0) {
				super.onCreateOptionsMenu(menu, inflater);
			} else {
				switch (annotationHandler.getOptionsMenuFlags(0)) {
					case MenuOptions.IGNORE_SUPER:
						inflater.inflate(menuResource, menu);
						break;
					case MenuOptions.BEFORE_SUPER:
						inflater.inflate(menuResource, menu);
						super.onCreateOptionsMenu(menu, inflater);
						break;
					case MenuOptions.DEFAULT:
					default:
						super.onCreateOptionsMenu(menu, inflater);
						inflater.inflate(menuResource, menu);
						break;
				}
			}
		} else {
			super.onCreateOptionsMenu(menu, inflater);
		}
	}

	/**
	 */
	@Override public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.actionBarDelegate = ActionBarDelegate.create(requireActivity());
		this.invalidateActionBar();
	}

	/**
	 * Returns a boolean flag indicating whether the parent Activity's ActionBar is available or not.
	 *
	 * @return {@code True} if ActionBar obtained from the parent activity is available, {@code false}
	 * otherwise.
	 *
	 * @see #getActionBarDelegate()
	 */
	protected final boolean isActionBarAvailable() {
		return actionBarDelegate != null;
	}

	/**
	 * Returns the delegate for the ActionBar obtained from the parent activity of this fragment.
	 * <p>
	 * This delegate is used by this fragment for configuration of the ActionBar.
	 *
	 * @return Delegate for ActionBar if the parent activity has ActionBar available, {@code null}
	 * if the ActionBar is not available.
	 *
	 * @see #isActionBarAvailable()
	 */
	@NonNull protected ActionBarDelegate getActionBarDelegate() {
		if (actionBarDelegate == null) {
			throw new IllegalStateException("The parent activity does not have ActionBar presented!");
		}
		return actionBarDelegate;
	}

	/**
	 * Returns the instance of ActionBar that can be accessed by this fragment via its parent Activity.
	 * <p>
	 * <b>Note</b>, that ActionBar can be accessed only for duration of {@link #onAttach(android.content.Context)}
	 * and {@link #onDetach()}, otherwise an exception will be thrown.
	 *
	 * @return Instance of ActionBar obtained from the parent activity or {@code null} if the parent
	 * activity does not have ActionBar available.
	 * @throws IllegalStateException If this fragment is not attached to its parent activity
	 *                               yet or it has been already detached.
	 * @see #isActionBarAvailable()
	 */
	@Nullable protected ActionBar getActionBar() {
		if (activityDelegate == null) {
			throw new IllegalStateException(
					"Cannot access ActionBar. " + getClass() + " is not attached " +
							"to the parent activity yet or it has been already detached!"
			);
		}
		return activityDelegate.getActionBar();
	}

	/**
	 * Same as {@link #getActionBar()} but this returns the instance of support ActionBar that can be
	 * accessed by this fragment only if it is presented within context of {@link AppCompatActivity}.
	 *
	 * @return Instance of support ActionBar obtained from the parent activity or {@code null} if
	 * the parent activity does not have ActionBar available or the activity is not an AppCompatActivity.
	 * @throws IllegalStateException If this fragment is not attached to its parent activity
	 *                               yet or it has been already detached.
	 */
	@Nullable protected androidx.appcompat.app.ActionBar getSupportActionBar() {
		if (activityDelegate == null) {
			throw new IllegalStateException(
					"Cannot access support ActionBar. " + getClass() + " is not attached " +
							"to the parent activity yet or it has been already detached!"
			);
		}
		return activityDelegate.getSupportActionBar();
	}

	/**
	 * Called to invalidate {@link ActionBar} of the parent Activity according to the configuration
	 * of this fragment.
	 * <p>
	 * Default implementation configures the ActionBar with options specified via
	 * {@link ActionBarOptions @ActionBarOptions} annotation (if presented).
	 * <p>
	 * Inheritance hierarchies of ActionBarFragment may override this method to perform custom or
	 * additional ActionBar's invalidation.
	 */
	public void invalidateActionBar() {
		if (actionBarDelegate != null && annotationHandler != null) {
			final ActionBarFragmentAnnotationHandler annotationHandler = (ActionBarFragmentAnnotationHandler) this.annotationHandler;
			annotationHandler.configureActionBar(actionBarDelegate);
			if (annotationHandler.hasOptionsMenu()) {
				setHasOptionsMenu(true);
			}
		}
	}

	/**
	 * Same as {@link #startActionMode(ActionMode.Callback)} with a new instance of
	 * {@link ActionModeCallback} for this fragment.
	 */
	protected boolean startActionMode() {
		return startActionMode(new ActionModeCallback(this));
	}

	/**
	 * Starts a new action mode for this fragment.
	 *
	 * @param callback The callback used to manage action mode.
	 * @return {@code True} if action mode has been started, {@code false} if this fragment
	 * is already in the action mode or the parent activity of this fragment is not available or some
	 * error occurs.
	 *
	 * @see #isInActionMode()
	 * @see #isAttached()
	 */
	protected boolean startActionMode(@NonNull final ActionMode.Callback callback) {
		if (!isInActionMode() && activityDelegate != null) {
			final ActionMode actionMode = activityDelegate.startActionMode(callback);
			if (actionMode != null) {
				onActionModeStarted(actionMode);
				return true;
			}
		}
		return false;
	}

	/**
	 * Invoked immediately after {@link #startActionMode(ActionMode.Callback)} was called
	 * and this fragment was not in the action mode yet.
	 * <p>
	 * <em>Derived classes should call through to the super class's implementation of this method.
	 * If not, proper working of action mode cannot be ensured.</em>
	 *
	 * @param actionMode Currently started action mode.
	 */
	@CallSuper protected void onActionModeStarted(@NonNull final ActionMode actionMode) {
		this.actionMode = actionMode;
	}

	/**
	 * Returns a boolean flag indicating whether this fragment is in action mode or not.
	 *
	 * @return {@code True} if this fragment is in the action mode, {@code false} otherwise.
	 *
	 * @see #getActionMode()
	 * @see #startActionMode(ActionMode.Callback)
	 */
	protected boolean isInActionMode() {
		return actionMode != null;
	}

	/**
	 * Returns the current action mode (if started).
	 *
	 * @return The current action mode, or {@code null} if this fragment is not in action mode.
	 *
	 * @see #isInActionMode()
	 * @see #startActionMode(ActionMode.Callback)
	 */
	@Nullable protected ActionMode getActionMode() {
		return actionMode;
	}

	/**
	 * Finishes the current action mode (if started).
	 *
	 * @return {@code True} if action mode has been finished, {@code false} if this fragment is not
	 * in action mode.
	 *
	 * @see #isInActionMode()
	 */
	protected boolean finishActionMode() {
		if (actionMode != null) {
			actionMode.finish();
			return true;
		}
		return false;
	}

	/**
	 * Invoked whenever {@link ActionModeCallback#onDestroyActionMode(ActionMode)} is
	 * called on the current action mode callback (if instance of {@link ActionModeCallback}).
	 * <p>
	 * <em>Derived classes should call through to the super class's implementation of this method.
	 * If not, proper working of action mode cannot be ensured.</em>
	 *
	 * @see #finishActionMode()
	 */
	@CallSuper protected void onActionModeFinished() {
		this.actionMode = null;
	}

	/**
	 */
	@Override protected boolean onBackPress() {
		return finishActionMode() || super.onBackPress();
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link ActionMode.Callback} basic implementation for {@link ActionBarFragment} that may be
	 * used to simplify action mode management within an implementation of such a fragment.
	 * <p>
	 * Instance of this action mode is by default instantiated by ActionBarFragment whenever
	 * {@link #startActionMode()} is called.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 */
	public static class ActionModeCallback implements ActionMode.Callback {

		/**
		 * Instance of fragment within which context was this action mode started.
		 */
		protected final ActionBarFragment fragment;

		/**
		 * Creates a new instance of ActionModeCallback without fragment.
		 */
		public ActionModeCallback() {
			this(null);
		}

		/**
		 * Creates a new instance of ActionModeCallback for the context of the given <var>fragment</var>.
		 *
		 * @param fragment The instance of fragment in which is action mode started.
		 */
		public ActionModeCallback(@Nullable final ActionBarFragment fragment) {
			this.fragment = fragment;
		}

		/**
		 */
		@Override public boolean onCreateActionMode(@NonNull final ActionMode actionMode, @NonNull final Menu menu) {
			if (fragment == null || !FragmentAnnotations.isEnabled()) {
				return false;
			}
			return fragment.getAnnotationHandler().handleCreateActionMode(actionMode, menu);
		}

		/**
		 */
		@Override public boolean onPrepareActionMode(@NonNull final ActionMode actionMode, @NonNull final Menu menu) {
			return false;
		}

		/**
		 */
		@Override public boolean onActionItemClicked(@NonNull final ActionMode actionMode, @NonNull final MenuItem menuItem) {
			if (fragment != null && fragment.onOptionsItemSelected(menuItem)) {
				actionMode.finish();
				return true;
			}
			return false;
		}

		/**
		 */
		@Override public void onDestroyActionMode(@NonNull final ActionMode actionMode) {
			if (fragment != null) fragment.onActionModeFinished();
		}
	}
}