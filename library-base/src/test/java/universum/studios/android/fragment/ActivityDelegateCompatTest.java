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
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import org.hamcrest.core.Is;
import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActivityDelegateCompatTest extends RobolectricTestCase {

	@Test
	public void testInstantiation() {
		final AppCompatActivity activity = new AppCompatActivity();
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(activity);
		assertThat(delegate.mActivity, Is.<Activity>is(activity));
	}

	@Test
	public void testRequestWindowFeature() {
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		assertThat(
				delegate.requestWindowFeature(Window.FEATURE_ACTION_BAR),
				is(mockActivity.supportRequestWindowFeature(Window.FEATURE_ACTION_BAR))
		);
		verify(mockActivity, times(2)).supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
	}

	@Test
	public void testInvalidateOptionsMenu() {
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		delegate.invalidateOptionsMenu();
		verify(mockActivity, times(1)).supportInvalidateOptionsMenu();
	}

	@Test
	public void testGetActionBar() {
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		assertThat(delegate.getActionBar(), is(mockActionBar));
		verify(mockActivity, times(1)).getActionBar();
		verify(mockActivity, times(0)).getSupportActionBar();
	}

	@Test
	public void testGetSupportActionBar() {
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final android.support.v7.app.ActionBar mockSupportActionBar = mock(android.support.v7.app.ActionBar.class);
		when(mockActivity.getSupportActionBar()).thenReturn(mockSupportActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		assertThat(delegate.getSupportActionBar(), is(mockSupportActionBar));
		verify(mockActivity, times(1)).getSupportActionBar();
		verify(mockActivity, times(0)).getActionBar();
	}
}
