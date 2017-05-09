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
package universum.studios.android.fragment.manage;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.annotation.handler.FragmentFactoryAnnotationHandler;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class BaseFragmentFactoryTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "BaseFragmentFactoryTest";

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		// Ensure that we have always annotations processing enabled.
		FragmentAnnotations.setEnabled(true);
	}

	@Test
	public void testCreateFragmentTagUtility() {
	    assertThat(
	    		BaseFragmentFactory.createFragmentTag(TestFactory.class, "TestFragment"),
			    is(TestFactory.class.getName() + ".TAG.TestFragment")
	    );
    }

    @Test
	public void testCreateFragmentTagUtilityWithEmptyName() {
		assertThat(BaseFragmentFactory.createFragmentTag(TestFactory.class, ""), is(nullValue()));
	}

	@Test
	public void testGetAnnotationHandler() {
		final BaseFragmentFactory factory = new TestFactory();
		final FragmentFactoryAnnotationHandler annotationHandler = factory.getAnnotationHandler();
		assertThat(annotationHandler, is(not(nullValue())));
		assertThat(annotationHandler, is(factory.getAnnotationHandler()));
	}

	@Test(expected = IllegalStateException.class)
	public void testGetAnnotationHandlerWhenAnnotationsAreDisabled() {
		FragmentAnnotations.setEnabled(false);
		new TestFactory().getAnnotationHandler();
	}

    @Test
	public void testIsFragmentProvided() {
	    final BaseFragmentFactory factory = new TestFactory();
	    assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
	    for (final int providedFragmentId : TestFactory.providedFragmentIds) {
		    assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
	    }
    }

	@Test
	public void testIsFragmentProvidedLastChecked() {
		final BaseFragmentFactory factory = new TestFactory();
		assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		assertThat(factory.isFragmentProvided(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
		for (final int providedFragmentId : TestFactory.providedFragmentIds) {
			assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
			assertThat(factory.isFragmentProvided(providedFragmentId), is(true));
		}
	}

    @Test
	public void testProvidesFragment() {
	    final BaseFragmentFactory factory = new TestFactory();
	    assertThat(factory.providesFragment(TestFactory.FRAGMENT_NOT_PROVIDED), is(false));
	    for (final int providedFragmentId : TestFactory.providedFragmentIds) {
		    assertThat(factory.providesFragment(providedFragmentId), is(true));
	    }
    }

    @Test
    public void testProvidesFragmentOnFactoryWithoutFragments() {
	    assertThat(new TestFactoryWithoutFragments().providesFragment(0), is(false));
    }

    @Test
	public void testCreateFragment() {
	    final BaseFragmentFactory factory = new TestFactory();
	    assertThat(factory.createFragment(TestFactory.FRAGMENT_NOT_PROVIDED), is(nullValue()));
	    assertThat(factory.createFragment(TestFactory.FRAGMENT_1), instanceOf(TestFragment1.class));
	    // assertThat(factory.createFragment(TestFactory.FRAGMENT_2), instanceOf(TestFragment2.class));
	    assertThat(factory.createFragment(TestFactory.FRAGMENT_3), instanceOf(TestFragment3.class));
	    assertThat(factory.createFragment(TestFactory.FRAGMENT_4), instanceOf(TestFragment4.class));
    }

	@Test(expected = IllegalArgumentException.class)
	public void testCreateFragmentForNotInstantiableFragment() {
		new TestFactory().createFragment(TestFactory.FRAGMENT_2);
	}

    @Test(expected = IllegalArgumentException.class)
	public void testOnCreateFragmentThatIsNotProvided() {
	    new TestFactory().onCreateFragment(TestFactory.FRAGMENT_NOT_PROVIDED);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
	public void testCreateFragmentTag() {
	    final BaseFragmentFactory factory = new TestFactory();
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

    @Test
    @SuppressWarnings("ConstantConditions")
	public void testOnCreateFragmentTag() {
	    final BaseFragmentFactory factory = new TestFactory();
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

		@NonNull
		@Override
		protected Fragment onCreateFragment(int fragmentId) {
			switch (fragmentId) {
				case FRAGMENT_1: return new TestFragment1();
				// case FRAGMENT_2: return new TestFragment2();
				default: return super.onCreateFragment(fragmentId);
			}
		}
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
