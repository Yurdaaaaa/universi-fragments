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
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

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
public final class FragmentItemTest extends BaseInstrumentedTest {
    
	@SuppressWarnings("unused")
	private static final String TAG = "FragmentItemTest";

    @Test
	public void testInstantiation() {
		final FragmentItem item = new FragmentItem(1, TestFragment.class);
	    assertThat(item.id, is(1));
	    assertSame(item.type, TestFragment.class);
	    assertThat(item.tag, is(nullValue()));
	}

	@Test
	public void testInstantiationWithTag() {
		final FragmentItem item = new FragmentItem(1, TestFragment.class, "TAG.TestFragment");
		assertThat(item.id, is(1));
		assertSame(item.type, TestFragment.class);
		assertThat(item.tag, is("TAG.TestFragment"));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testNewFragmentInstanceWithArguments() {
		final Bundle args = new Bundle();
		final Fragment fragment = new FragmentItem(1, TestFragment.class).newFragmentInstance(args);
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(args));
	}

	@Test
	@SuppressWarnings("ConstantConditions")
	public void testNewFragmentInstanceWithoutArguments() {
		final Fragment fragment = new FragmentItem(1, TestFragment.class).newFragmentInstance(null);
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(nullValue()));
	}

	@Test
	public void testNewFragmentInstanceForFragmentClass() {
		assertThat(new FragmentItem(1, Fragment.class).newFragmentInstance(new Bundle()), is(nullValue()));
	}

	@Test
	public void testNewFragmentInstanceForAbstractFragment() {
		assertThat(new FragmentItem(1, TestAbstractFragment.class).newFragmentInstance(new Bundle()), is(nullValue()));
	}

	@Test
	public void testNewFragmentInstanceForFragmentWithPrivateConstructor() {
		assertThat(new FragmentItem(1, TestFragmentWithPrivateConstructor.class).newFragmentInstance(new Bundle()), is(nullValue()));
	}

	public static class TestFragment extends Fragment {
	}

	public static abstract class TestAbstractFragment extends Fragment {
	}

	public static class TestFragmentWithPrivateConstructor extends Fragment {

		@SuppressLint("ValidFragment")
		private TestFragmentWithPrivateConstructor() {
		}
	}
}
