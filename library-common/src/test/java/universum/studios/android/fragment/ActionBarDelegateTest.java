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
import android.content.Context;
import android.graphics.drawable.Drawable;

import org.junit.Test;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import universum.studios.android.test.AndroidTestCase;
import universum.studios.android.test.TestActivity;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateTest extends AndroidTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final Activity activity = new Activity();
		// Act:
		final TestDelegate delegate = new TestDelegate(activity);
		// Assert:
		assertThat(delegate.getContext(), is(activity));
	}

	@Test public void testCreateForFrameworkActivity() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		// Act:
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		// Assert:
		assertThat(actionBarDelegate, is(notNullValue()));
		assertThat(actionBarDelegate.getContext(), is(mockActivity));
	}

	@Test public void testCreateForFrameworkActivityWithoutActionBar() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getActionBar()).thenReturn(null);
		// Act + Assert:
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@Test public void testCreateForCompatActivity() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		final androidx.appcompat.app.ActionBar mockActionBar = mock(androidx.appcompat.app.ActionBar.class);
		when(mockActivity.getSupportActionBar()).thenReturn(mockActionBar);
		// Act:
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		// Assert:
		assertThat(actionBarDelegate, is(not(nullValue())));
		assertThat(actionBarDelegate.getContext(), is(mockActivity));
	}

	@Test public void testCreateForCompatActivityWithoutActionBar() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		when(mockActivity.getSupportActionBar()).thenReturn(null);
		// Act + Assert:
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@Test public void testCreateForNullActionBar() {
		// Act + Assert:
		assertThat(ActionBarDelegate.create(context(), (ActionBar) null), is(notNullValue()));
		assertThat(ActionBarDelegate.create(context(), (androidx.appcompat.app.ActionBar) null), is(notNullValue()));
	}

	private static class TestCompatActivity extends AppCompatActivity {}

	private static final class TestDelegate extends ActionBarDelegate {

		TestDelegate(@NonNull final Context context) {
			super(context);
		}

		@Override public void setDisplayHomeAsUpEnabled(boolean enabled) {}

		@Override public void setHomeAsUpIndicator(@DrawableRes int resId) {}

		@Override public void setHomeAsUpVectorIndicator(@DrawableRes int resId) {}

		@Override public void setHomeAsUpIndicator(@Nullable Drawable indicator) {}

		@Override public void setIcon(@DrawableRes int resId) {}

		@Override public void setIcon(@Nullable Drawable icon) {}

		@Override public void setTitle(@StringRes int resId) {}

		@Override public void setTitle(@Nullable CharSequence title) {}
	}
}