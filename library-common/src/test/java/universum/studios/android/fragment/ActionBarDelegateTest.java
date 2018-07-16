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
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;
import universum.studios.android.test.local.TestCompatActivity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final Activity activity = new Activity();
		// Act:
		final TestDelegate delegate = new TestDelegate(activity);
		// Assert:
		assertThat(delegate.getContext(), Matchers.<Context>is(activity));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateForFrameworkActivity() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		// Act:
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		// Assert:
		assertThat(actionBarDelegate, is(notNullValue()));
		assertThat(actionBarDelegate.getContext(), Matchers.<Context>is(mockActivity));
	}

	@Test public void testCreateForFrameworkActivityWithoutActionBar() {
		// Arrange:
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getActionBar()).thenReturn(null);
		// Act + Assert:
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateForCompatActivity() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		when(mockActivity.getSupportActionBar()).thenReturn(mockActionBar);
		// Act:
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		// Assert:
		assertThat(actionBarDelegate, is(not(nullValue())));
		assertThat(actionBarDelegate.getContext(), Is.<Context>is(mockActivity));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateForCompatActivityWithoutActionBar() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		when(mockActivity.getSupportActionBar()).thenReturn(null);
		// Act + Assert:
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@Test public void testCreateForNullActionBar() {
		// Act + Assert:
		assertThat(ActionBarDelegate.create(application, (ActionBar) null), is(notNullValue()));
		assertThat(ActionBarDelegate.create(application, (android.support.v7.app.ActionBar) null), is(notNullValue()));
	}

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