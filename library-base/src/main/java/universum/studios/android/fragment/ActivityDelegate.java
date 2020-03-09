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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;

/**
 * ActivityDelegate is used to wrap an instance of {@link Activity} in order to hide some implementation
 * details when using Activity context within fragments.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@SuppressWarnings("WeakerAccess")
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
	 * Wrapped activity instance to which will be this delegate delegating its calls.
	 */
	@NonNull final Activity activity;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ActivityDelegate for the given <var>activity</var>.
	 *
	 * @param activity The activity for which is the new delegate being created.
	 */
	protected ActivityDelegate(@NonNull final Activity activity) {
		this.activity = activity;
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
		return activity;
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
	@Nullable public abstract androidx.appcompat.app.ActionBar getSupportActionBar();

	/**
	 * Delegates to {@link AppCompatActivity#startSupportActionMode(ActionMode.Callback)}.
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
			return activity.requestWindowFeature(featureId);
		}

		/**
		 */
		@Override public void invalidateOptionsMenu() {
			this.activity.invalidateOptionsMenu();
		}

		/**
		 */
		@Override @Nullable public ActionBar getActionBar() {
			return this.activity.getActionBar();
		}

		/**
		 */
		@Override @Nullable public androidx.appcompat.app.ActionBar getSupportActionBar() {
			return null;
		}

		/**
		 */
		@Override @Nullable public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
			return null;
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
			return ((AppCompatActivity) activity).supportRequestWindowFeature(featureId);
		}

		/**
		 */
		@Override public void invalidateOptionsMenu() {
			((AppCompatActivity) activity).supportInvalidateOptionsMenu();
		}

		/**
		 */
		@Override public androidx.appcompat.app.ActionBar getSupportActionBar() {
			return ((AppCompatActivity) activity).getSupportActionBar();
		}

		/**
		 */
		@Override @Nullable public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
			return ((AppCompatActivity) activity).startSupportActionMode(callback);
		}
	}
}