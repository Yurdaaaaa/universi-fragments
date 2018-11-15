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

import androidx.fragment.app.Fragment;

import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.NonNull;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.not;

/**
 * @author Martin Albedinsky
 */
public final class BaseAnnotationHandlerTest extends RobolectricTestCase {

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testInstantiation() {
		// Act:
		final TestHandler handler = new TestHandler(Fragment.class);
		// Assert:
		assertThat(handler.annotatedClass, is(not(nullValue())));
		assertSame(handler.annotatedClass, Fragment.class);
	}

    @Test public void testGetAnnotatedClass() {
	    // Arrange:
	    final TestHandler handler = new TestHandler(Fragment.class);
	    // Act + Assert:
		assertSame(handler.getAnnotatedClass(), Fragment.class);
	}

	@Test public void testFindAnnotation() {
		// Arrange + Act + Assert:
		assertThat(
				new TestHandler(Component.class).findAnnotation(ComponentAnnotation.class),
				is(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, null))
		);
		assertThat(
				new TestHandler(AnnotatedComponent.class).findAnnotation(ComponentAnnotation.class),
				is(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, AnnotatedComponent.class, null))
		);
	}

	private static final class TestHandler extends BaseAnnotationHandler {

		TestHandler(@NonNull final Class<?> annotatedClass) {
			super(annotatedClass);
		}
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ComponentAnnotation {}

	private static final class Component {}

	@ComponentAnnotation
	private static final class AnnotatedComponent {}
}