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
import android.view.Menu;
import android.view.MenuInflater;

import org.junit.Test;
import org.robolectric.Robolectric;

import androidx.appcompat.view.ActionMode;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.MenuOptions;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;
import universum.studios.android.test.AndroidTestCase;
import universum.studios.android.test.TestActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarFragmentTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testOnCreateAnnotationHandler() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		// Act:
		final ActionBarFragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		// Assert:
		assertThat(annotationHandler, is(notNullValue()));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test public void testGetAnnotationHandler() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.getAnnotationHandler(), is(notNullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final TestFragment fragment = new TestFragment();
		// Act:
		fragment.getAnnotationHandler();
	}

	@Test public void testOnCreate() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@Test public void testOnCreateForFragmentWithoutMenu() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@Test public void testOnCreateWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
	}

	@SuppressWarnings("ResourceType")
	@Test public void testOnCreateOptionsMenu() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithMenuOptions();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verify(mockMenuInflater).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
		verifyNoMoreInteractions(mockMenuInflater);
		verifyZeroInteractions(mockMenu);
	}

	@SuppressWarnings("ResourceType")
	@Test public void testOnCreateOptionsMenuIgnoringSuper() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsIgnoringSuper();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verify(mockMenuInflater).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
		verifyNoMoreInteractions(mockMenuInflater);
		verifyZeroInteractions(mockMenu);
	}

	@SuppressWarnings("ResourceType")
	@Test public void testOnCreateOptionsMenuBeforeSuper() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsBeforeSuper();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verify(mockMenuInflater).inflate(TestFragmentWithMenuOptions.MENU_RESOURCE, mockMenu);
		verifyNoMoreInteractions(mockMenuInflater);
		verifyZeroInteractions(mockMenu);
	}

	@Test public void testOnCreateOptionsMenuWithoutMenuResource() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithMenuOptionsWithoutResource();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verify(mockMenu).clear();
		verifyNoMoreInteractions(mockMenu);
		verifyZeroInteractions(mockMenuInflater);
	}

	@Test public void testOnCreateOptionsMenuForFragmentWithoutMenu() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verifyZeroInteractions(mockMenu, mockMenuInflater);
	}

	@Test public void testOnCreateOptionsMenuWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final ActionBarFragment fragment = new TestFragment();
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		// Act:
		fragment.onCreateOptionsMenu(mockMenu, mockMenuInflater);
		// Assert:
		verifyZeroInteractions(mockMenu, mockMenuInflater);
	}

	@Test public void testOnActivityCreated() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final ActionBarFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isActionBarAvailable(), is(true));
		assertThat(fragment.getActionBarDelegate(), is(notNullValue()));
	}

	@Test public void testIsActionBarAvailable() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		fragment.actionBarDelegate = mock(ActionBarDelegate.class);
		// Act + Assert:
		assertThat(fragment.isActionBarAvailable(), is(true));
	}

	@Test public void testIsActionBarAvailableWhenNotAttached() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.isActionBarAvailable(), is(false));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetActionBarDelegateWhenNotAttached() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		fragment.getActionBarDelegate();
	}

	@Test public void testGetActionBar() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockDelegate = mock(ActivityDelegate.class);
		fragment.activityDelegate = mockDelegate;
		final ActionBar mockActionBar = mock(ActionBar.class);
		when(mockDelegate.getActionBar()).thenReturn(mockActionBar);
		// Act + Assert:
		assertThat(fragment.getActionBar(), is(mockActionBar));
		verify(mockDelegate).getActionBar();
		verifyNoMoreInteractions(mockDelegate);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetActionBarWhenNotAvailable() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		fragment.getActionBar();
	}

	@Test public void testGetSupportActionBar() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockDelegate = mock(ActivityDelegate.class);
		fragment.activityDelegate = mockDelegate;
		final androidx.appcompat.app.ActionBar mockActionBar = mock(androidx.appcompat.app.ActionBar.class);
		when(mockDelegate.getSupportActionBar()).thenReturn(mockActionBar);
		// Act + Assert:
		assertThat(fragment.getSupportActionBar(), is(mockActionBar));
		verify(mockDelegate).getSupportActionBar();
		verifyNoMoreInteractions(mockDelegate);
	}

	@Test(expected = IllegalStateException.class)
	public void testGetSupportActionBarWhenNotAvailable() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		fragment.getSupportActionBar();
	}

	@Test public void testInvalidateActionBar() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		fragment.actionBarDelegate = mockActionBarDelegate;
		// Act:
		fragment.invalidateActionBar();
		// Assert:
		verify(mockActionBarDelegate).setIcon(TestFragment.ICON_RESOURCE);
		verify(mockActionBarDelegate).setTitle(TestFragment.TITLE_RESOURCE);
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testInvalidateActionBarForFragmentWithoutMenu() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragmentWithoutAnnotation();
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		fragment.actionBarDelegate = mockActionBarDelegate;
		// Act:
		fragment.invalidateActionBar();
		// Assert:
		verifyZeroInteractions(mockActionBarDelegate);
	}

	@Test public void testInvalidateActionBarWhenNotAttached() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that invocation of the method does not cause any troubles.
		fragment.invalidateActionBar();
	}

	@Test public void testInvalidateActionBarWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final ActionBarFragment fragment = new TestFragment();
		fragment.activityDelegate = mock(ActivityDelegate.class);
		// Act:
		// Only ensure that invocation of the method does not cause any troubles.
		fragment.invalidateActionBar();
	}

	@Test public void testStartActionMode() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(mockActionMode);
		fragment.activityDelegate = mockActivityDelegate;
		// Act + Assert:
		assertThat(fragment.startActionMode(), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
	}

	@Test public void testStartActionModeWhenAlreadyIn() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(mockActionMode);
		fragment.activityDelegate = mockActivityDelegate;
		// Act + Assert:
		assertThat(fragment.startActionMode(), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
		assertThat(fragment.startActionMode(), is(false));
	}

	@Test public void testStartActionModeWhenNotAttached() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.startActionMode(), is(false));
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
	}

	@Test public void testStartActionModeForInvalidActionMode() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		when(mockActivityDelegate.startActionMode(any(ActionMode.Callback.class))).thenReturn(null);
		fragment.activityDelegate = mockActivityDelegate;
		// Act + Assert:
		assertThat(fragment.startActionMode(), is(false));
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
	}

	@Test public void testStartActionModeWithCallback() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActivityDelegate mockActivityDelegate = mock(ActivityDelegate.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionMode.Callback mockCallback = mock(ActionMode.Callback.class);
		when(mockActivityDelegate.startActionMode(mockCallback)).thenReturn(mockActionMode);
		fragment.activityDelegate = mockActivityDelegate;
		// Act + Assert:
		assertThat(fragment.startActionMode(mockCallback), is(true));
		assertThat(fragment.isInActionMode(), is(true));
		assertThat(fragment.getActionMode(), is(mockActionMode));
	}

	@Test public void testFinishActionMode() {
		// Arrange:
		final ActionBarFragment fragment = new TestFragment();
		final ActionMode mockActionMode = mock(ActionMode.class);
		fragment.onActionModeStarted(mockActionMode);
		// Act:
		fragment.onActionModeFinished();
		// Assert:
		assertThat(fragment.isInActionMode(), is(false));
		assertThat(fragment.getActionMode(), is(nullValue()));
		verifyZeroInteractions(mockActionMode);
	}

	@Test public void testOnBackPress() {
		// Arrange:
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionBarFragment fragment = new TestFragment();
		fragment.onActionModeStarted(mockActionMode);
		// Act + Assert:
		assertThat(fragment.onBackPress(), is(true));
		verify(mockActionMode).finish();
		verifyNoMoreInteractions(mockActionMode);
	}

	@Test public void testOnBackPressWithoutStartedActionMode() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.onBackPress(), is(false));
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
	public static class TestFragmentWithMenuOptionsWithoutResource extends ActionBarFragment {}

	@SuppressWarnings("ResourceType")
	@MenuOptions(
			value = TestFragmentWithMenuOptions.MENU_RESOURCE,
			flags = MenuOptions.IGNORE_SUPER
	)
	public static class TestFragmentWithMenuOptionsIgnoringSuper extends TestFragmentWithMenuOptions {}

	@SuppressWarnings("ResourceType")
	@MenuOptions(
			value = TestFragmentWithMenuOptions.MENU_RESOURCE,
			flags = MenuOptions.BEFORE_SUPER
	)
	public static class TestFragmentWithMenuOptionsBeforeSuper extends TestFragmentWithMenuOptions {}

	public static class TestFragmentWithoutAnnotation extends ActionBarFragment {}
}