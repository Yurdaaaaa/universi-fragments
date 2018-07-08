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
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;

/**
 * ActivityDelegate is used to wrap an instance of {@link Activity} in order to hide some implementation
 * details when using Activity context within fragments.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public abstract class ActivityDelegate {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ActivityDelegate";

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
	 * <b>This field has been deprecated and will be made private in version 1.4.0.</b>
	 * <p>
	 * Wrapped activity instance to which will be this delegate delegating its calls.
	 *
	 * @deprecated Use {@link #getActivity()} instead.
	 */
	@Deprecated
	@NonNull protected final Activity mActivity;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ActivityDelegate for the given <var>activity</var>.
	 *
	 * @param activity The activity for which is the new delegate being created.
	 */
	protected ActivityDelegate(@NonNull final Activity activity) {
		this.mActivity = activity;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Wraps the given <var>activity</var> into its corresponding delegate depending on the activity's
	 * implementation.
	 *
	 * @param activity The activity to be wrapped.
	 * @return Instance of ActivityDelegate for the specified activity.
	 */
	@NonNull public static ActivityDelegate create(@NonNull final Activity activity) {
		if (activity instanceof AppCompatActivity) {
			return new AppCompatImpl((AppCompatActivity) activity);
		}
		return new Impl(activity);
	}

	/**
	 * Returns the activity this delegate for created for.
	 *
	 * @return This delegate's activity.
	 *
	 * @see #ActivityDelegate(Activity)
	 */
	@NonNull protected final Activity getActivity() {
		return mActivity;
	}

	/**
	 * Delegates to {@link Activity#requestWindowFeature(int)}.
	 */
	public abstract boolean requestWindowFeature(int featureId);

	/**
	 * Delegates to {@link Activity#invalidateOptionsMenu()}.
	 */
	public abstract void invalidateOptionsMenu();

	/**
	 * Delegates to {@link Activity#getActionBar()}.
	 */
	@Nullable public abstract ActionBar getActionBar();

	/**
	 * Delegates to {@link AppCompatActivity#getSupportActionBar()}.
	 */
	@Nullable public abstract android.support.v7.app.ActionBar getSupportActionBar();

	/**
	 * Delegates to {@link Activity#startActionMode(ActionMode.Callback)}.
	 */
	@Nullable public abstract ActionMode startActionMode(@NonNull ActionMode.Callback callback);

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * An {@link ActivityDelegate} implementation used to wrap basic {@link Activity}.
	 */
	@VisibleForTesting static class Impl extends ActivityDelegate {

		/**
		 * Creates a new instance of Impl to wrap the given <var>activity</var>.
		 *
		 * @param activity The Activity instance to be wrapped.
		 */
		Impl(final Activity activity) {
			super(activity);
		}

		/**
		 */
		@Override public boolean requestWindowFeature(final int featureId) {
			return mActivity.requestWindowFeature(featureId);
		}

		/**
		 */
		@Override public void invalidateOptionsMenu() {
			this.mActivity.invalidateOptionsMenu();
		}

		/**
		 */
		@Override @Nullable public ActionBar getActionBar() {
			return this.mActivity.getActionBar();
		}

		/**
		 */
		@Override @Nullable public android.support.v7.app.ActionBar getSupportActionBar() {
			return null;
		}

		/**
		 */
		@Override @Nullable public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
			return mActivity.startActionMode(callback);
		}
	}

	/**
	 * A {@link Impl} implementation used to wrap {@link AppCompatActivity}.
	 */
	@VisibleForTesting static final class AppCompatImpl extends Impl {

		/**
		 * Creates a new instance of AppCompatImpl to wrap the given <var>activity</var>.
		 *
		 * @param activity The AppCompatImpl instance to be wrapped.
		 */
		AppCompatImpl(final AppCompatActivity activity) {
			super(activity);
		}

		/**
		 */
		@Override public boolean requestWindowFeature(final int featureId) {
			return ((AppCompatActivity) mActivity).supportRequestWindowFeature(featureId);
		}

		/**
		 */
		@Override public void invalidateOptionsMenu() {
			((AppCompatActivity) mActivity).supportInvalidateOptionsMenu();
		}

		/**
		 */
		@Override public android.support.v7.app.ActionBar getSupportActionBar() {
			return ((AppCompatActivity) mActivity).getSupportActionBar();
		}
	}
}