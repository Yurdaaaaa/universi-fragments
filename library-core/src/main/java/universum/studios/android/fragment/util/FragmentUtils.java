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
package universum.studios.android.fragment.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.TransitionRes;
import androidx.annotation.VisibleForTesting;
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat;
import universum.studios.android.fragment.FragmentPolicies;

/**
 * Utility class for the Fragments library.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class FragmentUtils {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "FragmentUtils";

	/**
	 * Boolean flag indicating whether we can use resources access in a way appropriate for
	 * {@link Build.VERSION_CODES#LOLLIPOP} Android version.
	 */
	@VisibleForTesting static final boolean ACCESS_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	private FragmentUtils() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Checks whether the custom animations specified via {@link android.app.FragmentTransaction#setCustomAnimations(int, int, int, int)
	 * FragmentTransaction.setCustomAnimations(int, int, int, int)} will be actually played.
	 * <p>
	 * Implementation of this check combines results of {@link #isPowerSaveModeActive(Context)} and
	 * {@link #areAnimationsEnabled(Context)}.
	 * <p>
	 * If this check returns {@code false} it is useless to specify any custom animations to a
	 * {@link android.app.FragmentTransaction FragmentTransaction} as such animations will not be
	 * played by the Android framework.
	 *
	 * @param context Context used to obtain required information about the system in order to perform
	 *                the check.
	 * @return {@code True} if animations will be played, that is, power save mode is not active and
	 * animations are enabled, {@code false} otherwise.
	 */
	public static boolean willBeCustomAnimationsPlayed(@NonNull final Context context) {
		return !isPowerSaveModeActive(context) && areAnimationsEnabled(context);
	}

	/**
	 * Checks whether power save mode is active or not.
	 *
	 * @param context Context used to obtain power service.
	 * @return {@code True} if power save mode is active at this time, {@code false} otherwise.
	 *
	 * @see PowerManager#isPowerSaveMode()
	 */
	public static boolean isPowerSaveModeActive(@NonNull final Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			final PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
			return powerManager != null && powerManager.isPowerSaveMode();
		}
		return false;
	}

	/**
	 * Checks whether fragment related animations are enabled by the system.
	 * <p>
	 * Implementation of this check queries value of {@link Settings.Global#ANIMATOR_DURATION_SCALE}
	 * setting and checks if {@code animatorDurationScale > 0} for Android versions above
	 * {@link Build.VERSION_CODES#JELLY_BEAN_MR1 JELLY_BEAN} API level. For older Android versions
	 * this check always returns {@code true}.
	 *
	 * @param context Context used to obtain required information about the system in order to perform
	 *                the check.
	 * @return {@code True} if fragment related animations are enabled, {@code false} otherwise.
	 */
	@SuppressWarnings("deprecation")
	public static boolean areAnimationsEnabled(@NonNull final Context context) {
		float animatorDurationScale = -1;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			animatorDurationScale = Settings.Global.getFloat(
					context.getContentResolver(),
					Settings.Global.ANIMATOR_DURATION_SCALE,
					animatorDurationScale
			);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			animatorDurationScale = Settings.System.getFloat(
					context.getContentResolver(),
					Settings.System.ANIMATOR_DURATION_SCALE,
					animatorDurationScale
			);
		}
		return animatorDurationScale == -1 || animatorDurationScale > 0;
	}

	/**
	 * Inflates a desired Transition from the specified <var>resource</var>.
	 *
	 * @param context  Context used for inflation process.
	 * @param resource Resource id of the desired transition to inflate.
	 * @return Inflated transition or {@code null} if the current API level does not support transitions.
	 *
	 * @see TransitionInflater#inflateTransition(int)
	 * @see #inflateTransitionManager(Context, int, ViewGroup)
	 */
	@SuppressLint("NewApi")
	@Nullable public static Transition inflateTransition(@NonNull final Context context, @TransitionRes final int resource) {
		return FragmentPolicies.TRANSITIONS_SUPPORTED && context.getResources() != null ? TransitionInflater.from(context).inflateTransition(resource) : null;
	}

	/**
	 * Inflates a desired TransitionManager from the specified <var>resource</var>.
	 *
	 * @param context   Context used for inflation process.
	 * @param resource  Resource id of the desired transition manager to inflate.
	 * @param sceneRoot Root of the scene for which to inflate transition manager.
	 * @return Inflated transition manager or {@code null} if the current API level does not support
	 * transitions.
	 *
	 * @see TransitionInflater#inflateTransitionManager(int, ViewGroup)
	 * @see #inflateTransition(Context, int)
	 */
	@SuppressLint("NewApi")
	@Nullable public static TransitionManager inflateTransitionManager(
			@NonNull final Context context,
			@TransitionRes final int resource,
			@NonNull final ViewGroup sceneRoot
	) {
		return FragmentPolicies.TRANSITIONS_SUPPORTED && context.getResources() != null ?
				TransitionInflater.from(context).inflateTransitionManager(resource, sceneRoot) :
				null;
	}

	/**
	 * Obtains vector drawable with the specified <var>resId</var> using the given <var>resources</var>.
	 * <p>
	 * This utility method will obtain the requested vector drawable in a way that is appropriate
	 * for the current Android version.
	 *
	 * @param resources The resources that should be used to obtain the vector drawable.
	 * @param resId     Resource id of the desired vector drawable to obtain.
	 * @param theme     Theme that will be used to resolve theme attributes for the requested drawable
	 *                  on {@link Build.VERSION_CODES#LOLLIPOP} and above Android versions.
	 * @return Instance of the requested vector drawable or {@code null} if the specified resource
	 * id is {@code 0}.
	 *
	 * @see #getDrawable(Resources, int, Resources.Theme)
	 * @see VectorDrawableCompat#create(Resources, int, Resources.Theme)
	 */
	@Nullable public static Drawable getVectorDrawable(
			@NonNull final Resources resources,
			@DrawableRes final int resId,
			@Nullable final Resources.Theme theme
	) throws Resources.NotFoundException {
		if (resId == 0) return null;
		else return ACCESS_LOLLIPOP ? getDrawable(resources, resId, theme) : VectorDrawableCompat.create(resources, resId, theme);
	}

	/**
	 * Obtains drawable with the specified <var>resId</var> using the given <var>resources</var>.
	 * <p>
	 * This utility method will obtain the requested drawable in a way that is appropriate for the
	 * current Android version.
	 *
	 * @param resources The resources that should be used to obtain the drawable.
	 * @param resId     Resource id of the desired drawable to obtain.
	 * @param theme     Theme that will be used to resolve theme attributes for the requested drawable
	 *                  on {@link Build.VERSION_CODES#LOLLIPOP} and above Android versions.
	 * @return Instance of the requested drawable or {@code null} if the specified resource id is {@code 0}.
	 *
	 * @see Resources#getDrawable(int, Resources.Theme)
	 * @see Resources#getDrawable(int)
	 */
	@SuppressWarnings({"NewApi", "deprecation"})
	@Nullable public static Drawable getDrawable(
			@NonNull final Resources resources,
			@DrawableRes final int resId,
			@Nullable final Resources.Theme theme
	) throws Resources.NotFoundException {
		if (resId == 0) return null;
		else return ACCESS_LOLLIPOP ? resources.getDrawable(resId, theme) : resources.getDrawable(resId);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}