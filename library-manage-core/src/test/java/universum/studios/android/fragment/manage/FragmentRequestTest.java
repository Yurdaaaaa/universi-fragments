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
package universum.studios.android.fragment.manage;

import android.os.Build;
import android.os.Bundle;
import android.transition.Fade;
import android.transition.Transition;
import android.view.View;

import org.junit.Test;
import org.robolectric.annotation.Config;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * @author Martin Albedinsky
 */
public final class FragmentRequestTest extends AndroidTestCase {

    @Test public void testContract() {
		// Assert:
    	assertThat(FragmentRequest.NO_ID, is(-1));
		assertThat(FragmentRequest.NO_STYLE, is(-1));
    }

	@Test public void testInstantiation() {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		// Act:
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID);
		// Assert:
		assertThat(request.fragment(), is(nullValue()));
		assertThat(request.fragmentId(), is(FragmentRequest.NO_ID));
		assertThat(request.outgoingFragmentId(), is(FragmentRequest.NO_ID));
		assertThat(request.arguments(), is(nullValue()));
		assertThat(request.transaction(), is(FragmentRequest.REPLACE));
		assertThat(request.tag(), is(nullValue()));
		assertThat(request.viewContainerId(), is(FragmentController.NO_CONTAINER_ID));
		assertThat(request.transition(), is(nullValue()));
		assertThat(request.transitionStyle(), is(FragmentRequest.NO_STYLE));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_ENTER), is(false));
		assertThat(request.enterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_EXIT), is(false));
		assertThat(request.exitTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_REENTER), is(false));
		assertThat(request.reenterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_RETURN), is(false));
		assertThat(request.returnTransition(), is(nullValue()));
		assertThat(request.allowEnterTransitionOverlap(), is(nullValue()));
		assertThat(request.allowReturnTransitionOverlap(), is(nullValue()));
		assertThat(request.sharedElements(), is(nullValue()));
		assertThat(request.singleSharedElement(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_ENTER), is(false));
		assertThat(request.sharedElementEnterTransition(), is(nullValue()));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_RETURN), is(false));
		assertThat(request.sharedElementReturnTransition(), is(nullValue()));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(false));
		assertThat(request.replaceSame(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(false));
		assertThat(request.addToBackStack(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IGNORE_LIFECYCLE_STATE), is(false));
		assertThat(request.ignoreLifecycleState(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(false));
		assertThat(request.allowStateLoss(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(false));
		assertThat(request.immediate(), is(false));
	}

	@Test public void testInstantiationWithFragment() {
		// Arrange:
    	final FragmentController mockController = mock(FragmentController.class);
		final Fragment mockFragment = mock(Fragment.class);
		// Act:
		final FragmentRequest request = new FragmentRequest(mockController, mockFragment);
		// Assert:
		assertThat(request.fragment(), is(mockFragment));
	}

	@Test public void testToString() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act:
		final String toString = request.toString();
		// Assert:
		assertThat(toString.startsWith(FragmentRequest.class.getSimpleName()), is(true));
	}

	@Test public void testToStringWithTransition() {
		// Arrange:
    	final FragmentTransition mockTransition = mock(FragmentTransition.class);
		when(mockTransition.getName()).thenReturn("TestFragmentTransition");
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transition(mockTransition);
		// Act:
		final String toString = request.toString();
		// Assert:
		assertThat(toString.startsWith(FragmentRequest.class.getSimpleName()), is(true));
		assertThat(toString.contains(mockTransition.getName()), is(true));
	}

	@Test public void testFragmentId() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.fragmentId(100), is(request));
    	assertThat(request.fragmentId(), is(100));
	}

	@Test public void testOutgoingFragmentId() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
    	assertThat(request.outgoingFragmentId(2), is(request));
    	assertThat(request.outgoingFragmentId(), is(2));
	}

	@Test public void testArguments() {
		// Arrange:
		final Bundle args = new Bundle();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.arguments(args), is(request));
		assertThat(request.arguments(), is(args));
	}

	@Test public void testTransaction() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.transaction(FragmentRequest.REMOVE), is(request));
		assertThat(request.transaction(), is(FragmentRequest.REMOVE));
	}

	@Test public void testTag() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
    	assertThat(request.tag("TAG.TestFragment"), is(request));
    	assertThat(request.tag(), is("TAG.TestFragment"));
	}

	@Test public void testViewContainerId() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
    	assertThat(request.viewContainerId(android.R.id.list), is(request));
    	assertThat(request.viewContainerId(), is(android.R.id.list));
	}

	@Test public void testTransition() {
		// Arrange:
		final FragmentTransition transition = mock(FragmentTransition.class);
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.transition(transition), is(request));
		assertThat(request.transition(), is(transition));
	}

	@Test public void testTransitionStyle() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.transitionStyle(android.R.style.Animation), is(request));
		assertThat(request.transitionStyle(), is(android.R.style.Animation));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testEnterTransitionOnJellyBeanApiLevel() {
		// Arrange:
		final Object transition = createTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.enterTransition(transition), is(request));
		assertThat(request.enterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_ENTER), is(true));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testExitTransition() {
		// Arrange:
		final Object transition = createTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.exitTransition(transition), is(request));
		assertThat(request.exitTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_EXIT), is(true));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testReenterTransition() {
		// Arrange:
		final Object transition = createTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.reenterTransition(transition), is(request));
		assertThat(request.reenterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_REENTER), is(true));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testReturnTransition() {
		// Arrange:
		final Object transition = createTestTransition();
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.returnTransition(transition), is(request));
		assertThat(request.returnTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_RETURN), is(true));
	}

	@Test public void testAllowEnterTransitionOverlap() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
    	assertThat(request.allowEnterTransitionOverlap(false), is(request));
    	assertThat(request.allowEnterTransitionOverlap(), is(false));
		assertThat(request.allowEnterTransitionOverlap(true), is(request));
		assertThat(request.allowEnterTransitionOverlap(), is(true));
	}

	@Test public void testAllowReturnTransitionOverlap() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.allowReturnTransitionOverlap(false), is(request));
		assertThat(request.allowReturnTransitionOverlap(), is(false));
		assertThat(request.allowReturnTransitionOverlap(true), is(request));
		assertThat(request.allowReturnTransitionOverlap(), is(true));
    }

	@Test public void testSharedElements() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(context());
		final View secondElement = new View(context());
		final View thirdElement = new View(context());
		// Act:
		request.sharedElements(
				new Pair<>(firstElement, "first_element"),
				new Pair<>(secondElement, "second_element")
		);
		request.sharedElements(new Pair<>(thirdElement, "third_element"));
		// Assert:
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

	@Test public void testSharedElement() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(context());
		final View secondElement = new View(context());
		final View thirdElement = new View(context());
		// Act:
		request.sharedElement(firstElement, "first_element");
		request.sharedElement(secondElement, "second_element");
		request.sharedElement(thirdElement, "third_element");
		// Assert:
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

	@SuppressWarnings("ConstantConditions")
	@Test public void testSingleSharedElement() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(context());
		// Act + Assert:
		request.sharedElement(firstElement, "first_element");
		assertThat(request.singleSharedElement(), is(notNullValue()));
		assertThat(request.singleSharedElement().first, is(firstElement));
		assertThat(request.singleSharedElement().second, is("first_element"));
		final View secondElement = new View(context());
		request.sharedElement(secondElement, "second_element");
		assertThat(request.singleSharedElement(), is(notNullValue()));
		assertThat(request.singleSharedElement().first, is(firstElement));
		assertThat(request.singleSharedElement().second, is("first_element"));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testSingleSharedElementOnEmptySharedElements() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final View firstElement = new View(context());
		request.sharedElement(firstElement, "first_element");
		request.sharedElements().clear();
		// Act + Assert:
		assertThat(request.singleSharedElement(), is(nullValue()));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testSharedElementEnterTransition() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Object transition = createTestTransition();
		// Act + Assert:
		assertThat(request.sharedElementEnterTransition(transition), is(request));
		assertThat(request.sharedElementEnterTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_ENTER), is(true));
	}

	@Config(sdk = {Build.VERSION_CODES.JELLY_BEAN, Build.VERSION_CODES.LOLLIPOP})
	@Test public void testSharedElementReturnTransition() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		final Object transition = createTestTransition();
		// Act + Assert:
		assertThat(request.sharedElementReturnTransition(transition), is(request));
		assertThat(request.sharedElementReturnTransition(), is(transition));
		assertThat(request.hasTransition(FragmentRequest.TRANSITION_SHARED_ELEMENT_RETURN), is(true));
	}

	@Test public void testReplaceSame() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.replaceSame(true).replaceSame(), is(true));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(true));
		assertThat(request.replaceSame(false).replaceSame(), is(false));
		assertThat(request.hasFlag(FragmentRequest.REPLACE_SAME), is(false));
	}

	@Test public void testAddToBackStack() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.addToBackStack(true).addToBackStack(), is(true));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(true));
		assertThat(request.addToBackStack(false).addToBackStack(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ADD_TO_BACK_STACK), is(false));
	}

	@Test public void testIgnoreLifecycleState() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.ignoreLifecycleState(true).ignoreLifecycleState(), is(true));
		assertThat(request.hasFlag(FragmentRequest.IGNORE_LIFECYCLE_STATE), is(true));
		assertThat(request.ignoreLifecycleState(false).ignoreLifecycleState(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IGNORE_LIFECYCLE_STATE), is(false));
	}

	@Test public void testAllowStateLoss() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.allowStateLoss(true).allowStateLoss(), is(true));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(true));
		assertThat(request.allowStateLoss(false).allowStateLoss(), is(false));
		assertThat(request.hasFlag(FragmentRequest.ALLOW_STATE_LOSS), is(false));
	}

	@Test public void testImmediate() {
		// Arrange:
    	final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.immediate(true).immediate(), is(true));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(true));
		assertThat(request.immediate(false).immediate(), is(false));
		assertThat(request.hasFlag(FragmentRequest.IMMEDIATE), is(false));
	}

	@Test public void testExecuteReplace() {
		// Arrange:
    	final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID)
				.transaction(FragmentRequest.REPLACE)
				.viewContainerId(android.R.id.list);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		// Act + Assert:
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyNoInteractions(mockFragment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteReplaceWithoutContainerId() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.REPLACE);
		// Act:
    	request.execute();
	}

	@Test public void testExecuteAdd() {
		// Arrange:
    	final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID)
				.transaction(FragmentRequest.ADD)
				.viewContainerId(android.R.id.list);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		// Act + Assert:
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyNoInteractions(mockFragment);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExecuteAddWithoutContainerId() {
		// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.ADD);
		// Act:
		request.execute();
	}

	@Test public void testExecuteRemove() {
		testInnerExecuteForTransaction(FragmentRequest.REMOVE);
	}

	@Test public void testExecuteShow() {
		testInnerExecuteForTransaction(FragmentRequest.SHOW);
	}

	@Test public void testExecuteHide() {
		testInnerExecuteForTransaction(FragmentRequest.HIDE);
	}

	@Test public void testExecuteAttach() {
		testInnerExecuteForTransaction(FragmentRequest.ATTACH);
	}

	@Test public void testExecuteDetach() {
    	testInnerExecuteForTransaction(FragmentRequest.DETACH);
	}

	private static void testInnerExecuteForTransaction(@FragmentRequest.Transaction final int transaction) {
		// Arrange:
		final FragmentController mockController = mock(FragmentController.class);
		final FragmentRequest request = new FragmentRequest(mockController, FragmentRequest.NO_ID).transaction(transaction);
		final Fragment mockFragment = mock(Fragment.class);
		when(mockController.executeRequest(request)).thenReturn(mockFragment);
		// Act + Assert:
		assertThat(request.execute(), is(mockFragment));
		assertThat(request.executed(), is(true));
		verify(mockController).executeRequest(request);
		verifyNoMoreInteractions(mockController);
		verifyNoInteractions(mockFragment);
	}

	@Test(expected = IllegalStateException.class)
	public void testExecuteAlreadyExecuted() {
    	// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID).transaction(FragmentRequest.REMOVE);
		request.execute();
		// Act:
		request.execute();
	}

	@Test public void testExecutedDefault() {
    	// Arrange:
		final FragmentRequest request = new FragmentRequest(mock(FragmentController.class), FragmentRequest.NO_ID);
		// Act + Assert:
		assertThat(request.executed(), is(false));
	}

	@Nullable private static Transition createTestTransition() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? new Fade() : null;
	}
}