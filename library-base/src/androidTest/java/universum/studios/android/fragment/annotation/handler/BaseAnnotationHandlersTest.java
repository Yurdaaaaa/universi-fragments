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
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseAnnotationHandlersTest extends InstrumentedTestCase {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BaseAnnotationHandlersTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test(expected = UnsupportedOperationException.class)
	public void testInstantiation() throws Exception {
		new BaseAnnotationHandlers();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<BaseAnnotationHandlers> constructor = BaseAnnotationHandlers.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

    @Test
	public void testObtainFragmentHandler() {
		final FragmentAnnotationHandler handler = BaseAnnotationHandlers.obtainFragmentHandler(TestFragment.class);
	    assertThat(handler, is(not(nullValue())));
	    assertThat(handler, instanceOf(BaseAnnotationHandlers.FragmentHandler.class));
	}

	public static final class TestFragment extends Fragment {
	}
}
