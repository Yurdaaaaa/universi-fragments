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
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class FragmentUtilsTest extends RobolectricTestCase {

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
	public void testWillBeCustomAnimationsPlayed() {
		assertThat(FragmentUtils.willBeCustomAnimationsPlayed(application), is(true));
	}

	@Test
	public void testAreAnimationsEnabled() {
		assertThat(FragmentUtils.areAnimationsEnabled(application), is(true));
	}

	@Test
	public void testIsPowerSaveModeActive() {
		assertThat(FragmentUtils.isPowerSaveModeActive(application), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testInflateTransitionOnLollipopApiLevel() {
		final Transition transition = FragmentUtils.inflateTransition(application, android.R.transition.fade);
		assertThat(transition, is(notNullValue()));
		assertThat(transition, instanceOf(Fade.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testInflateTransitionOnJellyBeanApiLevel() {
		assertThat(FragmentUtils.inflateTransition(application, android.R.anim.fade_in), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testInflateTransitionInContextWithoutResources() {
		final Context mockContext = mock(Context.class);
		when(mockContext.getResources()).thenReturn(null);
		assertThat(FragmentUtils.inflateTransition(mockContext, android.R.anim.fade_in), is(nullValue()));
	}
}