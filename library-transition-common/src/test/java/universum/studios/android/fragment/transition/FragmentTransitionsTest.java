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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.fragment.manage.FragmentTransition;
import universum.studios.android.fragment.transition.common.R;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

/**
 * @author Martin Albedinsky
 */
public final class FragmentTransitionsTest extends RobolectricTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		FragmentTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<FragmentTransitions> constructor = FragmentTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

    @Test public void testNONE() {
	    // Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.NONE,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    "NONE"
	    );
	}

	@Test public void testCROSS_FADE() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.CROSS_FADE,
				R.animator.fragment_fade_in,
				R.animator.fragment_fade_out,
				R.animator.fragment_fade_in_back,
				R.animator.fragment_fade_out_back,
				"CROSS_FADE"
		);
	}

	@Test public void testCROSS_FADE_AND_HOLD() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.CROSS_FADE_AND_HOLD,
				R.animator.fragment_fade_in,
				R.animator.fragment_hold,
				R.animator.fragment_hold_back,
				R.animator.fragment_fade_out_back,
				"CROSS_FADE_AND_HOLD"
		);
	}

	@Test public void testSLIDE_TO_RIGHT() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_RIGHT,
				R.animator.fragment_slide_in_right,
				R.animator.fragment_slide_out_right,
				R.animator.fragment_slide_in_left_back,
				R.animator.fragment_slide_out_left_back,
				"SLIDE_TO_RIGHT"
		);
	}

	@Test public void testSLIDE_TO_LEFT() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_LEFT,
				R.animator.fragment_slide_in_left,
				R.animator.fragment_slide_out_left,
				R.animator.fragment_slide_in_right_back,
				R.animator.fragment_slide_out_right_back,
				"SLIDE_TO_LEFT"
		);
	}

	@Test public void testSLIDE_TO_TOP() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_TOP,
				R.animator.fragment_slide_in_top,
				R.animator.fragment_slide_out_top,
				R.animator.fragment_slide_in_bottom_back,
				R.animator.fragment_slide_out_bottom_back,
				"SLIDE_TO_TOP"
		);
	}

	@Test public void testSLIDE_TO_BOTTOM() {
		// Assert:
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_BOTTOM,
				R.animator.fragment_slide_in_bottom,
				R.animator.fragment_slide_out_bottom,
				R.animator.fragment_slide_in_top_back,
				R.animator.fragment_slide_out_top_back,
				"SLIDE_TO_BOTTOM"
		);
	}

	private void assertThatTransitionHasAttributes(
			FragmentTransition transition,
	        int inAnim,
	        int outAnim,
	        int inBackAnim,
	        int outBackAnim,
	        String name
	) {
		// Assert:
		assertThat(transition, is(notNullValue()));
		assertThat(transition.getIncomingAnimation(), is(inAnim));
		assertThat(transition.getOutgoingAnimation(), is(outAnim));
		assertThat(transition.getIncomingBackStackAnimation(), is(inBackAnim));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outBackAnim));
		assertThat(transition.getName(), is(name));
	}
}