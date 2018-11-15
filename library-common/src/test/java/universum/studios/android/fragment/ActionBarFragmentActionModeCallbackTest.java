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

import android.view.Menu;
import android.view.MenuItem;

import org.junit.Test;

import androidx.appcompat.view.ActionMode;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;
import universum.studios.android.test.local.RobolectricTestCase;

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
public final class ActionBarFragmentActionModeCallbackTest extends RobolectricTestCase {

	@Override public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the annotations processing is kept enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testInstantiation() {
		// Act:
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback();
		// Assert:
		assertThat(callback.fragment, is(nullValue()));
	}

	@Test public void testInstantiationWithFragment() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		// Act:
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Assert:
		assertThat(callback.fragment, is(mockFragment));
	}

	@Test public void testOnCreateActionMode() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragmentAnnotationHandler mockAnnotationHandler = mock(ActionBarFragmentAnnotationHandler.class);
		when(mockAnnotationHandler.handleCreateActionMode(mockActionMode, mockMenu)).thenReturn(true);
		when(mockFragment.getAnnotationHandler()).thenReturn(mockAnnotationHandler);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act + Assert:
		assertThat(callback.onCreateActionMode(mockActionMode, mockMenu), is(true));
		verify(mockFragment).getAnnotationHandler();
		verify(mockAnnotationHandler).handleCreateActionMode(mockActionMode, mockMenu);
		verifyNoMoreInteractions(mockFragment, mockAnnotationHandler);
		verifyZeroInteractions(mockActionMode, mockMenu);
	}

	@Test public void testOnCreateActionModeNotHandledByAnnotationHandler() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragmentAnnotationHandler mockAnnotationHandler = mock(ActionBarFragmentAnnotationHandler.class);
		when(mockAnnotationHandler.handleCreateActionMode(mockActionMode, mockMenu)).thenReturn(false);
		when(mockFragment.getAnnotationHandler()).thenReturn(mockAnnotationHandler);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act + Assert:
		assertThat(callback.onCreateActionMode(mockActionMode, mockMenu), is(false));
		verify(mockFragment).getAnnotationHandler();
		verify(mockAnnotationHandler).handleCreateActionMode(mockActionMode, mockMenu);
		verifyNoMoreInteractions(mockFragment, mockAnnotationHandler);
		verifyZeroInteractions(mockActionMode, mockMenu);
	}

	@Test public void testOnCreateActionModeWithDisabledAnnotations() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act + Assert:
		assertThat(callback.onCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode, mockMenu);
	}

	@Test public void testOnCreateActionModeWithoutAttachedFragment() {
		// Arrange:
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback();
		// Act + Assert:
		assertThat(callback.onCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode, mockMenu);
	}

	@Test public void testOnPrepareActionMode() {
		// Arrange:
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback();
		// Act + Assert:
		assertThat(callback.onPrepareActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode, mockMenu);
	}

	@Test public void testOnActionItemClicked() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		when(mockFragment.onOptionsItemSelected(mockMenuItem)).thenReturn(true);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act + Assert:
		assertThat(callback.onActionItemClicked(mockActionMode, mockMenuItem), is(true));
		verify(mockFragment).onOptionsItemSelected(mockMenuItem);
		verify(mockActionMode).finish();
		verifyNoMoreInteractions(mockFragment, mockActionMode);
	}

	@Test public void testOnActionItemClickedNotHandledByFragment() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		when(mockFragment.onOptionsItemSelected(mockMenuItem)).thenReturn(false);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act + Assert:
		assertThat(callback.onActionItemClicked(mockActionMode, mockMenuItem), is(false));
		verify(mockFragment).onOptionsItemSelected(mockMenuItem);
		verifyNoMoreInteractions(mockFragment);
		verifyZeroInteractions(mockActionMode);
	}

	@Test public void testOnActionItemClickedWithoutAttachedFragment() {
		// Arrange:
		// Only ensure that the callback does not cause any troubles when it has no fragment attached.
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback();
		// Act + Assert:
		assertThat(callback.onActionItemClicked(mockActionMode, mockMenuItem), is(false));
		verifyZeroInteractions(mockActionMode, mockMenuItem);
	}

	@Test public void testOnDestroyActionMode() {
		// Arrange:
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback(mockFragment);
		// Act:
		callback.onDestroyActionMode(mockActionMode);
		// Assert:
		verify(mockFragment).onActionModeFinished();
		verifyNoMoreInteractions(mockFragment);
		verifyZeroInteractions(mockActionMode);
	}

	@Test public void testOnDestroyActionModeWithoutAttachedFragment() {
		// Arrange:
		// Only ensure that the callback does not cause any troubles when it has no fragment attached.
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionBarFragment.ActionModeCallback callback = new ActionBarFragment.ActionModeCallback();
		// Act:
		callback.onDestroyActionMode(mockActionMode);
		// Assert:
		verifyZeroInteractions(mockActionMode);
	}

	public static class TestFragment extends ActionBarFragment {}
}