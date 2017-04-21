/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License
 * you may obtain at
 *
 * 		http://www.apache.org/licenses/LICENSE-2.0
 *
 * You can redistribute, modify or publish any part of the code written within this file but as it
 * is described in the License, the software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.fragment;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

/**
 * Class containing policies related to the Android fragments API.
 *
 * @author Martin Albedinsky
 */
public final class FragmentPolicies {

	/**
	 * Flag indicating whether a transitions API for fragments is supported by the current version
	 * of the Android or not.
	 */
	public static final boolean TRANSITIONS_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	/**
	 */
	private FragmentPolicies() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Checks whether the custom animations specified via {@link android.app.FragmentTransaction#setCustomAnimations(int, int, int, int)
	 * FragmentTransaction.setCustomAnimations(int, int, int, int)} will be actually played.
	 * <p>
	 * Implementation of this check queries value of {@link Settings.Global#ANIMATOR_DURATION_SCALE}
	 * setting and checks if {@code animatorDurationScale > 0} for Android API versions above
	 * {@link Build.VERSION_CODES#JELLY_BEAN_MR1 JELLY_BEAN_MR1}. For older Android versions this check
	 * always returns {@code true}.
	 * <p>
	 * If this check returns {@code false} it is useless to specify any custom animations to a
	 * {@link android.app.FragmentTransaction FragmentTransaction} as such animations will not be
	 * played by the Android framework.
	 *
	 * @return {@code True} if animations will be played, {@code false} otherwise.
	 */
	public static boolean willBeCustomAnimationsPlayed(@NonNull final Context context) {
		return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 ||
				Settings.Global.getFloat(
						context.getContentResolver(),
						Settings.Global.ANIMATOR_DURATION_SCALE,
						0
				) > 0f;
	}
}
