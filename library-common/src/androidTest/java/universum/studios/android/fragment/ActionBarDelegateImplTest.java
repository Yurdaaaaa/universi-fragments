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

import org.junit.Test;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateImplTest extends InstrumentedTestCase {

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
}
