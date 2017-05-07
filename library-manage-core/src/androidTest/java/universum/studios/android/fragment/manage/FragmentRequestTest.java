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
import android.support.v4.util.Pair;
import android.transition.Transition;
import android.view.View;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import universum.studios.android.fragment.util.FragmentUtils;
import universum.studios.android.test.BaseInstrumentedTest;
import universum.studios.android.test.TestResources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

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
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID);
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
	public void testToString() {
		final String toString = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).toString();
		assertThat(toString.startsWith(FragmentRequest.class.getSimpleName()), is(true));
	}

	@Test
	public void testToStringWithTransition() {
		final FragmentTransition mockTransition = mock(FragmentTransition.class);
		when(mockTransition.getName()).thenReturn("TestFragmentTransition");
		final String toString = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transition(mockTransition).toString();
		assertThat(toString.startsWith(FragmentRequest.class.getSimpleName()), is(true));
		assertThat(toString.contains(mockTransition.getName()), is(true));
	}

	@Test
	public void testFragmentId() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).fragmentId(100).fragmentId(), is(100));
	}

	@Test
	public void testOutgoingFragmentId() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).outgoingFragmentId(2).outgoingFragmentId(), is(2));
	}

	@Test
	public void testOutgoingFragmentIdDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).outgoingFragmentId(), is(FragmentRequest.NO_ID));
	}

	@Test
	public void testArguments() {
		final Bundle args = new Bundle();
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).arguments(args).arguments(), is(args));
	}

	@Test
	public void testArgumentsDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).arguments(), is(nullValue()));
	}

	@Test
	public void testTransaction() {
		assertThat(
				new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.REMOVE).transaction(),
				is(FragmentRequest.REMOVE)
		);
	}

	@Test
	public void testTransactionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(), is(FragmentRequest.REPLACE));
	}

	@Test
	public void testTag() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).tag("TAG.TestFragment").tag(), is("TAG.TestFragment"));
	}

	@Test
	public void testTagDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).tag(), is(nullValue()));
	}

	@Test
	public void testViewContainerId() {
		assertThat(
				new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).viewContainerId(android.R.id.list).viewContainerId(),
				is(android.R.id.list)
		);
	}

	@Test
	public void testViewContainerIdDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).viewContainerId(), is(FragmentController.NO_CONTAINER_ID));
	}

	@Test
	public void testTransition() {
		final FragmentTransition transition = mock(FragmentTransition.class);
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transition(transition).transition(), is(transition));
	}

	@Test
	public void testTransitionDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transition(), is(nullValue()));
	}

	@Test
	public void testTransitionStyle() {
		assertThat(
				new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transitionStyle(android.R.style.Animation).transitionStyle(),
				is(android.R.style.Animation)
		);
	}

	@Test
	public void testTransitionStyleDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transitionStyle(), is(FragmentRequest.NO_STYLE));
	}

	@Test
	public void testEnterTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.enterTransition(transition).enterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_ENTER), is(true));
	}

	@Test
	public void testEnterTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.enterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_ENTER), is(false));
	}

	@Test
	public void testExitTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.exitTransition(transition).exitTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_EXIT), is(true));
	}

	@Test
	public void testExitTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.exitTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_EXIT), is(false));
	}

	@Test
	public void testReenterTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.reenterTransition(transition).reenterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_REENTER), is(true));
	}

	@Test
	public void testReenterTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.reenterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_REENTER), is(false));
	}

	@Test
	public void testReturnTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.returnTransition(transition).returnTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_RETURN), is(true));
	}

	@Test
	public void testReturnTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.returnTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_RETURN), is(false));
	}

	@Test
	public void testAllowEnterTransitionOverlap() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowEnterTransitionOverlap(false).allowEnterTransitionOverlap(), is(false));
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowEnterTransitionOverlap(true).allowEnterTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowEnterTransitionOverlapDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowEnterTransitionOverlap(), is(nullValue()));
	}

	@Test
	public void testAllowReturnTransitionOverlap() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowReturnTransitionOverlap(false).allowReturnTransitionOverlap(), is(false));
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowReturnTransitionOverlap(true).allowReturnTransitionOverlap(), is(true));
	}

	@Test
	public void testAllowReturnTransitionOverlapDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).allowReturnTransitionOverlap(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSharedElements() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(mContext);
		final View secondElement = new View(mContext);
		final View thirdElement = new View(mContext);
		request.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		request.sharedElements(new Pair<>(thirdElement, "third_element"));
		final List<Pair<View, String>> sharedElements = request.sharedElements();
		assertThat(sharedElements, is(notNullValue()));
		assertThat(sharedElements.size(), is(3));
		assertThat(sharedElements.get(0).first, is(firstElement));
		assertThat(sharedElements.get(0).second, is("first_element"));
		assertThat(sharedElements.get(1).first, is(secondElement));
		assertThat(sharedElements.get(1).second, is("second_element"));
		assertThat(sharedElements.get(2).first, is(thirdElement));
		assertThat(sharedElements.get(2).second, is("third_element"));
	}

	@Test
	public void testSharedElementsDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).sharedElements(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSharedElement() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(mContext);
		final View secondElement = new View(mContext);
		final View thirdElement = new View(mContext);
		request.sharedElement(firstElement, "first_element");
		request.sharedElement(secondElement, "second_element");
		request.sharedElement(thirdElement, "third_element");
		final List<Pair<View, String>> sharedElements = request.sharedElements();
		assertThat(sharedElements, is(notNullValue()));
		assertThat(sharedElements.size(), is(3));
		assertThat(sharedElements.get(0).first, is(firstElement));
		assertThat(sharedElements.get(0).second, is("first_element"));
		assertThat(sharedElements.get(1).first, is(secondElement));
		assertThat(sharedElements.get(1).second, is("second_element"));
		assertThat(sharedElements.get(2).first, is(thirdElement));
		assertThat(sharedElements.get(2).second, is("third_element"));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSingleSharedElement() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(mContext);
		request.sharedElement(firstElement, "first_element");
		assertThat(request.singleSharedElement(), is(notNullValue()));
		assertThat(request.singleSharedElement().first, is(firstElement));
		assertThat(request.singleSharedElement().second, is("first_element"));
		final View secondElement = new View(mContext);
		request.sharedElement(secondElement, "second_element");
		assertThat(request.singleSharedElement(), is(notNullValue()));
		assertThat(request.singleSharedElement().first, is(firstElement));
		assertThat(request.singleSharedElement().second, is("first_element"));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testSingleSharedElementOnEmptySharedElements() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(mContext);
		request.sharedElement(firstElement, "first_element");
		request.sharedElements().clear();
		assertThat(request.singleSharedElement(), is(nullValue()));
	}

	@Test
	public void testSingleSharedElementDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).singleSharedElement(), is(nullValue()));
	}

	@Test
	public void testSharedElementEnterTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.sharedElementEnterTransition(transition).sharedElementEnterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_ENTER), is(true));
	}

	@Test
	public void testSharedElementEnterTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.sharedElementEnterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_ENTER), is(false));
	}

	@Test
	public void testSharedElementReturnTransition() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Transition transition = inflateTestTransition();
		assertThat(request.sharedElementReturnTransition(transition).sharedElementReturnTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_RETURN), is(true));
	}

	@Test
	public void testSharedElementReturnTransitionDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.sharedElementReturnTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_RETURN), is(false));
	}

	@Test
	public void testReplaceSame() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.replaceSame(true).replaceSame(), is(true));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(true));
		assertThat(request.replaceSame(false).replaceSame(), is(false));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(false));
	}

	@Test
	public void testReplaceSameDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.replaceSame(), is(false));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(false));
	}

	@Test
	public void testAddToBackStack() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.addToBackStack(true).addToBackStack(), is(true));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(true));
		assertThat(request.addToBackStack(false).addToBackStack(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(false));
	}

	@Test
	public void testAddToBackStackDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.addToBackStack(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(false));
	}

	@Test
	public void testAllowStateLoss() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.allowStateLoss(true).allowStateLoss(), is(true));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(true));
		assertThat(request.allowStateLoss(false).allowStateLoss(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(false));
	}

	@Test
	public void testAllowStateLossDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.allowStateLoss(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(false));
	}

	@Test
	public void testImmediate() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.immediate(true).immediate(), is(true));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(true));
		assertThat(request.immediate(false).immediate(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(false));
	}

	@Test
	public void testImmediateDefault() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		assertThat(request.immediate(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(false));
	}

	@Test
	public void testExecuteReplace() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID)
				.transaction(FragmentRequest.REPLACE)
				.viewContainerId(android.R.id.list);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController, times(1)).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyZeroInteractions(mockFragment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteReplaceWithoutContainerId() {
		new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.REPLACE).execute();
	}

	@Test
	public void testExecuteAdd() {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID)
				.transaction(FragmentRequest.ADD)
				.viewContainerId(android.R.id.list);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController, times(1)).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyZeroInteractions(mockFragment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteAddWithoutContainerId() {
		new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.ADD).execute();
	}

	@Test
	public void testExecuteRemove() {
		testInnerExecuteForTransaction(FragmentRequest.REMOVE);
	}

	@Test
	public void testExecuteShow() {
		testInnerExecuteForTransaction(FragmentRequest.SHOW);
	}

	@Test
	public void testExecuteHide() {
		testInnerExecuteForTransaction(FragmentRequest.HIDE);
	}

	@Test
	public void testExecuteAttach() {
		testInnerExecuteForTransaction(FragmentRequest.ATTACH);
	}

	@Test
	public void testExecuteDetach() {
		testInnerExecuteForTransaction(FragmentRequest.DETACH);
	}

	private static void testInnerExecuteForTransaction(@FragmentRequest.Transaction int transaction) {
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID).transaction(transaction);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController, times(1)).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyZeroInteractions(mockFragment);
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteAlreadyExecuted() {
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.REMOVE);
		request.execute();
		request.execute();
	}

	@Test
	public void testExecutedDefault() {
		assertThat(new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).executed(), is(false));
	}
}
