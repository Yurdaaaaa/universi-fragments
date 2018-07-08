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
		final ViewGroup sceneRoot = new FrameLayout(context);
		final TransitionManager transitionManager = FragmentUtils.inflateTransitionManager(context, TestResources.resourceIdentifier(
				context,
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
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		assertThat(FragmentUtils.getVectorDrawable(context.getResources(), TestResources.resourceIdentifier(
				context,
				TestResources.DRAWABLE,
				"vc_ic_android_24dp"
		), null), is(not(nullValue())));
	}

	@Test
	public void testGetVectorDrawableWithNoResource() {
		assertThat(FragmentUtils.getVectorDrawable(context.getResources(), 0, null), is(nullValue()));
	}

	@Test
	public void testGetDrawable() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(context));
		assertThat(FragmentUtils.getDrawable(context.getResources(), TestResources.resourceIdentifier(
				context,
				TestResources.DRAWABLE,
				"ic_android"
		), null), is(not(nullValue())));
	}

	@Test
	public void testGetDrawableWithNoResource() {
		assertThat(FragmentUtils.getDrawable(context.getResources(), 0, null), is(nullValue()));
	}
}