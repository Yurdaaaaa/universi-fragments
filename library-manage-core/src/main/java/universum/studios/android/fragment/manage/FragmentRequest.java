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

import android.os.Bundle;
import android.view.View;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

/**
 * A request that may be used to replace|add|remove|show|hide|attach|detach a desired {@link Fragment}
 * in a view hierarchy. New instance of request may be created via {@link FragmentController#newRequest(Fragment)}
 * or via {@link FragmentController#newRequest(int)} for fragments provided by {@link FragmentFactory}.
 * When request is created it may be configured via methods listed below and then executed via
 * {@link #execute()}.
 *
 * <h3>Configuration</h3>
 * <ul>
 * <li>{@link #fragmentId(int)}</li>
 * <li>{@link #outgoingFragmentId(int)}</li>
 * <li>{@link #arguments(Bundle)}</li>
 * <li>{@link #transaction(int)}</li>
 * <li>{@link #tag(String)}</li>
 * <li>{@link #viewContainerId(int)}</li>
 * <li>{@link #transition(FragmentTransition)}</li>
 * <li>{@link #transitionStyle(int)}</li>
 * <li>{@link #enterTransition(Object)}</li>
 * <li>{@link #exitTransition(Object)}</li>
 * <li>{@link #reenterTransition(Object)}</li>
 * <li>{@link #returnTransition(Object)}</li>
 * <li>{@link #allowEnterTransitionOverlap(boolean)}</li>
 * <li>{@link #allowReturnTransitionOverlap(boolean)}</li>
 * <li>{@link #sharedElementEnterTransition(Object)}</li>
 * <li>{@link #sharedElementReturnTransition(Object)}</li>
 * <li>{@link #sharedElement(View, String)}</li>
 * <li>{@link #sharedElements(Pair[])}</li>
 * <li>{@link #replaceSame(boolean)}</li>
 * <li>{@link #addToBackStack(boolean)}</li>
 * <li>{@link #allowStateLoss(boolean)}</li>
 * <li>{@link #immediate(boolean)}</li>
 * </ul>
 * <p>
 * <b>Note that each fragment request may be executed only once.</b>
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see FragmentController
 * @see FragmentRequestInterceptor
 */
public final class FragmentRequest {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "FragmentRequest";

	/**
	 * Constant used to determine that no fragment id has been specified.
	 */
	public static final int NO_ID = -1;

	/**
	 * Constant used to determine that no style resource has been specified.
	 */
	public static final int NO_STYLE = -1;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>replace</b> operation.
	 *
	 * @see FragmentTransaction#replace(int, Fragment, String)
	 */
	public static final int REPLACE = 0x00;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>add</b> operation.
	 *
	 * @see FragmentTransaction#add(int, Fragment, String)
	 */
	public static final int ADD = 0x01;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>remove</b> operation.
	 *
	 * @see FragmentTransaction#remove(Fragment)
	 */
	public static final int REMOVE = 0x02;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>show</b> operation.
	 *
	 * @see FragmentTransaction#show(Fragment)
	 */
	public static final int SHOW = 0x03;

	/**
	 * Fragment transaction type used to indicate that the associated fragment should be committed
	 * using <b>hide</b> operation.
	 *
	 * @see FragmentTransaction#hide(Fragment)
	 */
	public static final int HIDE = 0x04;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>attach</b> operation.
	 *
	 * @see FragmentTransaction#attach(Fragment)
	 */
	public static final int ATTACH = 0x05;

	/**
	 * Fragment transaction type used to indicate that the associated fragment transaction should be
	 * committed using <b>detach</b> operation.
	 *
	 * @see FragmentTransaction#detach(Fragment)
	 */
	public static final int DETACH = 0x06;

	/**
	 * Defines an annotation for determining available transaction types for {@link #transaction(int)}
	 * method.
	 *
	 * <h3>Available types:</h3>
	 * <ul>
	 * <li>{@link #REPLACE}</li>
	 * <li>{@link #ADD}</li>
	 * <li>{@link #REMOVE}</li>
	 * <li>{@link #SHOW}</li>
	 * <li>{@link #HIDE}</li>
	 * <li>{@link #ATTACH}</li>
	 * <li>{@link #DETACH}</li>
	 * </ul>
	 *
	 * @see #transaction(int)
	 */
	@IntDef({
			REPLACE,
			ADD, REMOVE,
			SHOW, HIDE,
			ATTACH, DETACH
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Transaction {}

	/**
	 * Flag indicating that a same fragment (currently displayed) can be replaced by the associated fragment.
	 *
	 * @see FragmentTransaction#replace(int, Fragment, String)
	 */
	static final int REPLACE_SAME = 0x00000001;

	/**
	 * Flag indicating that the associated fragment should be added into back stack.
	 *
	 * @see FragmentTransaction#addToBackStack(String)
	 */
	static final int ADD_TO_BACK_STACK = 0x00000001 << 1;

	/**
	 * Flag indicating that the associated {@link FragmentTransaction} should be committed allowing
	 * state loss.
	 *
	 * @see FragmentTransaction#commitAllowingStateLoss()
	 */
	static final int ALLOW_STATE_LOSS = 0x00000001 << 2;

	/**
	 * Flag indicating that the associated {@link FragmentTransaction} should be executed immediately.
	 *
	 * @see FragmentManager#executePendingTransactions()
	 */
	static final int IMMEDIATE = 0x00000001 << 3;

	/**
	 * Flag indicating that execution of fragment request should be performed regardless of the current
	 * {@link Lifecycle}'s state.
	 */
	static final int IGNORE_LIFECYCLE_STATE = 0x00000001 << 4;

	/**
	 * Defines an annotation for determining available boolean flags for FragmentRequest.
	 */
	@IntDef(flag = true, value = {
			REPLACE_SAME,
			ADD_TO_BACK_STACK,
			ALLOW_STATE_LOSS,
			IMMEDIATE,
			IGNORE_LIFECYCLE_STATE
	})
	@Retention(RetentionPolicy.SOURCE)
	private @interface Flag {}

	/**
	 * Flag indicating whether {@link #enterTransition} has been specified or not.
	 */
	static final int TRANSITION_ENTER = 0x00000001;

	/**
	 * Flag indicating whether {@link #exitTransition} has been specified or not.
	 */
	static final int TRANSITION_EXIT = 0x00000001 << 1;

	/**
	 * Flag indicating whether {@link #reenterTransition} has been specified or not.
	 */
	static final int TRANSITION_REENTER = 0x00000001 << 2;

	/**
	 * Flag indicating whether {@link #returnTransition} has been specified or not.
	 */
	static final int TRANSITION_RETURN = 0x00000001 << 3;

	/**
	 * Flag indicating whether {@link #sharedElementEnterTransition} has been specified or not.
	 */
	static final int TRANSITION_SHARED_ELEMENT_ENTER = 0x00000001 << 4;

	/**
	 * Flag indicating whether {@link #sharedElementReturnTransition} has been specified or not.
	 */
	static final int TRANSITION_SHARED_ELEMENT_RETURN = 0x00000001 << 5;

	/**
	 * Defines an annotation for determining available transition flags for FragmentRequest.
	 */
	@IntDef(flag = true, value = {
			TRANSITION_ENTER,
			TRANSITION_EXIT,
			TRANSITION_REENTER,
			TRANSITION_RETURN,
			TRANSITION_SHARED_ELEMENT_ENTER,
			TRANSITION_SHARED_ELEMENT_RETURN
	})
	@Retention(RetentionPolicy.SOURCE)
	private @interface TransitionFlag {}

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Controller that has been used to create this request and also is responsible for execution
	 * of this request.
	 *
	 * @see #execute()
	 */
	private final FragmentController controller;

	/**
	 * Fragment instance associated with this request.
	 */
	Fragment fragment;

	/**
	 * Id of the associated fragment.
	 */
	int fragmentId = NO_ID;

	/**
	 * Id of the outgoing fragment that will be replaced by the associated fragment.
	 */
	private int outgoingFragmentId = NO_ID;

	/**
	 * Arguments for the associated fragment.
	 *
	 * @see Fragment#setArguments(Bundle)
	 */
	Bundle arguments;

	/**
	 * Type determining what {@link FragmentTransaction} to perform for the associated fragment.
	 *
	 * @see Transaction @Transaction
	 */
	@Transaction int transaction = REPLACE;

	/**
	 * Tag for the associated fragment.
	 */
	String tag;

	/**
	 * Id of a view container where to place view hierarchy of the associated fragment.
	 */
	int viewContainerId = FragmentController.NO_CONTAINER_ID;

	/**
	 * Transition object specifying transition resources for the associated {@link FragmentTransaction}.
	 *
	 * @see FragmentTransaction#setCustomAnimations(int, int, int, int)
	 */
	FragmentTransition transition;

	/**
	 * Resource id of the style containing transitions used to animate fragment.
	 *
	 * @see FragmentTransaction#setTransitionStyle(int)
	 */
	int transitionStyle = -1;

	/**
	 * Enter transition the associated fragment.
	 *
	 * @see Fragment#setEnterTransition(Object)
	 */
	Object enterTransition;

	/**
	 * Exit transition the associated fragment.
	 *
	 * @see Fragment#setExitTransition(Object)
	 */
	Object exitTransition;

	/**
	 * Reenter transition the associated fragment.
	 *
	 * @see Fragment#setReenterTransition(Object)
	 */
	Object reenterTransition;

	/**
	 * Return transition the associated fragment.
	 *
	 * @see Fragment#setReturnTransition(Object)
	 */
	Object returnTransition;

	/**
	 * Shared element's enter transition the associated fragment.
	 *
	 * @see Fragment#setSharedElementEnterTransition(Object)
	 */
	Object sharedElementEnterTransition;

	/**
	 * Shared element's return transition the associated fragment.
	 *
	 * @see Fragment#setSharedElementReturnTransition(Object)
	 */
	Object sharedElementReturnTransition;

	/**
	 * Transition flags determining which transitions has been specified for this request.
	 *
	 * @see TransitionFlag @TransitionFlag
	 */
	@TransitionFlag private int specifiedTransitions;

	/**
	 * Flag indicating whether enter transition for the associated fragment can overlap or not.
	 *
	 * @see Fragment#setAllowReturnTransitionOverlap(boolean)
	 */
	Boolean allowEnterTransitionOverlap;

	/**
	 * Flag indicating whether return transition for the associated fragment can overlap or not.
	 *
	 * @see Fragment#setAllowEnterTransitionOverlap(boolean)
	 */
	Boolean allowReturnTransitionOverlap;

	/**
	 * Set of shared elements for the associated fragment.
	 *
	 * @see FragmentTransaction#addSharedElement(View, String)
	 */
	List<Pair<View, String>> sharedElements;

	/**
	 * Flags specified for this request.
	 *
	 * @see Flag @Flag
	 */
	@Flag private int flags;

	/**
	 * Boolean flag indicating whether this request has been already executed via {@link #execute()}
	 * or not.
	 */
	private boolean executed;

	/*
	 * Constructors ============================================================================
	 */

	/**
	 * Creates a new instance of FragmentRequest for the given <var>controller</var>.
	 *
	 * @param controller Fragment controller that creates the new request and will be also responsible
	 *                   for its execution.
	 * @param fragmentId Id of the factory fragment to be associated with this request.
	 */
	FragmentRequest(final FragmentController controller, int fragmentId) {
		this(controller, null);
		this.fragmentId = fragmentId;
	}

	/**
	 * Creates a new instance of FragmentRequest for the given <var>fragment</var>.
	 *
	 * @param controller Fragment controller that creates the new request and will be also responsible
	 *                   for its execution.
	 * @param fragment   The fragment to associate with the new request.
	 */
	FragmentRequest(final FragmentController controller, final Fragment fragment) {
		this.controller = controller;
		this.fragment = fragment;
	}

	/*
	 * Methods =================================================================================
	 */

	/**
	 */
	@SuppressWarnings("StringBufferReplaceableByString")
	@Override public String toString() {
		final StringBuilder builder = new StringBuilder(128);
		builder.append("FragmentRequest{fragmentId: ");
		builder.append(fragmentId);
		builder.append(", outgoingFragmentId: ");
		builder.append(outgoingFragmentId);
		builder.append(", arguments: ");
		builder.append(arguments);
		builder.append(", transactionType: ");
		builder.append(transition);
		builder.append(", tag: ");
		builder.append(tag);
		builder.append(", viewContainerId: ");
		builder.append(viewContainerId);
		builder.append(", transition: ");
		builder.append(transition == null ? "null" : transition.getName());
		builder.append(", transitionStyle: ");
		builder.append(transitionStyle);
		builder.append(", replaceSame: ");
		builder.append(hasFlag(REPLACE_SAME));
		builder.append(", addToBackStack: ");
		builder.append(hasFlag(ADD_TO_BACK_STACK));
		builder.append(", ignoreLifecycleState: ");
		builder.append(hasFlag(IGNORE_LIFECYCLE_STATE));
		builder.append(", allowStateLoss: ");
		builder.append(hasFlag(ALLOW_STATE_LOSS));
		builder.append(", immediate: ");
		builder.append(hasFlag(IMMEDIATE));
		builder.append(", executed: ");
		builder.append(executed);
		return builder.append("}").toString();
	}

	/**
	 * Returns the fragment instance associated with this request.
	 *
	 * @return This request's fragment.
	 *
	 * @see FragmentController#newRequest(Fragment)
	 */
	@NonNull public Fragment fragment() {
		return fragment;
	}

	/**
	 * Sets an id of the fragment associated with this request.
	 * <p>
	 * This id along with {@link #outgoingFragmentId()} may be used to determine exact change between
	 * two fragments and configure this request accordingly when using {@link FragmentRequestInterceptor}.
	 * <p>
	 * Default value: <b>{@link #NO_ID}</b>
	 *
	 * @param fragmentId The desired fragment id.
	 * @return This request to allow methods chaining.
	 *
	 * @see #fragmentId()
	 * @see #outgoingFragmentId(int)
	 */
	public FragmentRequest fragmentId(final int fragmentId) {
		this.fragmentId = fragmentId;
		return this;
	}

	/**
	 * Returns the id of the associated fragment.
	 *
	 * @return Fragment id or {@link #NO_ID} if no id has been specified.
	 *
	 * @see #fragmentId(int)
	 */
	public int fragmentId() {
		return fragmentId;
	}

	/**
	 * Sets an id of the outgoing fragment that is to be replaced by the fragment associated with
	 * this request.
	 * <p>
	 * This id along with {@link #fragmentId()} may be used to determine exact change between two
	 * fragments and configure this request accordingly when using {@link FragmentRequestInterceptor}.
	 * <p>
	 * Default value: <b>{@link #NO_ID}</b>
	 *
	 * @param fragmentId The desired fragment id.
	 * @return This request to allow methods chaining.
	 *
	 * @see #outgoingFragmentId()
	 * @see #fragmentId(int)
	 */
	public FragmentRequest outgoingFragmentId(final int fragmentId) {
		this.outgoingFragmentId = fragmentId;
		return this;
	}

	/**
	 * Returns the id of the outgoing fragment.
	 *
	 * @return Fragment id or {@link #NO_ID} if no id has been specified.
	 *
	 * @see #outgoingFragmentId(int)
	 */
	public int outgoingFragmentId() {
		return outgoingFragmentId;
	}

	/**
	 * Sets an arguments for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param arguments The desired arguments for fragment. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see Fragment#setArguments(Bundle)
	 * @see #arguments()
	 */
	public FragmentRequest arguments(@Nullable final Bundle arguments) {
		this.arguments = arguments;
		return this;
	}

	/**
	 * Returns the arguments that should be attached to the associated fragment.
	 *
	 * @return Arguments for fragment or {@code null} if no arguments have been specified yet.
	 *
	 * @see #arguments(Bundle)
	 */
	@Nullable public Bundle arguments() {
		return arguments;
	}

	/**
	 * Sets a transaction type determining what {@link FragmentTransaction} to perform for the
	 * associated fragment.
	 * <p>
	 * Default value: <b>{@link #REPLACE}</b>
	 *
	 * @param transaction The desired transaction type. One of types defined by
	 *                    {@link Transaction @Transaction} annotation.
	 * @return This request to allow methods chaining.
	 *
	 * @see #transaction()
	 */
	public FragmentRequest transaction(@Transaction final int transaction) {
		this.transaction = transaction;
		return this;
	}

	/**
	 * Returns the transaction type determining what {@link FragmentTransaction} to perform.
	 *
	 * @return One of transaction types defined by {@link Transaction @Transaction} annotation.
	 *
	 * @see #transaction(int)
	 */
	@Transaction public int transaction() {
		return transaction;
	}

	/**
	 * Sets a tag for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param fragmentTag The desired fragment tag. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #tag()
	 * @see Fragment#getTag()
	 */
	public FragmentRequest tag(@Nullable final String fragmentTag) {
		this.tag = fragmentTag;
		return this;
	}

	/**
	 * Returns the tag by which should be the associated fragment identified.
	 *
	 * @return Tag for the associated fragment to be shown using these options.
	 *
	 * @see #tag(String)
	 */
	@Nullable public String tag() {
		return tag;
	}

	/**
	 * Sets an id of a view container where to place view hierarchy of the associated fragment.
	 * <p>
	 * Default value: <b>{@link FragmentController#NO_CONTAINER_ID}</b>
	 *
	 * @param containerId The desired view container id.
	 * @return This request to allow methods chaining.
	 *
	 * @see #viewContainerId()
	 */
	public FragmentRequest viewContainerId(@IdRes final int containerId) {
		this.viewContainerId = containerId;
		return this;
	}

	/**
	 * Returns the id of view container where a view hierarchy of the associated fragment should be
	 * placed.
	 *
	 * @return View container id or {@link FragmentController#NO_CONTAINER_ID NO_CONTAINER_ID} if no
	 * id has been specified.
	 *
	 * @see #viewContainerId(int)
	 */
	@IdRes public int viewContainerId() {
		return viewContainerId;
	}

	/**
	 * Sets a transition that should be used to provide animation resources for the associated
	 * {@link FragmentTransaction}.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition Transition providing animation resources.
	 * @return This request to allow methods chaining.
	 *
	 * @see FragmentTransaction#setCustomAnimations(int, int, int, int)
	 * @see #transition()
	 */
	public FragmentRequest transition(@Nullable final FragmentTransition transition) {
		this.transition = transition;
		return this;
	}

	/**
	 * Returns the transition providing animation resources for {@link FragmentTransaction}.
	 *
	 * @return Transition with animation resources used to animate change of view between incoming
	 * and outgoing fragment.
	 */
	@Nullable public FragmentTransition transition() {
		return transition;
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next minor release.</b>
	 * <p>
	 * Sets a resource id of the style containing transitions used to animate change between incoming
	 * and outgoing fragment.
	 * <p>
	 * Default value: <b>{@link #NO_STYLE}</b>
	 *
	 * @param transitionStyle Resource id of the desired style.
	 * @return This request to allow methods chaining.
	 *
	 * @see FragmentTransaction#setTransitionStyle(int)
	 * @see #transitionStyle()
	 *
	 * @deprecated See {@link FragmentTransaction#setTransitionStyle(int)}.
	 */
	@Deprecated
	public FragmentRequest transitionStyle(@StyleRes final int transitionStyle) {
		this.transitionStyle = transitionStyle;
		return this;
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next minor release.</b>
	 * <p>
	 * Returns the transition style providing transitions for fragment view changes.
	 *
	 * @return Transition style resource or {@link #NO_STYLE} if no style has been specified.
	 *
	 * @deprecated See {@link FragmentTransaction#setTransitionStyle(int)}.
	 */
	@Deprecated
	@StyleRes public int transitionStyle() {
		return transitionStyle;
	}

	/**
	 * Sets an enter transition for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired enter transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #enterTransition()
	 * @see Fragment#setEnterTransition(Object)
	 */
	public FragmentRequest enterTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_ENTER;
		this.enterTransition = transition;
		return this;
	}

	/**
	 * Returns the enter transition to be played for the associated fragment.
	 *
	 * @return Transition or {@code null} if no enter transition has been specified yet.
	 *
	 * @see #enterTransition(Object)
	 */
	@Nullable public Object enterTransition() {
		return enterTransition;
	}

	/**
	 * Sets an exit transition for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired exit transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #exitTransition()
	 * @see Fragment#setExitTransition(Object)
	 */
	public FragmentRequest exitTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_EXIT;
		this.exitTransition = transition;
		return this;
	}

	/**
	 * Returns the exit transition to be played for the associated fragment.
	 *
	 * @return Transition or {@code null} if no exit transition has been specified yet.
	 *
	 * @see #exitTransition(Object)
	 */
	@Nullable public Object exitTransition() {
		return exitTransition;
	}

	/**
	 * Sets a reenter transition for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired reenter transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #reenterTransition()
	 * @see Fragment#setReenterTransition(Object)
	 */
	public FragmentRequest reenterTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_REENTER;
		this.reenterTransition = transition;
		return this;
	}

	/**
	 * Returns the reenter transition to be played for the associated fragment.
	 *
	 * @return Transition or {@code null} if no reenter transition has been specified yet.
	 *
	 * @see #reenterTransition(Object)
	 */
	@Nullable public Object reenterTransition() {
		return reenterTransition;
	}

	/**
	 * Sets a return transition for the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired return transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #exitTransition()
	 * @see Fragment#setReturnTransition(Object)
	 */
	public FragmentRequest returnTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_RETURN;
		this.returnTransition = transition;
		return this;
	}

	/**
	 * Returns the return transition to be played for the associated fragment.
	 *
	 * @return Transition or {@code null} if no return transition has been specified yet.
	 *
	 * @see #returnTransition(Object)
	 */
	@Nullable public Object returnTransition() {
		return returnTransition;
	}

	/**
	 * Sets a boolean flag indicating whether enter transition for the associated fragment may overlap
	 * or not.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param allowOverlap {@code True} to allow enter transition overlapping, {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @see Fragment#setAllowEnterTransitionOverlap(boolean)
	 * @see #allowEnterTransitionOverlap()
	 */
	public FragmentRequest allowEnterTransitionOverlap(final boolean allowOverlap) {
		this.allowEnterTransitionOverlap = allowOverlap;
		return this;
	}

	/**
	 * Returns boolean flag indicating whether overlapping for enter transition is allowed.
	 *
	 * @return {@code True} if overlapping for enter transition is allowed, {@code false} otherwise
	 * or {@code null} if this option has not been specified yet.
	 *
	 * @see #allowEnterTransitionOverlap(boolean)
	 */
	public Boolean allowEnterTransitionOverlap() {
		return allowEnterTransitionOverlap;
	}

	/**
	 * Sets a boolean flag indicating whether return transition for the associated fragment may overlap
	 * or not.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param allowOverlap {@code True} to allow return transition overlapping, {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @see Fragment#setAllowReturnTransitionOverlap(boolean)
	 * @see #allowReturnTransitionOverlap()
	 */
	public FragmentRequest allowReturnTransitionOverlap(final boolean allowOverlap) {
		this.allowReturnTransitionOverlap = allowOverlap;
		return this;
	}

	/**
	 * Returns boolean flag indicating whether overlapping for return transition is allowed.
	 *
	 * @return {@code True} if overlapping for return transition is allowed, {@code false} otherwise
	 * or {@code null} if this option has not been specified yet.
	 *
	 * @see #allowReturnTransitionOverlap(boolean)
	 */
	public Boolean allowReturnTransitionOverlap() {
		return allowReturnTransitionOverlap;
	}

	/**
	 * Bulk method for adding shared element pairs into this request.
	 *
	 * @param elements The desired shared elements pairs.
	 * @return This request to allow methods chaining.
	 *
	 * @see #sharedElement(View, String)
	 */
	@SafeVarargs public final FragmentRequest sharedElements(@NonNull final Pair<View, String>... elements) {
		if (sharedElements == null) {
			this.sharedElements = new ArrayList<>(elements.length);
		}
		this.sharedElements.addAll(Arrays.asList(elements));
		return this;
	}

	/**
	 * Adds a shared element view and its name for the associated fragment.
	 * <p>
	 * Subsequent calls to this method will append list of already specified shared element pairs.
	 *
	 * @param element     The view to be shared via transition.
	 * @param elementName The name of the shared element.
	 * @return This request to allow methods chaining.
	 *
	 * @see #sharedElements(Pair[])
	 * @see FragmentTransaction#addSharedElement(View, String)
	 */
	public FragmentRequest sharedElement(@NonNull final View element, @NonNull final String elementName) {
		if (sharedElements == null) {
			this.sharedElements = new ArrayList<>(1);
		}
		this.sharedElements.add(new Pair<>(element, elementName));
		return this;
	}

	/**
	 * Returns a list of all shared elements specified for this request.
	 *
	 * @return List of shared elements or {@code null} if there are no shared elements specified.
	 *
	 * @see #sharedElement(View, String)
	 * @see #sharedElements(Pair[])
	 */
	@Nullable public List<Pair<View, String>> sharedElements() {
		return sharedElements;
	}

	/**
	 * Returns the single shared element at the {@code 0} position among the current shared elements.
	 * <p>
	 * This method may be used to obtain a single shared element when there is specified only one for
	 * this request.
	 *
	 * @return Single shared element or {@code null} if there are no shared elements specified.
	 *
	 * @see #sharedElements()
	 * @see #sharedElement(View, String)
	 */
	@Nullable public Pair<View, String> singleSharedElement() {
		return sharedElements == null || sharedElements.isEmpty() ? null : sharedElements.get(0);
	}

	/**
	 * Sets an enter transition for shared elements of the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired shared elements's enter transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #sharedElementEnterTransition()
	 * @see Fragment#setSharedElementEnterTransition(Object)
	 */
	public FragmentRequest sharedElementEnterTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_SHARED_ELEMENT_ENTER;
		this.sharedElementEnterTransition = transition;
		return this;
	}

	/**
	 * Returns the enter transition to be played for shared elements of the associated fragment.
	 *
	 * @return Transition or {@code null} if no shared element enter transition has been specified yet.
	 *
	 * @see #sharedElementEnterTransition(Object)
	 */
	@Nullable public Object sharedElementEnterTransition() {
		return sharedElementEnterTransition;
	}

	/**
	 * Sets an return transition for shared elements of the associated fragment.
	 * <p>
	 * Default value: <b>{@code null}</b>
	 *
	 * @param transition The desired shared elements's return transition. May be {@code null}.
	 * @return This request to allow methods chaining.
	 *
	 * @see #sharedElementEnterTransition()
	 * @see Fragment#setSharedElementReturnTransition(Object)
	 */
	public FragmentRequest sharedElementReturnTransition(@Nullable final Object transition) {
		this.specifiedTransitions |= TRANSITION_SHARED_ELEMENT_RETURN;
		this.sharedElementReturnTransition = transition;
		return this;
	}

	/**
	 * Returns the return transition to be played for shared elements of the associated fragment.
	 *
	 * @return Transition or {@code null} if no shared element return transition has been specified yet.
	 *
	 * @see #sharedElementReturnTransition(Object)
	 */
	@Nullable public Object sharedElementReturnTransition() {
		return sharedElementReturnTransition;
	}

	/**
	 * Checks whether a transition with the specified <var>transitionFlag</var> has been specified
	 * for this request or not.
	 * <p>
	 * <b>Note</b> that also {@code null} transitions may be specified.
	 *
	 * @param transitionFlag One of transition flags defined by {@link TransitionFlag @TransitionFlag}
	 *                       annotation.
	 * @return {@code True} if transition has been specified, {@code false} otherwise.
	 */
	boolean hasTransition(@TransitionFlag final int transitionFlag) {
		return (specifiedTransitions & transitionFlag) != 0;
	}

	/**
	 * Sets a boolean flag indicating whether the already displayed fragment with the same TAG as that
	 * specified for this request may be replaced by the associated fragment or not.
	 * <p>
	 * Default value: <b>{@code false}</b>
	 *
	 * @param replace {@code True} to replace an existing fragment with the same TAG as specified
	 *                via {@link #tag(String)} with the associated one, {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @see #replaceSame()
	 */
	public FragmentRequest replaceSame(final boolean replace) {
		return setHasFlag(REPLACE_SAME, replace);
	}

	/**
	 * Returns boolean flag indicating whether already displayed fragment with the same TAG may be
	 * replaced by a new one.
	 *
	 * @return {@code True} if already displayed fragment with the same TAG may be replaced, {@code false}
	 * otherwise.
	 *
	 * @see #replaceSame(boolean)
	 */
	public boolean replaceSame() {
		return hasFlag(REPLACE_SAME);
	}

	/**
	 * Sets a boolean flag indicating whether the associated fragment should be added into fragments
	 * back stack under its tag or not.
	 * <p>
	 * Default value: <b>{@code false}</b>
	 *
	 * @param add {@code True} to add fragment into back stack, {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @see FragmentTransaction#addToBackStack(String)
	 * @see #addToBackStack()
	 */
	public FragmentRequest addToBackStack(final boolean add) {
		return setHasFlag(ADD_TO_BACK_STACK, add);
	}

	/**
	 * Returns boolean indicating whether to add fragment into back stack.
	 *
	 * @return {@code True} if to add the associated fragment into back stack, {@code false} otherwise.
	 *
	 * @see #addToBackStack(boolean)
	 */
	public boolean addToBackStack() {
		return hasFlag(ADD_TO_BACK_STACK);
	}

	/**
	 * Sets a boolean flag indicating whether {@link FragmentController} should ignore {@link Lifecycle}'s
	 * current state when executing this request or not.
	 * <p>
	 * Default value: <b>{@code false}</b>
	 *
	 * @param ignore {@code True} to ignore lifecycle's current state when executing this request,
	 *               {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @since 1.4.1
	 *
	 * @see #ignoreLifecycleState()
	 * @see #allowStateLoss(boolean)
	 */
	public FragmentRequest ignoreLifecycleState(final boolean ignore) {
		return setHasFlag(IGNORE_LIFECYCLE_STATE, ignore);
	}

	/**
	 * Returns boolean flag indicating whether current {@link Lifecycle}'s state should be ignored
	 * when executing this request.
	 *
	 * @return {@code True} if lifecycle's current state should be ignored, {@code false} otherwise.
	 *
	 * @since 1.4.1
	 *
	 * @see #ignoreLifecycleState(boolean)
	 */
	public boolean ignoreLifecycleState() {
		return hasFlag(IGNORE_LIFECYCLE_STATE);
	}

	/**
	 * Sets a boolean flag indicating whether {@link FragmentTransaction} for the associated fragment
	 * may be committed allowing state loss or not.
	 * <p>
	 * Default value: <b>{@code false}</b>
	 *
	 * @param allow {@code True} to allow state loss when committing transaction, {@code false} otherwise.
	 * @return This request to allow methods chaining.
	 *
	 * @see FragmentTransaction#commitAllowingStateLoss()
	 * @see #allowStateLoss()
	 * @see #ignoreLifecycleState(boolean)
	 */
	public FragmentRequest allowStateLoss(final boolean allow) {
		return setHasFlag(ALLOW_STATE_LOSS, allow);
	}

	/**
	 * Returns boolean flag indicating whether to commit fragment transaction allowing state loss.
	 *
	 * @return {@code True} if transaction may be committed allowing state loss, {@code false} otherwise.
	 *
	 * @see #allowStateLoss(boolean)
	 */
	public boolean allowStateLoss() {
		return hasFlag(ALLOW_STATE_LOSS);
	}

	/**
	 * Sets a boolean flag indicating whether {@link FragmentTransaction} for the associated fragment
	 * should be executed immediately or not.
	 * <p>
	 * Default value: <b>{@code false}</b>
	 *
	 * @param immediate {@code True} to execute immediately (synchronously), {@code false} otherwise
	 *                  (asynchronously).
	 * @return This request to allow methods chaining.
	 *
	 * @see FragmentManager#executePendingTransactions()
	 * @see #immediate()
	 */
	public FragmentRequest immediate(final boolean immediate) {
		return setHasFlag(IMMEDIATE, immediate);
	}

	/**
	 * Returns boolean indicating whether to execute fragment transaction immediately.
	 *
	 * @return {@code True} if fragment transaction should be executed immediately (synchronously),
	 * {@code false} otherwise (asynchronously).
	 *
	 * @see #immediate(boolean)
	 */
	public boolean immediate() {
		return hasFlag(IMMEDIATE);
	}

	/**
	 * Sets whether this request has the specified <var>flag</var> registered or not.
	 *
	 * @param flag One of flags defined by {@link Flag @Flag} annotation.
	 * @param has  {@code True} to determine that this request has this flag, {@code false} that it
	 *             has not.
	 * @return This request to allow methods chaining.
	 */
	private FragmentRequest setHasFlag(@Flag final int flag, final boolean has) {
		if (has) this.flags |= flag;
		else this.flags &= ~flag;
		return this;
	}

	/**
	 * Checks whether this request has the specified <var>flag</var> registered or not.
	 *
	 * @param flag One of flags defined by {@link Flag @Flag} annotation.
	 * @return {@code True} if flag is registered, {@code false} otherwise.
	 */
	boolean hasFlag(@Flag final int flag) {
		return (flags & flag) != 0;
	}

	/**
	 * Executes this request via the associated {@link FragmentController} that was used to create
	 * this request instance.
	 * <p>
	 * <b>Note</b> that each request may be executed only once and any subsequent calls to this
	 * method will throw an exception.
	 *
	 * @return The fragment that has been associated with this request either during its initialization
	 * or as result of execution process. May be {@code null} if the execution has failed.
	 * @throws IllegalStateException    If this request has been already executed.
	 * @throws IllegalArgumentException If current configuration of this request does not meet the
	 *                                  requirements. For example, request with transaction type of
	 *                                  {@link #REPLACE} or {@link #ADD} cannot be executed without
	 *                                  view container id specified.
	 */
	@Nullable public Fragment execute() {
		this.assertNotExecuted();
		switch (transaction) {
			case REPLACE:
			case ADD:
				if (viewContainerId == FragmentController.NO_CONTAINER_ID) {
					throw new IllegalArgumentException("Cannot execute request for REPLACE|ADD transaction. No view container id specified!");
				}
			case REMOVE:
			case SHOW:
			case HIDE:
			case ATTACH:
			case DETACH:
			default:
				final Fragment fragment = controller.executeRequest(this);
				this.executed = true;
				return fragment;
		}
	}

	/**
	 * Asserts that this request has not been executed yet. If it has been executed, an exception is
	 * thrown.
	 */
	private void assertNotExecuted() {
		if (executed) throw new IllegalStateException("Already executed!");
	}

	/**
	 * Returns boolean flag indicating whether this request has been executed.
	 *
	 * @return {@code True} if {@link #execute()} has been called for this request, {@code false}
	 * otherwise.
	 */
	public boolean executed() {
		return executed;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}