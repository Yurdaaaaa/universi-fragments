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
import android.app.FragmentManager;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;

import org.junit.Test;
import org.robolectric.Robolectric;

import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.MenuOptions;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;
import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarFragmentTest extends RobolectricTestCase {
    
	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testOnCreateAnnotationHandler() {
		final ActionBarFragment fragment = new TestFragment();
		final ActionBarFragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		assertThat(annotationHandler, is(not(nullValue())));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test
	public void testGetAnnotationHandler() {
		assertThat(new TestFragment().getAnnotationHandler(), is(not(nullValue())));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		new TestFragment().getAnnotationHandler();
	}

	@Test
	public void testOnCreate() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@Test
	public void testOnCreateForFragmentWithoutMenu() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@Test
	public void testOnCreateWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testOnCreateOptionsMenu() {
		final ActionBarFragment fragment = new TestFragmentWithMenuOptions();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verifyZeroInteractions(mockMenu);
		verify(mockMenuInflater, times(1)).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testOnCreateOptionsMenuIgnoringSuper() {
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsIgnoringSuper();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verifyZeroInteractions(mockMenu);
		verify(mockMenuInflater, times(1)).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testOnCreateOptionsMenuBeforeSuper() {
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsBeforeSuper();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verifyZeroInteractions(mockMenu);
		verify(mockMenuInflater, times(1)).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
	}

	@Test
	public void testOnCreateOptionsMenuWithoutMenuResource() {
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsWithoutResource();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verify(mockMenu, times(1)).clear();
		verifyNoMoreInteractions(mockMenu);
		verifyZeroInteractions(mockMenuInflater);
	}

	@Test
	public void testOnCreateOptionsMenuForFragmentWithoutMenu() {
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verifyZeroInteractions(mockMenu);
		verifyZeroInteractions(mockMenuInflater);
	}

	@Test
	public void testOnCreateOptionsMenuWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		final ActionBarFragment fragment = new TestFragment();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		verifyZeroInteractions(mockMenu);
		verifyZeroInteractions(mockMenuInflater);
	}

	@Test
	public void testOnActivityCreated() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isActionBarAvailable(), is(true));
		assertThat(fragment.getActionBarDelegate(), is(not(nullValue())));
	}


	@Test
	public void testIsActionBarAvailable() {
		final ActionBarFragment fragment = new TestFragment();
		fragment.mActionBarDelegate = mock(ActionBarDelegate.class);
		assertThat(fragment.isActionBarAvailable(), is(true));
	}

	@Test
	public void testIsActionBarAvailableWhenNotAttached() {
		assertThat(new TestFragment().isActionBarAvailable(), is(false));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetActionBarDelegateWhenNotAttached() {
		new TestFragment().getActionBarDelegate();
	}

	@Test
	public void testGetActionBar() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockDelegate = mock(ActivityDelegate.class);
		fragment.mActivityDelegate = mockDelegate;
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockDelegate.getActionBar()).thenReturn(mockActionBar);
		assertThat(fragment.getActionBar(), is(mockActionBar));
		verify(mockDelegate, times(1)).getActionBar();
	}

	@Test(expected = IllegalStateException.class)
	public void testGetActionBarWhenNotAvailable() {
		new TestFragment().getActionBar();
	}

	@Test
	public void testGetSupportActionBar() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockDelegate = mock(ActivityDelegate.class);
		fragment.mActivityDelegate = mockDelegate;
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		when(mockDelegate.getSupportActionBar()).thenReturn(mockActionBar);
		assertThat(fragment.getSupportActionBar(), is(mockActionBar));
		verify(mockDelegate, times(1)).getSupportActionBar();
	}

	@Test(expected = IllegalStateException.class)
	public void testGetSupportActionBarWhenNotAvailable() {
		new TestFragment().getSupportActionBar();
	}

	@Test
	public void testInvalidateActionBar() {
		final ActionBarFragment fragment = new TestFragment();
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		fragment.mActionBarDelegate = mockActionBarDelegate;
		fragment.invalidateActionBar();
		verify(mockActionBarDelegate, times(1)).setIcon(TestFragment.ICON_RESOURCE);
		verify(mockActionBarDelegate, times(1)).setTitle(TestFragment.TITLE_RESOURCE);
	}

	@Test
	public void testInvalidateActionBarForFragmentWithoutMenu() {
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		fragment.mActionBarDelegate = mockActionBarDelegate;
		fragment.invalidateActionBar();
		verifyZeroInteractions(mockActionBarDelegate);
	}

	@Test
	public void testInvalidateActionBarWhenNotAttached() {
		// Only ensure that invocation of the method does not cause any troubles.
		new TestFragment().invalidateActionBar();
	}

	@Test
	public void testInvalidateActionBarWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		// Only ensure that invocation of the method does not cause any troubles.
		final ActionBarFragment fragment = new TestFragment();
		fragment.mActivityDelegate = mock(ActivityDelegate.class);
		fragment.invalidateActionBar();
	}

	@Test
	public void testStartActionMode() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(mockActionMode);
		fragment.mActivityDelegate = mockActivityDelegate;
		assertThat(fragment.startActionMode(), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
	}

	@Test
	public void testStartActionModeWhenAlreadyIn() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(mockActionMode);
		fragment.mActivityDelegate = mockActivityDelegate;
		assertThat(fragment.startActionMode(), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
		assertThat(fragment.startActionMode(), is(false));
	}

	@Test
	public void testStartActionModeWhenNotAttached() {
		final ActionBarFragment fragment = new TestFragment();
		assertThat(fragment.startActionMode(), is(false));
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
	}

	@Test
	public void testStartActionModeForInvalidActionMode() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(null);
		fragment.mActivityDelegate = mockActivityDelegate;
		assertThat(fragment.startActionMode(), is(false));
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
	}

	@Test
	public void testStartActionModeWithCallback() {
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionMode.Callback mockCallback = mock(ActionMode.Callback.class);
		when(mockActivityDelegate.startActionMode(mockCallback)).thenReturn(mockActionMode);
		fragment.mActivityDelegate = mockActivityDelegate;
		assertThat(fragment.startActionMode(mockCallback), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
	}

	@Test
	public void testFinishActionMode() {
		final ActionBarFragment fragment = new TestFragment();
		final ActionMode mockActionMode = mock(ActionMode.class);
		fragment.onActionModeStarted(mockActionMode);
		fragment.onActionModeFinished();
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
		verifyZeroInteractions(mockActionMode);
	}

	@Test
	public void testOnBackPress() {
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionBarFragment fragment = new TestFragment();
		fragment.onActionModeStarted(mockActionMode);
		assertThat(fragment.onBackPress(), is(true));
		verify(mockActionMode, times(1)).finish();
	}

	@Test
	public void testOnBackPressWithoutStartedActionMode() {
		assertThat(new TestFragment().onBackPress(), is(false));
	}

	@MenuOptions
	@ActionBarOptions(
			icon = TestFragment.ICON_RESOURCE,
			title = TestFragment.TITLE_RESOURCE
	)
	public static class TestFragment extends ActionBarFragment {

		static final int ICON_RESOURCE = android.R.drawable.ic_delete;
		static final int TITLE_RESOURCE = android.R.string.ok;
	}

	@SuppressWarnings("ResourceType")
	@MenuOptions(value = TestFragmentWithMenuOptions.MENU_RESOURCE)
	public static class TestFragmentWithMenuOptions extends ActionBarFragment {

		static final int MENU_RESOURCE = 1;
	}

	@MenuOptions(clear = true)
	public static class TestFragmentWithMenuOptionsWithoutResource extends ActionBarFragment {
	}

	@SuppressWarnings("ResourceType")
	@MenuOptions(
			value = TestFragmentWithMenuOptions.MENU_RESOURCE,
			flags = MenuOptions.IGNORE_SUPER
	)
	public static class TestFragmentWithMenuOptionsIgnoringSuper extends TestFragmentWithMenuOptions {
	}

	@SuppressWarnings("ResourceType")
	@MenuOptions(
			value = TestFragmentWithMenuOptions.MENU_RESOURCE,
			flags = MenuOptions.BEFORE_SUPER
	)
	public static class TestFragmentWithMenuOptionsBeforeSuper extends TestFragmentWithMenuOptions {
	}

	public static class TestFragmentWithoutAnnotation extends ActionBarFragment {
	}
}