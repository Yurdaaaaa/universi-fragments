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

import android.support.test.runner.AndroidJUnit4;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;
import universum.studios.android.test.BaseInstrumentedTest;

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
@RunWith(AndroidJUnit4.class)
public final class ActionBarFragmentActionModeCallbackTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ActionBarFragmentActionModeCallbackTest";

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the annotations processing is kept enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testInstantiation() {
		assertThat(new ActionBarFragment.ActionModeCallback().fragment, is(nullValue()));
	}

	@Test
	public void testInstantiationWithFragment() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).fragment, is(mockFragment));
	}

	@Test
	public void testOnCreateActionMode() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragmentAnnotationHandler mockAnnotationHandler = mock(ActionBarFragmentAnnotationHandler.class);
		when(mockAnnotationHandler.handleCreateActionMode(mockActionMode, mockMenu)).thenReturn(true);
		when(mockFragment.getAnnotationHandler()).thenReturn(mockAnnotationHandler);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).onCreateActionMode(mockActionMode, mockMenu), is(true));
		verify(mockFragment, times(1)).getAnnotationHandler();
		verify(mockAnnotationHandler, times(1)).handleCreateActionMode(mockActionMode, mockMenu);
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testOnCreateActionModeNotHandledByAnnotationHandler() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final ActionBarFragmentAnnotationHandler mockAnnotationHandler = mock(ActionBarFragmentAnnotationHandler.class);
		when(mockAnnotationHandler.handleCreateActionMode(mockActionMode, mockMenu)).thenReturn(false);
		when(mockFragment.getAnnotationHandler()).thenReturn(mockAnnotationHandler);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).onCreateActionMode(mockActionMode, mockMenu), is(false));
		verify(mockFragment, times(1)).getAnnotationHandler();
		verify(mockAnnotationHandler, times(1)).handleCreateActionMode(mockActionMode, mockMenu);
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testOnCreateActionModeWithDisabledAnnotations() {
		FragmentAnnotations.setEnabled(false);
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).onCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testOnCreateActionModeWithoutAttachedFragment() {
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		assertThat(new ActionBarFragment.ActionModeCallback().onCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testOnPrepareActionMode() {
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		assertThat(new ActionBarFragment.ActionModeCallback().onPrepareActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testOnActionItemClicked() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		when(mockFragment.onOptionsItemSelected(mockMenuItem)).thenReturn(true);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).onActionItemClicked(mockActionMode, mockMenuItem), is(true));
		verify(mockFragment, times(1)).onOptionsItemSelected(mockMenuItem);
		verify(mockActionMode, times(1)).finish();
	}

	@Test
	public void testOnActionItemClickedNotHandledByFragment() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		when(mockFragment.onOptionsItemSelected(mockMenuItem)).thenReturn(false);
		assertThat(new ActionBarFragment.ActionModeCallback(mockFragment).onActionItemClicked(mockActionMode, mockMenuItem), is(false));
		verify(mockFragment, times(1)).onOptionsItemSelected(mockMenuItem);
		verifyZeroInteractions(mockActionMode);
	}

	@Test
	public void testOnActionItemClickedWithoutAttachedFragment() {
		// Only ensure that the callback does not cause any troubles when it has no fragment attached.
		final ActionMode mockActionMode = mock(ActionMode.class);
		final MenuItem mockMenuItem = mock(MenuItem.class);
		assertThat(new ActionBarFragment.ActionModeCallback().onActionItemClicked(mockActionMode, mockMenuItem), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenuItem);
	}

	@Test
	public void testOnDestroyActionMode() {
		final ActionBarFragment mockFragment = mock(TestFragment.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		new ActionBarFragment.ActionModeCallback(mockFragment).onDestroyActionMode(mockActionMode);
		verifyZeroInteractions(mockActionMode);
		verify(mockFragment, times(1)).onActionModeFinished();
	}

	@Test
	public void testOnDestroyActionModeWithoutAttachedFragment() {
		// Only ensure that the callback does not cause any troubles when it has no fragment attached.
		final ActionMode mockActionMode = mock(ActionMode.class);
		new ActionBarFragment.ActionModeCallback().onDestroyActionMode(mockActionMode);
		verifyZeroInteractions(mockActionMode);
	}

	public static class TestFragment extends ActionBarFragment {
	}
}
