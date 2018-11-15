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
import android.util.SparseArray;

import org.junit.Test;

import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.manage.BaseFragmentFactory;
import universum.studios.android.fragment.manage.FragmentItem;
import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class FragmentFactoryAnnotationHandlerTest extends RobolectricTestCase {

	@Override public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetFragmentItems() {
		// Arrange:
		final FragmentFactoryAnnotationHandler handler = BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactory.class);
		// Act:
		final SparseArray<FragmentItem> items = handler.getFragmentItems();
		// Assert:
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

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetFragmentItemsForFactoryWithoutFragments() {
		// Arrange:
		final FragmentFactoryAnnotationHandler handler = BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithoutFragments.class);
		// Act + Assert:
		assertThat(handler.getFragmentItems(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetFragmentItemsForFactoryWithEmptyFactoryFragments() {
		// Arrange:
		final FragmentFactoryAnnotationHandler handler = BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithEmptyFactoryFragments.class);
		// Act + Assert:
		assertThat(handler.getFragmentItems(), is(nullValue()));
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testGetFragmentItemsForFactoryWithUnsupportedFragmentsFields() {
		// Arrange:
		final FragmentFactoryAnnotationHandler handler = BaseManagementAnnotationHandlers.obtainFactoryHandler(TestFactoryWithUnsupportedFragmentFields.class);
		// Act + Assert:
		assertThat(handler.getFragmentItems(), is(nullValue()));
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

		@FactoryFragment(TestFragment1.class) static final String FRAGMENT_1 = null;
		@FactoryFragment(TestFragment2.class) static final float FRAGMENT_2 = 0.05f;
	}

	@FactoryFragments({})
	private static final class TestFactoryWithEmptyFactoryFragments extends BaseFragmentFactory {}

	public static final class TestFragment1 extends Fragment {}

	public static final class TestFragment2 extends Fragment {}

	public static final class TestFragment3 extends Fragment {}

	public static final class TestFragment4 extends Fragment {}

	private static final class TestFactoryWithoutFragments extends BaseFragmentFactory {}
}