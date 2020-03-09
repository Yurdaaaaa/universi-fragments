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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.concurrent.Executors;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import universum.studios.android.fragment.annotation.ContentView;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.FragmentAnnotationHandler;
import universum.studios.android.test.AndroidTestCase;
import universum.studios.android.test.TestActivity;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class BaseFragmentTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testNewInstanceWithArguments() {
		// Arrange:
		final Bundle args = new Bundle();
		// Act:
		final TestFragment fragment = BaseFragment.newInstanceWithArguments(TestFragment.class, args);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(args));
	}

	@Test public void testNewInstanceWithNullArguments() {
		// Act:
		final BaseFragment fragment = BaseFragment.newInstanceWithArguments(TestFragment.class, null);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(nullValue()));
	}

	@Test public void testNewInstanceWithArgumentsForAbstractFragment() {
		// Act + Assert:
		assertThat(BaseFragment.newInstanceWithArguments(AbstractTestFragment.class, new Bundle()), is(nullValue()));
	}

	@Test public void testNewInstanceWithArgumentsForFragmentWithPrivateConstructor() {
		// Act + Assert:
		assertThat(BaseFragment.newInstanceWithArguments(TestFragmentWithPrivateConstructor.class, new Bundle()), is(nullValue()));
	}

	@Test public void testOnCreateAnnotationHandler() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act:
		final FragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		// Assert:
		assertThat(annotationHandler, is(notNullValue()));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test public void testGetAnnotationHandler() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.getAnnotationHandler(), is(notNullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final TestFragment fragment = new TestFragment();
		// Act:
		fragment.getAnnotationHandler();
	}

	@Test public void testOnAttach() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		fragment.onDetach();
		// Act:
		fragment.onAttach(new Activity());
		// Assert:
		assertThat(fragment.isAttached(), is(true));
		assertThat(fragment.hasLifecycleFlag(BaseFragment.LIFECYCLE_DETACHED), is(false));
	}

	@Test public void testGetContextTheme() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.getContextTheme(), is(activity.getTheme()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetContextThemeWhenNotAttached() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragment.getContextTheme();
	}

	@Test public void testRunOnUiThread() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		Executors.newSingleThreadExecutor().execute(() -> {
			// Act + Assert:
			assertThat(fragment.runOnUiThread(() -> {
				fragmentManager.beginTransaction().remove(fragment).commit();
				fragmentManager.executePendingTransactions();
			}), is(true));
		});
	}

	@Test public void testRunOnUiThreadWhenNotAttached() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.runOnUiThread(() -> {
			// Will not run.
			throw new AssertionError("Should not but run on Ui thread!");
		}), is(false));
	}

	@Test public void testOnCreate() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isCreated(), is(true));
		assertThat(fragment.isDestroyed(), is(false));
	}

	@Test public void testOnStart() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isStarted(), is(true));
		assertThat(fragment.isStopped(), is(false));
	}

	@Test public void testOnCreateView() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragment();
		// Act:
		final View view = fragment.onCreateView(activity.getLayoutInflater(), null, null);
		// Assert:
		assertThat(view, is(notNullValue()));
	}

	@Test public void testOnCreateViewWithoutResource() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragmentWithoutContentView();
		// Act:
		final View view = fragment.onCreateView(activity.getLayoutInflater(), null, null);
		// Assert:
		assertThat(view, is(nullValue()));
	}

	@Test public void testOnCreateViewToBeAttachedToTheContainer() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragmentWithContentViewToBeAttachedToContainer();
		final ViewGroup container = new FrameLayout(activity);
		// Act:
		final View view = fragment.onCreateView(activity.getLayoutInflater(), container, null);
		// Assert:
		assertThat(view, is(nullValue()));
		assertThat(container.getChildCount(), is(1));
	}

	@Test public void testOnCreateViewWhenAnnotationsAreDisabled() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		FragmentAnnotations.setEnabled(false);
		final BaseFragment fragment = new TestFragment();
		// Act:
		final View view = fragment.onCreateView(activity.getLayoutInflater(), null, null);
		// Assert:
		assertThat(view, is(nullValue()));
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testOnViewCreated() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final View mockView = mock(FrameLayout.class);
		// Act:
		fragment.onViewCreated(mockView, null);
		// Assert:
		verifyNoInteractions(mockView);
	}

	@Test public void testOnViewCreatedWithBackgroundResource() {
		// Arrange:
		final BaseFragment fragment = new TestFragmentWithContentViewWithBackgroundResource();
		final View mockView = mock(FrameLayout.class);
		// Act:
		fragment.onViewCreated(mockView, null);
		// Assert:
		verify(mockView).setBackgroundResource(TestFragmentWithContentViewWithBackgroundResource.BACKGROUND_RESOURCE);
		verifyNoMoreInteractions(mockView);
	}

	@Test public void testOnViewCreatedWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final BaseFragment fragment = new TestFragment();
		final View mockView = mock(FrameLayout.class);
		// Act:
		fragment.onViewCreated(mockView, null);
		// Assert:
		verifyNoInteractions(mockView);
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testIsViewCreated() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final View contentView = new FrameLayout(activity);
		contentView.setId(android.R.id.list);
		activity.setContentView(contentView);
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(android.R.id.list, fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.isViewCreated(), is(true));
	}

	@Test public void testIsViewCreatedWhenNot() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.isViewCreated(), is(false));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testInflateTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.inflateTransition(1), is(nullValue()));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testInflateTransitionOnLollipopApiLevel() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(
				fragment.inflateTransition(android.R.transition.fade),
				is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? notNullValue() : nullValue())
		);
	}

	@Test public void testInflateTransitionWhenNotAttached() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.inflateTransition(1), is(nullValue()));
	}

	@Test public void testOnResume() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().detach(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().attach(fragment).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isPaused(), is(false));
		assertThat(fragment.isResumed(), is(true));
	}

	@Test public void testInvalidateOptionsMenu() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.invalidateOptionsMenu(), is(true));
	}

	@Test public void testInvalidateOptionsMenuWhenNotAdded() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		// Only ensure that invocation of the method does not cause any troubles.
		assertThat(fragment.invalidateOptionsMenu(), is(false));
	}

	@Test public void testInvalidateOptionsMenuWhenHidden() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().hide(fragment).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.invalidateOptionsMenu(), is(false));
	}

	@Test public void testDispatchViewClick() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		final View view = new Button(context());
		// Act + Assert:
		assertThat(fragment.dispatchViewClick(view), is(false));
		assertThat(fragment.dispatchedClickedView, is(view));
	}

	@Test public void testOnPause() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		fragment.onResume();
		// Act:
		fragment.onPause();
		// Assert:
		assertThat(fragment.isPaused(), is(true));
		assertThat(fragment.isResumed(), is(false));
	}

	@Test public void testOnStop() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isStopped(), is(true));
		assertThat(fragment.isStarted(), is(false));
	}

	@Test public void testDispatchBackPress() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act + Assert:
		fragment.onBackPressResult = true;
		assertThat(fragment.dispatchBackPress(), is(fragment.onBackPressResult));
		fragment.onBackPressResult = false;
		assertThat(fragment.dispatchBackPress(), is(fragment.onBackPressResult));
	}

	@SuppressLint("ValidFragment")
	@Test public void testOnBackPress() {
		// Arrange:
		final BaseFragment fragment = new BaseFragment() {};
		// Act + Assert:
		assertThat(fragment.onBackPress(), is(false));
	}

	@Test public void testOnDestroy() {
		// Arrange:
		final FragmentActivity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getSupportFragmentManager();
		final BaseFragment fragment = new TestFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.isDestroyed(), is(true));
		assertThat(fragment.isCreated(), is(false));
	}

	@Test public void testOnDetach() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		fragment.onAttach(new Activity());
		// Act:
		fragment.onDetach();
		// Assert:
		assertThat(fragment.hasLifecycleFlag(BaseFragment.LIFECYCLE_DETACHED), is(true));
		assertThat(fragment.isAttached(), is(false));
	}

	@ContentView(android.R.layout.simple_list_item_1)
	public static class TestFragment extends BaseFragment {

		boolean onBackPressResult;
		View dispatchedClickedView;

		@Override protected boolean onBackPress() {
			return onBackPressResult;
		}

		@Override protected void onViewClick(@NonNull final View view) {
			super.onViewClick(dispatchedClickedView = view);
		}
	}

	@ContentView(value = android.R.layout.simple_list_item_1, attachToContainer = true)
	public static class TestFragmentWithContentViewToBeAttachedToContainer extends BaseFragment {}

	@ContentView(
			value = android.R.layout.simple_list_item_1,
			background = TestFragmentWithContentViewWithBackgroundResource.BACKGROUND_RESOURCE
	)
	public static class TestFragmentWithContentViewWithBackgroundResource extends BaseFragment {

		static final int BACKGROUND_RESOURCE = android.R.color.black;
	}

	@ContentView(0)
	public static class TestFragmentWithoutContentView extends BaseFragment {}

	public static abstract class AbstractTestFragment extends BaseFragment {}

	public static class TestFragmentWithPrivateConstructor extends BaseFragment {

		@SuppressLint("ValidFragment")
		private TestFragmentWithPrivateConstructor() {}
	}
}