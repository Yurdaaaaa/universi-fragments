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

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.fragment.BaseFragment;
import universum.studios.android.fragment.annotation.ContentView;

/**
 * An {@link AnnotationHandlers} implementation providing {@link AnnotationHandler} instances for
 * classes from the fragments base package.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class BaseAnnotationHandlers extends AnnotationHandlers {

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Obtains a {@link FragmentAnnotationHandler} implementation for the given <var>classOfFragment</var>.
	 *
	 * @return Annotation handler ready to be used.
	 *
	 * @see AnnotationHandlers#obtainHandler(Class, Class)
	 */
	@Nullable public static FragmentAnnotationHandler obtainFragmentHandler(@NonNull final Class<?> classOfFragment) {
		return obtainHandler(FragmentHandler.class, classOfFragment);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link FragmentAnnotationHandler} implementation for {@link BaseFragment} class.
	 */
	@SuppressWarnings("WeakerAccess")
	static class FragmentHandler extends BaseAnnotationHandler implements FragmentAnnotationHandler {

		/**
		 * Boolean flat determining whether to attach content view to the related fragment's parent
		 * container.
		 * <p>
		 * Obtained via {@link ContentView @ContentView} annotation.
		 */
		private boolean attachContentViewToContainer;

		/**
		 * Layout resource of the related fragment's content view obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ContentView @ContentView} annotation.
		 */
		private int contentViewResource = NO_RES;

		/**
		 * Background resource id of the related fragment's content view obtained from the annotated
		 * class.
		 * <p>
		 * Obtained via {@link ContentView @ContentView} annotation.
		 */
		private int contentViewBackgroundResId = NO_RES;

		/**
		 * Creates a new instance of FragmentHandler for the given <var>annotatedClass</var>.
		 *
		 * @see BaseAnnotationHandler#BaseAnnotationHandler(Class)
		 */
		public FragmentHandler(@NonNull final Class<?> annotatedClass) {
			super(annotatedClass);
			final ContentView contentView = findAnnotation(ContentView.class);
			if (contentView != null) {
				this.attachContentViewToContainer = contentView.attachToContainer();
				this.contentViewResource = contentView.value();
				this.contentViewBackgroundResId = contentView.background();
			}
		}

		/**
		 */
		@Override @LayoutRes public int getContentViewResource(@LayoutRes final int defaultViewResource) {
			return contentViewResource == NO_RES ? defaultViewResource : contentViewResource;
		}

		/**
		 */
		@Override public boolean shouldAttachContentViewToContainer() {
			return attachContentViewToContainer;
		}

		/**
		 */
		@Override @ColorRes @DrawableRes public int getContentViewBackgroundResId(final int defaultResId) {
			return contentViewBackgroundResId == NO_RES ? defaultResId : contentViewBackgroundResId;
		}
	}
}