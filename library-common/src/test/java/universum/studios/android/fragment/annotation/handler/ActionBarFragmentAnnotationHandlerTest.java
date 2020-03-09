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
package universum.studios.android.fragment.annotation.handler;

import android.graphics.drawable.ColorDrawable;
import android.view.Menu;
import android.view.MenuInflater;

import org.junit.Test;

import androidx.appcompat.view.ActionMode;
import universum.studios.android.fragment.ActionBarDelegate;
import universum.studios.android.fragment.ActionBarFragment;
import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.ActionModeOptions;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.MenuOptions;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarFragmentAnnotationHandlerTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testActionBarOptions() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(0), is(0));
		assertThat(annotationHandler.handleCreateActionMode(mock(ActionMode.class), mock(Menu.class)), is(false));
		verify(mockActionBarDelegate).setDisplayHomeAsUpEnabled(true);
		verify(mockActionBarDelegate).setHomeAsUpIndicator(android.R.drawable.ic_lock_lock);
		verify(mockActionBarDelegate, times(0)).setHomeAsUpVectorIndicator(anyInt());
		verify(mockActionBarDelegate).setIcon(android.R.drawable.ic_delete);
		verify(mockActionBarDelegate).setTitle(android.R.string.ok);
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithDisabledHomeAsUp() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithDisabledHomeAsUp.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setDisplayHomeAsUpEnabled(false);
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithUnchangedHomeAsUp() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verifyNoInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithNoneHomeAsUpIndicator() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneHomeAsUpIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setHomeAsUpIndicator(any(ColorDrawable.class));
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithUnchangedHomeAsUpIndicator() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verifyNoInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithHomeAsUpVectorIndicator() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithHomeAsUpVectorIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setHomeAsUpVectorIndicator(android.R.drawable.ic_lock_lock);
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithNoneHomeAsUpVectorIndicator() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneHomeAsUpVectorIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setHomeAsUpIndicator(any(ColorDrawable.class));
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithUnchangedIcon() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verifyNoInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithNoneIcon() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneIcon.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setIcon(any(ColorDrawable.class));
		verifyNoMoreInteractions(mockActionBarDelegate);
	}

	@Test public void testActionBarOptionsWithNoneTitle() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneTitle.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verify(mockActionBarDelegate).setTitle("");
		verify(mockActionBarDelegate, times(0)).setTitle(anyInt());
	}

	@Test public void testActionBarOptionsWithUnchangedTitle() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		// Act:
		annotationHandler.configureActionBar(mockActionBarDelegate);
		// Assert:
		verifyNoInteractions(mockActionBarDelegate);
	}

	@Test public void testMenuOptions() {
		// Arrange + Act:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithMenuOptions.class);
		// Assert:
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(true));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(TestFragmentWithMenuOptions.MENU_RESOURCE));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(MenuOptions.IGNORE_SUPER));
	}

	@Test public void testEmptyMenuOptions() {
		// Arrange + Act:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyMenuOptions.class);
		// Assert:
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(-1));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(MenuOptions.DEFAULT));
	}

	@SuppressWarnings("ResourceType")
	@Test public void testActionModeOptions() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionModeOptions.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		// Act + Assert:
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(true));
		verify(mockActionMode).getMenuInflater();
		verify(mockMenuInflater).inflate(TestFragmentWithActionModeOptions.MENU_RESOURCE, mockMenu);
	}

	@Test public void testActionModeOptionsWithoutMenu() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		// Act + Assert:
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyNoInteractions(mockActionMode, mockMenuInflater, mockMenu);
	}

	@Test public void testFragmentWithoutAnnotation() {
		// Arrange:
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithoutAnnotation.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verifyNoInteractions(mockActionBarDelegate);
		assertThat(annotationHandler.hasOptionsMenu(), is(false));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(-1));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(-1));
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		// Act + Assert:
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyNoInteractions(mockActionMode, mockMenuInflater, mockMenu);
	}

	@ActionBarOptions(
			title = android.R.string.ok,
			icon = android.R.drawable.ic_delete,
			homeAsUp = ActionBarOptions.HOME_AS_UP_ENABLED,
			homeAsUpIndicator = android.R.drawable.ic_lock_lock
	)
	public static class TestFragmentActionBarOptions extends ActionBarFragment {}

	@ActionBarOptions
	public static class TestFragmentWithEmptyActionBarOptions extends ActionBarFragment {}

	@ActionBarOptions(homeAsUp = ActionBarOptions.HOME_AS_UP_DISABLED)
	public static class TestFragmentWithActionBarOptionsWithDisabledHomeAsUp extends ActionBarFragment {}

	@ActionBarOptions(homeAsUpIndicator = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneHomeAsUpIndicator extends ActionBarFragment {}

	@ActionBarOptions(homeAsUpVectorIndicator = android.R.drawable.ic_lock_lock)
	public static class TestFragmentWithActionBarOptionsWithHomeAsUpVectorIndicator extends ActionBarFragment {}

	@ActionBarOptions(homeAsUpVectorIndicator = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneHomeAsUpVectorIndicator extends ActionBarFragment {}

	@ActionBarOptions(icon = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneIcon extends ActionBarFragment {}

	@ActionBarOptions(title = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneTitle extends ActionBarFragment {}

	@SuppressWarnings("ResourceType")
	@MenuOptions(
			value = TestFragmentWithMenuOptions.MENU_RESOURCE,
			clear = true,
			flags = MenuOptions.IGNORE_SUPER
	)
	public static class TestFragmentWithMenuOptions extends ActionBarFragment {

		static final int MENU_RESOURCE = 1;
	}

	@MenuOptions
	public static class TestFragmentWithEmptyMenuOptions extends ActionBarFragment {}

	@SuppressWarnings("ResourceType")
	@ActionModeOptions(menu = TestFragmentWithActionModeOptions.MENU_RESOURCE)
	public static class TestFragmentWithActionModeOptions extends ActionBarFragment {

		static final int MENU_RESOURCE = 1;
	}

	@ActionModeOptions
	public static class TestFragmentWithEmptyActionModeOptions extends ActionBarFragment {}

	public static class TestFragmentWithoutAnnotation extends ActionBarFragment {}
}