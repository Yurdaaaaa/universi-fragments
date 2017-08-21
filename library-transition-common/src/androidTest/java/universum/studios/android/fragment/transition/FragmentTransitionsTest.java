/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
package universum.studios.android.fragment.transition;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.fragment.R;
import universum.studios.android.fragment.manage.FragmentTransition;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentTransitionsTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentTransitionsTest";

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		FragmentTransitions.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<FragmentTransitions> constructor = FragmentTransitions.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
	public void testNONE() {
	    assertThatTransitionHasAttributes(FragmentTransitions.NONE,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    FragmentTransition.NO_ANIMATION,
			    "NONE"
	    );
	}

	@Test
	public void testCROSS_FADE() {
		assertThatTransitionHasAttributes(FragmentTransitions.CROSS_FADE,
				R.animator.fragment_fade_in,
				R.animator.fragment_fade_out,
				R.animator.fragment_fade_in_back,
				R.animator.fragment_fade_out_back,
				"CROSS_FADE"
		);
	}

	@Test
	public void testCROSS_FADE_AND_HOLD() {
		assertThatTransitionHasAttributes(FragmentTransitions.CROSS_FADE_AND_HOLD,
				R.animator.fragment_fade_in,
				R.animator.fragment_hold,
				R.animator.fragment_hold_back,
				R.animator.fragment_fade_out_back,
				"CROSS_FADE_AND_HOLD"
		);
	}

	@Test
	public void testSLIDE_TO_RIGHT() {
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_RIGHT,
				R.animator.fragment_slide_in_right,
				R.animator.fragment_slide_out_right,
				R.animator.fragment_slide_in_left_back,
				R.animator.fragment_slide_out_left_back,
				"SLIDE_TO_RIGHT"
		);
	}

	@Test
	public void testSLIDE_TO_LEFT() {
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_LEFT,
				R.animator.fragment_slide_in_left,
				R.animator.fragment_slide_out_left,
				R.animator.fragment_slide_in_right_back,
				R.animator.fragment_slide_out_right_back,
				"SLIDE_TO_LEFT"
		);
	}

	@Test
	public void testSLIDE_TO_TOP() {
		assertThatTransitionHasAttributes(FragmentTransitions.SLIDE_TO_TOP,
				R.animator.fragment_slide_in_top,
				R.animator.fragment_slide_out_top,
				R.animator.fragment_slide_in_bottom_back,
				R.animator.fragment_slide_out_bottom_back,
				"SLIDE_TO_TOP"
		);
	}

	@Test
	public void testSLIDE_TO_BOTTOM() {
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
		assertThat(transition, is(not(nullValue())));
		assertThat(transition.getIncomingAnimation(), is(inAnim));
		assertThat(transition.getOutgoingAnimation(), is(outAnim));
		assertThat(transition.getIncomingBackStackAnimation(), is(inBackAnim));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outBackAnim));
		assertThat(transition.getName(), is(name));
	}
}
