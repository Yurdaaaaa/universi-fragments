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
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
public final class FragmentUtilsTest extends InstrumentedTestCase {
    
	@Test
	public void testInflateTransitionManager() {
		final ViewGroup sceneRoot = new FrameLayout(mContext);
		final TransitionManager transitionManager = FragmentUtils.inflateTransitionManager(mContext, TestResources.resourceIdentifier(
				mContext,
				TestResources.TRANSITION,
				"transition_manager"
		), sceneRoot);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
	public void testGetDrawable() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		assertThat(FragmentUtils.getDrawable(mContext.getResources(), TestResources.resourceIdentifier(
				mContext,
				TestResources.DRAWABLE,
				"ic_android"
		), null), is(not(nullValue())));
	}

	@Test
	public void testGetDrawableWithNoResource() {
		assertThat(FragmentUtils.getDrawable(mContext.getResources(), 0, null), is(nullValue()));
	}
}
