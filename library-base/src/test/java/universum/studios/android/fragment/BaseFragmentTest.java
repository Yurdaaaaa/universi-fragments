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
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import org.hamcrest.core.Is;
import org.junit.Test;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import java.util.concurrent.Executors;

import universum.studios.android.fragment.annotation.ContentView;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.FragmentAnnotationHandler;
import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class BaseFragmentTest extends RobolectricTestCase {

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testNewInstanceWithArguments() {
		// Arrange:
		final Bundle args = new Bundle();
		// Act:
		final TestFragment fragment = BaseFragment.newInstanceWithArguments(TestFragment.class, args);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(args));
	}

	@SuppressWarnings("ConstantConditions")
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
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
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
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override public void run() {
				// Act + Assert:
				assertThat(fragment.runOnUiThread(new Runnable() {

					@Override public void run() {
						fragmentManager.beginTransaction().remove(fragment).commit();
						fragmentManager.executePendingTransactions();
					}
				}), is(true));
			}
		});
	}

	@Test public void testRunOnUiThreadWhenNotAttached() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act + Assert:
		assertThat(fragment.runOnUiThread(new Runnable() {

			@Override public void run() {
				// Will not run.
				throw new AssertionError("Should not but run on Ui thread!");
			}
		}), is(false));
	}

	@Test public void testOnCreate() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		verifyZeroInteractions(mockView);
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

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnViewCreatedWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final BaseFragment fragment = new TestFragment();
		final View mockView = mock(FrameLayout.class);
		// Act:
		fragment.onViewCreated(mockView, null);
		// Assert:
		verifyZeroInteractions(mockView);
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testIsViewCreated() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final View contentView = new FrameLayout(activity);
		contentView.setId(android.R.id.list);
		activity.setContentView(contentView);
		final FragmentManager fragmentManager = activity.getFragmentManager();
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
	@Test public void testAllowEnterTransitionOverlapOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition flag on transitions not supported API level does not cause any trouble.
		fragment.setAllowEnterTransitionOverlap(false);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testAllowEnterTransitionOverlapOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act + Assert:
		fragment.setAllowEnterTransitionOverlap(false);
		assertThat(fragment.getAllowEnterTransitionOverlap(), is(false));
		fragment.setAllowEnterTransitionOverlap(true);
		assertThat(fragment.getAllowEnterTransitionOverlap(), is(true));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetAllowReturnTransitionOverlapOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition flag on transitions not supported API level does not cause any trouble.
		fragment.setAllowReturnTransitionOverlap(false);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testAllowReturnTransitionOverlapOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		// Act + Assert:
		fragment.setAllowReturnTransitionOverlap(false);
		assertThat(fragment.getAllowReturnTransitionOverlap(), is(false));
		fragment.setAllowReturnTransitionOverlap(true);
		assertThat(fragment.getAllowReturnTransitionOverlap(), is(true));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetEnterTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setEnterTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testEnterTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setEnterTransition(transition);
		assertThat(fragment.getEnterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetExitTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setExitTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testExitTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setExitTransition(transition);
		assertThat(fragment.getExitTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetReenterTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setReenterTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testReenterTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setReenterTransition(transition);
		assertThat(fragment.getReenterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetReturnTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setReturnTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testReturnTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setReturnTransition(transition);
		assertThat(fragment.getReturnTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetSharedElementEnterTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setSharedElementEnterTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementEnterTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setSharedElementEnterTransition(transition);
		assertThat(fragment.getSharedElementEnterTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testSetGetSharedElementReturnTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final TestFragment fragment = new TestFragment();
		// Act:
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		fragment.setSharedElementReturnTransition(null);
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testSharedElementReturnTransitionOnLollipopApiLevel() {
		// Arrange:
		final BaseFragment fragment = new TestFragment();
		final Transition transition = new Fade();
		// Act + Assert:
		fragment.setSharedElementReturnTransition(transition);
		assertThat(fragment.getSharedElementReturnTransition(), is(transition));
	}

	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	@Test public void testInflateTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act + Assert:
		assertThat(fragment.inflateTransition(1), is(nullValue()));
	}

	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	@Test public void testInflateTransitionOnLollipopApiLevel() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final View view = new Button(application);
		// Act + Assert:
		assertThat(fragment.dispatchViewClick(view), is(false));
		assertThat(fragment.dispatchedClickedView, is(view));
	}

	@Test public void testStartLoader() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader<Cursor> loader = new CursorLoader(application, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		// Act + Assert:
		assertThat(fragment.startLoader(1, null, mockLoaderCallbacks), Is.<Loader>is(loader));
		assertThat(loaderManager.getLoader(1), is(not(nullValue())));
		loaderManager.destroyLoader(1);
	}

	@Test public void testStartLoaderThatIsAlreadyInitialized() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader firstLoader = new CursorLoader(application, Uri.EMPTY, null, null, null, null);
		final Loader secondLoader = new CursorLoader(application, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(firstLoader);
		loaderManager.initLoader(1, null, mockLoaderCallbacks);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(secondLoader);
		// Act + Assert:
		assertThat(fragment.startLoader(1, null, mockLoaderCallbacks), is(secondLoader));
		assertThat(loaderManager.getLoader(1), is(secondLoader));
		loaderManager.destroyLoader(1);
	}

	@Test public void testInitLoader() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader loader = new CursorLoader(application, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		// Act + Assert:
		assertThat(fragment.initLoader(1, null, mockLoaderCallbacks), is(loader));
		assertThat(loaderManager.getLoader(1), is(loader));
		loaderManager.destroyLoader(1);
	}

	@Test public void testRestartLoader() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader loader = new CursorLoader(application, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		// Act + Assert:
		assertThat(fragment.restartLoader(1, null, mockLoaderCallbacks), is(loader));
		assertThat(loaderManager.getLoader(1), is(loader));
		loaderManager.destroyLoader(1);
	}

	@Test public void testDestroyLoader() {
		// Arrange:
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(new CursorLoader(application, Uri.EMPTY, null, null, null, null));
		loaderManager.initLoader(1, null, mockLoaderCallbacks);
		// Act:
		fragment.destroyLoader(1);
		// Assert:
		assertThat(loaderManager.getLoader(1), is(nullValue()));
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
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