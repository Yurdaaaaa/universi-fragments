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
		assertThat(FragmentUtils.willBeCustomAnimationsPlayed(mApplication), is(true));
	}

	@Test
	public void testAreAnimationsEnabled() {
		assertThat(FragmentUtils.areAnimationsEnabled(mApplication), is(true));
	}

	@Test
	public void testIsPowerSaveModeActive() {
		assertThat(FragmentUtils.isPowerSaveModeActive(mApplication), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testInflateTransitionOnLollipopApiLevel() {
		final Transition transition = FragmentUtils.inflateTransition(mApplication, android.R.transition.fade);
		assertThat(transition, is(notNullValue()));
		assertThat(transition, instanceOf(Fade.class));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testInflateTransitionOnJellyBeanApiLevel() {
		assertThat(FragmentUtils.inflateTransition(mApplication, android.R.anim.fade_in), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testInflateTransitionInContextWithoutResources() {
		final Context mockContext = mock(Context.class);
		when(mockContext.getResources()).thenReturn(null);
		assertThat(FragmentUtils.inflateTransition(mockContext, android.R.anim.fade_in), is(nullValue()));
	}
}
