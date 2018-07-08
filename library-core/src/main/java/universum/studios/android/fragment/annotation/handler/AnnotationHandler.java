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

import android.support.annotation.NonNull;

/**
 * Base interface for annotation handlers from the Fragments library that are used to handle processing
 * of annotations attached to classes derived from base classes provided by this library or to theirs
 * fields.
 * <p>
 * Each handler instance has attached a single class for which annotations handling is responsible.
 * The attached class may be obtained via {@link #getAnnotatedClass()}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface AnnotationHandler {

	/**
	 * Returns the class for which has been this handler created.
	 *
	 * @return Annotated class attached to this handler.
	 */
	@NonNull Class<?> getAnnotatedClass();
}