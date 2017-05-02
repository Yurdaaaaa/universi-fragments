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
import android.support.test.runner.AndroidJUnit4;
import android.view.ActionMode;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.ActionBarFragmentAnnotationHandler;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class ActionBarFragmentTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "ActionBarFragmentTest";

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the annotations processing is kept enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testOnCreateAnnotationHandler() {
		final ActionBarFragment fragment = new TestFragment();
		final ActionBarFragmentAnnotationHandler annotationHandler = fragment.onCreateAnnotationHandler();
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
	public void testOnCreate() {
		// todo:: implement test
	}

	@Test
	public void testOnCreateOptionsMenu() {
		// todo:: implement test
	}

	@Test
	public void testOnActivityCreated() {
		// todo:: implement test
	}

	@Test
	public void testIsActionBarAvailable() {
		// todo:: implement test
	}

	@Test
	public void testGetActionBarDelegate() {
		// todo:: implement test
	}

	@Test
	public void testGetActionBar() {
		// todo:: implement test
	}

	@Test
	public void testGetSupportActionBar() {
		// todo:: implement test
	}

	@Test
	public void testInvalidateActionBar() {
		// todo:: implement test
	}

	@Test
	public void testStartActionMode() {
		// todo:: implement test
	}

	@Test
	public void testStartActionModeWithCallback() {
		// todo:: implement test
	}

	@Test
	public void testFinishActionMode() {
		// todo:: implement test
	}

	@Test
	public void testOnBackPress() {
		final ActionMode mockActionMode = mock(ActionMode.class);
		final ActionBarFragment fragment = new TestFragment();
		fragment.onActionModeStarted(mockActionMode);
		assertThat(fragment.onBackPress(), is(true));
		verify(mockActionMode, times(1)).finish();
	}

	@Test
	public void testOnBackPressWithoutStartedActionMode() {
		assertThat(new TestFragment().onBackPress(), is(false));
	}

	public static class TestFragment extends ActionBarFragment {
	}
}
