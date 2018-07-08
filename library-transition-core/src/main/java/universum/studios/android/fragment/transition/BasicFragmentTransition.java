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
import android.support.annotation.AnimatorRes;
import android.support.annotation.NonNull;

import universum.studios.android.fragment.manage.FragmentTransition;

/**
 * Basic implementation of {@link FragmentTransition} that may be used to create instances of fragment
 * transactions with desired fragment animations.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public class BasicFragmentTransition implements FragmentTransition {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BasicFragmentTransition";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Creator used to create an instance or array of instances of BasicFragmentTransition from {@link Parcel}.
	 */
	public static final Creator<BasicFragmentTransition> CREATOR = new Creator<BasicFragmentTransition>() {

		/**
		 */
		@Override public BasicFragmentTransition createFromParcel(@NonNull final Parcel source) {
			return new BasicFragmentTransition(source);
		}

		/**
		 */
		@Override public BasicFragmentTransition[] newArray(final int size) {
			return new BasicFragmentTransition[size];
		}
	};

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Animation resource for a new incoming fragment.
	 */
	private final int inAnimRes;

	/**
	 * Animation resource for an old outgoing fragment.
	 */
	private final int outAnimRes;

	/**
	 * Animation resource for an old incoming fragment when it is being popped from the back stack.
	 */
	private final int inBackAnimRes;

	/**
	 * Animation resource for a current outgoing fragment when it is being popped from the back stack.
	 */
	private final int outBackAnimRes;

	/**
	 * Name of this transition.
	 */
	private final String name;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #BasicFragmentTransition(int, int, int, int)} with back-stack animations set
	 * to {@link #NO_ANIMATION}.
	 */
	public BasicFragmentTransition(@AnimatorRes final int inAnim, @AnimatorRes final int outAnim) {
		this(inAnim, outAnim, NO_ANIMATION, NO_ANIMATION);
	}

	/**
	 * Same as {@link #BasicFragmentTransition(int, int, int, int, String)} with name specified
	 * as {@code "UNSPECIFIED"}.
	 */
	public BasicFragmentTransition(
			@AnimatorRes final int inAnim,
			@AnimatorRes final int outAnim,
			@AnimatorRes final int inBackAnim,
			@AnimatorRes final int outBackAnim
	) {
		this(inAnim, outAnim, inBackAnim, outBackAnim, "UNSPECIFIED");
	}

	/**
	 * Creates a new instance of BasicFragmentTransition with the specified animations and name.
	 *
	 * @param inAnim      A resource id of the animation for an incoming fragment.
	 * @param outAnim     A resource id of the animation for an outgoing fragment to be added to the
	 *                    back stack or to be destroyed and replaced by the incoming one.
	 * @param inBackAnim  A resource id of the animation for an incoming fragment to be showed from
	 *                    the back stack.
	 * @param outBackAnim A resource id of the animation for an outgoing fragment to be destroyed and
	 *                    replaced by the incoming one.
	 * @param name        Name for the new transition.
	 */
	public BasicFragmentTransition(
			@AnimatorRes final int inAnim,
			@AnimatorRes final int outAnim,
			@AnimatorRes final int inBackAnim,
			@AnimatorRes final int outBackAnim,
			@NonNull final String name
	) {
		this.inAnimRes = inAnim;
		this.outAnimRes = outAnim;
		this.inBackAnimRes = inBackAnim;
		this.outBackAnimRes = outBackAnim;
		this.name = name;
	}

	/**
	 * Called form {@link #CREATOR} to create an instance of FragmentTransition form the given parcel
	 * <var>source</var>.
	 *
	 * @param source Parcel with data for the new instance.
	 */
	protected BasicFragmentTransition(@NonNull final Parcel source) {
		this.inAnimRes = source.readInt();
		this.outAnimRes = source.readInt();
		this.inBackAnimRes = source.readInt();
		this.outBackAnimRes = source.readInt();
		this.name = source.readString();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override public void writeToParcel(@NonNull final Parcel dest, final int flags) {
		dest.writeInt(inAnimRes);
		dest.writeInt(outAnimRes);
		dest.writeInt(inBackAnimRes);
		dest.writeInt(outBackAnimRes);
		dest.writeString(name);
	}

	/**
	 */
	@Override public int describeContents() {
		return 0;
	}

	/**
	 */
	@Override @AnimatorRes public int getIncomingAnimation() {
		return inAnimRes;
	}

	/**
	 */
	@Override @AnimatorRes public int getOutgoingAnimation() {
		return outAnimRes;
	}

	/**
	 */
	@Override @AnimatorRes public int getIncomingBackStackAnimation() {
		return inBackAnimRes;
	}

	/**
	 */
	@Override @AnimatorRes public int getOutgoingBackStackAnimation() {
		return outBackAnimRes;
	}

	/**
	 */
	@Override @NonNull public String getName() {
		return name;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}