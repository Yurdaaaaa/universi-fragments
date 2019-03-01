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

import android.annotation.SuppressLint;
import android.os.Bundle;

import org.junit.Test;

import androidx.fragment.app.Fragment;
import universum.studios.android.test.AndroidTestCase;

import static junit.framework.Assert.assertSame;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class FragmentItemTest extends AndroidTestCase {

	@Test public void testInstantiation() {
		// Act:
		final FragmentItem item = new FragmentItem(1, TestFragment.class);
		// Assert:
		assertThat(item.id, is(1));
		assertSame(item.type, TestFragment.class);
		assertThat(item.tag, is(nullValue()));
	}

	@Test public void testInstantiationWithTag() {
		// Act:
		final FragmentItem item = new FragmentItem(1, TestFragment.class, "TAG.TestFragment");
		// Assert:
		assertThat(item.id, is(1));
		assertSame(item.type, TestFragment.class);
		assertThat(item.tag, is("TAG.TestFragment"));
	}

	@Test public void testNewFragmentInstanceWithArguments() {
		// Arrange:
		final Bundle args = new Bundle();
		final FragmentItem item = new FragmentItem(1, TestFragment.class);
		// Act:
		final Fragment fragment = item.newFragmentInstance(args);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(args));
	}

	@Test public void testNewFragmentInstanceWithoutArguments() {
		// Arrange:
		final FragmentItem item = new FragmentItem(1, TestFragment.class);
		// Act:
		final Fragment fragment = item.newFragmentInstance(null);
		// Assert:
		assertThat(fragment, is(notNullValue()));
		assertThat(fragment.getArguments(), is(nullValue()));
	}

	@Test public void testNewFragmentInstanceForFragmentClass() {
		// Arrange:
		final FragmentItem item = new FragmentItem(1, Fragment.class);
		// Act + Assert:
		assertThat(item.newFragmentInstance(new Bundle()), is(nullValue()));
	}

	@Test public void testNewFragmentInstanceForAbstractFragment() {
		// Arrange:
		final FragmentItem item = new FragmentItem(1, TestAbstractFragment.class);
		// Act + Assert:
		assertThat(item.newFragmentInstance(new Bundle()), is(nullValue()));
	}

	@Test public void testNewFragmentInstanceForFragmentWithPrivateConstructor() {
		// Arrange:
		final FragmentItem item = new FragmentItem(1, TestFragmentWithPrivateConstructor.class);
		// Act:
		// Assert:
		assertThat(item.newFragmentInstance(new Bundle()), is(nullValue()));
	}

	public static class TestFragment extends Fragment {}

	public static abstract class TestAbstractFragment extends Fragment {}

	public static class TestFragmentWithPrivateConstructor extends Fragment {

		@SuppressLint("ValidFragment")
		private TestFragmentWithPrivateConstructor() {}
	}
}