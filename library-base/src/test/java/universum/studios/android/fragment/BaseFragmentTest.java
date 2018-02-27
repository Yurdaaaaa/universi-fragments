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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class BaseFragmentTest extends RobolectricTestCase {

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testNewInstanceWithArguments() {
		final Bundle args = new Bundle();
		final TestFragment fragment = BaseFragment.newInstanceWithArguments(TestFragment.class, args);
		assertThat(fragment, is(not(nullValue())));
		assertThat(fragment.getArguments(), is(args));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testNewInstanceWithNullArguments() {
		final BaseFragment fragment = BaseFragment.newInstanceWithArguments(TestFragment.class, null);
		assertThat(fragment, is(not(nullValue())));
		assertThat(fragment.getArguments(), is(nullValue()));
	}

	@Test
	public void testNewInstanceWithArgumentsForAbstractFragment() {
		assertThat(BaseFragment.newInstanceWithArguments(AbstractTestFragment.class, new Bundle()), is(nullValue()));
	}

	@Test
	public void testNewInstanceWithArgumentsForFragmentWithPrivateConstructor() {
		assertThat(BaseFragment.newInstanceWithArguments(TestFragmentWithPrivateConstructor.class, new Bundle()), is(nullValue()));
	}

	@Test
	public void testOnCreateAnnotationHandler() {
		final BaseFragment fragment = new TestFragment();
		final FragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		assertThat(annotationHandler, is(not(nullValue())));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test
	public void testGetAnnotationHandler() {
		assertThat(new TestFragment().getAnnotationHandler(), is(not(nullValue())));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		new TestFragment().getAnnotationHandler();
	}

	@Test
	public void testOnAttach() {
		final BaseFragment fragment = new TestFragment();
		fragment.onDetach();
		fragment.onAttach(new Activity());
		assertThat(fragment.isAttached(), is(true));
		assertThat(fragment.hasLifecycleFlag(BaseFragment.LIFECYCLE_DETACHED), is(false));
	}

	@Test
	public void testGetContextTheme() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.getContextTheme(), is(activity.getTheme()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetContextThemeWhenNotAttached() {
		final BaseFragment fragment = new TestFragment();
		fragment.getContextTheme();
	}

	@Test
	public void testRunOnUiThread() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		Executors.newSingleThreadExecutor().execute(new Runnable() {

			@Override
			public void run() {
				assertThat(fragment.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						fragmentManager.beginTransaction().remove(fragment).commit();
						fragmentManager.executePendingTransactions();
					}
				}), is(true));
			}
		});
	}

	@Test
	public void testRunOnUiThreadWhenNotAttached() {
		final BaseFragment fragment = new TestFragment();
		assertThat(fragment.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// Will not run.
				throw new AssertionError("Should not but run on Ui thread!");
			}
		}), is(false));
	}

	@Test
	public void testOnCreate() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isCreated(), is(true));
		assertThat(fragment.isDestroyed(), is(false));
	}

	@Test
	public void testOnStart() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isStarted(), is(true));
		assertThat(fragment.isStopped(), is(false));
	}

	@Test
	public void testOnCreateView() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragment();
		final View view = fragment.onCreateView(activity.getLayoutInflater(), null, null);
		assertThat(view, is(not(nullValue())));
	}

	@Test
	public void testOnCreateViewWithoutResource() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragmentWithoutContentView();
		assertThat(fragment.onCreateView(activity.getLayoutInflater(), null, null), is(nullValue()));
	}

	@Test
	public void testOnCreateViewToBeAttachedToTheContainer() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final BaseFragment fragment = new TestFragmentWithContentViewToBeAttachedToContainer();
		final ViewGroup container = new FrameLayout(activity);
		assertThat(fragment.onCreateView(activity.getLayoutInflater(), container, null), is(nullValue()));
		assertThat(container.getChildCount(), is(1));
	}

	@Test
	public void testOnCreateViewWhenAnnotationsAreDisabled() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		FragmentAnnotations.setEnabled(false);
		final BaseFragment fragment = new TestFragment();
		assertThat(fragment.onCreateView(activity.getLayoutInflater(), null, null), is(nullValue()));
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testOnViewCreated() {
		final BaseFragment fragment = new TestFragment();
		final View mockView = mock(FrameLayout.class);
		fragment.onViewCreated(mockView, null);
		verifyZeroInteractions(mockView);
	}

	@Test
	public void testOnViewCreatedWithBackgroundResource() {
		final BaseFragment fragment = new TestFragmentWithContentViewWithBackgroundResource();
		final View mockView = mock(FrameLayout.class);
		fragment.onViewCreated(mockView, null);
		verify(mockView, times(1)).setBackgroundResource(TestFragmentWithContentViewWithBackgroundResource.BACKGROUND_RESOURCE);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testOnViewCreatedWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		final BaseFragment fragment = new TestFragment();
		final View mockView = mock(FrameLayout.class);
		fragment.onViewCreated(mockView, null);
		verifyZeroInteractions(mockView);
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testIsViewCreated() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final View contentView = new FrameLayout(activity);
		contentView.setId(android.R.id.list);
		activity.setContentView(contentView);
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(android.R.id.list, fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isViewCreated(), is(true));
	}

	@Test
	public void testIsViewCreatedWhenNot() {
		assertThat(new TestFragment().isViewCreated(), is(false));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetAllowEnterTransitionOverlapOnJellyBeanApiLevel() {
		// Only ensure that setting transition flag on transitions not supported API level does not cause any trouble.
		new TestFragment().setAllowEnterTransitionOverlap(false);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetAllowEnterTransitionOverlapOnLollipopApiLevel() {
		final BaseFragment fragment = new TestFragment();
		fragment.setAllowEnterTransitionOverlap(false);
		assertThat(fragment.getAllowEnterTransitionOverlap(), is(false));
		fragment.setAllowEnterTransitionOverlap(true);
		assertThat(fragment.getAllowEnterTransitionOverlap(), is(true));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetAllowReturnTransitionOverlapOnJellyBeanApiLevel() {
		// Only ensure that setting transition flag on transitions not supported API level does not cause any trouble.
		new TestFragment().setAllowReturnTransitionOverlap(false);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetAllowReturnTransitionOverlapOnLollipopApiLevel() {
		final BaseFragment fragment = new TestFragment();
		fragment.setAllowReturnTransitionOverlap(false);
		assertThat(fragment.getAllowReturnTransitionOverlap(), is(false));
		fragment.setAllowReturnTransitionOverlap(true);
		assertThat(fragment.getAllowReturnTransitionOverlap(), is(true));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetEnterTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setEnterTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetEnterTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setEnterTransition(transition);
		assertThat(fragment.getEnterTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetExitTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setExitTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetExitTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setExitTransition(transition);
		assertThat(fragment.getExitTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetReenterTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setReenterTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetReenterTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setReenterTransition(transition);
		assertThat(fragment.getReenterTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetReturnTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setReturnTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetReturnTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setReturnTransition(transition);
		assertThat(fragment.getReturnTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetSharedElementEnterTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setSharedElementEnterTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetSharedElementEnterTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setSharedElementEnterTransition(transition);
		assertThat(fragment.getSharedElementEnterTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testSetGetSharedElementReturnTransitionOnJellyBeanApiLevel() {
		// Only ensure that setting transition on transitions not supported API level does not cause any trouble.
		new TestFragment().setSharedElementReturnTransition(null);
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testSetGetSharedElementReturnTransitionOnLollipopApiLevel() {
		final Transition transition = new Fade();
		final BaseFragment fragment = new TestFragment();
		fragment.setSharedElementReturnTransition(transition);
		assertThat(fragment.getSharedElementReturnTransition(), is(transition));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.JELLY_BEAN)
	public void testInflateTransitionOnJellyBeanApiLevel() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.inflateTransition(1), is(nullValue()));
	}

	@Test
	@Config(sdk = Build.VERSION_CODES.LOLLIPOP)
	public void testInflateTransitionOnLollipopApiLevel() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(
				fragment.inflateTransition(android.R.transition.fade),
				is(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? notNullValue() : nullValue())
		);
	}

	@Test
	public void testInflateTransitionWhenNotAttached() {
		assertThat(new TestFragment().inflateTransition(1), is(nullValue()));
	}

	@Test
	public void testOnResume() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().detach(fragment).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().attach(fragment).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isPaused(), is(false));
		assertThat(fragment.isResumed(), is(true));
	}

	@Test
	public void testInvalidateOptionsMenu() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.invalidateOptionsMenu(), is(true));
	}

	@Test
	public void testInvalidateOptionsMenuWhenNotAdded() {
		// Only ensure that invocation of the method does not cause any troubles.
		assertThat(new TestFragment().invalidateOptionsMenu(), is(false));
	}

	@Test
	public void testInvalidateOptionsMenuWhenHidden() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().hide(fragment).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.invalidateOptionsMenu(), is(false));
	}

	@Test
	public void testDispatchViewClick() {
		final TestFragment fragment = new TestFragment();
		final View view = new Button(mApplication);
		assertThat(fragment.dispatchViewClick(view), is(false));
		assertThat(fragment.dispatchedClickedView, is(view));
	}

	@Test
	public void testStartLoader() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader<Cursor> loader = new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		assertThat(fragment.startLoader(1, null, mockLoaderCallbacks), Is.<Loader>is(loader));
		assertThat(loaderManager.getLoader(1), is(not(nullValue())));
		loaderManager.destroyLoader(1);
	}

	@Test
	public void testStartLoaderThatIsAlreadyInitialized() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader firstLoader = new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null);
		final Loader secondLoader = new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(firstLoader);
		loaderManager.initLoader(1, null, mockLoaderCallbacks);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(secondLoader);
		assertThat(fragment.startLoader(1, null, mockLoaderCallbacks), is(secondLoader));
		assertThat(loaderManager.getLoader(1), is(secondLoader));
		loaderManager.destroyLoader(1);
	}

	@Test
	public void testInitLoader() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader loader = new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		assertThat(fragment.initLoader(1, null, mockLoaderCallbacks), is(loader));
		assertThat(loaderManager.getLoader(1), is(loader));
		loaderManager.destroyLoader(1);
	}

	@Test
	public void testRestartLoader() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		final Loader loader = new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(loader);
		assertThat(fragment.restartLoader(1, null, mockLoaderCallbacks), is(loader));
		assertThat(loaderManager.getLoader(1), is(loader));
		loaderManager.destroyLoader(1);
	}

	@Test
	public void testDestroyLoader() {
		final Activity activity = Robolectric.buildActivity(TestActivity.class).create().start().resume().get();
		final FragmentManager fragmentManager = activity.getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final LoaderManager loaderManager = fragment.getLoaderManager();
		final LoaderManager.LoaderCallbacks mockLoaderCallbacks = mock(LoaderManager.LoaderCallbacks.class);
		when(mockLoaderCallbacks.onCreateLoader(1, null)).thenReturn(new CursorLoader(mApplication, Uri.EMPTY, null, null, null, null));
		loaderManager.initLoader(1, null, mockLoaderCallbacks);
		fragment.destroyLoader(1);
		assertThat(loaderManager.getLoader(1), is(nullValue()));
	}

	@Test
	public void testOnPause() {
		final BaseFragment fragment = new TestFragment();
		fragment.onResume();
		fragment.onPause();
		assertThat(fragment.isPaused(), is(true));
		assertThat(fragment.isResumed(), is(false));
	}

	@Test
	public void testOnStop() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isStopped(), is(true));
		assertThat(fragment.isStarted(), is(false));
	}

	@Test
	public void testDispatchBackPress() {
		final TestFragment fragment = new TestFragment();
		fragment.onBackPressResult = true;
		assertThat(fragment.dispatchBackPress(), is(fragment.onBackPressResult));
		fragment.onBackPressResult = false;
		assertThat(fragment.dispatchBackPress(), is(fragment.onBackPressResult));
	}

	@Test
	@SuppressLint("ValidFragment")
	public void testOnBackPress() {
		assertThat(new BaseFragment(){}.onBackPress(), is(false));
	}

	@Test
	public void testOnDestroy() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final BaseFragment fragment = new TestFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragmentManager.beginTransaction().remove(fragment).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.isDestroyed(), is(true));
		assertThat(fragment.isCreated(), is(false));
	}

	@Test
	public void testOnDetach() {
		final TestFragment fragment = new TestFragment();
		fragment.onAttach(new Activity());
		fragment.onDetach();
		assertThat(fragment.hasLifecycleFlag(BaseFragment.LIFECYCLE_DETACHED), is(true));
		assertThat(fragment.isAttached(), is(false));
	}

	@ContentView(android.R.layout.simple_list_item_1)
	public static class TestFragment extends BaseFragment {

		boolean onBackPressResult;
		View dispatchedClickedView;

		@Override
		protected boolean onBackPress() {
			return onBackPressResult;
		}

		@Override
		protected void onViewClick(@NonNull View view) {
			super.onViewClick(dispatchedClickedView = view);
		}
	}

	@ContentView(value = android.R.layout.simple_list_item_1, attachToContainer = true)
	public static class TestFragmentWithContentViewToBeAttachedToContainer extends BaseFragment {
	}

	@ContentView(
			value = android.R.layout.simple_list_item_1,
			background = TestFragmentWithContentViewWithBackgroundResource.BACKGROUND_RESOURCE
	)
	public static class TestFragmentWithContentViewWithBackgroundResource extends BaseFragment {

		static final int BACKGROUND_RESOURCE = android.R.color.black;
	}

	@ContentView(0)
	public static class TestFragmentWithoutContentView extends BaseFragment {
	}

	public static abstract class AbstractTestFragment extends BaseFragment {
	}

	public static class TestFragmentWithPrivateConstructor extends BaseFragment {

		@SuppressLint("ValidFragment")
		private TestFragmentWithPrivateConstructor() {
		}
	}
}
