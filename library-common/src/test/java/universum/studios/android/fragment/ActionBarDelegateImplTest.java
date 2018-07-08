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

import universum.studios.android.test.local.RobolectricTestCase;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateImplTest extends RobolectricTestCase {

	@Test
	public void testSetDisplayHomeAsUpEnabled() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		delegate.setDisplayHomeAsUpEnabled(true);
		verify(mockActionBar, times(1)).setDisplayHomeAsUpEnabled(true);
		delegate.setDisplayHomeAsUpEnabled(false);
		verify(mockActionBar, times(1)).setDisplayHomeAsUpEnabled(false);
	}

	@Test
	public void testSetDisplayHomeAsUpEnabledWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setDisplayHomeAsUpEnabled(true);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetHomeAsUpIndicatorAsResource() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		delegate.setHomeAsUpIndicator(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@Test
	public void testSetHomeAsUpVectorIndicatorWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@Test
	@SuppressWarnings("deprecation")
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetHomeAsUpIndicator() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		final Drawable indicator = new ColorDrawable(Color.WHITE);
		delegate.setHomeAsUpIndicator(indicator);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(indicator);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testSetHomeAsUpIndicatorWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setHomeAsUpIndicator(new ColorDrawable(Color.WHITE));
	}

	@Test
	public void testSetTitleAsResource() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		delegate.setTitle(android.R.string.dialog_alert_title);
		verify(mockActionBar, times(1)).setTitle(application.getText(android.R.string.dialog_alert_title));
	}

	@Test
	public void testSetTitle() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		delegate.setTitle(application.getText(android.R.string.dialog_alert_title));
		verify(mockActionBar, times(1)).setTitle(application.getText(android.R.string.dialog_alert_title));
	}

	@Test
	public void testSetTitleWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setTitle("Title");
	}

	@Test
	public void testSetIconAsResource() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		delegate.setIcon(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setIcon(android.R.drawable.ic_delete);
	}

	@Test
	public void testSetIconAsResourceWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setIcon(android.R.drawable.ic_delete);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testSetIcon() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(application, mockActionBar);
		final Drawable icon = application.getResources().getDrawable(android.R.drawable.ic_delete);
		delegate.setIcon(icon);
		verify(mockActionBar, times(1)).setIcon(icon);
	}

	@Test
	public void testSetIconWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(application, null).setIcon(null);
	}
}