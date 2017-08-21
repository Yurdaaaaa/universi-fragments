/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
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
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class AnnotationHandlersTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "AnnotationHandlersTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
		// Ensure that we have a clean slate before each test.
		AnnotationHandlers.clearHandlers();
	}

	@Test(expected = InstantiationException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		AnnotationHandlers.class.getDeclaredConstructor().newInstance();
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInstantiationDirect() throws Exception {
		new AnnotationHandlers(){};
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testObtainHandler() {
		final Handler handler = AnnotationHandlers.obtainHandler(Handler.class, AnnotatedComponent.class);
		assertThat(handler, is(not(nullValue())));
		assertSame(handler.getAnnotatedClass(), AnnotatedComponent.class);
	}

	@Test(expected = IllegalStateException.class)
	public void testObtainHandlerThatIsAbstract() {
		AnnotationHandlers.obtainHandler(AbstractHandler.class, AnnotatedComponent.class);
	}

	@Test(expected = IllegalStateException.class)
	public void testObtainHandlerWithPrivateConstructor() {
		AnnotationHandlers.obtainHandler(PrivateHandler.class, AnnotatedComponent.class);
	}

	@Test
	public void testObtainHandlerAlreadyInstantiated() {
		assertThat(
				AnnotationHandlers.obtainHandler(Handler.class, AnnotatedComponent.class),
				is(AnnotationHandlers.obtainHandler(Handler.class, AnnotatedComponent.class))
		);
	}

	@Test(expected = ClassCastException.class)
	public void testObtainHandlerOfDifferentType() {
		AnnotationHandlers.obtainHandler(Handler.class, AnnotatedComponent.class);
		AnnotationHandlers.obtainHandler(SecondHandler.class, AnnotatedComponent.class);
	}

	@Test
	public void testObtainHandlerWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		assertThat(AnnotationHandlers.obtainHandler(Handler.class, AnnotatedComponent.class), is(nullValue()));
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testClearHandlersWhenAlreadyCleared() {
		AnnotationHandlers.clearHandlers();
		AnnotationHandlers.clearHandlers();
	}

	private static final class Handler implements AnnotationHandler {

		private final Class<?> annotatedClass;

		public Handler(Class<?> annotatedClass) {
			this.annotatedClass = annotatedClass;
		}

		@NonNull
		@Override
		public Class<?> getAnnotatedClass() {
			return annotatedClass;
		}
	}

	private static final class SecondHandler implements AnnotationHandler {

		private final Class<?> annotatedClass;

		public SecondHandler(Class<?> annotatedClass) {
			this.annotatedClass = annotatedClass;
		}

		@NonNull
		@Override
		public Class<?> getAnnotatedClass() {
			return annotatedClass;
		}
	}

	private static abstract class AbstractHandler implements AnnotationHandler {
	}

	private static abstract class PrivateHandler implements AnnotationHandler {

		private PrivateHandler() {
		}
	}

	private @interface ComponentAnnotation {
	}

	@ComponentAnnotation
	private static final class AnnotatedComponent {
	}
}
