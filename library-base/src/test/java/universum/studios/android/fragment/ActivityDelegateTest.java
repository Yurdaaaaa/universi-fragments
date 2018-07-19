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
package universum.studios.android.fragment;

import android.app.ActionBar;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;

import org.hamcrest.core.Is;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class ActivityDelegateTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final Activity activity = new Activity();
		// Act:
		final TestDelegate delegate = new TestDelegate(activity);
		// Assert:
		assertThat(delegate.getActivity(), is(activity));
	}

    @Test public void testCreateForFrameworkActivity() {
	    // Arrange:
		final Activity activity = new Activity();
	    // Act:
	    final ActivityDelegate activityDelegate = ActivityDelegate.create(activity);
	    // Assert:
	    assertThat(activityDelegate, is(not(nullValue())));
	    assertThat(activityDelegate.getActivity(), is(activity));
    }

	@Test public void testCreateForCompatActivity() {
		// Arrange:
		final AppCompatActivity activity = new AppCompatActivity();
		// Act:
		final ActivityDelegate activityDelegate = ActivityDelegate.create(activity);
		// Assert:
		assertThat(activityDelegate, is(not(nullValue())));
		assertThat(activityDelegate.getActivity(), Is.<Activity>is(activity));
	}

	private static final class TestDelegate extends ActivityDelegate {

		TestDelegate(@NonNull final Activity activity) {
			super(activity);
		}

		@Override public boolean requestWindowFeature(final int featureId) {
			return false;
		}

		@Override public void invalidateOptionsMenu() {}

		@Override @Nullable public ActionBar getActionBar() {
			return null;
		}

		@Override @Nullable public android.support.v7.app.ActionBar getSupportActionBar() {
			return null;
		}

		@Override @Nullable public ActionMode startActionMode(@NonNull final ActionMode.Callback callback) {
			return null;
		}
	}
}