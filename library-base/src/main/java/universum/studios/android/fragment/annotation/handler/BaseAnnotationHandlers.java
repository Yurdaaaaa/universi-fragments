/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
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

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import universum.studios.android.fragment.BaseFragment;
import universum.studios.android.fragment.annotation.ContentView;

/**
 * An {@link AnnotationHandlers} implementation providing {@link AnnotationHandler} instances for
 * classes from the fragments base package.
 *
 * @author Martin Albedinsky
 */
@SuppressWarnings("unused")
public final class BaseAnnotationHandlers extends AnnotationHandlers {

	/*
	 * Constructors ================================================================================
	 */

	/**
	 */
	private BaseAnnotationHandlers() {
		super();
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Obtains a {@link FragmentAnnotationHandler} implementation for the given <var>classOfFragment</var>.
	 *
	 * @see AnnotationHandlers#obtainHandler(Class, Class)
	 */
	@Nullable
	public static FragmentAnnotationHandler obtainFragmentHandler(@NonNull final Class<?> classOfFragment) {
		return obtainHandler(FragmentHandler.class, classOfFragment);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * A {@link FragmentAnnotationHandler} implementation for {@link BaseFragment} class.
	 */
	@SuppressWarnings("WeakerAccess") static class FragmentHandler extends BaseAnnotationHandler implements FragmentAnnotationHandler {

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
		@Override
		@LayoutRes
		public int getContentViewResource(@LayoutRes final int defaultViewResource) {
			return contentViewResource == NO_RES ? defaultViewResource : contentViewResource;
		}

		/**
		 */
		@Override
		public boolean shouldAttachContentViewToContainer() {
			return attachContentViewToContainer;
		}

		/**
		 */
		@Override
		@ColorRes
		@DrawableRes
		public int getContentViewBackgroundResId(final int defaultResId) {
			return contentViewBackgroundResId == NO_RES ? defaultResId : contentViewBackgroundResId;
		}
	}
}
