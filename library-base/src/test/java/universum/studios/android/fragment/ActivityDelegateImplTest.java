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
import android.view.Window;

import org.junit.Test;

import androidx.appcompat.view.ActionMode;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActivityDelegateImplTest extends AndroidTestCase {

    @Test public void testInstantiation() {
	    // Arrange:
    	final Activity activity = new Activity();
	    // Act:
	    final ActivityDelegate delegate = new ActivityDelegate.Impl(activity);
	    // Assert:
	    assertThat(delegate.getActivity(), is(activity));
	}

	@Test public void testRequestWindowFeature() {
		// Arrange:
    	final Activity mockActivity = mock(Activity.class);
		final Window mockWindow = mock(Window.class);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		when(mockActivity.getWindow()).thenReturn(mockWindow);
		// Act + Assert:

		assertThat(
				delegate.requestWindowFeature(Window.FEATURE_ACTION_BAR),
				is(mockWindow.requestFeature(Window.FEATURE_ACTION_BAR))
		);
		verify(mockWindow).requestFeature(Window.FEATURE_ACTION_BAR);
		verifyNoMoreInteractions(mockWindow);
	}

	@Test public void testInvalidateOptionsMenu() {
		// Arrange:
    	final Activity mockActivity = mock(Activity.class);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		// Act:
		delegate.invalidateOptionsMenu();
		// Assert:
		verify(mockActivity).invalidateOptionsMenu();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testGetActionBar() {
		// Arrange:
    	final Activity mockActivity = mock(Activity.class);
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockActivity.getActionBar()).thenReturn(mockActionBar);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		// Act + Assert:
		assertThat(delegate.getActionBar(), is(mockActionBar));
		verify(mockActivity).getActionBar();
		verifyNoMoreInteractions(mockActivity);
	}

	@Test public void testGetSupportActionBar() {
		// Arrange:
    	final Activity mockActivity = mock(Activity.class);
		final ActivityDelegate.Impl delegate = new ActivityDelegate.Impl(mockActivity);
		// Act + Assert:
		assertThat(delegate.getSupportActionBar(), is(nullValue()));
		verifyZeroInteractions(mockActivity);
	}

	@Test public void testStartActionMode() {
		// Arrange:
    	final Activity mockActivity = mock(Activity.class);
		final ActionMode.Callback mockActionModeCallback = mock(ActionMode.Callback.class);
		final ActivityDelegate delegate = new ActivityDelegate.Impl(mockActivity);
		// Act + Assert:
		assertThat(delegate.startActionMode(mockActionModeCallback), is(nullValue()));
		verifyZeroInteractions(mockActivity);
	}
}