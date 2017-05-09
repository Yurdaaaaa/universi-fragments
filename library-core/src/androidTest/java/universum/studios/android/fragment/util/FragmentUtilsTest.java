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
package universum.studios.android.fragment.util;

import android.os.Build;
import android.support.test.runner.AndroidJUnit4;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestResources;
import universum.studios.android.test.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentUtilsTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentUtilsTest";

	@Test
	public void testConstants() {
		assertThat(FragmentUtils.ACCESS_LOLLIPOP, is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP));
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		FragmentUtils.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<FragmentUtils> constructor = FragmentUtils.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
    public void testInflateTransition() {
		final Transition transition = FragmentUtils.inflateTransition(mContext, TestResources.resourceIdentifier(
				mContext,
				TestResources.TRANSITION,
				"transition_fade"
		));
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		    assertThat(transition, is(not(nullValue())));
		    assertThat(transition, instanceOf(Fade.class));
	    } else {
		    assertThat(transition, is(nullValue()));
	    }
    }

	@Test
	public void testInflateTransitionManager() {
		final ViewGroup sceneRoot = new FrameLayout(mContext);
		final TransitionManager transitionManager = FragmentUtils.inflateTransitionManager(mContext, TestResources.resourceIdentifier(
				mContext,
				TestResources.TRANSITION,
				"transition_manager"
		), sceneRoot);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			assertThat(transitionManager, is(not(nullValue())));
		} else {
			assertThat(transitionManager, is(nullValue()));
		}
	}

	@Test
	public void testGetVectorDrawable() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		assertThat(FragmentUtils.getVectorDrawable(mContext.getResources(), TestResources.resourceIdentifier(
				mContext,
				TestResources.DRAWABLE,
				"vc_ic_android_24dp"
		), null), is(not(nullValue())));
	}

	@Test
	public void testGetVectorDrawableWithNoResource() {
		assertThat(FragmentUtils.getVectorDrawable(mContext.getResources(), 0, null), is(nullValue()));
	}

	@Test
	public void testDrawable() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		assertThat(FragmentUtils.getDrawable(mContext.getResources(), TestResources.resourceIdentifier(
				mContext,
				TestResources.DRAWABLE,
				"ic_android"
		), null), is(not(nullValue())));
	}

	@Test
	public void testDrawableWithNoResource() {
		assertThat(FragmentUtils.getDrawable(mContext.getResources(), 0, null), is(nullValue()));
	}
}
