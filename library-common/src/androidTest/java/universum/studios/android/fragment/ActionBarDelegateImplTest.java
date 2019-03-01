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
import android.graphics.drawable.Drawable;
import android.os.Build;

import org.junit.Test;

import androidx.annotation.RequiresApi;
import universum.studios.android.test.AndroidTestCase;

import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class ActionBarDelegateImplTest extends AndroidTestCase {

	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Test public void testSetHomeAsUpVectorIndicator() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
		// Arrange:
		final ActionBar mockActionBar = mock(ActionBar.class);
		final ActionBarDelegate delegate = new ActionBarDelegate.Impl(context(), mockActionBar);
		// Act:
		delegate.setHomeAsUpVectorIndicator(android.R.drawable.ic_delete);
		// Assert:
		verify(mockActionBar).setHomeAsUpIndicator(any(Drawable.class));
		verifyNoMoreInteractions(mockActionBar);
	}
}