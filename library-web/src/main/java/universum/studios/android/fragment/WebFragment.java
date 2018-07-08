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

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.WebAnnotationHandlers;
import universum.studios.android.fragment.annotation.handler.WebFragmentAnnotationHandler;

/**
 * An {@link ActionBarFragment} implementation that allows to show a web content within a {@link WebView}.
 * This fragment creates WebView as its root view when {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}
 * is called. That WebView is used to present the web content supplied to this web fragment.
 * <p>
 * The desired web content may be specified through {@link WebOptions} when creating new instance of
 * WebFragment via {@link #newInstance(WebOptions)} or when you have access to already
 * visible and showing web fragment via {@link #loadContent(String)}.
 *
 * <h3>Web content types</h3>
 * Following content types are supported as content that may be loaded into WebView:
 * <ul>
 * <li>
 * <b>URL</b>
 * <p>
 * http://www.google.com
 * </li>
 * <li>
 * <b>HTML</b>
 * <pre>
 * &lt;h6&gt;Content heading&lt;/h6&gt;
 * &lt;p&gt;
 * Content paragraph.
 * &lt;/p&gt;
 * </pre>
 * </li>
 * <li>
 * <b>FILE</b>
 * <p>
 * file://PATH_TO_THE_FILE_WITH_WEB_CONTENT
 * </li>
 * </ul>
 *
 * <h3>Accepted annotations</h3>
 * <ul>
 * <li>
 * {@link universum.studios.android.fragment.annotation.WebContent @WebContent} <b>[class]</b>
 * <p>
 * If this annotation is presented, the content specified via this annotation will be loaded into
 * {@link WebView} of an instance of annotated WebFragment sub-class.
 * </li>
 * </ul>
 *
 * @author Martin Albedinsky
 * @see OnWebContentLoadingListener
 */
public class WebFragment extends ActionBarFragment {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "WebFragment";

	/**
	 * Key used to store {@link WebOptions} in {@link Bundle}.
	 */
	@VisibleForTesting
	static final String BUNDLE_OPTIONS = WebFragment.class.getName() + ".BUNDLE.Options";

	/**
	 * Key used to store {@link #mContent} in {@link Bundle}.
	 */
	@VisibleForTesting
	static final String BUNDLE_CONTENT = WebFragment.class.getName() + ".BUNDLE.Content";

	/**
	 * Key used to store {@link #mPrivateFlags} in {@link Bundle}.
	 */
	@VisibleForTesting
	static final String BUNDLE_PRIVATE_FLAGS = WebFragment.class.getName() + ".BUNDLE.PrivateFlags";

	/**
	 * Flag indicating no content to load.
	 */
	protected static final int CONTENT_EMPTY = 0x00;

	/**
	 * Flag indicating that {@link #mContent} should be loaded as HTML.
	 */
	protected static final int CONTENT_HTML = 0x01;

	/**
	 * Flag indicating that {@link #mContent} should be loaded as URL.
	 */
	protected static final int CONTENT_URL = 0x02;

	/**
	 * Flag indicating that {@link #mContent} should be loaded as FILE.
	 */
	protected static final int CONTENT_FILE = 0x03;

	/**
	 * Defines an annotation for determining set of allowed content type flags for
	 * {@link #onLoadContent(String, int)} method.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({CONTENT_EMPTY, CONTENT_URL, CONTENT_HTML, CONTENT_FILE})
	public @interface ContentType {
	}

	/**
	 * Content data encoding.
	 */
	private static final String DATA_ENCODING = "UTF-8";

	/**
	 * Content data mime type.
	 */
	private static final String DATA_MIME_TYPE = "text/html";

	/**
	 * Private flag indicating whether this fragment is ready to load the current content or not.
	 */
	private static final int PFLAG_READY_TO_LOAD_CONTENT = 0x00000001;

	/**
	 * The maximum length of the substring of the current content to log with log cat output.
	 */
	private static final int LOG_CONTENT_MAX_LENGTH = 256;

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Simple listener for {@link WebFragment} which receives callbacks whenever loading process of
	 * a specific <b>web url</b> was started or finished.
	 *
	 * @author Martin Albedinsky
	 */
	public interface OnWebContentLoadingListener {

		/**
		 * Invoked whenever loading process of the specified <var>webUrl</var> within an instance of
		 * {@link WebFragment} for which is this callback registered just started.
		 *
		 * @param webUrl The web url that is currently being loaded into web view.
		 */
		void onLoadingStarted(@NonNull String webUrl);

		/**
		 * Invoked whenever loading process of the specified <var>webUrl</var> within an instance of
		 * {@link WebFragment} for which is this callback registered just finished.
		 *
		 * @param webUrl The web url that was currently loaded into web view.
		 */
		void onLoadingFinished(@NonNull String webUrl);
	}

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Matcher used to validate web urls.
	 */
	private static final Matcher WEB_URL_MATCHER = Patterns.WEB_URL.matcher("");

	/**
	 * Matcher for the {@code file://PATH_TO_FILE} pattern.
	 */
	private static final Matcher FILE_URL_MATCHER = Pattern.compile("file://(.*)").matcher("");

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Options specified for this fragment instance via {@link #newInstance(WebOptions)}.
	 */
	private WebOptions mOptions = new WebOptions();

	/**
	 * Web view to manage HTML or URL content.
	 */
	private WebView mWebView;

	/**
	 * Content to load into the web view.
	 */
	private String mContent;

	/**
	 * Type of the current content.
	 */
	private int mContentType = CONTENT_EMPTY;

	/**
	 * Content loading listener.
	 */
	private OnWebContentLoadingListener mContentLoadingListener;

	/**
	 * Stores all private flags for this fragment.
	 */
	private int mPrivateFlags;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Checks whether the given <var>url</var> is valid and can be loaded into web view.
	 *
	 * @param url Url to check.
	 * @return {@code True} if url matches valid web URL format, {@code false} otherwise.
	 */
	public static boolean isValidWebUrl(@Nullable final String url) {
		return WEB_URL_MATCHER.reset(url).matches();
	}

	/**
	 * Creates a new instance of WebFragment with no initial content to load.
	 *
	 * @return New instance of WebFragment.
	 */
	@NonNull
	public static WebFragment newInstance() {
		return newInstance(new WebOptions());
	}

	/**
	 * Creates a new instance of WebFragment with the given options.
	 *
	 * @param options Options to manage WebFragment.
	 * @return New instance of WebFragment.
	 */
	@NonNull
	public static WebFragment newInstance(@NonNull final WebOptions options) {
		final WebFragment fragment = new WebFragment();
		fragment.mOptions = options;
		return fragment;
	}

	/**
	 */
	@Override
	WebFragmentAnnotationHandler onCreateAnnotationHandler() {
		return WebAnnotationHandlers.obtainWebFragmentHandler(getClass());
	}

	/**
	 */
	@NonNull
	@Override
	protected WebFragmentAnnotationHandler getAnnotationHandler() {
		FragmentAnnotations.checkIfEnabledOrThrow();
		return (WebFragmentAnnotationHandler) mAnnotationHandler;
	}

	/**
	 * Updates the current private flags.
	 *
	 * @param flag Value of the desired flag to add/remove to/from the current private flags.
	 * @param add  Boolean flag indicating whether to add or remove the specified <var>flag</var>.
	 */
	private void updatePrivateFlags(final int flag, final boolean add) {
		if (add) this.mPrivateFlags |= flag;
		else this.mPrivateFlags &= ~flag;
	}

	/**
	 */
	@Override
	public void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mAnnotationHandler != null) {
			final WebFragmentAnnotationHandler annotationHandler = (WebFragmentAnnotationHandler) mAnnotationHandler;
			final int contentResId = annotationHandler.getWebContentResId(-1);
			if (contentResId == -1) {
				this.mContent = annotationHandler.getWebContent(null);
			} else {
				this.mContent = getString(contentResId);
			}
		}
		if (savedInstanceState != null) {
			this.mOptions = savedInstanceState.getParcelable(BUNDLE_OPTIONS);
			this.mPrivateFlags = savedInstanceState.getInt(BUNDLE_PRIVATE_FLAGS);
			this.mContent = savedInstanceState.getString(BUNDLE_CONTENT);
		} else if (mOptions != null && !TextUtils.isEmpty(mOptions.content)) {
			this.mContent = mOptions.content;
		}
		this.mContentType = resolveContentType(mContent);
	}

	/**
	 * Returns the web options specified for this fragment instance.
	 *
	 * @return This fragment's web options.
	 * @see #newInstance(WebOptions)
	 */
	@NonNull
	public final WebOptions getOptions() {
		return mOptions;
	}

	/**
	 * Registers a callback to be invoked when loading process of the current content into web view
	 * starts or finishes.
	 *
	 * @param listener The desired listener callback. May be {@code null} to clear the current one.
	 */
	public void setOnWebContentLoadingListener(@Nullable final OnWebContentLoadingListener listener) {
		this.mContentLoadingListener = listener;
	}

	/**
	 * Called to notify, that loading process of the specified <var>webUrl</var> just started.
	 * <p>
	 * By default this will dispatch {@link OnWebContentLoadingListener#onLoadingStarted(String)}
	 * callback to the current OnWebContentLoadingListener listener.
	 *
	 * @param webUrl Web url which is currently being loaded into the current web view.
	 */
	protected void notifyLoadingStarted(@NonNull final String webUrl) {
		if (mContentLoadingListener != null) mContentLoadingListener.onLoadingStarted(webUrl);
	}

	/**
	 * Called to notify, that loading process of the specified <var>webUrl</var> was finished.
	 * <p>
	 * By default, this will dispatch {@link OnWebContentLoadingListener#onLoadingFinished(String)}
	 * callback to the current OnWebContentLoadingListener listener.
	 *
	 * @param webUrl Web url which was currently loaded into the current web view.
	 */
	protected void notifyLoadingFinished(@NonNull final String webUrl) {
		if (mContentLoadingListener != null) mContentLoadingListener.onLoadingFinished(webUrl);
	}

	/**
	 */
	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
		this.mWebView = new WebView(inflater.getContext());
		this.mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		this.mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
		// Set custom WebViewClient and WebChromeClient.
		final WebViewClient client = onCreateWebViewClient();
		if (client != null) {
			this.mWebView.setWebViewClient(client);
		}
		final WebChromeClient chromeClient = onCreateWebChromeClient();
		if (chromeClient != null) {
			this.mWebView.setWebChromeClient(chromeClient);
		}
		this.mWebView.getSettings().setJavaScriptEnabled(mOptions.javaScriptEnabled);
		return mWebView;
	}

	/**
	 * Invoked during web view's initialization process. You can create here your custom implementation
	 * of WebViewClient to manage specific callbacks for such a client.
	 *
	 * @return Default web view client.
	 * @see #onCreateWebChromeClient()
	 */
	@Nullable
	protected WebViewClient onCreateWebViewClient() {
		return new WebViewClient() {

			/**
			 */
			@Override
			public void onPageFinished(final WebView view, final String url) {
				super.onPageFinished(view, url);
				notifyLoadingFinished(url);
			}

			/**
			 */
			@Override
			public void onPageStarted(final WebView view, final String url, final Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				notifyLoadingStarted(url);
			}
		};
	}

	/**
	 * Invoked during web view's initialization process. You can create here your custom implementation
	 * of WebChromeClient to manage specific callbacks for such a client.
	 *
	 * @return Default web chrome client.
	 * @see #onCreateWebViewClient()
	 */
	@Nullable
	protected WebChromeClient onCreateWebChromeClient() {
		return new WebChromeClient();
	}

	/**
	 * Returns the instance of WebView of this fragment instance.
	 *
	 * @return WebView of this web fragment instance.
	 */
	@Nullable
	public WebView getWebView() {
		return mWebView;
	}

	/**
	 */
	@Override
	public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.updatePrivateFlags(PFLAG_READY_TO_LOAD_CONTENT, true);
		if (savedInstanceState != null && mWebView != null && mContentType != CONTENT_EMPTY && mContentType != CONTENT_HTML) {
			mWebView.restoreState(savedInstanceState);
		} else {
			onLoadContent(mContent, mContentType);
		}
	}

	/**
	 * Loads the given content into the WebView of this web fragment instance.
	 *
	 * @param content Content to load. May be a raw <b>HTML</b>, web <b>URL</b> or path to a <b>FILE</b>
	 *                with HTML content.
	 * @return {@code True} if content was loaded, {@code false} if it was prepared for loading and
	 * will be loaded in the feature when WebView is ready.
	 * @see #getContent()
	 */
	public boolean loadContent(@Nullable final String content) {
		this.mContent = content;
		this.mContentType = resolveContentType(mContent);
		if ((mPrivateFlags & PFLAG_READY_TO_LOAD_CONTENT) != 0) {
			onLoadContent(mContent, mContentType);
			return true;
		}
		return false;
	}

	/**
	 * Returns the current content that is loaded or prepared to be loaded into the web view of this
	 * web fragment instance.
	 *
	 * @return Current content. This can be a raw HTML or web URL or a FILE path.
	 * @see #loadContent(String)
	 */
	@Nullable
	public String getContent() {
		return mContent;
	}

	/**
	 * Returns the type of the current content that is loaded or prepared to be loaded into the web
	 * view of this web fragment instance.
	 *
	 * @return Type of the current content. One of types defined by {@link ContentType @ContentType}
	 * annotation.
	 * @see #loadContent(String)
	 */
	@ContentType
	public int getContentType() {
		return mContentType;
	}

	/**
	 * Runs resolving process of the current content.
	 *
	 * @return One of the flags {@link #CONTENT_EMPTY}, {@link #CONTENT_HTML}, {@link #CONTENT_URL}
	 * or {@link #CONTENT_FILE}.
	 */
	@ContentType
	@VisibleForTesting
	static int resolveContentType(@Nullable String content) {
		final int contentType;
		if (TextUtils.isEmpty(content)) {
			contentType = CONTENT_EMPTY;
		} else {
			if (WEB_URL_MATCHER.reset(content).matches()) {
				contentType = CONTENT_URL;
			} else if (FILE_URL_MATCHER.reset(content).matches()) {
				contentType = CONTENT_FILE;
			} else {
				contentType = CONTENT_HTML;
			}
		}
		return contentType;
	}

	/**
	 * Invoked whenever {@link #loadContent(String)} is called and this fragment is ready (READY means
	 * after {@link #onActivityCreated(Bundle)} was called) to load that specific content
	 * or from {@link #onActivityCreated(Bundle)} when this fragment is being first time
	 * created.
	 *
	 * @param content Content to load. This can be a raw HTML, web URL or a path to FILE.
	 * @param type    A type of the specified <var>content</var>. One of flags {@link #CONTENT_EMPTY},
	 *                {@link #CONTENT_HTML}, {@link #CONTENT_URL} or {@link #CONTENT_FILE}.
	 */
	protected void onLoadContent(@Nullable final String content, @ContentType final int type) {
		if (mWebView != null) {
			if (!TextUtils.isEmpty(content)) {
				if (content.length() > LOG_CONTENT_MAX_LENGTH) {
					FragmentsLogging.d(TAG, "Loading content('" + content.substring(0, LOG_CONTENT_MAX_LENGTH) + "') into web view.");
				} else {
					FragmentsLogging.d(TAG, "Loading content('" + content + "') into web view.");
				}
			}
			switch (type) {
				case CONTENT_EMPTY:
					mWebView.loadDataWithBaseURL("", "", DATA_MIME_TYPE, DATA_ENCODING, "");
					break;
				case CONTENT_URL:
				case CONTENT_FILE:
					mWebView.loadUrl(content);
					break;
				case CONTENT_HTML:
				default:
					mWebView.loadDataWithBaseURL("", content, DATA_MIME_TYPE, DATA_ENCODING, "");
					break;
			}
		}
	}

	/**
	 */
	@Override
	public void onSaveInstanceState(@NonNull final Bundle state) {
		super.onSaveInstanceState(state);
		if (mWebView != null) {
			mWebView.saveState(state);
		}
		state.putParcelable(BUNDLE_OPTIONS, mOptions);
		state.putString(BUNDLE_CONTENT, mContent);
		state.putInt(BUNDLE_PRIVATE_FLAGS, mPrivateFlags);
	}

	/**
	 */
	@Override
	protected boolean onBackPress() {
		if (mWebView != null && mWebView.canGoBack()) {
			mWebView.goBack();
			return true;
		}
		return false;
	}

	/**
	 */
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		this.updatePrivateFlags(PFLAG_READY_TO_LOAD_CONTENT, false);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * Simple class specifying options for {@link WebFragment} like {@link #content(String)}
	 * {@link #javaScriptEnabled(boolean)}.
	 *
	 * @author Martin Albedinsky
	 */
	public static class WebOptions implements Parcelable {

		/**
		 * Creator used to create an instance or array of instances of WebOptions from {@link Parcel}.
		 */
		public static final Creator<WebOptions> CREATOR = new Creator<WebOptions>() {

			/**
			 */
			@Override
			public WebOptions createFromParcel(@NonNull final Parcel source) {
				return new WebOptions(source);
			}

			/**
			 */
			@Override
			public WebOptions[] newArray(final int size) {
				return new WebOptions[size];
			}
		};

		/**
		 * Content to load into web view.
		 */
		String content;

		/**
		 * Flag indicating whether Java-Script should be enabled or not.
		 */
		boolean javaScriptEnabled;

		/**
		 * Creates a new instance of WebOptions with default options.
		 */
		public WebOptions() {
			this.content = "";
			this.javaScriptEnabled = true;
		}

		/**
		 * Called from {@link #CREATOR} to create an instance of WebOptions form the given parcel
		 * <var>source</var>.
		 *
		 * @param source Parcel with data for the new instance.
		 */
		protected WebOptions(@NonNull final Parcel source) {
			this.content = source.readString();
			this.javaScriptEnabled = source.readInt() != 0;
		}

		/**
		 */
		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeString(content);
			dest.writeInt(javaScriptEnabled ? 1 : 0);
		}

		/**
		 */
		@Override
		public int describeContents() {
			return 0;
		}

		/**
		 * Sets a content to load into {@link WebView}.
		 *
		 * @param content Content to load. This may be a raw <b>HTML</b>, web <b>URL</b> or path to
		 *                a <b>FILE</b> with HTML content.
		 * @return These options to allow methods chaining.
		 * @see #content()
		 */
		public WebOptions content(@NonNull String content) {
			this.content = content;
			return this;
		}

		/**
		 * Returns the content to load into {@link WebView}
		 *
		 * @return Content to be loaded into web view.
		 * @see #content(String)
		 */
		public String content() {
			return content;
		}

		/**
		 * Sets a boolean flag indicating whether to enable Java-Script or not.
		 *
		 * @param enabled {@code True} to enable, {@code false} otherwise.
		 * @return These options to allow methods chaining.
		 * @see #javaScriptEnabled()
		 */
		public WebOptions javaScriptEnabled(boolean enabled) {
			this.javaScriptEnabled = enabled;
			return this;
		}

		/**
		 * Returns boolean flag indicating whether to enable Java-Script in {@link WebView}.
		 *
		 * @return {@code True} if enable, {@code false} otherwise.
		 * @see #javaScriptEnabled(boolean)
		 */
		public boolean javaScriptEnabled() {
			return javaScriptEnabled;
		}
	}
}