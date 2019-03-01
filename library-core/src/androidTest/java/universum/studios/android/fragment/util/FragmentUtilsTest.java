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

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.transition.TransitionManager;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import org.junit.Test;

import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class FragmentUtilsTest extends AndroidTestCase {
    
	@Test public void testInflateTransitionManager() {
		// Arrange:
		final ViewGroup sceneRoot = new FrameLayout(context());
		// Act:
		final TransitionManager transitionManager = FragmentUtils.inflateTransitionManager(context(), context().getResources().getIdentifier(
				"transition_manager",
				"transition",
				context().getPackageName()
		), sceneRoot);
		// Assert:
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			assertThat(transitionManager, is(notNullValue()));
		} else {
			assertThat(transitionManager, is(nullValue()));
		}
	}

	@Test public void testGetVectorDrawable() {
		// Arrange:
		final int resId = context().getResources().getIdentifier(
				"vc_ic_android_24dp",
				"drawable",
				context().getPackageName()
		);
		// Act:
		final Drawable drawable = FragmentUtils.getVectorDrawable(context().getResources(), resId, null);
		// Assert:
		assertThat(drawable, is(notNullValue()));
	}

	@Test public void testGetVectorDrawableWithNoResource() {
		// Arrange + Act + Assert:
		assertThat(FragmentUtils.getVectorDrawable(context().getResources(), 0, null), is(nullValue()));
	}

	@Test public void testGetDrawable() {
		// Arrange:
		final int resId = context().getResources().getIdentifier(
				"ic_android",
				"drawable",
				context().getPackageName()
		);
		// Act:
		final Drawable drawable = FragmentUtils.getDrawable(context().getResources(), resId, null);
		// Assert:
		assertThat(drawable, is(notNullValue()));
	}

	@Test public void testGetDrawableWithNoResource() {
		// Arrange + Act + Assert:
		assertThat(FragmentUtils.getDrawable(context().getResources(), 0, null), is(nullValue()));
	}
}