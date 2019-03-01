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

import androidx.fragment.app.Fragment;
import universum.studios.android.fragment.annotation.ContentView;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class FragmentHandlerTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testGetContentViewResource() {
		// Arrange + Act + Assert:
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).getContentViewResource(-1), is(android.R.layout.simple_list_item_1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithAttachToContainer.class).getContentViewResource(-1), is(android.R.layout.simple_list_item_1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).getContentViewResource(-1), is(-1));
	}

	@Test public void testShouldAttachContentViewToContainer() {
		// Arrange + Act + Assert:
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).shouldAttachContentViewToContainer(), is(false));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithAttachToContainer.class).shouldAttachContentViewToContainer(), is(true));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).shouldAttachContentViewToContainer(), is(false));
	}

	@Test public void testGetContentViewBackgroundResId() {
		// Arrange + Act + Assert:
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).getContentViewBackgroundResId(-1), is(-1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithBackground.class).getContentViewBackgroundResId(-1), is(android.R.color.white));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).getContentViewBackgroundResId(-1), is(-1));
	}

	@ContentView(android.R.layout.simple_list_item_1)
	public static class TestFragment extends Fragment {}

	@ContentView(value = android.R.layout.simple_list_item_1, attachToContainer = true)
	public static final class TestFragmentWithAttachToContainer extends TestFragment {}

	@ContentView(value = android.R.layout.simple_list_item_1, background = android.R.color.white)
	public static final class TestFragmentWithBackground extends TestFragment {}

	public static final class TestFragmentWithoutAnnotation extends Fragment {}
}