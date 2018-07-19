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

import android.graphics.drawable.Drawable;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateSupportTest extends RobolectricTestCase {

	@Test public void testDisplayHomeAsUpEnabled() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
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
		new ActionBarDelegate.SupportImpl(application, null).setDisplayHomeAsUpEnabled(true);
	}

	@Test public void testHomeAsUpIndicatorAsResource() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		// Act:
		delegate.setHomeAsUpIndicator(android.R.drawable.ic_delete);
		// Assert:
		verify(mockActionBar).setHomeAsUpIndicator(android.R.drawable.ic_delete);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testHomeAsUpVectorIndicatorWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.SupportImpl(application, null).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@SuppressWarnings("deprecation")
	@Test public void testHomeAsUpIndicator() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		final Drawable indicator = application.getResources().getDrawable(android.R.drawable.ic_delete);
		// Act:
		delegate.setHomeAsUpIndicator(indicator);
		// Assert:
		verify(mockActionBar).setHomeAsUpIndicator(indicator);
		verifyNoMoreInteractions(mockActionBar);
	}

	@SuppressWarnings("deprecation")
	@Test public void testHomeAsUpIndicatorWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.SupportImpl(application, null).setHomeAsUpIndicator(application.getResources().getDrawable(android.R.drawable.ic_delete));
	}

	@Test public void testTitleAsResource() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		// Act:
		delegate.setTitle(android.R.string.dialog_alert_title);
		// Assert:
		verify(mockActionBar).setTitle(application.getText(android.R.string.dialog_alert_title));
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testTitle() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		// Act:
		delegate.setTitle(application.getText(android.R.string.dialog_alert_title));
		// Assert:
		verify(mockActionBar).setTitle(application.getText(android.R.string.dialog_alert_title));
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testTitleWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.SupportImpl(application, null).setTitle("Title");
	}

	@Test public void testIconAsResource() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		// Act:
		delegate.setIcon(android.R.drawable.ic_delete);
		// Assert:
		verify(mockActionBar).setIcon(android.R.drawable.ic_delete);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testIconAsResourceWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.SupportImpl(application, null).setIcon(android.R.drawable.ic_delete);
	}

	@SuppressWarnings("deprecation")
	@Test public void testIcon() {
		// Arrange:
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(application, mockActionBar);
		final Drawable icon = application.getResources().getDrawable(android.R.drawable.ic_delete);
		// Act:
		delegate.setIcon(icon);
		// Assert:
		verify(mockActionBar).setIcon(icon);
		verifyNoMoreInteractions(mockActionBar);
	}

	@Test public void testSetIconWithoutActionBar() {
		// Act:
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.SupportImpl(application, null).setIcon(null);
	}
}