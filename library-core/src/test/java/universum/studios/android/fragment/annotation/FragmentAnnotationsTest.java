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

import org.junit.Ignore;
import org.junit.Test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Martin Albedinsky
 */
public final class FragmentAnnotationsTest extends LocalTestCase {

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		FragmentAnnotations.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<FragmentAnnotations> constructor = FragmentAnnotations.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testContract() {
		// Act + Assert:
		assertThat(FragmentAnnotations.isEnabled(), is(true));
	}

	@Test public void testEnabled() {
		// Act + Assert:
		FragmentAnnotations.setEnabled(false);
		assertThat(FragmentAnnotations.isEnabled(), is(false));
	}

	@Test public void testCheckIfEnabledOrThrowWhenEnabled() {
		// Act:
		FragmentAnnotations.checkIfEnabledOrThrow();
	}

	@Test(expected = IllegalStateException.class)
	public void testCheckIfEnabledOrThrowWhenDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		// Act:
		FragmentAnnotations.checkIfEnabledOrThrow();
	}

	@Test public void testObtainAnnotationFrom() {
		// Act + Assert:
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, AnnotatedComponent.class, null), is(notNullValue()));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ChildComponentAnnotation.class, ChildAnnotatedComponent.class, null), is(notNullValue()));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, ChildAnnotatedComponent.class, null), is(nullValue()));
	}

	@Test public void testObtainAnnotationFromWithMaxSuperClass() {
		// Act + Assert:
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, ChildAnnotatedComponent.class, BaseComponent.class), is(notNullValue()));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, BaseComponent.class), is(nullValue()));
	}

	@Test public void testObtainAnnotationFromWithMaxSuperClassForClassWithoutSuper() {
		// Act + Assert:
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Object.class, BaseComponent.class), is(nullValue()));
	}

	@Test public void testObtainAnnotationFromNotAnnotatedComponent() {
		// Act + Assert:
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, null), is(nullValue()));
	}

	@Test public void testIterateFields() {
		// Arrange:
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		// Act:
		FragmentAnnotations.iterateFields(mockProcessor, AnnotatedComponent.class, null);
		// Assert:
		final Field[] fields = AnnotatedComponent.class.getDeclaredFields();
		for (final Field field : fields) {
			verify(mockProcessor).onProcessField(field, field.getName());
		}
	}

	@Test public void testIterateFieldsWithMaxSuperClass() {
		// Arrange:
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		// Act:
		FragmentAnnotations.iterateFields(mockProcessor, ChildAnnotatedComponent.class, BaseComponent.class);
		// Assert:
		for (final Field field : AnnotatedComponent.class.getDeclaredFields()) {
			verify(mockProcessor).onProcessField(field, field.getName());
		}
		for (final Field field : ChildAnnotatedComponent.class.getDeclaredFields()) {
			verify(mockProcessor).onProcessField(field, field.getName());
		}
	}

	@Test public void testIterateFieldsWithMaxSuperClassForClassWithoutSuper() {
		// Arrange:
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		// Act:
		FragmentAnnotations.iterateFields(mockProcessor, Object.class, BaseComponent.class);
	}

	@Ignore("Fails when running coverage report.")
	@Test public void testIterateFieldsOfComponentWithoutFields() {
		// Arrange:
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		// Act:
		FragmentAnnotations.iterateFields(mockProcessor, Component.class, null);
		// Assert:
		verifyZeroInteractions(mockProcessor);
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ComponentAnnotation {}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ChildComponentAnnotation {}

	private static abstract class BaseComponent {}

	@ComponentAnnotation
	@SuppressWarnings("unused")
	private static class AnnotatedComponent extends BaseComponent {

		public String firstField;
		public String secondField;
	}

	@ChildComponentAnnotation
	@SuppressWarnings("unused")
	private static class ChildAnnotatedComponent extends AnnotatedComponent {

		public String firstChildField;
		public String secondChildField;
	}

	private static final class Component extends BaseComponent {}
}