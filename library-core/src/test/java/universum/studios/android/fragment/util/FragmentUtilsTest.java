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

import android.content.Context;
import android.os.Build;
import android.transition.Fade;
import android.transition.Transition;

import org.junit.Test;
import org.robolectric.annotation.Config;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class FragmentUtilsTest extends RobolectricTestCase {

	@Test public void testContract() {
		// Assert:
		assertThat(FragmentUtils.ACCESS_LOLLIPOP, is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP));
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		FragmentUtils.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<FragmentUtils> constructor = FragmentUtils.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testWillBeCustomAnimationsPlayed() {
		// Act + Assert:
		assertThat(FragmentUtils.willBeCustomAnimationsPlayed(context), is(true));
	}

	@Test public void testAreAnimationsEnabled() {
		// Act + Assert:
		assertThat(FragmentUtils.areAnimationsEnabled(context), is(true));
	}

	@Test public void testIsPowerSaveModeActive() {
		// Act + Assert:
		assertThat(FragmentUtils.isPowerSaveModeActive(context), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testInflateTransitionOnLollipopApiLevel() {
		// Act:
		final Transition transition = FragmentUtils.inflateTransition(context, android.R.transition.fade);
		// Assert:
		assertThat(transition, is(notNullValue()));
		assertThat(transition, instanceOf(Fade.class));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testInflateTransitionOnJellyBeanApiLevel() {
		// Act + Assert:
		assertThat(FragmentUtils.inflateTransition(context, android.R.anim.fade_in), is(nullValue()));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testInflateTransitionInContextWithoutResources() {
		// Arrange:
		final Context mockContext = mock(Context.class);
		when(mockContext.getResources()).thenReturn(null);
		// Act + Assert:
		assertThat(FragmentUtils.inflateTransition(mockContext, android.R.anim.fade_in), is(nullValue()));
	}
}