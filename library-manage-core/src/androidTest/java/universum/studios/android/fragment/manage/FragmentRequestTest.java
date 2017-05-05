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
package universum.studios.android.fragment.manage;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.test.runner.AndroidJUnit4;
import android.transition.Transition;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.util.FragmentUtils;
import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestResources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentRequestTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentRequestTest";

    @Test
	public void testConstants() {
		assertThat(FragmentRequest.NO_ID, is(-1));
		assertThat(FragmentRequest.NO_STYLE, is(-1));
    }

    @Nullable
    private Transition inflateTestTransition() {
	    return FragmentUtils.inflateTransition(mContext, TestResources.resourceIdentifier(mContext, TestResources.TRANSITION, "transition_fade"));
    }

	@Test
	public void testInstantiation() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController);
		assertThat(request.fragment(), is(nullValue()));
	}

	@Test
	public void testInstantiationWithFragment() {
		final FragmentController mockController = mock(FragmentController.class);
		final Fragment mockFragment = mock(Fragment.class);
		final FragmentRequest request = new FragmentRequest(mockController, mockFragment);
		assertThat(request.fragment(), is(mockFragment));
	}

	@Test
	public void testFragmentId() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).fragmentId(1).fragmentId(), is(1));
	}

	@Test
	public void testFragmentIdDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).fragmentId(), is(FragmentRequest.NO_ID));
	}

	@Test
	public void testOutgoingFragmentId() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).outgoingFragmentId(2).outgoingFragmentId(), is(2));
	}

	@Test
	public void testOutgoingFragmentIdDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).outgoingFragmentId(), is(FragmentRequest.NO_ID));
	}

	@Test
	public void testArguments() {
		final Bundle args = new Bundle();
		assertThat(new FragmentRequest(mock(FragmentController.class)).arguments(args).arguments(), is(args));
	}

	@Test
	public void testArgumentsDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).arguments(), is(nullValue()));
	}

	@Test
	public void testTransaction() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).transaction(FragmentRequest.REMOVE).transaction(), is(FragmentRequest.REMOVE));
	}

	@Test
	public void testTransactionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).transaction(), is(FragmentRequest.REPLACE));
	}

	@Test
	public void testTag() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).tag("TAG.TestFragment").tag(), is("TAG.TestFragment"));
	}

	@Test
	public void testTagDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).tag(), is(nullValue()));
	}

	@Test
	public void testViewContainerId() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).viewContainerId(android.R.id.list).viewContainerId(), is(android.R.id.list));
	}

	@Test
	public void testViewContainerIdDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).viewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test
	public void testTransitionStyle() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).transitionStyle(android.R.style.Animation).transitionStyle(), is(android.R.style.Animation));
	}

	@Test
	public void testTransitionStyleDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).transitionStyle(), is(FragmentRequest.NO_STYLE));
	}

	@Test
	public void testEnterTransition() {
		final Transition transition = inflateTestTransition();
		assertThat(new FragmentRequest(mock(FragmentController.class)).enterTransition(transition).enterTransition(), is(transition));
	}

	@Test
	public void testEnterTransitionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).enterTransition(), is(nullValue()));
	}

	@Test
	public void testExitTransition() {
		final Transition transition = inflateTestTransition();
		assertThat(new FragmentRequest(mock(FragmentController.class)).exitTransition(transition).exitTransition(), is(transition));
	}

	@Test
	public void testExitTransitionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).exitTransition(), is(nullValue()));
	}

	@Test
	public void testReenterTransition() {
		final Transition transition = inflateTestTransition();
		assertThat(new FragmentRequest(mock(FragmentController.class)).reenterTransition(transition).reenterTransition(), is(transition));
	}

	@Test
	public void testReenterTransitionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).reenterTransition(), is(nullValue()));
	}

	@Test
	public void testReturnTransition() {
		final Transition transition = inflateTestTransition();
		assertThat(new FragmentRequest(mock(FragmentController.class)).returnTransition(transition).returnTransition(), is(transition));
	}

	@Test
	public void testReturnTransitionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).returnTransition(), is(nullValue()));
	}

	@Test
	public void testAllowEnterTransitionOverlap() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowEnterTransitionOverlap(false).allowEnterTransitionOverlap(), is(false));
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowEnterTransitionOverlap(true).allowEnterTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowEnterTransitionOverlapDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowEnterTransitionOverlap(), is(nullValue()));
	}

	@Test
	public void testAllowReturnTransitionOverlap() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowReturnTransitionOverlap(false).allowReturnTransitionOverlap(), is(false));
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowReturnTransitionOverlap(true).allowReturnTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowReturnTransitionOverlapDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).allowReturnTransitionOverlap(), is(nullValue()));
	}

	@Test
	public void test() {
		// todo:: implement test
	}

	@Test
	public void testExecutedDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class)).executed(), is(false));
	}
}
