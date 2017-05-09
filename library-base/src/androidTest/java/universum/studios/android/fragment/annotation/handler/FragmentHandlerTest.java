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
import android.app.Fragment;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.ContentView;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentHandlerTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentHandlerTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testGetContentViewResource() {
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).getContentViewResource(-1), is(android.R.layout.simple_list_item_1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithAttachToContainer.class).getContentViewResource(-1), is(android.R.layout.simple_list_item_1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).getContentViewResource(-1), is(-1));
	}

    @Test
	public void testShouldAttachContentViewToContainer() {
	    assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).shouldAttachContentViewToContainer(), is(false));
	    assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithAttachToContainer.class).shouldAttachContentViewToContainer(), is(true));
	    assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).shouldAttachContentViewToContainer(), is(false));
	}

	@Test
	public void testGetContentViewBackgroundResId() {
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragment.class).getContentViewBackgroundResId(-1), is(-1));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithBackground.class).getContentViewBackgroundResId(-1), is(android.R.color.white));
		assertThat(new BaseAnnotationHandlers.FragmentHandler(TestFragmentWithoutAnnotation.class).getContentViewBackgroundResId(-1), is(-1));
	}

	@ContentView(android.R.layout.simple_list_item_1)
	public static class TestFragment extends Fragment {
	}

	@ContentView(value = android.R.layout.simple_list_item_1, attachToContainer = true)
	public static final class TestFragmentWithAttachToContainer extends TestFragment {
	}

	@ContentView(value = android.R.layout.simple_list_item_1, background = android.R.color.white)
	public static final class TestFragmentWithBackground extends TestFragment {
	}

	public static final class TestFragmentWithoutAnnotation extends Fragment {
	}
}
