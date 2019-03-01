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

import universum.studios.android.fragment.R;
import universum.studios.android.fragment.manage.FragmentTransition;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class ExtraFragmentTransitionsTest extends AndroidTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		ExtraFragmentTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<ExtraFragmentTransitions> constructor = ExtraFragmentTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testSCALE_IN_AND_SLIDE_TO_LEFT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SCALE_IN_AND_SLIDE_TO_LEFT,
				R.animator.fragment_scale_in,
				R.animator.fragment_slide_out_left,
				R.animator.fragment_slide_in_right_back,
				R.animator.fragment_scale_out_back,
				"SCALE_IN_AND_SLIDE_TO_LEFT"
		);
	}

	@Test public void testSCALE_IN_AND_SLIDE_TO_RIGHT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SCALE_IN_AND_SLIDE_TO_RIGHT,
				R.animator.fragment_scale_in,
				R.animator.fragment_slide_out_right,
				R.animator.fragment_slide_in_left_back,
				R.animator.fragment_scale_out_back,
				"SCALE_IN_AND_SLIDE_TO_RIGHT"
		);
	}

	@Test public void testSCALE_IN_AND_SLIDE_TO_TOP() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SCALE_IN_AND_SLIDE_TO_TOP,
				R.animator.fragment_scale_in,
				R.animator.fragment_slide_out_top,
				R.animator.fragment_slide_in_bottom_back,
				R.animator.fragment_scale_out_back,
				"SCALE_IN_AND_SLIDE_TO_TOP"
		);
	}

	@Test public void testSCALE_IN_AND_SLIDE_TO_BOTTOM() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SCALE_IN_AND_SLIDE_TO_BOTTOM,
				R.animator.fragment_scale_in,
				R.animator.fragment_slide_out_bottom,
				R.animator.fragment_slide_in_top_back,
				R.animator.fragment_scale_out_back,
				"SCALE_IN_AND_SLIDE_TO_BOTTOM"
		);
	}

	@Test public void testSLIDE_TO_LEFT_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SLIDE_TO_LEFT_AND_SCALE_OUT,
				R.animator.fragment_slide_in_left,
				R.animator.fragment_scale_out,
				R.animator.fragment_scale_in_back,
				R.animator.fragment_slide_out_right_back,
				"SLIDE_TO_LEFT_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_RIGHT_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SLIDE_TO_RIGHT_AND_SCALE_OUT,
				R.animator.fragment_slide_in_right,
				R.animator.fragment_scale_out,
				R.animator.fragment_scale_in_back,
				R.animator.fragment_slide_out_left_back,
				"SLIDE_TO_RIGHT_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_TOP_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SLIDE_TO_TOP_AND_SCALE_OUT,
				R.animator.fragment_slide_in_top,
				R.animator.fragment_scale_out,
				R.animator.fragment_scale_in_back,
				R.animator.fragment_slide_out_bottom_back,
				"SLIDE_TO_TOP_AND_SCALE_OUT"
		);
	}

	@Test public void testSLIDE_TO_BOTTOM_AND_SCALE_OUT() {
		// Assert:
		assertThatTransitionHasAttributes(ExtraFragmentTransitions.SLIDE_TO_BOTTOM_AND_SCALE_OUT,
				R.animator.fragment_slide_in_bottom,
				R.animator.fragment_scale_out,
				R.animator.fragment_scale_in_back,
				R.animator.fragment_slide_out_top_back,
				"SLIDE_TO_BOTTOM_AND_SCALE_OUT"
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