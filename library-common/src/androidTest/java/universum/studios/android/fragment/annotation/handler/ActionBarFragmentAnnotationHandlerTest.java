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
package universum.studios.android.fragment.annotation.handler;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.test.runner.AndroidJUnit4;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.ActionBarDelegate;
import universum.studios.android.fragment.ActionBarFragment;
import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.ActionModeOptions;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.MenuOptions;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ActionBarFragmentAnnotationHandlerTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "ActionBarFragmentAnnotationHandlerTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testActionBarOptions() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setDisplayHomeAsUpEnabled(true);
		verify(mockActionBarDelegate, times(1)).setHomeAsUpIndicator(android.R.drawable.ic_lock_lock);
		verify(mockActionBarDelegate, times(0)).setHomeAsUpVectorIndicator(anyInt());
		verify(mockActionBarDelegate, times(1)).setIcon(android.R.drawable.ic_delete);
		verify(mockActionBarDelegate, times(1)).setTitle(android.R.string.ok);
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(0), is(0));
		assertThat(annotationHandler.handleCreateActionMode(mock(ActionMode.class), mock(Menu.class)), is(false));
	}

	@Test
	public void testActionBarOptionsWithDisabledHomeAsUp() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithDisabledHomeAsUp.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setDisplayHomeAsUpEnabled(false);
	}

	@Test
	public void testActionBarOptionsWithUnchangedHomeAsUp() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(0)).setDisplayHomeAsUpEnabled(anyBoolean());
	}

	@Test
	public void testActionBarOptionsWithNoneHomeAsUpIndicator() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneHomeAsUpIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setHomeAsUpIndicator(any(ColorDrawable.class));
		verify(mockActionBarDelegate, times(0)).setHomeAsUpVectorIndicator(anyInt());
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(anyInt());
	}

	@Test
	public void testActionBarOptionsWithUnchangedHomeAsUpIndicator() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(any(Drawable.class));
		verify(mockActionBarDelegate, times(0)).setHomeAsUpVectorIndicator(anyInt());
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(anyInt());
	}

	@Test
	public void testActionBarOptionsWithHomeAsUpVectorIndicator() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithHomeAsUpVectorIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setHomeAsUpVectorIndicator(android.R.drawable.ic_lock_lock);
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(anyInt());
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(any(Drawable.class));
	}

	@Test
	public void testActionBarOptionsWithNoneHomeAsUpVectorIndicator() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneHomeAsUpVectorIndicator.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setHomeAsUpIndicator(any(ColorDrawable.class));
		verify(mockActionBarDelegate, times(0)).setHomeAsUpVectorIndicator(anyInt());
		verify(mockActionBarDelegate, times(0)).setHomeAsUpIndicator(anyInt());
	}

	@Test
	public void testActionBarOptionsWithUnchangedIcon() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(0)).setIcon(anyInt());
		verify(mockActionBarDelegate, times(0)).setIcon(any(Drawable.class));
	}

	@Test
	public void testActionBarOptionsWithNoneIcon() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneIcon.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setIcon(any(ColorDrawable.class));
		verify(mockActionBarDelegate, times(0)).setIcon(anyInt());
	}

	@Test
	public void testActionBarOptionsWithNoneTitle() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionBarOptionsWithNoneTitle.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(1)).setTitle("");
		verify(mockActionBarDelegate, times(0)).setTitle(anyInt());
	}

	@Test
	public void testActionBarOptionsWithUnchangedTitle() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verify(mockActionBarDelegate, times(0)).setTitle(anyInt());
		verify(mockActionBarDelegate, times(0)).setTitle(any(CharSequence.class));
	}

	@Test
	public void testMenuOptions() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithMenuOptions.class);
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(true));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(TestFragmentWithMenuOptions.MENU_RESOURCE));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(MenuOptions.IGNORE_SUPER));
	}

	@Test
	public void testEmptyMenuOptions() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyMenuOptions.class);
		assertThat(annotationHandler.hasOptionsMenu(), is(true));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(-1));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(MenuOptions.DEFAULT));
	}

	@Test
	@SuppressWarnings("ResourceType")
	public void testActionModeOptions() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithActionModeOptions.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(true));
		verify(mockActionMode, times(1)).getMenuInflater();
		verify(mockMenuInflater, times(1)).inflate(TestFragmentWithActionModeOptions.MENU_RESOURCE, mockMenu);
	}

	@Test
	public void testActionModeOptionsWithoutMenu() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithEmptyActionBarOptions.class);
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenuInflater);
		verifyZeroInteractions(mockMenu);
	}

	@Test
	public void testFragmentWithoutAnnotation() {
		final ActionBarFragmentAnnotationHandler annotationHandler = new ActionBarAnnotationHandlers.ActionBarFragmentHandler(TestFragmentWithoutAnnotation.class);
		final ActionBarDelegate mockActionBarDelegate = mock(ActionBarDelegate.class);
		annotationHandler.configureActionBar(mockActionBarDelegate);
		verifyZeroInteractions(mockActionBarDelegate);
		assertThat(annotationHandler.hasOptionsMenu(), is(false));
		assertThat(annotationHandler.shouldClearOptionsMenu(), is(false));
		assertThat(annotationHandler.getOptionsMenuResource(-1), is(-1));
		assertThat(annotationHandler.getOptionsMenuFlags(-1), is(-1));
		final ActionMode mockActionMode = mock(ActionMode.class);
		final Menu mockMenu = mock(Menu.class);
		final MenuInflater mockMenuInflater = mock(MenuInflater.class);
		when(mockActionMode.getMenuInflater()).thenReturn(mockMenuInflater);
		assertThat(annotationHandler.handleCreateActionMode(mockActionMode, mockMenu), is(false));
		verifyZeroInteractions(mockActionMode);
		verifyZeroInteractions(mockMenuInflater);
		verifyZeroInteractions(mockMenu);
	}

	@ActionBarOptions(
			title = android.R.string.ok,
			icon = android.R.drawable.ic_delete,
			homeAsUp = ActionBarOptions.HOME_AS_UP_ENABLED,
			homeAsUpIndicator = android.R.drawable.ic_lock_lock
	)
	public static class TestFragmentActionBarOptions extends ActionBarFragment {
	}

	@ActionBarOptions
	public static class TestFragmentWithEmptyActionBarOptions extends ActionBarFragment {
	}

	@ActionBarOptions(homeAsUp = ActionBarOptions.HOME_AS_UP_DISABLED)
	public static class TestFragmentWithActionBarOptionsWithDisabledHomeAsUp extends ActionBarFragment {
	}

	@ActionBarOptions(homeAsUpIndicator = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneHomeAsUpIndicator extends ActionBarFragment {
	}

	@ActionBarOptions(homeAsUpVectorIndicator = android.R.drawable.ic_lock_lock)
	public static class TestFragmentWithActionBarOptionsWithHomeAsUpVectorIndicator extends ActionBarFragment {
	}

	@ActionBarOptions(homeAsUpVectorIndicator = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneHomeAsUpVectorIndicator extends ActionBarFragment {
	}

	@ActionBarOptions(icon = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneIcon extends ActionBarFragment {
	}

	@ActionBarOptions(title = ActionBarOptions.NONE)
	public static class TestFragmentWithActionBarOptionsWithNoneTitle extends ActionBarFragment {
	}

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
	public static class TestFragmentWithEmptyMenuOptions extends ActionBarFragment {
	}

	@SuppressWarnings("ResourceType")
	@ActionModeOptions(menu = TestFragmentWithActionModeOptions.MENU_RESOURCE)
	public static class TestFragmentWithActionModeOptions extends ActionBarFragment {

		static final int MENU_RESOURCE = 1;
	}

	@ActionModeOptions
	public static class TestFragmentWithEmptyActionModeOptions extends ActionBarFragment {
	}

	public static class TestFragmentWithoutAnnotation extends ActionBarFragment {
	}
}
