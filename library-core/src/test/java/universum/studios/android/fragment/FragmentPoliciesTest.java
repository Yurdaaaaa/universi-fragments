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
package universum.studios.android.fragment;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

/**
 * @author Martin Albedinsky
 */
public final class FragmentPoliciesTest extends LocalTestCase {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentPoliciesTest";

	@Test
	public void testConstants() {
		assertThat(FragmentPolicies.TRANSITIONS_SUPPORTED, is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP));
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		FragmentPolicies.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<FragmentPolicies> constructor = FragmentPolicies.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testWillBeCustomAnimationsPlayed() {
		assertThat(FragmentPolicies.willBeCustomAnimationsPlayed(RuntimeEnvironment.application), is(true));
	}
}