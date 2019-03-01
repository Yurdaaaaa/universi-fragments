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
package universum.studios.android.fragment.manage;

import org.junit.Test;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.FragmentFactoryAnnotationHandler;
import universum.studios.android.test.AndroidTestCase;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class BaseFragmentFactoryTest extends AndroidTestCase {

	@Override public void beforeTest() {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test public void testCreateFragmentTagUtility() {
		// Act + Assert:
		assertThat(
				BaseFragmentFactory.createFragmentTag(TestFactory.class, "TestFragment"),
				is(TestFactory.class.getName() + ".TAG.TestFragment")
		);
	}

	@Test public void testCreateFragmentTagUtilityWithEmptyName() {
		// Act + Assert:
		assertThat(BaseFragmentFactory.createFragmentTag(TestFactory.class, ""), is(nullValue()));
	}

	@Test public void testGetAnnotationHandler() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act:
		final FragmentFactoryAnnotationHandler annotationHandler = factory.getAnnotationHandler();
		// Assert:
		assertThat(annotationHandler, is(notNullValue()));
		assertThat(annotationHandler, is(factory.getAnnotationHandler()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		// Arrange:
		FragmentAnnotations.setEnabled(false);
		final TestFactory factory = new TestFactory();
		// Act:
		factory.getAnnotationHandler();
	}

	@Test public void testIsFragmentProvided() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act + Assert:
		assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
		}
	}

	@Test public void testIsFragmentProvidedLastChecked() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act Assert:
		assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
			assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
		}
	}

	@Test public void testProvidesFragment() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act + Assert:
		assertThat(factory.providesFragment(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(factory.providesFragment(providedFragmentId), is(true));
		}
	}

	@Test public void testProvidesFragmentOnFactoryWithoutFragments() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactoryWithoutFragments();
		// Act + Assert:
		assertThat(factory.providesFragment(0), is(false));
	}

	@Test public void testCreateFragment() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act + Assert:
		assertThat(factory.createFragment(TestFactory.FRAGMENT_NOT_PROVIDED), is(nullValue()));
		assertThat(factory.createFragment(TestFactory.FRAGMENT_1), instanceOf(TestFragment1.class));
		assertThat(factory.createFragment(TestFactory.FRAGMENT_3), instanceOf(TestFragment3.class));
		assertThat(factory.createFragment(TestFactory.FRAGMENT_4), instanceOf(TestFragment4.class));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCreateFragmentForNotInstantiableFragment() {
		// Arrange:
		final TestFactory factory = new TestFactory();
		// Act:
		factory.createFragment(TestFactory.FRAGMENT_2);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testOnCreateFragmentThatIsNotProvided() {
		// Arrange:
		final TestFactory factory = new TestFactory();
		// Act:
		factory.onCreateFragment(TestFactory.FRAGMENT_NOT_PROVIDED);
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testCreateFragmentTag() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act + Assert:
		assertThat(
				factory.createFragmentTag(TestFactory.FRAGMENT_NOT_PROVIDED),
				nullValue()
		);
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(
					factory.createFragmentTag(providedFragmentId),
					is(factory.getAnnotationHandler().getFragmentItems().get(providedFragmentId).tag)
			);
		}
	}

	@SuppressWarnings("ConstantConditions")
	@Test public void testOnCreateFragmentTag() {
		// Arrange:
		final BaseFragmentFactory factory = new TestFactory();
		// Act + Assert:
		assertThat(
				factory.onCreateFragmentTag(TestFactory.FRAGMENT_NOT_PROVIDED),
				is(BaseFragmentFactory.createFragmentTag(TestFactory.class, Integer.toString(TestFactory.FRAGMENT_NOT_PROVIDED)))
		);
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(
					factory.onCreateFragmentTag(providedFragmentId),
					is(factory.getAnnotationHandler().getFragmentItems().get(providedFragmentId).tag)
			);
		}
	}

	@FactoryFragments({
			TestFactory.FRAGMENT_1,
			TestFactory.FRAGMENT_2
	})
	private static final class TestFactory extends BaseFragmentFactory {

		static final int FRAGMENT_NOT_PROVIDED = 0x00;
		static final int FRAGMENT_1 = 0x01;
		static final int FRAGMENT_2 = 0x02;
		@FactoryFragment(TestFragment3.class) static final int FRAGMENT_3 = 0x03;
		@FactoryFragment(value = TestFragment4.class, taggedName = "Test4") static final int FRAGMENT_4 = 0x04;

		static final int[] providedFragmentIds = {FRAGMENT_1, FRAGMENT_2, FRAGMENT_3, FRAGMENT_4};

		@Override @NonNull protected Fragment onCreateFragment(final int fragmentId) {
			switch (fragmentId) {
				case FRAGMENT_1: return new TestFragment1();
				default: return super.onCreateFragment(fragmentId);
			}
		}
	}

	static final class TestFragment1 extends Fragment {}

	static final class TestFragment2 extends Fragment {}

	static final class TestFragment3 extends Fragment {}

	static final class TestFragment4 extends Fragment {}

	private static final class TestFactoryWithoutFragments extends BaseFragmentFactory {}
}