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

import android.os.Build;
import android.provider.Settings;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentPoliciesTest extends BaseInstrumentedTest {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentPoliciesTest";

	@Test
	public void testFlags() {
		assertThat(FragmentPolicies.TRANSITIONS_SUPPORTED, is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP));
	}

	@Test
	public void testWillBeCustomAnimationsPlayed() {
		assertThat(
				FragmentPolicies.willBeCustomAnimationsPlayed(mContext),
				is(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 ||
						Settings.Global.getFloat(
								mContext.getContentResolver(),
								Settings.Global.ANIMATOR_DURATION_SCALE,
								0
						) > 0f)
		);
	}
}