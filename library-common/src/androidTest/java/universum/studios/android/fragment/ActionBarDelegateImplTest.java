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

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.BaseInstrumentedTest;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ActionBarDelegateImplTest extends BaseInstrumentedTest {

	/**
	 * Log TAG.
	 */
	@SuppressWarnings("unused")
	private static final String TAG = "ActionBarDelegateImplTest";

	@Test
	public void testSetDisplayHomeAsUpEnabled() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setDisplayHomeAsUpEnabled(true);
		verify(mockActionBar, times(1)).setDisplayHomeAsUpEnabled(true);
		delegate.setDisplayHomeAsUpEnabled(false);
		verify(mockActionBar, times(1)).setDisplayHomeAsUpEnabled(false);
	}

	@Test
	public void testSetDisplayHomeAsUpEnabledWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setDisplayHomeAsUpEnabled(true);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetHomeAsUpIndicatorAsResource() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setHomeAsUpIndicator(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@Test
	@SuppressWarnings("deprecation")
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetHomeAsUpVectorIndicator() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setHomeAsUpVectorIndicator(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(any(Drawable.class));
	}

	@Test
	public void testSetHomeAsUpVectorIndicatorWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setHomeAsUpIndicator(android.R.drawable.ic_delete);
	}

	@Test
	@SuppressWarnings("deprecation")
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	public void testSetHomeAsUpIndicator() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		final Drawable indicator = mContext.getResources().getDrawable(android.R.drawable.ic_delete);
		delegate.setHomeAsUpIndicator(indicator);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(indicator);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testSetHomeAsUpIndicatorWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setHomeAsUpIndicator(mContext.getResources().getDrawable(android.R.drawable.ic_delete));
	}

	@Test
	public void testSetTitleAsResource() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setTitle(android.R.string.dialog_alert_title);
		verify(mockActionBar, times(1)).setTitle(mContext.getText(android.R.string.dialog_alert_title));
	}

	@Test
	public void testSetTitle() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setTitle(mContext.getText(android.R.string.dialog_alert_title));
		verify(mockActionBar, times(1)).setTitle(mContext.getText(android.R.string.dialog_alert_title));
	}

	@Test
	public void testSetTitleWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setTitle("Title");
	}

	@Test
	public void testSetIconAsResource() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		delegate.setIcon(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setIcon(android.R.drawable.ic_delete);
	}

	@Test
	public void testSetIconAsResourceWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setIcon(android.R.drawable.ic_delete);
	}

	@Test
	@SuppressWarnings("deprecation")
	public void testSetIcon() {
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(mContext, mockActionBar);
		final Drawable icon = mContext.getResources().getDrawable(android.R.drawable.ic_delete);
		delegate.setIcon(icon);
		verify(mockActionBar, times(1)).setIcon(icon);
	}

	@Test
	public void testSetIconWithoutActionBar() {
		// Only ensure that the delegate does not cause any troubles when it does not have ActionBar.
		new ActionBarDelegate.Impl(mContext, null).setIcon(null);
	}
}
