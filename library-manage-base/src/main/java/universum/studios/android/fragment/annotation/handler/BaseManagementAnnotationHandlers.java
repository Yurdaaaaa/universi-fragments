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

import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;

import java.lang.reflect.Field;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import universum.studios.android.fragment.annotation.FactoryFragment;
import universum.studios.android.fragment.annotation.FactoryFragments;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.manage.BaseFragmentFactory;
import universum.studios.android.fragment.manage.FragmentItem;

/**
 * An {@link AnnotationHandlers} implementation providing {@link AnnotationHandler} instances for
 * <b>base management</b> associated fragments and classes.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class BaseManagementAnnotationHandlers extends AnnotationHandlers {

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Obtains a {@link FragmentFactoryAnnotationHandler} implementation for the given <var>classOfFactory</var>.
	 *
	 * @return Annotation handler ready to be used.
	 *
	 * @see AnnotationHandlers#obtainHandler(Class, Class)
	 */
	@Nullable public static FragmentFactoryAnnotationHandler obtainFactoryHandler(@NonNull final Class<?> classOfFactory) {
		return obtainHandler(FragmentFactoryHandler.class, classOfFactory);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link FragmentFactoryAnnotationHandler} implementation for {@link BaseFragmentFactory} class.
	 */
	static final class FragmentFactoryHandler extends BaseAnnotationHandler implements FragmentFactoryAnnotationHandler {

		/**
		 * Array of fragment items populated from the {@link FactoryFragments @FactoryFragments} or
		 * {@link FactoryFragment @FactoryFragment} annotations if presented.
		 */
		final SparseArray<FragmentItem> items;

		/**
		 * Creates a new instance of FragmentFactoryHandler for the given <var>annotatedClass</var>.
		 *
		 * @see BaseAnnotationHandler#BaseAnnotationHandler(Class)
		 */
		public FragmentFactoryHandler(@NonNull final Class<?> annotatedClass) {
			super(annotatedClass);
			final SparseArray<FragmentItem> items = new SparseArray<>();
			final FactoryFragments fragments = findAnnotation(FactoryFragments.class);
			if (fragments != null) {
				final int[] ids = fragments.value();
				if (ids.length > 0) {
					for (final int id : ids) {
						items.put(id, new FragmentItem(
								id,
								Fragment.class,
								BaseFragmentFactory.createFragmentTag(annotatedClass, Integer.toString(id))
						));
					}
				}
			}
			FragmentAnnotations.iterateFields(new FragmentAnnotations.FieldProcessor() {

				/**
				 */
				@Override public void onProcessField(@NonNull Field field, @NonNull String name) {
					if (field.isAnnotationPresent(FactoryFragment.class) && int.class.equals(field.getType())) {
						final FactoryFragment factoryFragment = field.getAnnotation(FactoryFragment.class);
						try {
							field.setAccessible(true);
							final int id = (int) field.get(null);
							items.put(id, new FragmentItem(
									id,
									factoryFragment.value(),
									BaseFragmentFactory.createFragmentTag(
											annotatedClass,
											TextUtils.isEmpty(factoryFragment.taggedName()) ?
													Integer.toString(id) :
													factoryFragment.taggedName()
									)
							));
						} catch (IllegalAccessException e) {
							// This exception should not be thrown as we are changing accessibility
							// of the field via field.setAccessible(true);
							Log.e(
									FragmentFactoryAnnotationHandler.class.getSimpleName(),
									"Failed to obtain id value from @FactoryFragment " + name + " of " + annotatedClass.getName() + "!",
									e
							);
						}
					}
				}
			}, annotatedClass, BaseFragmentFactory.class);
			this.items = items.size() > 0 ? items : null;
		}

		/**
		 */
		@Override @Nullable public SparseArray<FragmentItem> getFragmentItems() {
			return items;
		}
	}
}