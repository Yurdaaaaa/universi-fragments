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

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.fragment.annotation.FactoryFragment;

/**
 * Item that is used by {@link BaseFragmentFactory} to instantiate new fragments that has been
 * specified via {@link FactoryFragment @FactoryFragment} annotation.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class FragmentItem {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "FragmentItem";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Fragment id specified for this item.
	 */
	public final int id;

	/**
	 * Fragment class specified for this item.
	 */
	public final Class<? extends Fragment> type;

	/**
	 * Fragment tag specified for this item.
	 */
	public final String tag;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Same as {@link #FragmentItem(int, Class, String)} with {@code null} <var>tag</var>.
	 */
	public FragmentItem(final int id, @NonNull final Class<? extends Fragment> type) {
		this(id, type, null);
	}

	/**
	 * Creates a new instance of FragmentItem with the given parameters.
	 *
	 * @param id   Id of the fragment for the new item.
	 * @param type Class of the fragment for the new item.
	 * @param tag  Tag of the fragment for the new item.
	 */
	public FragmentItem(final int id, @NonNull final Class<? extends Fragment> type, @Nullable final String tag) {
		this.id = id;
		this.tag = tag;
		this.type = type;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of Fragment type of specified for this item.
	 *
	 * @param arguments Arguments to be attached to the new Fragment instance via {@link Fragment#setArguments(Bundle)}.
	 * @return New fragment instance or {@code null} if fragment type specified for this item is
	 * {@link Fragment Fragment.class} which is a default type and such type cannot be instantiated
	 * or some instantiation error occurs.
	 */
	@SuppressWarnings("TryWithIdenticalCatches")
	@Nullable public Fragment newFragmentInstance(@Nullable final Bundle arguments) {
		if (type.equals(Fragment.class)) {
			return null;
		}
		try {
			final Fragment fragment = type.newInstance();
			if (arguments != null) {
				fragment.setArguments(arguments);
			}
			return fragment;
		} catch (InstantiationException e) {
			Log.e(
					TAG,
					"Failed to instantiate a new fragment instance class of(" + type + "). " +
							"Make sure that this fragment class is accessible and has public empty constructor.",
					e
			);
		} catch (IllegalAccessException e) {
			Log.e(
					TAG,
					"Failed to instantiate a new fragment instance class of(" + type + "). " +
							"Make sure that this fragment class is accessible and has public empty constructor.",
					e
			);
		}
		return null;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}