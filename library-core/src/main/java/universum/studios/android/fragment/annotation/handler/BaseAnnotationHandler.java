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
package universum.studios.android.fragment.annotation.handler;

import android.support.annotation.NonNull;

import java.lang.annotation.Annotation;

import universum.studios.android.fragment.annotation.FragmentAnnotations;

/**
 * An {@link AnnotationHandler} base implementation.
 *
 * @author Martin Albedinsky
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
	final Class<?> mAnnotatedClass;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of BaseAnnotationHandler for the specified <var>annotatedClass</var>.
	 *
	 * @param annotatedClass The class of which annotations processing should the new handler handle.
	 */
	BaseAnnotationHandler(@NonNull final Class<?> annotatedClass) {
		this.mAnnotatedClass = annotatedClass;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 */
	@NonNull
	@Override
	public final Class<?> getAnnotatedClass() {
		return mAnnotatedClass;
	}

	/**
	 * Delegates to {@link FragmentAnnotations#obtainAnnotationFrom(Class, Class, Class)
	 * FragmentAnnotations.obtainAnnotationFrom(classOfAnnotation, mAnnotatedClass, null)}.
	 *
	 * @param classOfAnnotation Class of the annotation to find.
	 * @param <A>               Type of the annotation to find.
	 * @return Found annotation or {@code null} if there is no such annotation presented.
	 */
	final <A extends Annotation> A findAnnotation(final Class<A> classOfAnnotation) {
		return FragmentAnnotations.obtainAnnotationFrom(classOfAnnotation, mAnnotatedClass, null);
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
