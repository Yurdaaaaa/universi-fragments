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

import androidx.fragment.app.Fragment;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class BaseAnnotationHandlersTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInstantiation() {
		// Act:
		new BaseAnnotationHandlers();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<BaseAnnotationHandlers> constructor = BaseAnnotationHandlers.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testObtainFragmentHandler() {
		// Act:
		final FragmentAnnotationHandler handler = BaseAnnotationHandlers.obtainFragmentHandler(TestFragment.class);
		// Assert:
		assertThat(handler, is(notNullValue()));
		assertThat(handler, instanceOf(BaseAnnotationHandlers.FragmentHandler.class));
	}

	public static final class TestFragment extends Fragment {}
}