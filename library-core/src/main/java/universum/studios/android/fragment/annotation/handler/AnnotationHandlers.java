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

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.fragment.annotation.FragmentAnnotations;

/**
 * Base factory and cache for {@link AnnotationHandler} instances for a specific classes from the
 * Fragments library.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public abstract class AnnotationHandlers {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "AnnotationHandlers";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Lock used for synchronized operations.
	 */
	private static final Object LOCK = new Object();

	/**
	 * Initial capacity for the handlers map.
	 */
	private static final int HANDLERS_INITIAL_CAPACITY = 20;

	/**
	 * Map with annotation handlers where each handler is mapped to a particular class for which
	 * has been that handler instantiated.
	 */
	private static Map<Class<?>, Object> handlers;

	/*
	 * Members =====================================================================================
	 */

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	AnnotationHandlers() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Obtains an annotation handler with the specified <var>classOfHandler</var> class for the
	 * given <var>annotatedClass</var>. If there is no such handler already instantiated and cached,
	 * its instance will be created and cached.
	 * <p>
	 * Each handler is mapped to its annotated class, so there may be only one handler of the same
	 * type created for the same annotated class.
	 *
	 * @param classOfHandler Class of the handler used to instantiate the requested handler instance
	 *                       if needed.
	 * @param annotatedClass Class for which to obtain the requested handler.
	 * @param <T>            Type of the handler to be obtained.
	 * @return Always valid instance of the requested handler or {@code null} if annotations processing
	 * is disabled for the Fragments library.
	 * @throws ClassCastException If there is already an annotation handler instantiated for the
	 *                            specified annotated class but it is of different type as requested.
	 *
	 * @see FragmentAnnotations#isEnabled()
	 */
	@SuppressWarnings({"unchecked", "ConstantConditions"})
	@Nullable public static <T extends AnnotationHandler> T obtainHandler(@NonNull final Class<T> classOfHandler, @NonNull final Class<?> annotatedClass) {
		Object handler = null;
		if (FragmentAnnotations.isEnabled()) {
			synchronized (LOCK) {
				if (handlers == null) {
					handlers = new HashMap<>(HANDLERS_INITIAL_CAPACITY);
				}
				handler = handlers.get(annotatedClass);
				if (handler == null) {
					handler = instantiateHandler(classOfHandler, annotatedClass);
					handlers.put(annotatedClass, handler);
				} else if (!handler.getClass().equals(classOfHandler)) {
					final String newHandlerName = classOfHandler.getSimpleName();
					final String currentHandlerName = handler.getClass().getSimpleName();
					final String className = annotatedClass.getSimpleName();
					throw new ClassCastException(
							"Trying to obtain handler(" + newHandlerName + ") for class(" + className + ") while there " +
									"is already handler(" + currentHandlerName + ") of different type for that class!"
					);
				}
			}
		}
		return (T) handler;
	}

	/**
	 * Instantiates a new annotation handler instance of the specified <var>classOfHandler</var> class
	 * for the given <var>annotatedClass</var>.
	 *
	 * @param classOfHandler Class of the handler to instantiate.
	 * @param annotatedClass Class for which to instantiate the requested handler.
	 * @param <T>            Type of the handler that will be instantiated.
	 * @return New instance of the requested handler with the annotated class attached.
	 * @throws IllegalStateException If the requested handler failed to be instantiated.
	 */
	private static <T> T instantiateHandler(final Class<T> classOfHandler, final Class<?> annotatedClass) {
		try {
			return classOfHandler.getConstructor(Class.class).newInstance(annotatedClass);
		} catch (Exception e) {
			// Happens when the handler implementation is not properly implemented:
			// - handler class is not visible,
			// - handler class does not have public constructor taking annotated class as single argument.
			final String handlerName = classOfHandler.getSimpleName();
			final String className = annotatedClass.getSimpleName();
			throw new IllegalStateException("Failed to instantiate annotation handler(" + handlerName + ") for(" + className + ").", e);
		}
	}

	/**
	 * Clears cache with already obtained/instantiated annotation handlers.
	 */
	static void clearHandlers() {
		synchronized (LOCK) {
			if (handlers != null) {
				handlers.clear();
				handlers = null;
			}
		}
	}

	/*
	 * Inner classes ===============================================================================
	 */
}