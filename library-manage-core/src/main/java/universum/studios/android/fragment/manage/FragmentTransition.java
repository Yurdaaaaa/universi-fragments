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

import android.app.FragmentTransaction;
import android.os.Parcelable;
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;

/**
 * FragmentTransition provides a foursome of animation resources that are meant for {@link FragmentTransaction}.
 * <p>
 * Implementations of FragmentTransition class may be supplied to {@link FragmentRequest} to animate
 * changes between desired fragments.
 *
 * @author Martin Albedinsky
 */
public interface FragmentTransition extends Parcelable {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Constant used to identify no animation resource provided by fragment transition.
	 */
	int NO_ANIMATION = 0;

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Returns the animation resource for a new incoming fragment.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * incoming fragment.
	 */
	@AnimatorRes
	int getIncomingAnimation();

	/**
	 * Returns the animation resource for the current outgoing fragment.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * outgoing fragment.
	 */
	@AnimatorRes
	int getOutgoingAnimation();

	/**
	 * Returns the animation resource for an old incoming fragment when it is being popped from
	 * the back stack.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * outgoing back-stacked fragment.
	 */
	@AnimatorRes
	int getIncomingBackStackAnimation();

	/**
	 * Returns the animation resource for the current outgoing fragment when it is being popped from
	 * the back stack.
	 *
	 * @return Animation resource or {@link #NO_ANIMATION} if no animation should be played for
	 * outgoing back-stacked fragment.
	 */
	@AnimatorRes
	int getOutgoingBackStackAnimation();

	/**
	 * Returns the name of this fragment transition.
	 *
	 * @return Name of this transition.
	 */
	@NonNull
	String getName();
}