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
package universum.studios.android.fragment.annotation; 
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentAnnotationsTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentAnnotationsTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		FragmentAnnotations.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<FragmentAnnotations> constructor = FragmentAnnotations.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
	public void testSetIsEnabled() {
	    FragmentAnnotations.setEnabled(false);
	    assertThat(FragmentAnnotations.isEnabled(), is(false));
	}

	@Test
	public void testIsEnabledDefault() {
		assertThat(FragmentAnnotations.isEnabled(), is(true));
	}

	@Test
	public void testCheckIfEnabledOrThrowWhenEnabled() {
		FragmentAnnotations.checkIfEnabledOrThrow();
	}

	@Test(expected = IllegalStateException.class)
	public void testCheckIfEnabledOrThrowWhenDisabled() {
		FragmentAnnotations.setEnabled(false);
		FragmentAnnotations.checkIfEnabledOrThrow();
	}

	@Test
	public void testObtainAnnotationFrom() {
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, AnnotatedComponent.class, null), is(not(nullValue())));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ChildComponentAnnotation.class, ChildAnnotatedComponent.class, null), is(not(nullValue())));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, ChildAnnotatedComponent.class, null), is(nullValue()));
	}

	@Test
	public void testObtainAnnotationFromWithMaxSuperClass() {
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, ChildAnnotatedComponent.class, BaseComponent.class), is(not(nullValue())));
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, BaseComponent.class), is(nullValue()));
	}

	@Test
	public void testObtainAnnotationFromWithMaxSuperClassForClassWithoutSuper() {
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Object.class, BaseComponent.class), is(nullValue()));
	}

	@Test
	public void testObtainAnnotationFromNotAnnotatedComponent() {
		assertThat(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, null), is(nullValue()));
	}

	@Test
	public void testIterateFields() throws Exception {
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		FragmentAnnotations.iterateFields(mockProcessor, AnnotatedComponent.class, null);
		final Field[] fields = AnnotatedComponent.class.getDeclaredFields();
		for (final Field field : fields) {
			verify(mockProcessor, times(1)).onProcessField(field, field.getName());
		}
	}

	@Test
	public void testIterateFieldsWithMaxSuperClass() {
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		FragmentAnnotations.iterateFields(mockProcessor, ChildAnnotatedComponent.class, BaseComponent.class);
		for (final Field field : AnnotatedComponent.class.getDeclaredFields()) {
			verify(mockProcessor, times(1)).onProcessField(field, field.getName());
		}
		for (final Field field : ChildAnnotatedComponent.class.getDeclaredFields()) {
			verify(mockProcessor, times(1)).onProcessField(field, field.getName());
		}
	}

	@Test
	public void testIterateFieldsWithMaxSuperClassForClassWithoutSuper() {
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		FragmentAnnotations.iterateFields(mockProcessor, Object.class, BaseComponent.class);
	}

	@Test
	public void testIterateFieldsOfComponentWithoutFields() {
		final FragmentAnnotations.FieldProcessor mockProcessor = mock(FragmentAnnotations.FieldProcessor.class);
		FragmentAnnotations.iterateFields(mockProcessor, Component.class, null);
		verifyZeroInteractions(mockProcessor);
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ComponentAnnotation {
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ChildComponentAnnotation {
	}

	private static abstract class BaseComponent {
	}

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

	private static final class Component extends BaseComponent {
	}
}
