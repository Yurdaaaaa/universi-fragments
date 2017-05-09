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

import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;

import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ActivityDelegateTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ActivityDelegateTest";

	@Rule public final UiThreadTestRule UI_RULE = new UiThreadTestRule();

	@Test
	@UiThreadTest
	public void testInstantiation() {
		final Activity activity = new Activity();
		assertThat(new Delegate(activity).mActivity, is(activity));
	}

    @Test
    @UiThreadTest
	public void testCreateForFrameworkActivity() {
		final Activity activity = new Activity();
	    final ActivityDelegate activityDelegate = ActivityDelegate.create(activity);
	    assertThat(activityDelegate, is(not(nullValue())));
	    assertThat(activityDelegate.mActivity, is(activity));
	}

	@Test
	@UiThreadTest
	public void testCreateForCompatActivity() {
		final AppCompatActivity activity = new AppCompatActivity();
		final ActivityDelegate activityDelegate = ActivityDelegate.create(activity);
		assertThat(activityDelegate, is(not(nullValue())));
		assertThat(activityDelegate.mActivity, Is.<Activity>is(activity));
	}

	private static final class Delegate extends ActivityDelegate {

		Delegate(@NonNull Activity activity) {
			super(activity);
		}

		@Override
		public boolean requestWindowFeature(int featureId) {
			return false;
		}

		@Override
		public void invalidateOptionsMenu() {

		}

		@Nullable
		@Override
		public ActionBar getActionBar() {
			return null;
		}

		@Nullable
		@Override
		public android.support.v7.app.ActionBar getSupportActionBar() {
			return null;
		}

		@Nullable
		@Override
		public ActionMode startActionMode(@NonNull ActionMode.Callback callback) {
			return null;
		}
	}
}
