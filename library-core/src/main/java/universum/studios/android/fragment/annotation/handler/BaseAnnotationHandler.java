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

import java.lang.annotation.Annotation;

import androidx.annotation.NonNull;
import universum.studios.android.fragment.annotation.FragmentAnnotations;

/**
 * An {@link AnnotationHandler} base implementation.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
abstract class BaseAnnotationHandler implements AnnotationHandler {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "BaseAnnotationHandler";

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
	 * Class for which has been this handler created.
	 */
	final Class<?> annotatedClass;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of BaseAnnotationHandler for the specified <var>annotatedClass</var>.
	 *
	 * @param annotatedClass The class of which annotations processing should the new handler handle.
	 */
	BaseAnnotationHandler(@NonNull final Class<?> annotatedClass) {
		this.annotatedClass = annotatedClass;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@Override @NonNull public final Class<?> getAnnotatedClass() {
		return annotatedClass;
	}

	/**
	 * Delegates to {@link FragmentAnnotations#obtainAnnotationFrom(Class, Class, Class)
	 * FragmentAnnotations.obtainAnnotationFrom(classOfAnnotation, annotatedClass, null)}.
	 *
	 * @param classOfAnnotation Class of the annotation to find.
	 * @param <A>               Type of the annotation to find.
	 * @return Found annotation or {@code null} if there is no such annotation presented.
	 */
	final <A extends Annotation> A findAnnotation(final Class<A> classOfAnnotation) {
		return FragmentAnnotations.obtainAnnotationFrom(classOfAnnotation, annotatedClass, null);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}