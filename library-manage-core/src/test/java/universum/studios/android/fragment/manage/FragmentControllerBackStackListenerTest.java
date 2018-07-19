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
package universum.studios.android.fragment.manage;

import android.app.FragmentManager;

import org.junit.Test;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class FragmentControllerBackStackListenerTest extends RobolectricTestCase {

	@Test public void testInstantiation() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		// Act:
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		// Assert:
		assertThat(listener.controller, is(mockController));
		assertThat(listener.backStackSize, is(0));
	}

	@Test public void testOnBackStackChangedDueToAddition() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		// Act:
		listener.onBackStackChanged();
		// Assert:
		assertThat(listener.backStackSize, is(1));
		verify(mockController).handleBackStackChange(1, FragmentController.BackStackListener.ADDED);
	}

	@Test public void testOnBackStackChangedDueToRemoval() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.backStackSize = 2;
		// Act:
		listener.onBackStackChanged();
		// Assert:
		assertThat(listener.backStackSize, is(1));
		verify(mockController).handleBackStackChange(1, FragmentController.BackStackListener.REMOVED);
	}

	@Test public void testOnBackStackChangedWithSameSize() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(2);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.backStackSize = 2;
		// Act:
		listener.onBackStackChanged();
		// Assert:
		assertThat(listener.backStackSize, is(2));
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
	}

	@Test public void testOnBackStackChangedForEmptyStack() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		// Act:
		listener.onBackStackChanged();
		// Assert:
		assertThat(listener.backStackSize, is(0));
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
	}

	@Test public void testOnBackStackChangedForInvalidBackStackSize() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(-1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		// Act:
		listener.onBackStackChanged();
		// Assert:
		assertThat(listener.backStackSize, is(0));
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
	}
}