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
package universum.studios.android.fragment.annotation.handler;

import android.support.annotation.Nullable;
import android.support.annotation.StringRes;

import universum.studios.android.fragment.WebFragment;
import universum.studios.android.fragment.annotation.WebContent;

/**
 * An {@link ActionBarFragmentAnnotationHandler} extended interface for annotation handlers from the
 * Fragments library that are used to handle processing of annotations attached to classes derived
 * from {@link WebFragment} class provided by this library.
 *
 * @author Martin Albedinsky
 * @see WebFragment
 */
public interface WebFragmentAnnotationHandler extends ActionBarFragmentAnnotationHandler {

	/**
	 * Returns the web content string resource id obtained from {@link WebContent @WebContent}
	 * annotation (if presented) from {@link WebContent#valueRes()}.
	 *
	 * @param defaultResId Default content resource id to be returned if there is no annotation
	 *                     presented or resource id is not specified.
	 * @return Via annotation specified web content resource id or <var>defaultResId</var>.
	 */
	@StringRes
	int getWebContentResId(@StringRes int defaultResId);

	/**
	 * Returns the web content string obtained from {@link WebContent @WebContent} annotation
	 * (if presented) from {@link WebContent#value()}.
	 *
	 * @param defaultContent Default content to be returned if there is no annotation presented or
	 *                       content is not specified.
	 * @return Via annotation specified web content or <var>defaultContent</var>.
	 */
	@Nullable
	String getWebContent(@Nullable String defaultContent);
}