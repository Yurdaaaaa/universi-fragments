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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.transition.Transition;
import android.view.View;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import universum.studios.android.fragment.FragmentPolicies;
import universum.studios.android.fragment.util.FragmentUtils;
import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestActivity;
import universum.studios.android.test.TestFragment;
import universum.studios.android.test.TestResources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
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
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
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
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(activity.getFragmentManager());
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
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
	@SuppressLint("CommitTransaction")
	public void testExecuteRequest() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment);
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockManager, times(1)).beginTransaction();
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentReplace() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf1(FragmentRequest.REPLACE);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentAdd() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf1(FragmentRequest.ADD);
	}

	@SuppressLint("CommitTransaction")
	private void testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf1(@FragmentRequest.Transaction int transaction) {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		controller.setFactory(mockFactory);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1).transaction(transaction);
		final TestFactory factory = new TestFactory();
		final Fragment fragment = factory.createFragment(TestFactory.FRAGMENT_1);
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(factory.isFragmentProvided(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn(factory.createFragmentTag(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragment(TestFactory.FRAGMENT_1)).thenReturn(fragment);
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockFactory, times(1)).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(1)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(1)).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@SuppressLint("CommitTransaction")
	@Test(expected = IllegalArgumentException.class)
	public void testExecuteRequestForFactoryFragmentWithCheatingFactory() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		controller.setFactory(mockFactory);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1).transaction(FragmentRequest.REPLACE);
		final TestFactory factory = new TestFactory();
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(factory.isFragmentProvided(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn(factory.createFragmentTag(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragment(TestFactory.FRAGMENT_1)).thenReturn(null);
		controller.executeRequest(request);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentRemove() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.REMOVE);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentShow() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.SHOW);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentHide() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.HIDE);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentAttach() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.ATTACH);
	}

	@Test
	public void testExecuteRequestForFactoryFragmentDetach() {
		this.testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.DETACH);
	}

	@SuppressLint("CommitTransaction")
	private void testInnerExecuteRequestForFactoryFragmentWithTransactionTypeOf2(@FragmentRequest.Transaction int transaction) {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		controller.setFactory(mockFactory);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1).transaction(transaction);
		final Fragment fragment = new TestFragment();
		final TestFactory factory = new TestFactory();
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(factory.isFragmentProvided(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn(factory.createFragmentTag(TestFactory.FRAGMENT_1));
		when(mockManager.findFragmentByTag(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1))).thenReturn(fragment);
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockFactory, times(2)).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(3)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(0)).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testExecuteRequestForFactoryFragmentThatDoesNotExist() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		controller.setFactory(mockFactory);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1).transaction(FragmentRequest.REMOVE);
		final TestFactory factory = new TestFactory();
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(factory.isFragmentProvided(TestFactory.FRAGMENT_1));
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn(factory.createFragmentTag(TestFactory.FRAGMENT_1));
		when(mockManager.findFragmentByTag(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1))).thenReturn(null);
		assertThat(controller.executeRequest(request), is(nullValue()));
		verify(mockFactory, times(2)).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(2)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(0)).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager, times(0)).beginTransaction();
		verifyZeroInteractions(mockListener);
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteRequestForFactoryFragmentWithoutFactory() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		controller.executeRequest(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteRequestForFactoryFragmentNotProvidedByFactory() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(false);
		controller.executeRequest(request);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testExecuteRequestNotIntercepted() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentRequestInterceptor mockInterceptor = mock(FragmentRequestInterceptor.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.setRequestInterceptor(mockInterceptor);
		controller.registerOnRequestListener(mockListener);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment);
		when(mockInterceptor.interceptFragmentRequest(request)).thenReturn(null);
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockInterceptor, times(1)).interceptFragmentRequest(request);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testExecuteRequestIntercepted() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentRequestInterceptor mockInterceptor = mock(FragmentRequestInterceptor.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.setRequestInterceptor(mockInterceptor);
		controller.registerOnRequestListener(mockListener);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment);
		final Fragment interceptedFragment = new TestFragment();
		when(mockInterceptor.interceptFragmentRequest(request)).thenReturn(interceptedFragment);
		assertThat(controller.executeRequest(request), is(interceptedFragment));
		verify(mockManager, times(1)).addOnBackStackChangedListener(any(FragmentManager.OnBackStackChangedListener.class));
		verify(mockManager, times(1)).getBackStackEntryCount();
		verifyZeroInteractions(mockManager);
		verify(mockInterceptor, times(1)).interceptFragmentRequest(request);
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteRequestWhenDestroyed() {
		createDestroyedController().executeRequest(new FragmentRequest(null, FragmentRequest.NO_ID));
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequest() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		assertThat(
				controller.onExecuteRequest(controller.newRequest(fragment)
						.transaction(FragmentRequest.ADD)),
				is(fragment)
		);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commit();
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestReplacingSame() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment)
				.transaction(FragmentRequest.REPLACE)
				.replaceSame(true);
		final Fragment existingFragment = new TestFragment();
		when(mockManager.findFragmentByTag(request.mTag)).thenReturn(existingFragment);
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager, times(0)).findFragmentByTag(request.mTag);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commit();
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestNotReplacingSame() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment)
				.transaction(FragmentRequest.REPLACE)
				.replaceSame(false);
		when(mockManager.findFragmentByTag(request.mTag)).thenReturn(null);
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commit();
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestNotReplacingSameThatAlreadyExists() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment)
				.transaction(FragmentRequest.REPLACE)
				.replaceSame(false);
		final Fragment existingFragment = new TestFragment();
		when(mockManager.findFragmentByTag(request.mTag)).thenReturn(existingFragment);
		assertThat(controller.onExecuteRequest(request), is(existingFragment));
		verifyZeroInteractions(mockTransaction);
		verify(mockManager, times(1)).findFragmentByTag(request.mTag);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestBackStacked() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		assertThat(
				controller.onExecuteRequest(controller.newRequest(fragment)
						.transaction(FragmentRequest.REPLACE)
						.addToBackStack(true)),
				is(fragment)
		);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commit();
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestAllowingStateLoss() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		assertThat(
				controller.onExecuteRequest(controller.newRequest(fragment)
						.transaction(FragmentRequest.ADD)
						.allowStateLoss(true)),
				is(fragment)
		);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commitAllowingStateLoss();
		verify(mockTransaction, times(0)).commit();
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testOnExecuteRequestImmediate() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		assertThat(
				controller.onExecuteRequest(controller.newRequest(fragment)
						.transaction(FragmentRequest.ADD)
						.immediate(true)),
				is(fragment)
		);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockTransaction, times(1)).commit();
		verify(mockManager, times(1)).executePendingTransactions();
	}

	@Test(expected = IllegalStateException.class)
	@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void testOnExecuteRequestWhenManagerIsDestroyed() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1);
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.isDestroyed()).thenReturn(true);
		final FragmentController controller = new FragmentController(mockManager);
		controller.onExecuteRequest(controller.newRequest(new TestFragment()));
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransaction() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(mockFragment, times(0)).setArguments(any(Bundle.class));
		verify(mockTransaction, times(0)).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
		verify(mockTransaction, times(0)).setCustomAnimations(anyInt(), anyInt());
		verify(mockTransaction, times(0)).setTransitionStyle(anyInt());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			verify(mockTransaction, times(0)).addSharedElement(any(View.class), anyString());
		}
		verify(mockTransaction, times(0)).addToBackStack(anyString());
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionReplace() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.REPLACE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).replace(TestActivity.CONTENT_VIEW_ID, mockFragment, request.mTag);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionReplaceWithoutViewContainerIdSpecified() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.createTransaction(controller.newRequest(mock(TestFragment.class)).transaction(FragmentRequest.REPLACE));
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionAdd() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.ADD);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).add(TestActivity.CONTENT_VIEW_ID, mockFragment, request.mTag);
		verifyNoMoreInteractions(transaction);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionAddWithoutViewContainerIdSpecified() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.createTransaction(controller.newRequest(mock(TestFragment.class)).transaction(FragmentRequest.ADD));
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionRemove() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.REMOVE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).remove(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionShow() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.SHOW);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).show(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionHide() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.HIDE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).hide(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionAttach() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.ATTACH);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).attach(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionDetach() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.DETACH);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).detach(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@SuppressWarnings("ResourceType")
	@SuppressLint("CommitTransaction")
	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionUnsupported() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(-1);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		controller.createTransaction(request);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionWithArguments() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final Bundle args = new Bundle();
		final FragmentRequest request = controller.newRequest(mockFragment).arguments(args);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(mockFragment, times(1)).setArguments(args);
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionWithTransition() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentTransition transition = new TestTransition();
		final FragmentRequest request = controller.newRequest(mockFragment).transition(transition);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).setCustomAnimations(
				transition.getIncomingAnimation(),
				transition.getOutgoingAnimation(),
				transition.getIncomingBackStackAnimation(),
				transition.getOutgoingBackStackAnimation()
		);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
		verify(transaction, times(0)).setTransitionStyle(anyInt());
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionWithTransitionWhenContextIsAvailable() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mContext, mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentTransition transition = new TestTransition();
		final FragmentRequest request = controller.newRequest(mockFragment).transition(transition);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).setCustomAnimations(
				transition.getIncomingAnimation(),
				transition.getOutgoingAnimation(),
				transition.getIncomingBackStackAnimation(),
				transition.getOutgoingBackStackAnimation()
		);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
		verify(transaction, times(0)).setTransitionStyle(anyInt());
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionWithTransitionStyle() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transitionStyle(android.R.style.Animation);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).setTransitionStyle(android.R.style.Animation);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionWithSharedElements() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final View elementFirst = new View(mContext);
		final View elementSecond = new View(mContext);
		final FragmentRequest request = controller.newRequest(mockFragment)
				.sharedElement(elementFirst, "Element.First")
				.sharedElement(elementSecond, "Element.Second");
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			verify(transaction, times(1)).addSharedElement(elementFirst, "Element.First");
			verify(transaction, times(1)).addSharedElement(elementSecond, "Element.Second");
		}
	}

	@Test
	@SuppressLint("CommitTransaction")
	@SuppressWarnings("ConstantConditions")
	public void testCreateTransactionWithEmptySharedElements() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final View elementFirst = new View(mContext);
		final View elementSecond = new View(mContext);
		final FragmentRequest request = controller.newRequest(mockFragment)
				.sharedElement(elementFirst, "Element.First")
				.sharedElement(elementSecond, "Element.Second");
		request.sharedElements().clear();
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			verify(transaction, times(0)).addSharedElement(any(View.class), anyString());
		}
	}

	@Test
	@SuppressLint("CommitTransaction")
	public void testCreateTransactionToBeAddedIntoBackStack() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).addToBackStack(true);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final FragmentTransaction transaction = controller.createTransaction(request);
		assertThat(transaction, is(notNullValue()));
		verify(transaction, times(1)).addToBackStack(mockFragment.getTag());
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateTransactionWhenDestroyed() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(new TestFragment());
		controller.destroy();
		controller.createTransaction(request);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void testAttachTransitionsToFragment() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
		final Fragment mockFragment = mock(TestFragment.class);
		final Transition enterTransition = inflateTestTransition();
		final Transition exitTransition = inflateTestTransition();
		final Transition reenterTransition = inflateTestTransition();
		final Transition returnTransition = inflateTestTransition();
		final Transition sharedElementEnterTransition = inflateTestTransition();
		final Transition sharedElementEnterReturnTransition = inflateTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), mockFragment)
				.enterTransition(enterTransition)
				.exitTransition(exitTransition)
				.reenterTransition(reenterTransition)
				.returnTransition(returnTransition)
				.sharedElementEnterTransition(sharedElementEnterTransition)
				.sharedElementReturnTransition(sharedElementEnterReturnTransition)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(false);
		FragmentController.attachTransitionsToFragment(request, mockFragment);
		verify(mockFragment, times(1)).setEnterTransition(enterTransition);
		verify(mockFragment, times(1)).setExitTransition(exitTransition);
		verify(mockFragment, times(1)).setReenterTransition(reenterTransition);
		verify(mockFragment, times(1)).setReturnTransition(returnTransition);
		verify(mockFragment, times(1)).setSharedElementEnterTransition(sharedElementEnterTransition);
		verify(mockFragment, times(1)).setSharedElementReturnTransition(sharedElementEnterReturnTransition);
		verify(mockFragment, times(1)).setAllowEnterTransitionOverlap(true);
		verify(mockFragment, times(1)).setAllowReturnTransitionOverlap(false);
		verifyNoMoreInteractions(mockFragment);
	}

	@Test
	@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
	public void testAttachTransitionsToFragmentForRequestWithoutTransitions() {
		assumeTrue(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), mockFragment);
		FragmentController.attachTransitionsToFragment(request, mockFragment);
		verifyZeroInteractions(mockFragment);
	}

	@Test
	public void testFindCurrentFragment() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.findFragmentById(controller.getViewContainerId())).thenReturn(mockFragment);
		assertThat(controller.findCurrentFragment(), is(mockFragment));
		verify(mockManager, times(1)).findFragmentById(controller.getViewContainerId());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFindCurrentFragmentWithoutViewContainerIdSpecified() {
		new FragmentController(mock(FragmentManager.class)).findCurrentFragment();
	}

	@Test(expected = IllegalStateException.class)
	public void testFindCurrentFragmentWhenDestroyed() {
		createDestroyedController().findCurrentFragment();
	}

	@Test
	public void testFindFragmentByFactoryId() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(true);
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn("TAG.TestFragment");
		when(mockManager.findFragmentByTag("TAG.TestFragment")).thenReturn(mockFragment);
		assertThat(controller.findFragmentByFactoryId(TestFactory.FRAGMENT_1), is(mockFragment));
		verify(mockFactory, times(1)).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(1)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockManager, times(1)).findFragmentByTag("TAG.TestFragment");
	}

	@Test(expected = IllegalStateException.class)
	public void testFindFragmentByFactoryIdWithoutFactoryAttached() {
		new FragmentController(mock(FragmentManager.class)).findFragmentByFactoryId(TestFactory.FRAGMENT_1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindFragmentByFactoryIdWithFactoryNotProvidingFragment() {
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		when(mockFactory.isFragmentProvided(anyInt())).thenReturn(false);
		controller.findFragmentByFactoryId(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(1)).isFragmentProvided(TestFactory.FRAGMENT_1);
		verifyNoMoreInteractions(mockFactory);
		verifyZeroInteractions(mockManager);
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

	@Nullable
	private Transition inflateTestTransition() {
		return FragmentUtils.inflateTransition(mContext, TestResources.resourceIdentifier(mContext, TestResources.TRANSITION, "transition_fade"));
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

	@SuppressLint("ParcelCreator")
	private static final class TestTransition implements FragmentTransition {

		@Override
		public int getIncomingAnimation() {
			return android.R.animator.fade_in;
		}

		@Override
		public int getOutgoingAnimation() {
			return android.R.animator.fade_out;
		}

		@Override
		public int getIncomingBackStackAnimation() {
			return android.R.animator.fade_out;
		}

		@Override
		public int getOutgoingBackStackAnimation() {
			return android.R.animator.fade_in;
		}

		@NonNull
		@Override
		public String getName() {
			return TestTransition.class.getName();
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
		}
	}
}
