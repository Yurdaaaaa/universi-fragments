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

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.BaseInstrumentedTest;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseAnnotationHandlerTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BaseAnnotationHandlerTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testInstantiation() {
		final Handler handler = new Handler(Fragment.class);
		assertThat(handler.mAnnotatedClass, is(not(nullValue())));
		assertSame(handler.mAnnotatedClass, Fragment.class);
	}

    @Test
	public void testGetAnnotatedClass() {
	    assertSame(new Handler(Fragment.class).getAnnotatedClass(), Fragment.class);
	}

	@Test
	public void testFindAnnotation() {
		assertThat(
				new Handler(Component.class).findAnnotation(ComponentAnnotation.class),
				is(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, Component.class, null))
		);
		assertThat(
				new Handler(AnnotatedComponent.class).findAnnotation(ComponentAnnotation.class),
				is(FragmentAnnotations.obtainAnnotationFrom(ComponentAnnotation.class, AnnotatedComponent.class, null))
		);
	}

	private static final class Handler extends BaseAnnotationHandler {

		Handler(@NonNull Class<?> annotatedClass) {
			super(annotatedClass);
		}
	}

	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	private @interface ComponentAnnotation {
	}

	private static final class Component {
	}

	@ComponentAnnotation
	private static final class AnnotatedComponent {
	}
}
