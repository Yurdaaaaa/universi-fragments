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
import android.view.ActionMode;
import android.view.Window;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActivityDelegateImplTest extends RobolectricTestCase {
    
    @Test
	public void testInstantiation() {
	    final Activity activity = new Activity();
		final ActivityDelegate delegate = new ActivityDelegate.Impl(activity);
	    assertThat(delegate.mActivity, is(activity));
	}

	@Test
	public void testRequestWindowFeature() {
		final Activity mockActivity = mock(Activity.class);
		final Window mockWindow = mock(Window.class);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		assertThat(
				delegate.requestWindowFeature(Window.FEATURE_ACTION_BAR),
				is(mockWindow.requestFeature(Window.FEATURE_ACTION_BAR))
		);
		verify(mockWindow, times(1)).requestFeature(Window.FEATURE_ACTION_BAR);
	}

	@Test
	public void testInvalidateOptionsMenu() {
		final Activity mockActivity = mock(Activity.class);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		delegate.invalidateOptionsMenu();
		verify(mockActivity, times(1)).invalidateOptionsMenu();
	}

	@Test
	public void testGetActionBar() {
		final Activity mockActivity = mock(Activity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		assertThat(delegate.getActionBar(), is(mockActionBar));
		verify(mockActivity, times(1)).getActionBar();
	}

	@Test
	public void testGetSupportActionBar() {
		final Activity mockActivity = mock(Activity.class);
		assertThat(new ActivityDelegate.Impl(mockActivity).getSupportActionBar(), is(nullValue()));
		verifyZeroInteractions(mockActivity);
	}

	@Test
	public void testStartActionMode() {
		final Activity mockActivity = mock(Activity.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionMode.Callback mockActionModeCallback = mock(ActionMode.Callback.class);
		when(mockActivity.startActionMode(mockActionModeCallback)).thenReturn(mockActionMode);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		assertThat(delegate.startActionMode(mockActionModeCallback), is(mockActionMode));
		verify(mockActivity, times(1)).startActionMode(mockActionModeCallback);
	}
}