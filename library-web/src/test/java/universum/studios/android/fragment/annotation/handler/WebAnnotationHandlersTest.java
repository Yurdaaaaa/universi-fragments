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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.fragment.WebFragment;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class WebAnnotationHandlersTest extends RobolectricTestCase {

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}
    
	@Test(expected = UnsupportedOperationException.class)
	public void testInstantiation() throws Exception {
		new WebAnnotationHandlers();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<WebAnnotationHandlers> constructor = WebAnnotationHandlers.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testObtainFragmentHandler() {
		final WebFragmentAnnotationHandler handler = WebAnnotationHandlers.obtainWebFragmentHandler(TestFragment.class);
		assertThat(handler, is(not(nullValue())));
		assertThat(handler, instanceOf(WebAnnotationHandlers.WebFragmentHandler.class));
	}

	public static final class TestFragment extends WebFragment {
	}
}