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

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Menu;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.ActionMode;
import universum.studios.android.fragment.ActionBarDelegate;
import universum.studios.android.fragment.ActionBarFragment;
import universum.studios.android.fragment.annotation.ActionBarOptions;
import universum.studios.android.fragment.annotation.ActionModeOptions;
import universum.studios.android.fragment.annotation.MenuOptions;

/**
 * An {@link AnnotationHandlers} implementation providing {@link AnnotationHandler} instances for
 * <b>ActionBar</b> associated fragments and classes.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class ActionBarAnnotationHandlers extends AnnotationHandlers {

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Obtains a {@link ActionBarFragmentAnnotationHandler} implementation for the given <var>classOfFragment</var>.
	 *
	 * @return Annotation handler ready to be used.
	 *
	 * @see AnnotationHandlers#obtainHandler(Class, Class)
	 */
	@Nullable public static ActionBarFragmentAnnotationHandler obtainActionBarFragmentHandler(@NonNull final Class<?> classOfFragment) {
		return obtainHandler(ActionBarFragmentHandler.class, classOfFragment);
	}

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * An {@link ActionBarFragmentAnnotationHandler} implementation for {@link ActionBarFragment} class.
	 */
	static class ActionBarFragmentHandler extends BaseAnnotationHandlers.FragmentHandler implements ActionBarFragmentAnnotationHandler {

		/**
		 * Action bar's home as up flag obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ActionBarOptions @ActionBarOptions} annotation.
		 */
		private int homeAsUp = ActionBarOptions.UNCHANGED;

		/**
		 * Vector drawable resource id of a home as up indicator obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ActionBarOptions @ActionBarOptions} annotation.
		 */
		private int homeAsUpVectorIndicator = ActionBarOptions.UNCHANGED;

		/**
		 * Drawable resource id of a home as up indicator obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ActionBarOptions @ActionBarOptions} annotation.
		 */
		private int homeAsUpIndicator = ActionBarOptions.UNCHANGED;

		/**
		 * Drawable resource id of an action bar's icon obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ActionBarOptions @ActionBarOptions} annotation.
		 */
		private int icon = ActionBarOptions.UNCHANGED;

		/**
		 * Text resource id of an action bar's title obtained from the annotated class.
		 * <p>
		 * Obtained via {@link ActionBarOptions @ActionBarOptions} annotation.
		 */
		private int title = ActionBarOptions.UNCHANGED;

		/**
		 * Boolean flag indicating whether there were {@link MenuOptions @MenuOptions} annotation
		 * presented for the annotated class or {@link ActionBarOptions @ActionBarOptions} with
		 * home as up indicator enabled.
		 */
		private boolean hasOptionsMenu;

		/**
		 * Boolean flag indicating whether to clear options menu for the related fragment or not.
		 * <p>
		 * Obtained via {@link MenuOptions @MenuOptions} annotation.
		 */
		private boolean clearOptionsMenu;

		/**
		 * Menu resource id for a menu of the related fragment obtained from the annotated class.
		 * <p>
		 * Obtained via {@link MenuOptions @MenuOptions} annotation.
		 */
		private int optionsMenuResource = NO_RES;

		/**
		 * Flags for the options menu of the related fragment obtained from the annotated class.
		 * <p>
		 * Obtained via {@link MenuOptions @MenuOptions} annotation.
		 */
		private int optionsMenuFlags = -1;

		/**
		 * Menu resource id for an action mode of the related fragment obtained from the annotated
		 * class.
		 * <p>
		 * Obtained via {@link ActionModeOptions @ActionModeOptions} annotation.
		 */
		private int actionModeMenuResource = NO_RES;

		/**
		 * Creates a new instance of ActionBarFragmentHandler for the given <var>annotatedClass</var>.
		 *
		 * @see BaseAnnotationHandler#BaseAnnotationHandler(Class)
		 */
		@SuppressWarnings("WeakerAccess")
		public ActionBarFragmentHandler(@NonNull final Class<?> annotatedClass) {
			super(annotatedClass);
			final ActionBarOptions actionBarOptions = findAnnotation(ActionBarOptions.class);
			if (actionBarOptions != null) {
				this.homeAsUp = actionBarOptions.homeAsUp();
				this.homeAsUpVectorIndicator = actionBarOptions.homeAsUpVectorIndicator();
				this.homeAsUpIndicator = actionBarOptions.homeAsUpIndicator();
				this.icon = actionBarOptions.icon();
				this.title = actionBarOptions.title();
			}
			final MenuOptions menuOptions = findAnnotation(MenuOptions.class);
			if (menuOptions != null) {
				this.hasOptionsMenu = true;
				this.clearOptionsMenu = menuOptions.clear();
				this.optionsMenuFlags = menuOptions.flags();
				this.optionsMenuResource = menuOptions.value();
			}
			final ActionModeOptions actionModeOptions = findAnnotation(ActionModeOptions.class);
			if (actionModeOptions != null) {
				this.actionModeMenuResource = actionModeOptions.menu();
			}
		}

		/**
		 */
		@Override public void configureActionBar(@NonNull final ActionBarDelegate actionBarDelegate) {
			switch (homeAsUp) {
				case ActionBarOptions.HOME_AS_UP_ENABLED:
					actionBarDelegate.setDisplayHomeAsUpEnabled(true);
					this.hasOptionsMenu = true;
					break;
				case ActionBarOptions.HOME_AS_UP_DISABLED:
					actionBarDelegate.setDisplayHomeAsUpEnabled(false);
					break;
				default:
					// Do not "touch" ActionBar's enabled state.
					break;
			}
			if (homeAsUpVectorIndicator != ActionBarOptions.UNCHANGED) {
				switch (homeAsUpVectorIndicator) {
					case ActionBarOptions.NONE:
						actionBarDelegate.setHomeAsUpIndicator(new ColorDrawable(Color.TRANSPARENT));
						break;
					default:
						actionBarDelegate.setHomeAsUpVectorIndicator(homeAsUpVectorIndicator);
						break;
				}
			} else if (homeAsUpIndicator != ActionBarOptions.UNCHANGED) {
				switch (homeAsUpIndicator) {
					case ActionBarOptions.NONE:
						actionBarDelegate.setHomeAsUpIndicator(new ColorDrawable(Color.TRANSPARENT));
						break;
					default:
						actionBarDelegate.setHomeAsUpIndicator(homeAsUpIndicator);
						break;
				}
			}
			switch (icon) {
				case ActionBarOptions.UNCHANGED:
					break;
				case ActionBarOptions.NONE:
					actionBarDelegate.setIcon(new ColorDrawable(Color.TRANSPARENT));
					break;
				default:
					actionBarDelegate.setIcon(icon);
					break;
			}
			switch (title) {
				case ActionBarOptions.UNCHANGED:
					break;
				case ActionBarOptions.NONE:
					actionBarDelegate.setTitle("");
					break;
				default:
					actionBarDelegate.setTitle(title);
					break;
			}
		}

		/**
		 */
		@Override public boolean hasOptionsMenu() {
			return hasOptionsMenu;
		}

		/**
		 */
		@Override public boolean shouldClearOptionsMenu() {
			return clearOptionsMenu;
		}

		/**
		 */
		@Override @MenuRes public int getOptionsMenuFlags(@MenuRes final int defaultFlags) {
			return optionsMenuFlags == -1 ? defaultFlags : optionsMenuFlags;
		}

		/**
		 */
		@Override public int getOptionsMenuResource(final int defaultResource) {
			return optionsMenuResource == NO_RES ? defaultResource : optionsMenuResource;
		}

		/**
		 */
		@Override public boolean handleCreateActionMode(@NonNull final ActionMode actionMode, @NonNull final Menu menu) {
			if (actionModeMenuResource == NO_RES) {
				return false;
			}
			actionMode.getMenuInflater().inflate(actionModeMenuResource, menu);
			return true;
		}
	}
}