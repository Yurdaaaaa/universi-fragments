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

import android.support.annotation.Nullable;
import android.util.SparseArray;

import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.manage.BaseFragmentFactory;
import universum.studios.android.fragment.manage.FragmentItem;

/**
 * An {@link AnnotationHandler} extended interface for annotation handlers from the Fragments library
 * that are used to handle processing of annotations attached to classes derived from
 * {@link BaseFragmentFactory} class provided by this library.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface FragmentFactoryAnnotationHandler extends AnnotationHandler {

	/**
	 * Returns an array with FragmentItems mapped to theirs ids that has been created from
	 * {@link FactoryFragment @FactoryFragment} or {@link FactoryFragments @FactoryFragments}
	 * annotations (if presented).
	 *
	 * @return Array with fragment items created from the processed annotations or {@code null} if
	 * there were no annotations specified.
	 */
	@Nullable SparseArray<FragmentItem> getFragmentItems();
}