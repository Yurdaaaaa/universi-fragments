/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;

import universum.studios.android.fragment.util.FragmentUtils;

/**
 * Class that declares policies related to the Android {@code Fragments} API.
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
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
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
	 * @deprecated Use {@link FragmentUtils#willBeCustomAnimationsPlayed(Context)} instead.
	 */
	@Deprecated
	@SuppressWarnings("deprecation")
	public static boolean willBeCustomAnimationsPlayed(@NonNull final Context context) {
		return FragmentUtils.willBeCustomAnimationsPlayed(context);
	}
}