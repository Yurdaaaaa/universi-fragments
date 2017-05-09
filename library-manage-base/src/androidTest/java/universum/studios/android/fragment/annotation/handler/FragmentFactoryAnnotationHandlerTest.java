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
import android.util.SparseArray;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.manage.BaseFragmentFactory;
import universum.studios.android.fragment.manage.FragmentItem;
import universum.studios.android.test.BaseInstrumentedTest;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentFactoryAnnotationHandlerTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "FragmentFactoryAnnotationHandlerTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetFragmentItems() {
		final SparseArray<FragmentItem> items = BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactory.class).getFragmentItems();
		assertThat(items, is(notNullValue()));
		assertThat(items.size(), is(4));
		assertThat(items.get(TestFactory.FRAGMENT_1).id, is(TestFactory.FRAGMENT_1));
		assertThat(items.get(TestFactory.FRAGMENT_1).tag, is(BaseFragmentFactory.createFragmentTag(TestFactory.class, Integer.toString(TestFactory.FRAGMENT_1))));
		assertSame(items.get(TestFactory.FRAGMENT_1).type, Fragment.class);
		assertThat(items.get(TestFactory.FRAGMENT_2).id, is(TestFactory.FRAGMENT_2));
		assertThat(items.get(TestFactory.FRAGMENT_2).tag, is(BaseFragmentFactory.createFragmentTag(TestFactory.class, Integer.toString(TestFactory.FRAGMENT_2))));
		assertSame(items.get(TestFactory.FRAGMENT_2).type, Fragment.class);
		assertThat(items.get(TestFactory.FRAGMENT_3).id, is(TestFactory.FRAGMENT_3));
		assertThat(items.get(TestFactory.FRAGMENT_3).tag, is(BaseFragmentFactory.createFragmentTag(TestFactory.class, Integer.toString(TestFactory.FRAGMENT_3))));
		assertSame(items.get(TestFactory.FRAGMENT_3).type, TestFragment3.class);
		assertThat(items.get(TestFactory.FRAGMENT_4).id, is(TestFactory.FRAGMENT_4));
		assertThat(items.get(TestFactory.FRAGMENT_4).tag, is(BaseFragmentFactory.createFragmentTag(TestFactory.class, "Test4")));
		assertSame(items.get(TestFactory.FRAGMENT_4).type, TestFragment4.class);
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetFragmentItemsForFactoryWithoutFragments() {
		assertThat(BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithoutFragments.class).getFragmentItems(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetFragmentItemsForFactoryWithEmptyFactoryFragments() {
		assertThat(BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithEmptyFactoryFragments.class).getFragmentItems(), is(nullValue()));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testGetFragmentItemsForFactoryWithUnsupportedFragmentsFields() {
		assertThat(BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithUnsupportedFragmentFields.class).getFragmentItems(), is(nullValue()));
	}

	@FactoryFragments({
			TestFactory.FRAGMENT_1,
			TestFactory.FRAGMENT_2
	})
	private static final class TestFactory extends BaseFragmentFactory {

		static final int FRAGMENT_1 = 0x01;
		static final int FRAGMENT_2 = 0x02;
		@FactoryFragment(TestFragment3.class) static final int FRAGMENT_3 = 0x03;
		@FactoryFragment(value = TestFragment4.class, taggedName = "Test4") static final int FRAGMENT_4 = 0x04;
	}

	@SuppressWarnings("unused")
	private static final class TestFactoryWithUnsupportedFragmentFields extends BaseFragmentFactory {

		private static final String TAG = "TestFactoryWithUnsupportedFragmentFields";

		@FactoryFragment(TestFragment1.class) static final String FRAGMENT_1 = null;
		@FactoryFragment(TestFragment2.class) static final float FRAGMENT_2 = 0.05f;
	}

	@FactoryFragments({})
	private static final class TestFactoryWithEmptyFactoryFragments extends BaseFragmentFactory {
	}

	public static final class TestFragment1 extends Fragment {
	}

	public static final class TestFragment2 extends Fragment {
	}

	public static final class TestFragment3 extends Fragment {
	}

	public static final class TestFragment4 extends Fragment {
	}

	private static final class TestFactoryWithoutFragments extends BaseFragmentFactory {
	}
}
