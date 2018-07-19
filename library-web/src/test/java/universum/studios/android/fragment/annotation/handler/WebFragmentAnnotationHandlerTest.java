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
package universum.studios.android.fragment.annotation.handler;

import org.junit.Test;

import universum.studios.android.fragment.WebFragment;
import universum.studios.android.fragment.annotation.WebContent;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
public final class WebFragmentAnnotationHandlerTest extends RobolectricTestCase {

	@Test public void testWebContentResId() {
		// Arrange:
		final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithContentResource.class);
		// Act + Assert:
		assertThat(annotationHandler.getWebContentResId(-1), is(TestFragmentWithContentResource.CONTENT_RES));
		assertThat(annotationHandler.getWebContent("default"), is("default"));
	}

	@Test public void testWebContent() {
		// Arrange:
		final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithContent.class);
		// Act + Assert:
		assertThat(annotationHandler.getWebContentResId(-1), is(-1));
		assertThat(annotationHandler.getWebContent("default"), is(TestFragmentWithContent.CONTENT));
	}

	@Test public void testFragmentWithoutAnnotation() {
		// Arrange:
		final WebFragmentAnnotationHandler annotationHandler = new WebAnnotationHandlers.WebFragmentHandler(TestFragmentWithoutAnnotation.class);
		// Act + Assert:
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

	public static class TestFragmentWithoutAnnotation extends WebFragment {}
}