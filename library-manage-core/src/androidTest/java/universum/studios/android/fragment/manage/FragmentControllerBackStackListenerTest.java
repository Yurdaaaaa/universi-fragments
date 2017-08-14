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
package universum.studios.android.fragment.manage; 
import android.app.FragmentManager;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

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
@RunWith(AndroidJUnit4.class)
public final class FragmentControllerBackStackListenerTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentControllerBackStackListenerTest";

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
