/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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

import android.os.Build;
import android.util.Log;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.logging.Logger;

/**
 * <b>This class has been deprecated and will be removed in the next release.</b>
 * <p>
 * Configuration options for the Fragments library.
 *
 * @author Martin Albedinsky
 * @deprecated Use {@link FragmentsLogging} for logging control and {@link FragmentAnnotations#setEnabled(boolean)}
 * in order to enable/disable annotations processing instead.
 */
@Deprecated
public final class FragmentsConfig {

	/**
	 * Flag indicating whether the <b>verbose</b> output for the Fragments library trough log-cat is
	 * enabled or not.
	 *
	 * @see Log#v(String, String)
	 * @deprecated Use {@link FragmentsLogging#setLogger(Logger)} instead.
	 */
	@Deprecated
	public static boolean LOG_ENABLED = true;

	/**
	 * Flag indicating whether the <b>debug</b> output for the Fragments library trough log-cat is
	 * enabled or not.
	 *
	 * @see Log#d(String, String)
	 * @deprecated Use {@link FragmentsLogging#setLogger(Logger)} instead.
	 */
	@Deprecated
	public static boolean DEBUG_LOG_ENABLED = false;

	/**
	 * Flag indicating whether the processing of annotations for the Fragments library is enabled
	 * or not.
	 * <p>
	 * If annotations processing is enabled, it may decrease performance for the parts of an Android
	 * application depending on the classes from the Fragments library that uses annotations.
	 *
	 * @deprecated Use {@link FragmentAnnotations#setEnabled(boolean)} instead.
	 */
	@Deprecated
	public static boolean ANNOTATIONS_PROCESSING_ENABLED = true;

	/**
	 * Flag indicating whether a transitions API for fragments is supported by the current version
	 * of the Android or not.
	 *
	 * @deprecated Use {@link FragmentPolicies#TRANSITIONS_SUPPORTED} instead.
	 */
	@Deprecated
	public static final boolean TRANSITIONS_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;

	/**
	 */
	private FragmentsConfig() {
		// Not allowed to be instantiated publicly.
	}
}
