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

import android.graphics.drawable.Drawable;

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateSupportTest extends InstrumentedTestCase {

	@Test
	@SuppressWarnings("deprecation")
	public void testSetHomeAsUpVectorIndicator() {
		final android.support.v7.app.ActionBar mockActionBar = mock(android.support.v7.app.ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.SupportImpl(mContext, mockActionBar);
		delegate.setHomeAsUpVectorIndicator(android.R.drawable.ic_delete);
		verify(mockActionBar, times(1)).setHomeAsUpIndicator(any(Drawable.class));
	}
}
