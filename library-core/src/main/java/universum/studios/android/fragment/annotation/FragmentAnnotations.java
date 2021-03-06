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
package universum.studios.android.fragment.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Annotation utils for the Fragments library.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class FragmentAnnotations {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "FragmentAnnotations";

	/*
	 * Interface ===================================================================================
	 */

	/**
	 * Simple callback which allows processing of all declared fields of a desired class via
	 * {@link #iterateFields(FragmentAnnotations.FieldProcessor, Class, Class)}.
	 *
	 * @author Martin Albedinsky
	 * @since 1.0
	 */
	public interface FieldProcessor {

		/**
		 * Invoked for each of iterated fields.
		 *
		 * @param field The currently iterated field.
		 * @param name  Name of the currently iterated field.
		 */
		void onProcessField(@NonNull Field field, @NonNull String name);
	}

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Flag indicating whether the processing of annotations for the Fragments library is enabled.
	 */
	private static final AtomicBoolean enabled = new AtomicBoolean(false);

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	private FragmentAnnotations() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Enables/disables processing of annotations for the Fragments library.
	 * <p>
	 * If annotations processing is enabled, it may decrease performance for the parts of an Android
	 * context depending on the classes from the Fragments library that uses annotations.
	 * <p>
	 * Default value: {@code false}
	 *
	 * @param enabled {@code True} to enable annotations processing, {@code false} to disabled it.
	 *
	 * @see #isEnabled()
	 */
	public static void setEnabled(final boolean enabled) {
		FragmentAnnotations.enabled.set(enabled);
	}

	/**
	 * Returns a boolean flag indicating whether the annotations processing for the Fragments library
	 * is enabled or not.
	 *
	 * @return {@code True} if annotations processing is enabled, {@code false} if it is disabled.
	 *
	 * @see #setEnabled(boolean)
	 */
	public static boolean isEnabled() {
		return enabled.get();
	}

	/**
	 * Performs check for enabled state of the annotations processing for the Fragments library.
	 * <p>
	 * This check is requested mostly from parts of the Fragments library that require to be annotations
	 * processing enabled.
	 *
	 * @throws IllegalStateException If annotations processing is disabled.
	 *
	 * @see #setEnabled(boolean)
	 */
	public static void checkIfEnabledOrThrow() {
		if (!enabled.get()) {
			throw new IllegalStateException(
					"Trying to access logic that requires annotations processing to be enabled, " +
							"but it appears that the annotations processing is disabled for the Fragments library."
			);
		}
	}

	/**
	 * Obtains the requested type of annotation from the given <var>fromClass</var> if it is presented.
	 *
	 * @param classOfAnnotation Class of the requested annotation.
	 * @param fromClass         Class from which should be the requested annotation obtained.
	 * @param maxSuperClass     If {@code not null}, this method will be called (recursively) for
	 *                          all super classes of the given annotated class (max to the specified
	 *                          <var>maxSuperClass</var> excluding) until the requested annotation
	 *                          is presented and obtained, otherwise annotation will be obtained only
	 *                          from the given annotated class.
	 * @param <A>               Type of the requested annotation.
	 * @return Obtained annotation or {@code null} if the requested annotation is not presented
	 * for the given class or its supers if requested.
	 */
	@Nullable public static <A extends Annotation> A obtainAnnotationFrom(
			@NonNull final Class<A> classOfAnnotation,
			@NonNull final Class<?> fromClass,
			@Nullable final Class<?> maxSuperClass
	) {
		final A annotation = fromClass.getAnnotation(classOfAnnotation);
		if (annotation != null) {
			return annotation;
		} else if (maxSuperClass != null) {
			final Class<?> parent = fromClass.getSuperclass();
			if (parent != null && !parent.equals(maxSuperClass)) {
				return obtainAnnotationFrom(classOfAnnotation, parent, maxSuperClass);
			}
		}
		return null;
	}

	/**
	 * Iterates all declared fields of the given <var>ofClass</var>.
	 *
	 * @param processor     Field processor callback to be invoked for each of iterated fields.
	 * @param ofClass       Class of which fields to iterate.
	 * @param maxSuperClass If {@code not null}, this method will be called (recursively) for all
	 *                      super classes of the given class (max to the specified <var>maxSuperClass</var>
	 *                      excluding), otherwise only fields of the given class will be iterated.
	 */
	public static void iterateFields(@NonNull final FieldProcessor processor, @NonNull final Class<?> ofClass, @Nullable final Class<?> maxSuperClass) {
		final Field[] fields = ofClass.getDeclaredFields();
		if (fields.length > 0) {
			for (final Field field : fields) {
				processor.onProcessField(field, field.getName());
			}
		}
		if (maxSuperClass != null) {
			final Class<?> parent = ofClass.getSuperclass();
			if (parent != null && !parent.equals(maxSuperClass)) {
				iterateFields(processor, parent, maxSuperClass);
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}