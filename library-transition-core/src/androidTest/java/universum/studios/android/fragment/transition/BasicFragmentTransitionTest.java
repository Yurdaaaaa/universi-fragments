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
package universum.studios.android.fragment.transition; 
import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.test.instrumented.InstrumentedTestCase;
import universum.studios.android.test.instrumented.TestResources;
import universum.studios.android.test.instrumented.TestUtils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;
import static org.junit.Assume.assumeTrue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BasicFragmentTransitionTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BasicFragmentTransitionTest";

    @Test
	public void testInstantiationInOut() {
	    assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
	    final int inAnimation = obtainTransitionResource("transition_in");
	    final int outAnimation = obtainTransitionResource("transition_in");
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation);
	    assertThat(transition.getIncomingAnimation(), is(inAnimation));
	    assertThat(transition.getIncomingBackStackAnimation(), is(inAnimation));
	    assertThat(transition.getOutgoingAnimation(), is(outAnimation));
	    assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimation));
	    assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBack() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int inAnimation = obtainTransitionResource("transition_in");
		final int inAnimationBack = obtainTransitionResource("transition_in_back");
		final int outAnimation = obtainTransitionResource("transition_in");
		final int outAnimationBack = obtainTransitionResource("transition_in_back");
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation, inAnimationBack, outAnimationBack);
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(inAnimationBack));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimationBack));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBackAndName() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int inAnimation = obtainTransitionResource("transition_in");
		final int inAnimationBack = obtainTransitionResource("transition_in_back");
		final int outAnimation = obtainTransitionResource("transition_in");
		final int outAnimationBack = obtainTransitionResource("transition_in_back");
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation, inAnimationBack, outAnimationBack, "TEST_TRANSITION");
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(inAnimationBack));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimationBack));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
	}

	@Test
	public void testCreatorCreateFromParcel() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int inAnimation = obtainTransitionResource("transition_in");
		final int inAnimationBack = obtainTransitionResource("transition_in_back");
		final int outAnimation = obtainTransitionResource("transition_in");
		final int outAnimationBack = obtainTransitionResource("transition_in_back");
		final Parcel parcel = Parcel.obtain();
		parcel.writeInt(inAnimation);
		parcel.writeInt(outAnimation);
		parcel.writeInt(inAnimationBack);
		parcel.writeInt(outAnimationBack);
		parcel.writeString("TEST_TRANSITION");
		parcel.setDataPosition(0);
		final BasicFragmentTransition transition = BasicFragmentTransition.CREATOR.createFromParcel(parcel);
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(inAnimationBack));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimationBack));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test
	public void testCreatorNewArray() {
		final BasicFragmentTransition[] array = BasicFragmentTransition.CREATOR.newArray(10);
		for (final BasicFragmentTransition anArray : array) {
			assertThat(anArray, is(nullValue()));
		}
	}

	@Test
	public void testWriteToParcel() {
		assumeTrue(TestUtils.hasLibraryRootPackageName(mContext));
		final int inAnimation = obtainTransitionResource("transition_in");
		final int inAnimationBack = obtainTransitionResource("transition_in_back");
		final int outAnimation = obtainTransitionResource("transition_in");
		final int outAnimationBack = obtainTransitionResource("transition_in_back");
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation, inAnimationBack, outAnimationBack, "TEST_TRANSITION");
		final Parcel parcel = Parcel.obtain();
		transition.writeToParcel(parcel, 0);
		parcel.setDataPosition(0);
		assertThat(parcel.readInt(), is(inAnimation));
		assertThat(parcel.readInt(), is(outAnimation));
		assertThat(parcel.readInt(), is(inAnimationBack));
		assertThat(parcel.readInt(), is(outAnimationBack));
		assertThat(parcel.readString(), is("TEST_TRANSITION"));
		parcel.recycle();
	}

	@Test
	public void testDescribeContents() {
		assertThat(new BasicFragmentTransition(0, 0).describeContents(), is(0));
	}

	private int obtainTransitionResource(String transitionName) {
		return TestResources.resourceIdentifier(mContext, TestResources.TRANSITION, transitionName);
	}
}
