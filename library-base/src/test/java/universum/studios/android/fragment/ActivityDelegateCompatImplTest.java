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
import android.view.Window;

import org.junit.Test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActivityDelegateCompatImplTest extends AndroidTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final AppCompatActivity activity = new AppCompatActivity();
		// Act:
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(activity);
		// Assert:
		assertThat(delegate.getActivity(), is(activity));
	}

	@Test public void testRequestWindowFeature() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		// Act + Assert:
		assertThat(
				delegate.requestWindowFeature(Window.FEATURE_ACTION_BAR),
				is(mockActivity.supportRequestWindowFeature(Window.FEATURE_ACTION_BAR))
		);
		verify(mockActivity, times(2)).supportRequestWindowFeature(Window.FEATURE_ACTION_BAR);
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testInvalidateOptionsMenu() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		// Act:
		delegate.invalidateOptionsMenu();
		// Assert:
		verify(mockActivity).supportInvalidateOptionsMenu();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testGetActionBar() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		// Act + Assert:
		assertThat(delegate.getActionBar(), is(mockActionBar));
		verify(mockActivity).getActionBar();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testGetSupportActionBar() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final androidx.appcompat.app.ActionBar mockSupportActionBar = mock(androidx.appcompat.app.ActionBar.class);
		when(mockActivity.getSupportActionBar()).thenReturn(mockSupportActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		// Act + Assert:
		assertThat(delegate.getSupportActionBar(), is(mockSupportActionBar));
		verify(mockActivity).getSupportActionBar();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testStartActionMode() {
		// Arrange:
		final AppCompatActivity mockActivity = mock(AppCompatActivity.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionMode.Callback mockActionModeCallback = mock(ActionMode.Callback.class);
		when(mockActivity.startSupportActionMode(mockActionModeCallback)).thenReturn(mockActionMode);
		final ActivityDelegate delegate = new ActivityDelegate.AppCompatImpl(mockActivity);
		// Act + Assert:
		assertThat(delegate.startActionMode(mockActionModeCallback), is(mockActionMode));
		verify(mockActivity).startSupportActionMode(mockActionModeCallback);
		verifyNoMoreInteractions(mockActivity);
	}
}