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
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class WebFragmentTest extends RobolectricTestCase {

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}
	
    @Test public void testIsValidWebUrl() {
	    // Act + Assert:
		assertThat(WebFragment.isValidWebUrl("http://www.google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("www.google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("google.com"), is(true));
		assertThat(WebFragment.isValidWebUrl("google"), is(false));
	}

	@Test public void testNewInstance() {
		// Act:
		final WebFragment fragment = WebFragment.newInstance();
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getContent(), is(nullValue()));
		assertThat(fragment.getOptions(), is(notNullValue()));
	}

	@Test public void testNewInstanceWithOptions() {
		// Arrange:
		final WebFragment.WebOptions options = new WebFragment.WebOptions()
				.content("http://www.google.com")
				.javaScriptEnabled(true);
		// Act:
		final WebFragment fragment = WebFragment.newInstance(options);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getContent(), is(nullValue()));
		assertThat(fragment.getOptions(), is(options));
	}

	@Test public void testOnCreateAnnotationHandler() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act:
		final WebFragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
		// Assert:
		assertThat(annotationHandler, is(notNullValue()));
		assertThat(annotationHandler, is(fragment.onCreateAnnotationHandler()));
	}

	@Test public void testGetAnnotationHandler() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act + Assert:
		assertThat(fragment.getAnnotationHandler(), is(notNullValue()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final WebFragment fragment = new WebFragment();
		// Act:
		fragment.getAnnotationHandler();
	}

	@Test public void testOnCreate() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new TestFragmentWithWebContent();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.getContent(), is(TestFragmentWithWebContent.CONTENT));
	}

	@Test public void testOnCreateWithWebContentResource() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new TestFragmentWithWebContentResource();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.getContent(), is(application.getString(TestFragmentWithWebContentResource.CONTENT_RES)));
	}

	@Test public void testOnCreateWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.getContent(), is(nullValue()));
	}

	@Test public void testOnCreateWithOptions() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = WebFragment.newInstance(new WebFragment.WebOptions().content("http://www.google.com"));
		// Act:
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Assert:
		assertThat(fragment.getContent(), is("http://www.google.com"));
	}

	@Test public void testNotifyLoadingStarted() {
		// Arrange:
		final WebFragment.OnWebContentLoadingListener mockListener = mock(WebFragment.OnWebContentLoadingListener.class);
		final WebFragment fragment = new WebFragment();
		fragment.setOnWebContentLoadingListener(mockListener);
		// Act:
		fragment.notifyLoadingStarted("test_url");
		// Assert:
		verify(mockListener).onLoadingStarted("test_url");
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testNotifyLoadingStartedWithoutRegisteredListener() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act:
		// Only ensure that calling this method without registered listener does not cause any troubles.
		fragment.notifyLoadingStarted("test_url");
	}

	@Test public void testNotifyLoadingFinished() {
		// Arrange:
		final WebFragment.OnWebContentLoadingListener mockListener = mock(WebFragment.OnWebContentLoadingListener.class);
		final WebFragment fragment = new WebFragment();
		fragment.setOnWebContentLoadingListener(mockListener);
		// Act:
		fragment.notifyLoadingFinished("test_url");
		// Assert:
		verify(mockListener).onLoadingFinished("test_url");
		verifyNoMoreInteractions(mockListener);
	}

	@Test public void testNotifyLoadingFinishedWithoutRegisteredListener() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act:
		// Only ensure that calling this method without registered listener does not cause any troubles.
		fragment.notifyLoadingFinished("test_url");
	}

	@Test public void testOnCreateView() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act:
		final View view = fragment.onCreateView(LayoutInflater.from(application), null, null);
		// Assert:
		assertThat(view, is(notNullValue()));
		assertThat(view, instanceOf(WebView.class));
		assertThat(fragment.getWebView(), is(view));
	}

	@Test public void testOnCreateWebViewClient() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act + Assert:
		assertThat(fragment.onCreateWebViewClient(), is(notNullValue()));
	}

	@Test public void testOnCreateWebChromeClient() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act + Assert:
		assertThat(fragment.onCreateWebChromeClient(), is(notNullValue()));
	}

	@Test public void testLoadContent() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act + Assert:
		assertThat(fragment.loadContent("http://www.google.com"), is(false));
		assertThat(fragment.getContent(), is("http://www.google.com"));
		assertThat(fragment.getContentType(), is(WebFragment.CONTENT_URL));
	}

	@Test public void testResolveContentType() {
		// Act + Assert:
		assertThat(WebFragment.resolveContentType(null), is(WebFragment.CONTENT_EMPTY));
		assertThat(WebFragment.resolveContentType(""), is(WebFragment.CONTENT_EMPTY));
		assertThat(WebFragment.resolveContentType("http://www.google.com"), is(WebFragment.CONTENT_URL));
		assertThat(WebFragment.resolveContentType("<h3>Content</h3>"), is(WebFragment.CONTENT_HTML));
		assertThat(WebFragment.resolveContentType("Content"), is(WebFragment.CONTENT_HTML));
		assertThat(WebFragment.resolveContentType("file://content.html"), is(WebFragment.CONTENT_FILE));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnLoadContent() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		// Act:
		fragment.onLoadContent("http://www.google.com", WebFragment.CONTENT_URL);
		// Assert:
		assertThat(fragment.getWebView().getUrl(), is("http://www.google.com"));
	}

	@Test public void testOnLoadContentWithoutWebView() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act:
		fragment.onLoadContent("http://www.google.com", WebFragment.CONTENT_URL);
	}

	@Test public void testOnSaveInstanceState() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final Bundle state = new Bundle();
		// Act:
		fragment.onSaveInstanceState(state);
		// Assert:
		assertThat(state.containsKey(WebFragment.BUNDLE_OPTIONS), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_CONTENT), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_PRIVATE_FLAGS), is(true));
	}

	@Test public void testOnSaveInstanceStateWithoutWebView() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		final Bundle state = new Bundle();
		// Act:
		fragment.onSaveInstanceState(state);
		// Assert:
		assertThat(state.containsKey(WebFragment.BUNDLE_OPTIONS), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_CONTENT), is(true));
		assertThat(state.containsKey(WebFragment.BUNDLE_PRIVATE_FLAGS), is(true));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnBackPress() {
		// Arrange:
		final FragmentManager fragmentManager = Robolectric.buildActivity(TestActivity.class).create().start().resume().get().getFragmentManager();
		final WebFragment fragment = new WebFragment();
		fragmentManager.beginTransaction().add(fragment, null).commit();
		fragmentManager.executePendingTransactions();
		final WebView webView = fragment.getWebView();
		webView.loadUrl("http://www.google.com");
		// Act + Assert:
		assertThat(fragment.onBackPress(), is(true));
	}

	@Test public void testOnBackPressWithoutWebView() {
		// Arrange:
		final WebFragment fragment = new WebFragment();
		// Act + Assert:
		assertThat(fragment.onBackPress(), is(false));
	}

	@Test public void testWebOptionsCreatorCreateFromParcel() {
		// Arrange:
		final Parcel parcel = Parcel.obtain();
		parcel.writeString("http://www.google.com");
		parcel.writeInt(1);
		parcel.setDataPosition(0);
		// Act:
		final WebFragment.WebOptions options = WebFragment.WebOptions.CREATOR.createFromParcel(parcel);
		// Assert:
		assertThat(options, is(notNullValue()));
		assertThat(options.content, is("http://www.google.com"));
		assertThat(options.javaScriptEnabled, is(true));
		parcel.recycle();
	}

	@Test public void testWebOptionsCreatorNewArray() {
		// Act:
		final WebFragment.WebOptions[] optionsArray = WebFragment.WebOptions.CREATOR.newArray(5);
		// Assert:
		assertThat(optionsArray.length, is(5));
		for (final WebFragment.WebOptions options : optionsArray) {
			assertThat(options, is(nullValue()));
		}
	}

	@Test public void testWebOptionsWriteToParcel() {
		// Arrange:
		final Parcel parcel = Parcel.obtain();
		final WebFragment.WebOptions savedState = new WebFragment.WebOptions();
		savedState.content = "http://www.google.com";
		savedState.javaScriptEnabled = true;
		// Act:
		savedState.writeToParcel(parcel, 0);
		// Assert:
		parcel.setDataPosition(0);
		assertThat(parcel.readString(), is("http://www.google.com"));
		assertThat(parcel.readInt(), is(1));
		parcel.recycle();
	}

	@Test public void testWebOptionsInstantiation() {
		// Act:
		final WebFragment.WebOptions options = new WebFragment.WebOptions();
		// Assert:
		assertThat(options.describeContents(), is(0));
		assertThat(options.content, is(""));
		assertThat(options.javaScriptEnabled, is(true));
	}

	@Test public void testWebOptionsContent() {
		// Arrange:
		final WebFragment.WebOptions options = new WebFragment.WebOptions();
		// Act + Assert:
		assertThat(options.content("www.google.com"), is(options));
		assertThat(options.content(), is("www.google.com"));
	}

	@Test public void testWebOptionsJavaScriptEnabled() {
		// Arrange:
		final WebFragment.WebOptions options = new WebFragment.WebOptions();
		// Act + Assert:
		assertThat(options.javaScriptEnabled(true), is(options));
		assertThat(options.javaScriptEnabled, is(true));
		assertThat(options.javaScriptEnabled(false), is(options));
		assertThat(options.javaScriptEnabled, is(false));
	}

	public static class TestFragment extends WebFragment {}

	@WebContent(TestFragmentWithWebContent.CONTENT)
	public static class TestFragmentWithWebContent extends WebFragment {

		static final String CONTENT = "http://www.google.com";
	}

	@WebContent(valueRes = TestFragmentWithWebContentResource.CONTENT_RES)
	public static class TestFragmentWithWebContentResource extends WebFragment {

		static final int CONTENT_RES = android.R.string.ok;
	}
}