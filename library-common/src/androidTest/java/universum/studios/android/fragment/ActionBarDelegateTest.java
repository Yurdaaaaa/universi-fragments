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
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.app.AppCompatActivity;

import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestActivity;
import universum.studios.android.test.instrumented.TestCompatActivity;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ActionBarDelegateTest extends InstrumentedTestCase {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "ActionBarDelegate";

	@Rule public final UiThreadTestRule UI_RULE = new UiThreadTestRule();

	@Test
	@UiThreadTest
	public void testInstantiation() throws Throwable {
		final Activity activity = new Activity();
		assertThat(new Delegate(activity).mContext, Matchers.<Context>is(activity));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateForFrameworkActivity() {
		final Activity mockActivity = mock(TestActivity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		assertThat(actionBarDelegate, is(not(nullValue())));
		assertThat(actionBarDelegate.mContext, Matchers.<Context>is(mockActivity));
	}

	@Test
	public void testCreateForFrameworkActivityWithoutActionBar() {
		final Activity mockActivity = mock(TestActivity.class);
		when(mockActivity.getActionBar()).thenReturn(null);
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateForCompatActivity() {
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		when(mockActivity.getSupportActionBar()).thenReturn(mockActionBar);
		final ActionBarDelegate actionBarDelegate = ActionBarDelegate.create(mockActivity);
		assertThat(actionBarDelegate, is(not(nullValue())));
		assertThat(actionBarDelegate.mContext, Is.<Context>is(mockActivity));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testCreateForCompatActivityWithoutActionBar() {
		final AppCompatActivity mockActivity = mock(TestCompatActivity.class);
		when(mockActivity.getSupportActionBar()).thenReturn(null);
		assertThat(ActionBarDelegate.create(mockActivity), is(nullValue()));
	}

	@Test
	public void testCreateForNullActionBar() {
		assertThat(ActionBarDelegate.create(mContext, (ActionBar) null), is(not(nullValue())));
		assertThat(ActionBarDelegate.create(mContext, (android.support.v7.app.ActionBar) null), is(not(nullValue())));
	}

	private static final class Delegate extends ActionBarDelegate {

		Delegate(@NonNull Context context) {
			super(context);
		}

		@Override
		public void setDisplayHomeAsUpEnabled(boolean enabled) {
		}

		@Override
		public void setHomeAsUpIndicator(@DrawableRes int resId) {
		}

		@Override
		public void setHomeAsUpVectorIndicator(@DrawableRes int resId) {
		}

		@Override
		public void setHomeAsUpIndicator(@Nullable Drawable indicator) {
		}

		@Override
		public void setIcon(@DrawableRes int resId) {
		}

		@Override
		public void setIcon(@Nullable Drawable icon) {
		}

		@Override
		public void setTitle(@StringRes int resId) {
		}

		@Override
		public void setTitle(@Nullable CharSequence title) {
		}
	}
}
