/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
package universum.studios.android.fragment;

import android.os.Build;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class FragmentPoliciesTest extends RobolectricTestCase {

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
}