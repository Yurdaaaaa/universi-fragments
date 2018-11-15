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
package universum.studios.android.fragment.annotation;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import androidx.annotation.DrawableRes;
import androidx.annotation.IntDef;
import androidx.annotation.StringRes;

/**
 * Defines an annotation for determining how an {@link ActionBar} should be set.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ActionBarOptions {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Flag for all options from this annotation that can be used to identify that current value for
	 * such option should be left <b>unchanged</b>.
	 */
	int UNCHANGED = -0x01;

	/**
	 * Flag for {@link #title()}, {@link #icon()} or {@link #homeAsUpIndicator()} options to identify
	 * that the current value should be hided/removed from either ActionBar's title, icon or home as
	 * up indicator.
	 */
	int NONE = 0x00;

	/**
	 * Flag indicating that an ActionBar's home as up icon should be enabled (visible).
	 */
	int HOME_AS_UP_ENABLED = 0x01;

	/**
	 * Flag indicating that an ActionBar's home as up icon should be disabled (invisible).
	 */
	int HOME_AS_UP_DISABLED = 0x02;

	/**
	 * Defines an annotation for determining set of allowed flags for {@link #homeAsUp()} attribute.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({UNCHANGED, HOME_AS_UP_ENABLED, HOME_AS_UP_DISABLED})
	@interface HomeAsUp {}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * A resource id of the desired text that should be set as title for an ActionBar.
	 * <p>
	 * Use {@link #NONE} to remove the current title from ActionBar.
	 * <p>
	 * Default value: <b>{@link #UNCHANGED}</b>
	 *
	 * @see #icon()
	 * @see ActionBar#setTitle(int)
	 */
	@StringRes int title() default UNCHANGED;

	/**
	 * A resource id of the desired image which should be set as icon for an ActionBar.
	 * <p>
	 * Use {@link #NONE} to hide/remove the current icon from ActionBar.
	 * <p>
	 * Default value: <b>{@link #UNCHANGED}</b>
	 *
	 * @see #title()
	 * @see ActionBar#setIcon(int)
	 */
	@DrawableRes int icon() default UNCHANGED;

	/**
	 * Flag indicating whether to display/hide an ActionBar's home as up icon. May be one of
	 * {@link #HOME_AS_UP_ENABLED}, {@link #HOME_AS_UP_DISABLED} or {@link #UNCHANGED} to not "touch"
	 * home as up icon.
	 * <p>
	 * Default value: <b>{@link #UNCHANGED}</b>
	 *
	 * @see #homeAsUpVectorIndicator()
	 * @see #homeAsUpIndicator()
	 * @see ActionBar#setDisplayHomeAsUpEnabled(boolean)
	 */
	@HomeAsUp int homeAsUp() default UNCHANGED;

	/**
	 * A resource id of the vector drawable that should be set as home as up indicator for an ActionBar.
	 * <p>
	 * Use {@link #NONE} to hide/remove the current home as up indicator from ActionBar.
	 * <p>
	 * Default value: <b>{@link #UNCHANGED}</b>
	 *
	 * @see #homeAsUp()
	 * @see #homeAsUpIndicator()
	 * @see ActionBar#setHomeAsUpIndicator(Drawable)
	 */
	@DrawableRes int homeAsUpVectorIndicator() default UNCHANGED;

	/**
	 * A resource id of the drawable that should be set as home as up indicator for an ActionBar.
	 * <p>
	 * Use {@link #NONE} to hide/remove the current home as up indicator from ActionBar.
	 * <p>
	 * Default value: <b>{@link #UNCHANGED}</b>
	 *
	 * @see #homeAsUp()
	 * @see #homeAsUpVectorIndicator()
	 * @see ActionBar#setHomeAsUpIndicator(int)
	 * @see ActionBar#setHomeAsUpIndicator(Drawable)
	 */
	@DrawableRes int homeAsUpIndicator() default UNCHANGED;
}