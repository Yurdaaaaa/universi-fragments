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

import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;

import org.junit.Test;
import org.robolectric.Robolectric;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.WebContent;
import universum.studios.android.fragment.annotation.handler.WebFragmentAnnotationHandler;
import universum.studios.android.test.local.RobolectricTestCase;
import universum.studios.android.test.local.TestActivity;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class WebFragmentTest extends RobolectricTestCase {

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}
	
    @Test
	public void testIsValidWebUrl() {
		assertThat(WebFragment.isValidWebUrl("http://www.google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("www.google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("google"), is(false));
	}

	@Test
	public void testNewInstance() {
		final WebFragment fragment = WebFragment.newInstance();
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getContent(), is(nullValue()));
		assertThat(fragment.getOptions(), is(notNullValue()));
	}

	@Test
	public void testNewInstanceWithOptions() {
		final WebFragment.WebOptions options = new WebFragment.WebOptions()
				.content("http://www.google.com")
				.javaScriptEnabled(true);
		final WebFragment fragment = WebFragment.newInstance(options);
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getContent(), is(nullValue()));
		assertThat(fragment.getOptions(), is(options));
	}

	@Test
	public void testOnCreateAnnotationHandler() {
		final WebFragment fragment = new WebFragment();
		final WebFragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		assertThat(annotationHandler, is(not(nullValue())));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test
	public void testGetAnnotationHandler() {
		assertThat(new WebFragment().getAnnotationHandler(), is(not(nullValue())));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		new WebFragment().getAnnotationHandler();
	}

	@Test
	public void testOnCreate() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new TestFragmentWithWebContent();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.getContent(), is(TestFragmentWithWebContent.CONTENT));
	}

	@Test
	public void testOnCreateWithWebContentResource() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new TestFragmentWithWebContentResource();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.getContent(), is(application.getString(TestFragmentWithWebContentResource.CONTENT_RES)));
	}

	@Test
	public void testOnCreateWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.getContent(), is(nullValue()));
	}

	@Test
	public void testOnCreateWithOptions() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = WebFragment.newInstance(new WebFragment.WebOptions().content("http://www.google.com"));
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		assertThat(fragment.getContent(), is("http://www.google.com"));
	}

	@Test
	public void testNotifyLoadingStarted() {
		final WebFragment.OnWebContentLoadingListener mockListener = mock(WebFragment.OnWebContentLoadingListener.class);
		final WebFragment fragment = new WebFragment();
		fragment.setOnWebContentLoadingListener(mockListener);
		fragment.notifyLoadingStarted("test_url");
		verify(mockListener, times(1)).onLoadingStarted("test_url");
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testNotifyLoadingStartedWithoutRegisteredListener() {
		// Only ensure that calling this method without registered listener does not cause any troubles.
		new WebFragment().notifyLoadingStarted("test_url");
	}

	@Test
	public void testNotifyLoadingFinished() {
		final WebFragment.OnWebContentLoadingListener mockListener = mock(WebFragment.OnWebContentLoadingListener.class);
		final WebFragment fragment = new WebFragment();
		fragment.setOnWebContentLoadingListener(mockListener);
		fragment.notifyLoadingFinished("test_url");
		verify(mockListener, times(1)).onLoadingFinished("test_url");
		verifyNoMoreInteractions(mockListener);
	}

	@Test
	public void testNotifyLoadingFinishedWithoutRegisteredListener() {
		// Only ensure that calling this method without registered listener does not cause any troubles.
		new WebFragment().notifyLoadingFinished("test_url");
	}

	@Test
	public void testOnCreateView() {
		final WebFragment fragment = new WebFragment();
		final View view = fragment.onCreateView(LayoutInflater.from(application), null, null);
		assertThat(view, is(notNullValue()));
		assertThat(view, instanceOf(WebView.class));
		assertThat(fragment.getWebView(), is(view));
	}

	@Test
	public void testOnCreateWebViewClient() {
		assertThat(new WebFragment().onCreateWebViewClient(), is(notNullValue()));
	}

	@Test
	public void testOnCreateWebChromeClient() {
		assertThat(new WebFragment().onCreateWebChromeClient(), is(notNullValue()));
	}

	@Test
	public void testLoadContent() {
		final WebFragment fragment = new WebFragment();
		assertThat(fragment.loadContent("http://www.google.com"), is(false));
		assertThat(fragment.getContent(), is("http://www.google.com"));
		assertThat(fragment.getContentType(), is(WebFragment.CONTENT_URL));
	}

	@Test
	public void testResolveContentType() {
		assertThat(WebFragment.resolveContentType(null), is(WebFragment.CONTENT_EMPTY));
		assertThat(WebFragment.resolveContentType(""), is(WebFragment.CONTENT_EMPTY));
		assertThat(WebFragment.resolveContentType("http://www.google.com"), is(WebFragment.CONTENT_URL));
		assertThat(WebFragment.resolveContentType("<h3>Content</h3>"), is(WebFragment.CONTENT_HTML));
		assertThat(WebFragment.resolveContentType("Content"), is(WebFragment.CONTENT_HTML));
		assertThat(WebFragment.resolveContentType("file://content.html"), is(WebFragment.CONTENT_FILE));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testOnLoadContent() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		fragment.onLoadContent("http://www.google.com", WebFragment.CONTENT_URL);
		assertThat(fragment.getWebView().getUrl(), is("http://www.google.com"));
	}

	@Test
	public void testOnLoadContentWithoutWebView() {
		final WebFragment fragment = new WebFragment();
		fragment.onLoadContent("http://www.google.com", WebFragment.CONTENT_URL);
	}

	@Test
	public void testOnSaveInstanceState() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final Bundle state = new Bundle();
		fragment.onSaveInstanceState(state);
		assertThat(state.containsKey(WebFragment.BUNDLE_OPTIONS), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_CONTENT), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_PRIVATE_FLAGS), is(true));
	}

	@Test
	public void testOnSaveInstanceStateWithoutWebView() {
		final WebFragment fragment = new WebFragment();
		final Bundle state = new Bundle();
		fragment.onSaveInstanceState(state);
		assertThat(state.containsKey(WebFragment.BUNDLE_OPTIONS), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_CONTENT), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_PRIVATE_FLAGS), is(true));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testOnBackPress() {
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final WebView webView = fragment.getWebView();
		webView.loadUrl("http://www.google.com");
		assertThat(new WebFragment().onBackPress(), is(false));
	}

	@Test
	public void testOnBackPressWithoutWebView() {
		assertThat(new WebFragment().onBackPress(), is(false));
	}

	@Test
	public void testWebOptionsCreatorCreateFromParcel() {
		final Parcel parcel = Parcel.obtain();
		parcel.writeString("http://www.google.com");
		parcel.writeInt(1);
		parcel.setDataPosition(0);
		final WebFragment.WebOptions options = WebFragment.WebOptions.CREATOR.createFromParcel(parcel);
		assertThat(options, is(not(nullValue())));
		assertThat(options.content, is("http://www.google.com"));
		assertThat(options.javaScriptEnabled, is(true));
		parcel.recycle();
	}

	@Test
	public void testWebOptionsCreatorNewArray() {
		final WebFragment.WebOptions[] optionsArray = WebFragment.WebOptions.CREATOR.newArray(5);
		assertThat(optionsArray.length, is(5));
		for (final WebFragment.WebOptions options : optionsArray) {
			assertThat(options, is(nullValue()));
		}
	}

	@Test
	public void testWebOptionsWriteToParcel() {
		final Parcel parcel = Parcel.obtain();
		final WebFragment.WebOptions savedState = new WebFragment.WebOptions();
		savedState.content = "http://www.google.com";
		savedState.javaScriptEnabled = true;
		savedState.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		assertThat(parcel.readString(), is("http://www.google.com"));
		assertThat(parcel.readInt(), is(1));
		parcel.recycle();
	}

	@Test
	public void testWebOptionsDescribeContents() {
		assertThat(new WebFragment.WebOptions().describeContents(), is(0));
	}

	@Test
	public void testWebOptionsContent() {
		assertThat(new WebFragment.WebOptions().content("www.google.com").content(), is("www.google.com"));
	}

	@Test
	public void testWebOptionsJavaScriptEnabled() {
		assertThat(new WebFragment.WebOptions().javaScriptEnabled(true).javaScriptEnabled(), is(true));
		assertThat(new WebFragment.WebOptions().javaScriptEnabled(false).javaScriptEnabled(), is(false));
	}

	public static class TestFragment extends WebFragment {
	}

	@WebContent(TestFragmentWithWebContent.CONTENT)
	public static class TestFragmentWithWebContent extends WebFragment {

		static final String CONTENT = "http://www.google.com";
	}

	@WebContent(valueRes = TestFragmentWithWebContentResource.CONTENT_RES)
	public static class TestFragmentWithWebContentResource extends WebFragment {

		static final int CONTENT_RES = android.R.string.ok;
	}
}