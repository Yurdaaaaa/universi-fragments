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
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;

import org.junit.Test;
import org.robolectric.annotation.Config;

import universum.studios.android.test.AndroidTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateImplTest extends AndroidTestCase {

	@Test public void testDisplayHomeAsUpEnabled() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act + Assert:
		delegate.setDisplayHomeAsUpEnabled(true);
		verify(mockActionBar).setDisplayHomeAsUpEnabled(true);
		delegate.setDisplayHomeAsUpEnabled(false);
		verify(mockActionBar).setDisplayHomeAsUpEnabled(false);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testDisplayHomeAsUpEnabledWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setDisplayHomeAsUpEnabled(true);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Test public void testHomeAsUpIndicatorAsResource() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act:
		delegate.setHomeAsUpIndicator(android.R.drawable.ic_delete);
		// Assert:
		verify(mockActionBar).setHomeAsUpIndicator(android.R.drawable.ic_delete);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testHomeAsUpVectorIndicatorWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Test public void testHomeAsUpIndicator() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		final Drawable indicator = new ColorDrawable(Color.WHITE);
		// Act:
		delegate.setHomeAsUpIndicator(indicator);
		// Assert:
		verify(mockActionBar).setHomeAsUpIndicator(indicator);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testHomeAsUpIndicatorWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setHomeAsUpIndicator(new ColorDrawable(Color.WHITE));
	}

	@Test public void testTitleAsResource() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act:
		delegate.setTitle(android.R.string.dialog_alert_title);
		// Assert:
		verify(mockActionBar).setTitle(context().getText(android.R.string.dialog_alert_title));
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testTitle() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act:
		delegate.setTitle(context().getText(android.R.string.dialog_alert_title));
		// Assert:
		verify(mockActionBar).setTitle(context().getText(android.R.string.dialog_alert_title));
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testTitleWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setTitle("Title");
	}

	@Test public void testIconAsResource() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act:
		delegate.setIcon(android.R.drawable.ic_delete);
		// Assert:
		verify(mockActionBar).setIcon(android.R.drawable.ic_delete);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testIconAsResourceWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setIcon(android.R.drawable.ic_delete);
	}

	@Test public void testIcon() {
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		final Drawable icon = context().getResources().getDrawable(android.R.drawable.ic_delete);
		// Act:
		delegate.setIcon(icon);
		// Assert:
		verify(mockActionBar).setIcon(icon);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testIconWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(context(), null).setIcon(null);
	}
}