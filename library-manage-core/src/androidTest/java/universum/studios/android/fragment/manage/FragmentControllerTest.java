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

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.fragment.FragmentPolicies;
import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;
import universum.studios.android.test.TestFragment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentControllerTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "FragmentControllerTest";

	@Rule
	public final ActivityTestRule<TestActivity> ACTIVITY_RULE = new ActivityTestRule<>(TestActivity.class);

	@Rule
	public final ActivityTestRule<TestActivityWithAlInterfaces> ACTIVITY_RULE_ALL_INTERFACES = new ActivityTestRule<>(TestActivityWithAlInterfaces.class);

	@Test
	public void testConstants() {
		assertThat(FragmentController.FRAGMENT_TAG, is(FragmentPolicies.class.getPackage().getName() + ".TAG.Fragment"));
		assertThat(FragmentController.NO_CONTAINER_ID, is(-1));
	}

	@Test
	public void testInstantiationForActivity() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity);
		assertThat(controller.getFragmentManager(), is(activity.getFragmentManager()));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
	}

	@Test
	public void testInstantiationForActivityImplementingAllInterfaces() {
		final TestActivityWithAlInterfaces activity = ACTIVITY_RULE_ALL_INTERFACES.getActivity();
		final FragmentController controller = new FragmentController(activity);
		assertThat(controller.getFragmentManager(), is(activity.getFragmentManager()));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		final FragmentRequest request = new FragmentRequest(controller, new TestFragment()).viewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.executeRequest(request);
		controller.notifyRequestExecuted(request);
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
	}

	@Test
	public void testInstantiationForFragment() throws Throwable {
		final Activity activity = ACTIVITY_RULE.getActivity();
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final FragmentManager fragmentManager = activity.getFragmentManager();
				final Fragment fragment = new TestFragment();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				final FragmentController controller = new FragmentController(fragment);
				assertThat(controller.getFragmentManager(), is(fragmentManager));
				assertThat(controller.getTopBackStackEntry(), is(nullValue()));
			}
		});
	}

	@Test
	public void testInstantiationForFragmentImplementingAllInterfaces() throws Throwable {
		final Activity activity = ACTIVITY_RULE.getActivity();
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				final FragmentManager fragmentManager = activity.getFragmentManager();
				final TestFragmentWithAlInterfaces fragment = new TestFragmentWithAlInterfaces();
				fragmentManager.beginTransaction().add(fragment, null).commit();
				fragmentManager.executePendingTransactions();
				final FragmentController controller = new FragmentController(fragment);
				assertThat(controller.getFragmentManager(), is(fragmentManager));
				assertThat(controller.getTopBackStackEntry(), is(nullValue()));
				final FragmentRequest request = new FragmentRequest(controller, new TestFragment()).viewContainerId(TestActivity.CONTENT_VIEW_ID);
				controller.executeRequest(request);
				controller.notifyRequestExecuted(request);
				controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
				assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
				assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
				assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
			}
		});
	}

	@Test
	public void testInstantiationForFragmentManager() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final FragmentController controller = new FragmentController(fragmentManager);
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
	}

	@Test
	public void testInstantiationForFragmentManagerAndContext() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final FragmentController controller = new FragmentController(activity, fragmentManager);
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
	}

	@Test
	public void testInstantiationForFragmentManagerWithBackStackEntries() throws Throwable {
		final Activity activity = ACTIVITY_RULE.getActivity();
		ACTIVITY_RULE.runOnUiThread(new Runnable() {

			@Override
			@SuppressWarnings("ConstantConditions")
			public void run() {
				final FragmentManager fragmentManager = activity.getFragmentManager();
				fragmentManager.beginTransaction().add(TestActivity.CONTENT_VIEW_ID, new TestFragment(), "TAG.1").addToBackStack("TAG.1").commit();
				fragmentManager.beginTransaction().add(TestActivity.CONTENT_VIEW_ID, new TestFragment(), "TAG.2").addToBackStack("TAG.2").commit();
				fragmentManager.executePendingTransactions();
				final FragmentController controller = new FragmentController(activity, activity.getFragmentManager());
				assertThat(controller.getFragmentManager(), is(fragmentManager));
				final FragmentManager.BackStackEntry backStackEntry = controller.getTopBackStackEntry();
				assertThat(backStackEntry, is(notNullValue()));
				assertThat(backStackEntry.getId(), is(1));
				assertThat(backStackEntry.getName(), is("TAG.2"));
			}
		});
	}

	@Test
	public void testSetGetViewContainerId() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.setViewContainerId(android.R.id.custom);
		assertThat(controller.getViewContainerId(), is(android.R.id.custom));
	}

	@Test
	public void testGetViewContainerIdDefault() {
		assertThat(
				new FragmentController(ACTIVITY_RULE.getActivity().getFragmentManager()).getViewContainerId(),
				is(FragmentController.NO_CONTAINER_ID)
		);
	}

	@Test
	public void testSetGetFactory() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		controller.setFactory(mockFactory);
		assertThat(controller.getFactory(), is(mockFactory));
		assertThat(controller.hasFactory(), is(true));
		controller.setFactory(null);
		assertThat(controller.getFactory(), is(nullValue()));
		assertThat(controller.hasFactory(), is(false));
	}

	@Test
	public void testGetFactoryDefault() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		assertThat(controller.getFactory(), is(nullValue()));
		assertThat(controller.hasFactory(), is(false));
	}

	@Test
	public void testRegisterOnRequestListener() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(secondMockListener);
		final FragmentRequest mockRequest = new FragmentRequest(controller, FragmentRequest.NO_ID);
		controller.notifyRequestExecuted(mockRequest);
		verify(firstMockListener, times(1)).onRequestExecuted(mockRequest);
		verify(secondMockListener, times(1)).onRequestExecuted(mockRequest);
	}

	@Test
	public void testUnregisterOnRequestListener() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(secondMockListener);
		controller.unregisterOnRequestListener(firstMockListener);
		final FragmentRequest mockRequest = new FragmentRequest(controller, FragmentRequest.NO_ID);
		controller.notifyRequestExecuted(mockRequest);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener, times(1)).onRequestExecuted(mockRequest);
		controller.unregisterOnRequestListener(secondMockListener);
		controller.notifyRequestExecuted(mockRequest);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testUnregisterOnRequestListenerNotRegistered() {
		// Only ensure that un-registering not registered listener does not cause any troubles.
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		controller.unregisterOnRequestListener(mockListener);
	}

	@Test
	public void testNotifyRequestExecutedWithoutRegisteredListeners() {
		// Only ensure that notifying without registered listeners does not cause any troubles.
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.notifyRequestExecuted(new FragmentRequest(controller, FragmentRequest.NO_ID));
	}

	@Test
	public void testRegisterOnBackStackChangeListener() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(secondMockListener);
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		controller.notifyBackStackEntryChange(mockBackStackEntry, true);
		verify(firstMockListener, times(1)).onFragmentsBackStackChanged(mockBackStackEntry, true);
		verify(secondMockListener, times(1)).onFragmentsBackStackChanged(mockBackStackEntry, true);
	}

	@Test
	public void testUnregisterOnBackStackChangeListener() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(secondMockListener);
		controller.unregisterOnBackStackChangeListener(firstMockListener);
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		controller.notifyBackStackEntryChange(mockBackStackEntry, true);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener, times(1)).onFragmentsBackStackChanged(mockBackStackEntry, true);
		controller.unregisterOnBackStackChangeListener(secondMockListener);
		controller.notifyBackStackEntryChange(mockBackStackEntry, false);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test
	public void testUnregisterOnBackStackChangeListenerNotRegistered() {
		// Only ensure that un-registering not registered listener does not cause any troubles.
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		controller.unregisterOnBackStackChangeListener(mockListener);
	}

	@Test
	public void testNotifyBackStackEntryChangeWithoutRegisteredListeners() {
		// Only ensure that notifying without registered listeners does not cause any troubles.
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
	}

	@Test
	public void testNewRequestForFactoryFragment() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		assertThat(request, is(notNullValue()));
		assertThat(request.fragmentId(), is(TestFactory.FRAGMENT_1));
		assertThat(request.tag(), is(nullValue()));
		assertThat(request.viewContainerId(), is(controller.getViewContainerId()));
	}

	@Test(expected = IllegalStateException.class)
	public void testNewRequestForFactoryFragmentWhenDestroyed() {
		createDestroyedController().newRequest(TestFactory.FRAGMENT_1);
	}

	@Test
	public void testNewRequestForFragment() {
		final Activity activity = ACTIVITY_RULE.getActivity();
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentRequest request = controller.newRequest(mockFragment);
		assertThat(request, is(notNullValue()));
		assertThat(request.fragmentId(), is(FragmentRequest.NO_ID));
		assertThat(request.tag(), is(FragmentController.FRAGMENT_TAG));
		assertThat(request.viewContainerId(), is(controller.getViewContainerId()));
	}

	@Test(expected = IllegalStateException.class)
	public void testNewRequestForFragmentWhenDestroyed() {
		createDestroyedController().newRequest(new TestFragment());
	}

	@Test
	public void testExecuteRequest() {
		// todo:: implement test
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteRequestWhenDestroyed() {
		createDestroyedController().executeRequest(new FragmentRequest(null, FragmentRequest.NO_ID));
	}

	@Test
	public void testOnExecuteRequest() {
		// todo:: implement test
	}

	@Test
	public void testCreateTransaction() {
		// todo:: implement test
	}

	@Test
	public void testFindCurrentFragment() {
		// todo:: implement test
	}

	@Test(expected = IllegalStateException.class)
	public void testFindCurrentFragmentWhenDestroyed() {
		createDestroyedController().findCurrentFragment();
	}

	@Test
	public void testFindFragmentByFactoryId() {
		// todo:: implement test
	}

	@Test(expected = IllegalStateException.class)
	public void testFindFragmentByFactoryIdWhenDestroyed() {
		createDestroyedController().findFragmentByFactoryId(TestFactory.FRAGMENT_1);
	}

	@Test
	public void testHasBackStackEntries() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		assertThat(controller.hasBackStackEntries(), is(true));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test
	public void testHasBackStackEntriesOnEmptyStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		assertThat(controller.hasBackStackEntries(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test
	public void testGetTopBackStackEntryDefault() {
		assertThat(new FragmentController(mock(FragmentManager.class)).getTopBackStackEntry(), is(nullValue()));
	}

	@Test
	public void testClearBackStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		controller.clearBackStack();
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStack();
	}

	@Test
	public void testClearBackStackOnEmptyBackStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		controller.clearBackStack();
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test(expected = IllegalStateException.class)
	public void testClearBackStackWhenDestroyed() {
		createDestroyedController().clearBackStack();
	}

	@Test
	public void testClearBackStackImmediate() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		when(mockManager.popBackStackImmediate()).thenReturn(true);
		assertThat(controller.clearBackStackImmediate(), is(true));
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStackImmediate();
	}

	@Test
	public void testClearBackStackImmediateWhenManagerDoesNotPops() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		when(mockManager.popBackStackImmediate()).thenReturn(false);
		assertThat(controller.clearBackStackImmediate(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStackImmediate();
	}

	@Test
	public void testClearBackStackImmediateOnEmptyStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		assertThat(controller.clearBackStackImmediate(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test(expected = IllegalStateException.class)
	public void testClearBackStackImmediateWhenDestroyed() {
		createDestroyedController().clearBackStackImmediate();
	}

	@Test
	public void testDestroyWhenAlreadyDestroyed() {
		createDestroyedController().destroy();
	}

	@Test
	public void testHandleBackStackChangeDueToAddition() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.registerOnBackStackChangeListener(mockListener);
		when(mockManager.getBackStackEntryAt(0)).thenReturn(mockBackStackEntry);
		controller.handleBackStackChange(1, FragmentController.BackStackListener.ADDED);
		assertThat(controller.getTopBackStackEntry(), is(mockBackStackEntry));
		verify(mockListener, times(1)).onFragmentsBackStackChanged(mockBackStackEntry, true);
		verify(mockListener, times(0)).onFragmentsBackStackChanged(mockBackStackEntry, false);
	}

	@Test
	public void testHandleBackStackChangeDueToAdditionOfEntryNotInStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.registerOnBackStackChangeListener(mockListener);
		when(mockManager.getBackStackEntryAt(anyInt())).thenReturn(null);
		controller.handleBackStackChange(1, FragmentController.BackStackListener.ADDED);
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		verifyZeroInteractions(mockListener);
	}

	@Test
	public void testHandleBackStackChangeDueToRemoval() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentManager.BackStackEntry firstMockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentManager.BackStackEntry secondMockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryAt(1)).thenReturn(secondMockBackStackEntry);
		controller.handleBackStackChange(2, FragmentController.BackStackListener.ADDED);
		controller.registerOnBackStackChangeListener(mockListener);
		when(mockManager.getBackStackEntryAt(0)).thenReturn(firstMockBackStackEntry);
		controller.handleBackStackChange(1, FragmentController.BackStackListener.REMOVED);
		assertThat(controller.getTopBackStackEntry(), is(firstMockBackStackEntry));
		verify(mockListener, times(1)).onFragmentsBackStackChanged(secondMockBackStackEntry, false);
		verify(mockListener, times(0)).onFragmentsBackStackChanged(secondMockBackStackEntry, true);
		verify(mockListener, times(0)).onFragmentsBackStackChanged(firstMockBackStackEntry, true);
		verify(mockListener, times(0)).onFragmentsBackStackChanged(firstMockBackStackEntry, false);
	}

	@Test
	public void testHandleBackStackChangeDueToRemovalWithoutPreviousBackStackEntry() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.handleBackStackChange(0, FragmentController.BackStackListener.REMOVED);
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		verifyZeroInteractions(mockListener);
	}

	private FragmentController createDestroyedController() {
		final FragmentController controller = new FragmentController(ACTIVITY_RULE.getActivity().getFragmentManager());
		controller.destroy();
		return controller;
	}

	public static final class TestActivityWithAlInterfaces extends TestActivity
			implements
			FragmentRequestInterceptor,
			FragmentController.OnRequestListener,
			FragmentController.OnBackStackChangeListener {

		static final int CALLBACK_INTERCEPT_FRAGMENT_REQUEST = 0x00000001;
		static final int CALLBACK_ON_REQUEST_EXECUTED = 0x00000001 << 1;
		static final int CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED = 0x00000001 << 2;

		@IntDef(flag = true, value = {
				CALLBACK_INTERCEPT_FRAGMENT_REQUEST,
				CALLBACK_ON_REQUEST_EXECUTED,
				CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED
		})
		@Retention(RetentionPolicy.SOURCE)
		@interface CallbackId {
		}

		private int receivedCallbacks;

		@Nullable
		@Override
		public Fragment interceptFragmentRequest(@NonNull FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_INTERCEPT_FRAGMENT_REQUEST;
			return null;
		}

		@Override
		public void onRequestExecuted(@NonNull FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_ON_REQUEST_EXECUTED;
		}

		@Override
		public void onFragmentsBackStackChanged(@NonNull FragmentManager.BackStackEntry backStackEntry, boolean added) {
			this.receivedCallbacks |= CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED;
		}

		boolean receivedCallback(@CallbackId int callback) {
			return (receivedCallbacks & callback) == callback;
		}

		void reset() {
			this.receivedCallbacks = 0;
		}
	}

	public static final class TestFragmentWithAlInterfaces extends TestFragment
			implements
			FragmentRequestInterceptor,
			FragmentController.OnRequestListener,
			FragmentController.OnBackStackChangeListener {

		static final int CALLBACK_INTERCEPT_FRAGMENT_REQUEST = 0x00000001;
		static final int CALLBACK_ON_REQUEST_EXECUTED = 0x00000001 << 1;
		static final int CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED = 0x00000001 << 2;

		@IntDef(flag = true, value = {
				CALLBACK_INTERCEPT_FRAGMENT_REQUEST,
				CALLBACK_ON_REQUEST_EXECUTED,
				CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED
		})
		@Retention(RetentionPolicy.SOURCE)
		@interface CallbackId {
		}

		private int receivedCallbacks;

		@Nullable
		@Override
		public Fragment interceptFragmentRequest(@NonNull FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_INTERCEPT_FRAGMENT_REQUEST;
			return null;
		}

		@Override
		public void onRequestExecuted(@NonNull FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_ON_REQUEST_EXECUTED;
		}

		@Override
		public void onFragmentsBackStackChanged(@NonNull FragmentManager.BackStackEntry backStackEntry, boolean added) {
			this.receivedCallbacks |= CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED;
		}

		boolean receivedCallback(@CallbackId int callback) {
			return (receivedCallbacks & callback) == callback;
		}

		void reset() {
			this.receivedCallbacks = 0;
		}
	}

	private static class TestFactory implements FragmentFactory {

		static final int FRAGMENT_1 = 0x01;

		@Override
		public boolean isFragmentProvided(int fragmentId) {
			return fragmentId == FRAGMENT_1;
		}

		@Nullable
		@Override
		public Fragment createFragment(int fragmentId) {
			switch (fragmentId) {
				case FRAGMENT_1: return new TestFragment1();
				default: return null;
			}
		}

		@Nullable
		@Override
		public String createFragmentTag(int fragmentId) {
			return "TAG.Fragment." + Integer.toString(fragmentId);
		}
	}

	public static final class TestFragment1 extends TestFragment {
	}
}
