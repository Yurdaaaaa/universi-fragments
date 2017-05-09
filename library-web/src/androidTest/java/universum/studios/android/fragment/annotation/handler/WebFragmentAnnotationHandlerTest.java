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
package universum.studios.android.fragment.annotation.handler;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.WebFragment;
import universum.studios.android.fragment.annotation.WebContent;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class WebFragmentAnnotationHandlerTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "WebFragmentAnnotationHandlerTest";

    @Test
	public void testGetWebContentResId() {
	    final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithContentResource.class);
	    assertThat(annotationHandler.getWebContentResId(-1), is(TestFragmentWithContentResource.CONTENT_RES));
	    assertThat(annotationHandler.getWebContent("default"), is("default"));
	}

    @Test
	public void testGetWebContent() {
	    final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithContent.class);
	    assertThat(annotationHandler.getWebContentResId(-1), is(-1));
	    assertThat(annotationHandler.getWebContent("default"), is(TestFragmentWithContent.CONTENT));
	}

	@Test
	public void testFragmentWithoutAnnotation() {
		final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithoutAnnotation.class);
		assertThat(annotationHandler.getWebContentResId(-1), is(-1));
		assertThat(annotationHandler.getWebContent("default"), is("default"));
	}

	@WebContent(valueRes = TestFragmentWithContentResource.CONTENT_RES)
	public static class TestFragmentWithContentResource extends WebFragment {

		static final int CONTENT_RES = android.R.string.ok;
	}

	@WebContent(TestFragmentWithContent.CONTENT)
	public static class TestFragmentWithContent extends WebFragment {

		static final String CONTENT = "http://www.google.com";
	}

	public static class TestFragmentWithoutAnnotation extends WebFragment {
	}
}
