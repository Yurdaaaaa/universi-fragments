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

	@Test
	public void testInstantiation() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		assertThat(listener.controller, is(mockController));
		assertThat(listener.backStackSize, is(0));
	}

	@Test
	public void testOnBackStackChangedDueToAddition() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.onBackStackChanged();
		verify(mockController, times(1)).handleBackStackChange(1, FragmentController.BackStackListener.ADDED);
		assertThat(listener.backStackSize, is(1));
	}

	@Test
	public void testOnBackStackChangedDueToRemoval() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.backStackSize = 2;
		listener.onBackStackChanged();
		verify(mockController, times(1)).handleBackStackChange(1, FragmentController.BackStackListener.REMOVED);
		assertThat(listener.backStackSize, is(1));
	}

	@Test
	public void testOnBackStackChangedWithSameSize() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(2);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.backStackSize = 2;
		listener.onBackStackChanged();
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
		assertThat(listener.backStackSize, is(2));
	}

	@Test
	public void testOnBackStackChangedForEmptyStack() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.onBackStackChanged();
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
		assertThat(listener.backStackSize, is(0));
	}

	@Test
	public void testOnBackStackChangedForInvalidBackStackSize() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.getBackStackEntryCount()).thenReturn(-1);
		when(mockController.getFragmentManager()).thenReturn(mockManager);
		final FragmentController.BackStackListener listener = new FragmentController.BackStackListener(mockController);
		listener.onBackStackChanged();
		verify(mockController, times(0)).handleBackStackChange(anyInt(), anyInt());
		assertThat(listener.backStackSize, is(0));
	}
}