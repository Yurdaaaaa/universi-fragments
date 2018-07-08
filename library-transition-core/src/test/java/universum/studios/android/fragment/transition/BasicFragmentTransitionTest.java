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
package universum.studios.android.fragment.transition;

import android.os.Parcel;

import org.junit.Test;

import universum.studios.android.fragment.manage.FragmentTransition;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class BasicFragmentTransitionTest extends RobolectricTestCase {

	@Test
	public void testInstantiationInOut() {
		final int inAnimation = android.R.anim.fade_in;
		final int outAnimation = android.R.anim.fade_out;
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation);
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(FragmentTransition.NO_ANIMATION));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(FragmentTransition.NO_ANIMATION));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBack() {
		final int inAnimation = android.R.anim.fade_in;
		final int inAnimationBack = android.R.anim.fade_out;
		final int outAnimation = android.R.anim.slide_in_left;
		final int outAnimationBack = android.R.anim.slide_out_right;
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation, inAnimationBack, outAnimationBack);
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(inAnimationBack));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimationBack));
		assertThat(transition.getName(), is("UNSPECIFIED"));
	}

	@Test
	public void testInstantiationInOutBackAndName() {
		final int inAnimation = android.R.anim.fade_in;
		final int inAnimationBack = android.R.anim.fade_out;
		final int outAnimation = android.R.anim.slide_in_left;
		final int outAnimationBack = android.R.anim.slide_out_right;
		final BasicFragmentTransition transition = new BasicFragmentTransition(inAnimation, outAnimation, inAnimationBack, outAnimationBack, "TEST_TRANSITION");
		assertThat(transition.getIncomingAnimation(), is(inAnimation));
		assertThat(transition.getIncomingBackStackAnimation(), is(inAnimationBack));
		assertThat(transition.getOutgoingAnimation(), is(outAnimation));
		assertThat(transition.getOutgoingBackStackAnimation(), is(outAnimationBack));
		assertThat(transition.getName(), is("TEST_TRANSITION"));
	}

	@Test
	public void testCreatorCreateFromParcel() {
		final int inAnimation = android.R.anim.fade_in;
		final int inAnimationBack = android.R.anim.fade_out;
		final int outAnimation = android.R.anim.slide_in_left;
		final int outAnimationBack = android.R.anim.slide_out_right;
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
		final int inAnimation = android.R.anim.fade_in;
		final int inAnimationBack = android.R.anim.fade_out;
		final int outAnimation = android.R.anim.slide_in_left;
		final int outAnimationBack = android.R.anim.slide_out_right;
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
}