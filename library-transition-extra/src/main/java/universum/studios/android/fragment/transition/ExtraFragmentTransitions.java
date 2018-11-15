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
package universum.studios.android.fragment.transition;

import universum.studios.android.fragment.manage.FragmentTransition;

/**
 * Factory providing <b>extra</b> {@link FragmentTransition FragmentTransitions}.
 * <ul>
 * <li>{@link #SCALE_IN_AND_SLIDE_TO_LEFT}</li>
 * <li>{@link #SCALE_IN_AND_SLIDE_TO_RIGHT}</li>
 * <li>{@link #SCALE_IN_AND_SLIDE_TO_TOP}</li>
 * <li>{@link #SCALE_IN_AND_SLIDE_TO_BOTTOM}</li>
 * <li>{@link #SLIDE_TO_RIGHT_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_LEFT_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_BOTTOM_AND_SCALE_OUT}</li>
 * <li>{@link #SLIDE_TO_TOP_AND_SCALE_OUT}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @deprecated Use {@link FragmentExtraTransitions} instead.
 */
@SuppressWarnings("unused")
@Deprecated public final class ExtraFragmentTransitions {

	/**
	 * See {@link FragmentExtraTransitions#SCALE_IN_AND_SLIDE_TO_LEFT}.
	 */
	public static final FragmentTransition SCALE_IN_AND_SLIDE_TO_LEFT = FragmentExtraTransitions.SCALE_IN_AND_SLIDE_TO_LEFT;

	/**
	 * See {@link FragmentExtraTransitions#SCALE_IN_AND_SLIDE_TO_RIGHT}.
	 */
	public static final FragmentTransition SCALE_IN_AND_SLIDE_TO_RIGHT = FragmentExtraTransitions.SCALE_IN_AND_SLIDE_TO_RIGHT;

	/**
	 * See {@link FragmentExtraTransitions#SCALE_IN_AND_SLIDE_TO_TOP}.
	 */
	public static final FragmentTransition SCALE_IN_AND_SLIDE_TO_TOP = FragmentExtraTransitions.SCALE_IN_AND_SLIDE_TO_TOP;

	/**
	 * See {@link FragmentExtraTransitions#SCALE_IN_AND_SLIDE_TO_BOTTOM}.
	 */
	public static final FragmentTransition SCALE_IN_AND_SLIDE_TO_BOTTOM = FragmentExtraTransitions.SCALE_IN_AND_SLIDE_TO_BOTTOM;

	/**
	 * See {@link FragmentExtraTransitions#SLIDE_TO_LEFT_AND_SCALE_OUT}.
	 */
	public static final FragmentTransition SLIDE_TO_LEFT_AND_SCALE_OUT = FragmentExtraTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT;

	/**
	 * See {@link FragmentExtraTransitions#SLIDE_TO_RIGHT_AND_SCALE_OUT}.
	 */
	public static final FragmentTransition SLIDE_TO_RIGHT_AND_SCALE_OUT = FragmentExtraTransitions.SLIDE_TO_RIGHT_AND_SCALE_OUT;

	/**
	 * See {@link FragmentExtraTransitions#SLIDE_TO_TOP_AND_SCALE_OUT}.
	 */
	public static final FragmentTransition SLIDE_TO_TOP_AND_SCALE_OUT = FragmentExtraTransitions.SLIDE_TO_TOP_AND_SCALE_OUT;

	/**
	 * See {@link FragmentExtraTransitions#SLIDE_TO_BOTTOM_AND_SCALE_OUT}.
	 */
	public static final FragmentTransition SLIDE_TO_BOTTOM_AND_SCALE_OUT = FragmentExtraTransitions.SLIDE_TO_BOTTOM_AND_SCALE_OUT;

	/**
	 */
	private ExtraFragmentTransitions() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}