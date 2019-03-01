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

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;

import org.junit.Test;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import universum.studios.android.test.AndroidTestCase;
import universum.studios.android.test.TestActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
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
@SuppressLint("CommitTransaction")
public final class FragmentControllerTest extends AndroidTestCase {

	@Test public void testContract() {
		// Assert:
		assertThat(FragmentController.FRAGMENT_TAG, is("universum.studios.android.fragment.TAG.Fragment"));
		assertThat(FragmentController.NO_CONTAINER_ID, is(-1));
	}

	@Test public void testInstantiationForActivity() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		// Act:
		final FragmentController controller = new FragmentController(activity);
		// Assert:
		assertThat(controller.getFragmentManager(), is(activity.getSupportFragmentManager()));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testCreateForActivity() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		// Act:
		final FragmentController controller = FragmentController.create(activity);
		// Assert:
		assertThat(controller.context, is(activity));
		assertThat(controller.getFragmentManager(), is(activity.getSupportFragmentManager()));
		assertThat(controller.lifecycle, is(activity.getLifecycle()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testInstantiationForActivityImplementingAllInterfaces() {
		// Arrange:
		final TestActivityWithAlInterfaces activity = Robolectric.buildActivity(TestActivityWithAlInterfaces.class).create().start().resume().get();
		// Act:
		final FragmentController controller = new FragmentController(activity);
		// Assert:
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		final FragmentRequest request = new FragmentRequest(controller, new TestFragment())
				.viewContainerId(TestActivity.CONTENT_VIEW_ID)
				.allowStateLoss(true);
		controller.executeRequest(request);
		controller.notifyRequestExecuted(request);
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
	}

	@Test public void testCreateForActivityImplementingAllInterfaces() {
		// Arrange:
		final TestActivityWithAlInterfaces activity = Robolectric.buildActivity(TestActivityWithAlInterfaces.class).create().start().resume().get();
		// Act:
		final FragmentController controller = FragmentController.create(activity);
		// Assert:
		assertThat(controller.context, is(activity));
		assertThat(controller.getFragmentManager(), is(activity.getSupportFragmentManager()));
		assertThat(controller.lifecycle, is(activity.getLifecycle()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		final FragmentRequest request = new FragmentRequest(controller, new TestFragment())
				.viewContainerId(TestActivity.CONTENT_VIEW_ID)
				.allowStateLoss(true);
		controller.executeRequest(request);
		controller.notifyRequestExecuted(request);
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
		assertThat(activity.receivedCallback(TestActivityWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
	}

	@Test public void testInstantiationForFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act:
		final FragmentController controller = new FragmentController(fragment);
		// Assert:
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testCreateForFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final Fragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act:
		final FragmentController controller = FragmentController.create(fragment);
		// Assert:
		assertThat(controller.context, is(activity));
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.lifecycle, is(fragment.getLifecycle()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testInstantiationForFragmentImplementingAllInterfaces() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragmentWithAlInterfaces fragment = new TestFragmentWithAlInterfaces();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act:
		final FragmentController controller = new FragmentController(fragment);
		// Assert:
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		final FragmentRequest request = new FragmentRequest(controller, new TestFragment()).viewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.executeRequest(request);
		controller.notifyRequestExecuted(request);
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
	}

	@Test public void testCreateForFragmentImplementingAllInterfaces() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final TestFragmentWithAlInterfaces fragment = new TestFragmentWithAlInterfaces();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act:
		final FragmentController controller = FragmentController.create(fragment);
		// Assert:
		assertThat(controller.context, is(activity));
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.lifecycle, is(fragment.getLifecycle()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		final FragmentRequest request = new FragmentRequest(controller, new TestFragment()).viewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.executeRequest(request);
		controller.notifyRequestExecuted(request);
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_INTERCEPT_FRAGMENT_REQUEST), is(true));
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_REQUEST_EXECUTED), is(true));
		assertThat(fragment.receivedCallback(TestFragmentWithAlInterfaces.CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED), is(true));
	}

	@Test public void testInstantiationForFragmentManager() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		// Act:
		final FragmentController controller = new FragmentController(fragmentManager);
		// Assert:
		assertThat(controller.context, is(nullValue()));
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.lifecycle, is(nullValue()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testInstantiationForFragmentManagerAndContext() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		// Act:
		final FragmentController controller = new FragmentController(activity, fragmentManager);
		// Assert:
		assertThat(controller.context, is(activity));
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.lifecycle, is(nullValue()));
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.STARTED));
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test public void testInstantiationForFragmentManagerWithBackStackEntries() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		fragmentManager.beginTransaction().add(TestActivity.CONTENT_VIEW_ID, new TestFragment(), "TAG.1").addToBackStack("TAG.1").commit();
		fragmentManager.beginTransaction().add(TestActivity.CONTENT_VIEW_ID, new TestFragment(), "TAG.2").addToBackStack("TAG.2").commit();
		fragmentManager.executePendingTransactions();
		// Act:
		final FragmentController controller = new FragmentController(fragmentManager);
		// Assert:
		assertThat(controller.getFragmentManager(), is(fragmentManager));
		assertThat(controller.getViewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		final FragmentManager.BackStackEntry backStackEntry = controller.getTopBackStackEntry();
		assertThat(backStackEntry, is(notNullValue()));
		assertThat(backStackEntry.getId(), is(1));
		assertThat(backStackEntry.getName(), is("TAG.2"));
	}

	@Test public void testLifecycleRequiredState() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		// Act:
		controller.setLifecycleRequiredState(Lifecycle.State.RESUMED);
		// Assert:
		assertThat(controller.getLifecycleRequiredState(), is(Lifecycle.State.RESUMED));
	}

	@Test public void testViewContainerId() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		// Act:
		controller.setViewContainerId(android.R.id.custom);
		// Assert:
		assertThat(controller.getViewContainerId(), is(android.R.id.custom));
	}

	@Test public void testFactory() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		// Act + Assert:
		assertThat(controller.getFactory(), is(nullValue()));
		assertThat(controller.hasFactory(), is(false));
		controller.setFactory(mockFactory);
		assertThat(controller.getFactory(), is(mockFactory));
		assertThat(controller.hasFactory(), is(true));
		controller.setFactory(null);
		assertThat(controller.getFactory(), is(nullValue()));
		assertThat(controller.hasFactory(), is(false));
	}

	@Test public void testRegisterOnRequestListener() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		// Act:
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(secondMockListener);
		// Assert:
		final FragmentRequest mockRequest = new FragmentRequest(controller, FragmentRequest.NO_ID);
		controller.notifyRequestExecuted(mockRequest);
		verify(firstMockListener).onRequestExecuted(mockRequest);
		verify(secondMockListener).onRequestExecuted(mockRequest);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testUnregisterOnRequestListener() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnRequestListener firstMockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController.OnRequestListener secondMockListener = mock(FragmentController.OnRequestListener.class);
		controller.registerOnRequestListener(firstMockListener);
		controller.registerOnRequestListener(secondMockListener);
		// Act:
		controller.unregisterOnRequestListener(firstMockListener);
		// Assert:
		final FragmentRequest mockRequest = new FragmentRequest(controller, FragmentRequest.NO_ID);
		controller.notifyRequestExecuted(mockRequest);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener).onRequestExecuted(mockRequest);
		controller.unregisterOnRequestListener(secondMockListener);
		controller.notifyRequestExecuted(mockRequest);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test public void testUnregisterOnRequestListenerNotRegistered() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		// Act:
		// Only ensure that un-registering not registered listener does not cause any troubles.
		controller.unregisterOnRequestListener(mockListener);
	}

	@Test public void testNotifyRequestExecutedWithoutRegisteredListeners() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		// Act:
		// Only ensure that notifying without registered listeners does not cause any troubles.
		controller.notifyRequestExecuted(new FragmentRequest(controller, FragmentRequest.NO_ID));
	}

	@Test public void testRegisterOnBackStackChangeListener() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		// Act:
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(secondMockListener);
		// Assert:
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		controller.notifyBackStackEntryChange(mockBackStackEntry, true);
		verify(firstMockListener).onFragmentsBackStackChanged(mockBackStackEntry, true);
		verify(secondMockListener).onFragmentsBackStackChanged(mockBackStackEntry, true);
		verifyNoMoreInteractions(firstMockListener, secondMockListener);
	}

	@Test public void testUnregisterOnBackStackChangeListener() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnBackStackChangeListener firstMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController.OnBackStackChangeListener secondMockListener = mock(FragmentController.OnBackStackChangeListener.class);
		controller.registerOnBackStackChangeListener(firstMockListener);
		controller.registerOnBackStackChangeListener(secondMockListener);
		// Act:
		controller.unregisterOnBackStackChangeListener(firstMockListener);
		// Assert:
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		controller.notifyBackStackEntryChange(mockBackStackEntry, true);
		verifyZeroInteractions(firstMockListener);
		verify(secondMockListener).onFragmentsBackStackChanged(mockBackStackEntry, true);
		controller.unregisterOnBackStackChangeListener(secondMockListener);
		controller.notifyBackStackEntryChange(mockBackStackEntry, false);
		verifyNoMoreInteractions(secondMockListener);
	}

	@Test public void testUnregisterOnBackStackChangeListenerNotRegistered() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		// Act:
		// Only ensure that un-registering not registered listener does not cause any troubles.
		controller.unregisterOnBackStackChangeListener(mockListener);
	}

	@Test public void testNotifyBackStackEntryChangeWithoutRegisteredListeners() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		// Act:
		// Only ensure that notifying without registered listeners does not cause any troubles.
		controller.notifyBackStackEntryChange(mock(FragmentManager.BackStackEntry.class), false);
	}

	@Test public void testNewRequest() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		// Act:
		final FragmentRequest request = controller.newRequest();
		// Assert:
		assertThat(request, is(notNullValue()));
		assertThat(request.fragmentId(), is(FragmentRequest.NO_ID));
		assertThat(request.tag(), is(nullValue()));
		assertThat(request.viewContainerId(), is(controller.getViewContainerId()));
	}

	@Test public void testNewRequestForFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		final Fragment mockFragment = mock(TestFragment.class);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		// Act:
		final FragmentRequest request = controller.newRequest(mockFragment);
		// Assert:
		assertThat(request, is(notNullValue()));
		assertThat(request.fragmentId(), is(FragmentRequest.NO_ID));
		assertThat(request.tag(), is(FragmentController.FRAGMENT_TAG));
		assertThat(request.viewContainerId(), is(controller.getViewContainerId()));
	}

	@Test(expected = IllegalStateException.class)
	public void testNewRequestForFragmentWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().newRequest(new TestFragment());
	}

	@Test public void testNewRequestForFactoryFragment() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		// Act:
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		// Assert:
		assertThat(request, is(notNullValue()));
		assertThat(request.fragmentId(), is(TestFactory.FRAGMENT_1));
		assertThat(request.tag(), is(nullValue()));
		assertThat(request.viewContainerId(), is(controller.getViewContainerId()));
	}

	@Test(expected = IllegalStateException.class)
	public void testNewRequestForFactoryFragmentWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().newRequest(TestFactory.FRAGMENT_1);
	}

	@Test public void testExecuteRequest() {
		// Arrange:
		final Lifecycle mockLifecycle = mock(Lifecycle.class);
		when(mockLifecycle.getCurrentState()).thenReturn(Lifecycle.State.RESUMED);
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setLifecycle(mockLifecycle);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment);
		Mockito.clearInvocations(mockManager);
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockLifecycle).getCurrentState();
		verify(mockManager).isDestroyed();
		verify(mockManager).beginTransaction();
		verify(mockManager).findFragmentByTag(anyString());
		verify(mockListener).onRequestExecuted(request);
		verifyNoMoreInteractions(mockManager, mockLifecycle, mockListener);
	}

	@Test public void testExecuteRequestForFactoryFragmentReplace() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf1(FragmentRequest.REPLACE);
	}

	@Test public void testExecuteRequestForFactoryFragmentAdd() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf1(FragmentRequest.ADD);
	}

	private void innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf1(@FragmentRequest.Transaction final int transaction) {
		// Arrange:
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
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockFactory).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager).beginTransaction();
		verify(mockListener).onRequestExecuted(request);
		verifyNoMoreInteractions(mockFactory, mockListener);
	}

	@Test public void testExecuteRequestWhenLifecycleStateNotSatisfied() {
		// Arrange:
		final Lifecycle mockLifecycle = mock(Lifecycle.class);
		when(mockLifecycle.getCurrentState()).thenReturn(Lifecycle.State.CREATED);
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setLifecycle(mockLifecycle);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		final FragmentRequest request = controller.newRequest(new TestFragment());
		Mockito.clearInvocations(mockManager);
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(nullValue()));
		verify(mockLifecycle).getCurrentState();
		verifyNoMoreInteractions(mockLifecycle);
		verifyZeroInteractions(mockManager, mockListener);
	}

	@Test public void testExecuteRequestThatIgnoresLifecycleState() {
		// Arrange:
		final Lifecycle mockLifecycle = mock(Lifecycle.class);
		when(mockLifecycle.getCurrentState()).thenReturn(Lifecycle.State.CREATED);
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController.OnRequestListener mockListener = mock(FragmentController.OnRequestListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setLifecycle(mockLifecycle);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		controller.registerOnRequestListener(mockListener);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).ignoreLifecycleState(true);
		Mockito.clearInvocations(mockManager);
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockManager).isDestroyed();
		verify(mockManager).beginTransaction();
		verify(mockManager).findFragmentByTag(anyString());
		verify(mockListener).onRequestExecuted(request);
		verify(mockLifecycle).getCurrentState();
		verifyNoMoreInteractions(mockManager, mockLifecycle, mockListener);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteRequestForFactoryFragmentWithCheatingFactory() {
		// Arrange:
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
		// Act:
		controller.executeRequest(request);
	}

	@Test public void testExecuteRequestForFactoryFragmentRemove() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.REMOVE);
	}

	@Test public void testExecuteRequestForFactoryFragmentShow() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.SHOW);
	}

	@Test public void testExecuteRequestForFactoryFragmentHide() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.HIDE);
	}

	@Test public void testExecuteRequestForFactoryFragmentAttach() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.ATTACH);
	}

	@Test public void testExecuteRequestForFactoryFragmentDetach() {
		// Act:
		this.innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(FragmentRequest.DETACH);
	}

	private void innerTestExecuteRequestForFactoryFragmentWithTransactionTypeOf2(@FragmentRequest.Transaction final int transaction) {
		// Arrange:
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
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockFactory).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(2)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(0)).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager).beginTransaction();
		verify(mockListener).onRequestExecuted(request);
		verifyNoMoreInteractions(mockFactory, mockListener);
	}

	@Test public void testExecuteRequestForFactoryFragmentThatDoesNotExist() {
		// Arrange:
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
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(nullValue()));
		verify(mockFactory).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(2)).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockFactory, times(0)).createFragment(TestFactory.FRAGMENT_1);
		verify(mockManager, times(0)).beginTransaction();
		verifyZeroInteractions(mockListener);
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteRequestForFactoryFragmentWithoutFactory() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		// Act:
		controller.executeRequest(request);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteRequestForFactoryFragmentNotProvidedByFactory() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		final FragmentRequest request = controller.newRequest(TestFactory.FRAGMENT_1);
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(false);
		// Act:
		controller.executeRequest(request);
	}

	@Test public void testExecuteRequestNotIntercepted() {
		// Arrange:
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
		// Act + Assert:
		assertThat(controller.executeRequest(request), is(fragment));
		verify(mockInterceptor, times(1)).interceptFragmentRequest(request);
		verify(mockManager, times(1)).beginTransaction();
		verify(mockListener, times(1)).onRequestExecuted(request);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testExecuteRequestIntercepted() {
		// Arrange:
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
		// Act + Assert:
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
		// Arrange + Act:
		createDestroyedController().executeRequest(new FragmentRequest(null, FragmentRequest.NO_ID));
	}

	@Test public void testOnExecuteRequest() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.ADD);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commit();
	}

	@Test public void testOnExecuteRequestReplacingSame() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.REPLACE).replaceSame(true);
		final Fragment existingFragment = new TestFragment();
		when(mockManager.findFragmentByTag(request.tag)).thenReturn(existingFragment);
		assertThat(controller.onExecuteRequest(request), is(fragment));
		// Act + Assert:
		verify(mockManager, times(0)).findFragmentByTag(request.tag);
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commit();
	}

	@Test public void testOnExecuteRequestNotReplacingSame() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.REPLACE).replaceSame(false);
		when(mockManager.findFragmentByTag(request.tag)).thenReturn(null);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commit();
	}

	@Test public void testOnExecuteRequestNotReplacingSameThatAlreadyExists() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final Fragment existingFragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.REPLACE).replaceSame(false);
		when(mockManager.findFragmentByTag(request.tag)).thenReturn(existingFragment);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(existingFragment));
		verifyZeroInteractions(mockTransaction);
		verify(mockManager).findFragmentByTag(request.tag);
	}

	@Test public void testOnExecuteRequestBackStacked() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.REPLACE).addToBackStack(true);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commit();
	}

	@Test public void testOnExecuteRequestAllowingStateLoss() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.ADD).allowStateLoss(true);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commitAllowingStateLoss();
		verify(mockTransaction, times(0)).commit();
	}

	@Test public void testOnExecuteRequestImmediate() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		final Fragment fragment = new TestFragment();
		final FragmentRequest request = controller.newRequest(fragment).transaction(FragmentRequest.ADD).immediate(true);
		// Act + Assert:
		assertThat(controller.onExecuteRequest(request), is(fragment));
		verify(mockManager).beginTransaction();
		verify(mockTransaction).commit();
		verify(mockManager).executePendingTransactions();
	}

	@Test(expected = IllegalStateException.class)
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN_MR1)
	public void testOnExecuteRequestWhenManagerIsDestroyed() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		when(mockManager.isDestroyed()).thenReturn(true);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(new TestFragment());
		// Act:
		controller.onExecuteRequest(request);
	}

	@Test public void testCreateTransaction() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
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

	@Test public void testCreateTransactionReplace() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.REPLACE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).replace(TestActivity.CONTENT_VIEW_ID, mockFragment, request.tag);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionReplaceWithoutViewContainerIdSpecified() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mock(TestFragment.class)).transaction(FragmentRequest.REPLACE);
		// Act:
		controller.createTransaction(request);
	}

	@Test public void testCreateTransactionAdd() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.ADD);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).add(TestActivity.CONTENT_VIEW_ID, mockFragment, request.tag);
		verifyNoMoreInteractions(transaction);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionAddWithoutViewContainerIdSpecified() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		// Act:
		controller.createTransaction(controller.newRequest(mock(TestFragment.class)).transaction(FragmentRequest.ADD));
	}

	@Test public void testCreateTransactionRemove() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.REMOVE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).remove(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test public void testCreateTransactionShow() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.SHOW);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).show(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test public void testCreateTransactionHide() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.HIDE);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).hide(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test public void testCreateTransactionAttach() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.ATTACH);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).attach(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@Test public void testCreateTransactionDetach() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(FragmentRequest.DETACH);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).detach(mockFragment);
		verifyNoMoreInteractions(transaction);
	}

	@SuppressWarnings("ResourceType")
	@Test(expected = IllegalArgumentException.class)
	public void testCreateTransactionUnsupported() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentRequest request = controller.newRequest(mockFragment).transaction(-1);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		controller.createTransaction(request);
	}

	@Test public void testCreateTransactionWithArguments() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final Bundle args = new Bundle();
		final FragmentRequest request = controller.newRequest(mockFragment).arguments(args);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(mockFragment).setArguments(args);
	}

	@Test public void testCreateTransactionWithTransition() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentTransition transition = new TestTransition();
		final FragmentRequest request = controller.newRequest(mockFragment).transition(transition);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).setCustomAnimations(
				transition.getIncomingAnimation(),
				transition.getOutgoingAnimation(),
				transition.getIncomingBackStackAnimation(),
				transition.getOutgoingBackStackAnimation()
		);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
		verify(transaction, times(0)).setTransitionStyle(anyInt());
	}

	@Test public void testCreateTransactionWithTransitionWhenContextIsAvailable() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(context(), mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentTransition transition = new TestTransition();
		final FragmentRequest request = controller.newRequest(mockFragment).transition(transition);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).setCustomAnimations(
				transition.getIncomingAnimation(),
				transition.getOutgoingAnimation(),
				transition.getIncomingBackStackAnimation(),
				transition.getOutgoingBackStackAnimation()
		);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
		verify(transaction, times(0)).setTransitionStyle(anyInt());
	}

	@Test public void testCreateTransactionWithTransitionStyle() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).transitionStyle(android.R.style.Animation);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).setTransitionStyle(android.R.style.Animation);
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt(), anyInt(), anyInt());
		verify(transaction, times(0)).setCustomAnimations(anyInt(), anyInt());
	}

	@Test public void testCreateTransactionWithSharedElements() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final View elementFirst = new View(context());
		ViewCompat.setTransitionName(elementFirst, "Element.First");
		final View elementSecond = new View(context());
		final FragmentRequest request = controller.newRequest(mockFragment)
				.sharedElement(elementFirst, "Element.First")
				.sharedElement(elementSecond, "Element.Second");
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		assertThat(ViewCompat.getTransitionName(elementFirst), is("Element.First"));
		assertThat(ViewCompat.getTransitionName(elementSecond), is("Element.Second"));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			verify(transaction).addSharedElement(elementFirst, "Element.First");
			verify(transaction).addSharedElement(elementSecond, "Element.Second");
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateTransactionWithEmptySharedElements() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final View elementFirst = new View(context());
		final View elementSecond = new View(context());
		final FragmentRequest request = controller.newRequest(mockFragment)
				.sharedElement(elementFirst, "Element.First")
				.sharedElement(elementSecond, "Element.Second");
		request.sharedElements().clear();
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			verify(transaction, times(0)).addSharedElement(any(View.class), anyString());
		}
	}

	@Test public void testCreateTransactionToBeAddedIntoBackStack() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentTransaction mockTransaction = mock(FragmentTransaction.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		final FragmentRequest request = controller.newRequest(mockFragment).addToBackStack(true);
		when(mockManager.beginTransaction()).thenReturn(mockTransaction);
		// Act:
		final FragmentTransaction transaction = controller.createTransaction(request);
		// Assert:
		assertThat(transaction, is(notNullValue()));
		verify(transaction).addToBackStack(mockFragment.getTag());
	}

	@Test(expected = IllegalStateException.class)
	public void testCreateTransactionWhenDestroyed() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		final FragmentRequest request = controller.newRequest(new TestFragment());
		controller.destroy();
		// Act:
		controller.createTransaction(request);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testAttachTransitionsToFragment() {
		// Arrange:
		final Fragment mockFragment = mock(TestFragment.class);
		final Transition enterTransition = createTestTransition();
		final Transition exitTransition = createTestTransition();
		final Transition reenterTransition = createTestTransition();
		final Transition returnTransition = createTestTransition();
		final Transition sharedElementEnterTransition = createTestTransition();
		final Transition sharedElementEnterReturnTransition = createTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), mockFragment)
				.enterTransition(enterTransition)
				.exitTransition(exitTransition)
				.reenterTransition(reenterTransition)
				.returnTransition(returnTransition)
				.sharedElementEnterTransition(sharedElementEnterTransition)
				.sharedElementReturnTransition(sharedElementEnterReturnTransition)
				.allowEnterTransitionOverlap(true)
				.allowReturnTransitionOverlap(false);
		// Act:
		FragmentController.attachTransitionsToFragment(request, mockFragment);
		// Assert:
		verify(mockFragment).setEnterTransition(enterTransition);
		verify(mockFragment).setExitTransition(exitTransition);
		verify(mockFragment).setReenterTransition(reenterTransition);
		verify(mockFragment).setReturnTransition(returnTransition);
		verify(mockFragment).setSharedElementEnterTransition(sharedElementEnterTransition);
		verify(mockFragment).setSharedElementReturnTransition(sharedElementEnterReturnTransition);
		verify(mockFragment).setAllowEnterTransitionOverlap(true);
		verify(mockFragment).setAllowReturnTransitionOverlap(false);
		verifyNoMoreInteractions(mockFragment);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testAttachTransitionsToFragmentForRequestWithoutTransitions() {
		// Arrange:
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), mockFragment);
		// Act:
		FragmentController.attachTransitionsToFragment(request, mockFragment);
		// Assert:
		verifyZeroInteractions(mockFragment);
	}

	@Test public void testFindCurrentFragment() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setViewContainerId(TestActivity.CONTENT_VIEW_ID);
		when(mockManager.findFragmentById(controller.getViewContainerId())).thenReturn(mockFragment);
		// Act + Assert:
		assertThat(controller.findCurrentFragment(), is(mockFragment));
		verify(mockManager).findFragmentById(controller.getViewContainerId());
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testFindCurrentFragmentWithoutViewContainerIdSpecified() {
		// Arrange:
		final FragmentController controller = new FragmentController(mock(FragmentManager.class));
		// Act:
		controller.findCurrentFragment();
	}

	@Test(expected = IllegalStateException.class)
	public void testFindCurrentFragmentWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().findCurrentFragment();
	}

	@Test public void testFindFragmentByFactoryId() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final Fragment mockFragment = mock(TestFragment.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		when(mockFactory.isFragmentProvided(TestFactory.FRAGMENT_1)).thenReturn(true);
		when(mockFactory.createFragmentTag(TestFactory.FRAGMENT_1)).thenReturn("TAG.TestFragment");
		when(mockManager.findFragmentByTag("TAG.TestFragment")).thenReturn(mockFragment);
		// Act + Assert:
		assertThat(controller.findFragmentByFactoryId(TestFactory.FRAGMENT_1), is(mockFragment));
		verify(mockFactory).isFragmentProvided(TestFactory.FRAGMENT_1);
		verify(mockFactory).createFragmentTag(TestFactory.FRAGMENT_1);
		verify(mockManager).findFragmentByTag("TAG.TestFragment");
	}

	@Test(expected = IllegalStateException.class)
	public void testFindFragmentByFactoryIdWithoutFactoryAttached() {
		// Arrange:
		final FragmentController controller = new FragmentController(mock(FragmentManager.class));
		// Act:
		controller.findFragmentByFactoryId(TestFactory.FRAGMENT_1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testFindFragmentByFactoryIdWithFactoryNotProvidingFragment() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentFactory mockFactory = mock(FragmentFactory.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.setFactory(mockFactory);
		when(mockFactory.isFragmentProvided(anyInt())).thenReturn(false);
		// Act:
		controller.findFragmentByFactoryId(TestFactory.FRAGMENT_1);
		verify(mockFactory).isFragmentProvided(TestFactory.FRAGMENT_1);
		verifyNoMoreInteractions(mockFactory);
		verifyZeroInteractions(mockManager);
	}

	@Test(expected = IllegalStateException.class)
	public void testFindFragmentByFactoryIdWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().findFragmentByFactoryId(TestFactory.FRAGMENT_1);
	}

	@Test public void testHasBackStackEntries() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		// Act + Assert:
		assertThat(controller.hasBackStackEntries(), is(true));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test public void testHasBackStackEntriesOnEmptyStack() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		// Act + Assert:
		assertThat(controller.hasBackStackEntries(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test public void testTopBackStackEntry() {
		// Arrange:
		final FragmentController controller = new FragmentController(mock(FragmentManager.class));
		// Act + Assert:
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
	}

	@Test public void testClearBackStack() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		// Act:
		controller.clearBackStack();
		// Assert:
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStack();
	}

	@Test public void testClearBackStackOnEmptyBackStack() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		// Act:
		controller.clearBackStack();
		// Assert:
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test(expected = IllegalStateException.class)
	public void testClearBackStackWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().clearBackStack();
	}

	@Test public void testClearBackStackImmediate() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		when(mockManager.popBackStackImmediate()).thenReturn(true);
		// Act + Assert:
		assertThat(controller.clearBackStackImmediate(), is(true));
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStackImmediate();
	}

	@Test public void testClearBackStackImmediateWhenManagerDoesNotPops() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(5);
		when(mockManager.popBackStackImmediate()).thenReturn(false);
		// Act + Assert:
		assertThat(controller.clearBackStackImmediate(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
		verify(mockManager, times(5)).popBackStackImmediate();
	}

	@Test public void testClearBackStackImmediateOnEmptyStack() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryCount()).thenReturn(0);
		// Act + Assert:
		assertThat(controller.clearBackStackImmediate(), is(false));
		verify(mockManager, times(2)).getBackStackEntryCount();
	}

	@Test(expected = IllegalStateException.class)
	public void testClearBackStackImmediateWhenDestroyed() {
		// Arrange + Act:
		createDestroyedController().clearBackStackImmediate();
	}

	@Test public void testDestroyWhenAlreadyDestroyed() {
		// Arrange + Act:
		createDestroyedController().destroy();
	}

	@Test public void testHandleBackStackChangeDueToAddition() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentManager.BackStackEntry mockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		controller.registerOnBackStackChangeListener(mockListener);
		when(mockManager.getBackStackEntryAt(0)).thenReturn(mockBackStackEntry);
		// Act:
		controller.handleBackStackChange(1, FragmentController.BackStackListener.ADDED);
		// Assert:
		assertThat(controller.getTopBackStackEntry(), is(mockBackStackEntry));
		verify(mockListener).onFragmentsBackStackChanged(mockBackStackEntry, true);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testHandleBackStackChangeDueToRemoval() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentManager.BackStackEntry firstMockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentManager.BackStackEntry secondMockBackStackEntry = mock(FragmentManager.BackStackEntry.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		when(mockManager.getBackStackEntryAt(1)).thenReturn(secondMockBackStackEntry);
		// Act + Assert:
		controller.handleBackStackChange(2, FragmentController.BackStackListener.ADDED);
		controller.registerOnBackStackChangeListener(mockListener);
		when(mockManager.getBackStackEntryAt(0)).thenReturn(firstMockBackStackEntry);
		controller.handleBackStackChange(1, FragmentController.BackStackListener.REMOVED);
		assertThat(controller.getTopBackStackEntry(), is(firstMockBackStackEntry));
		verify(mockListener).onFragmentsBackStackChanged(secondMockBackStackEntry, false);
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testHandleBackStackChangeDueToRemovalWithoutPreviousBackStackEntry() {
		// Arrange:
		final FragmentManager mockManager = mock(FragmentManager.class);
		final FragmentController.OnBackStackChangeListener mockListener = mock(FragmentController.OnBackStackChangeListener.class);
		final FragmentController controller = new FragmentController(mockManager);
		// Act:
		controller.handleBackStackChange(0, FragmentController.BackStackListener.REMOVED);
		// Assert:
		assertThat(controller.getTopBackStackEntry(), is(nullValue()));
		verifyZeroInteractions(mockListener);
	}

	private static FragmentController createDestroyedController() {
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentController controller = new FragmentController(activity.getSupportFragmentManager());
		controller.destroy();
		return controller;
	}

	@Nullable private static Transition createTestTransition() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? new Fade() : null;
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
		@interface CallbackId {}

		private int receivedCallbacks;

		@Override @Nullable public Fragment interceptFragmentRequest(@NonNull final FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_INTERCEPT_FRAGMENT_REQUEST;
			return null;
		}

		@Override public void onRequestExecuted(@NonNull final FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_ON_REQUEST_EXECUTED;
		}

		@Override public void onFragmentsBackStackChanged(@NonNull final FragmentManager.BackStackEntry backStackEntry, final boolean added) {
			this.receivedCallbacks |= CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED;
		}

		boolean receivedCallback(@CallbackId final int callback) {
			return (receivedCallbacks & callback) == callback;
		}
	}

	public static class TestFragment extends Fragment {}

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
		@interface CallbackId {}

		private int receivedCallbacks;

		@Override @Nullable public Fragment interceptFragmentRequest(@NonNull final FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_INTERCEPT_FRAGMENT_REQUEST;
			return null;
		}

		@Override public void onRequestExecuted(@NonNull final FragmentRequest request) {
			this.receivedCallbacks |= CALLBACK_ON_REQUEST_EXECUTED;
		}

		@Override public void onFragmentsBackStackChanged(@NonNull final FragmentManager.BackStackEntry backStackEntry, final boolean added) {
			this.receivedCallbacks |= CALLBACK_ON_FRAGMENTS_BACK_STACK_CHANGED;
		}

		boolean receivedCallback(@CallbackId final int callback) {
			return (receivedCallbacks & callback) == callback;
		}
	}

	private static class TestFactory implements FragmentFactory {

		static final int FRAGMENT_1 = 0x01;

		@Override public boolean isFragmentProvided(final int fragmentId) {
			return fragmentId == FRAGMENT_1;
		}

		@Override @Nullable public Fragment createFragment(final int fragmentId) {
			switch (fragmentId) {
				case FRAGMENT_1: return new TestFragment();
				default: return null;
			}
		}

		@Override @Nullable public String createFragmentTag(final int fragmentId) {
			return "TAG.Fragment." + fragmentId;
		}
	}

	@SuppressLint("ParcelCreator")
	private static final class TestTransition implements FragmentTransition {

		@Override public int getIncomingAnimation() {
			return android.R.animator.fade_in;
		}

		@Override public int getOutgoingAnimation() {
			return android.R.animator.fade_out;
		}

		@Override public int getIncomingBackStackAnimation() {
			return android.R.animator.fade_out;
		}

		@Override public int getOutgoingBackStackAnimation() {
			return android.R.animator.fade_in;
		}

		@Override @NonNull public String getName() {
			return TestTransition.class.getName();
		}

		@Override public int describeContents() { return 0; }

		@Override public void writeToParcel(@NonNull Parcel dest, int flags) {}
	}
}