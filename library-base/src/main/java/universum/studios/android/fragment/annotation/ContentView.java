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

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an annotation for determining a layout resource that should be inflated as a content root
 * view.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ContentView {

	/**
	 * A resource id of the desired layout to inflate as root view.
	 *
	 * @see #attachToContainer()
	 */
	@LayoutRes int value();

	/**
	 * Flag indicating whether to attach inflated content view to its parent container or not.
	 * <p>
	 * Default value: <b>false</b>
	 */
	boolean attachToContainer() default false;

	/**
	 * A resource id of the background drawable/color to be set to the inflated root view. May be a
	 * resource id to either color or drawable.
	 * <p>
	 * Default value: <b>-1</b>
	 */
	@ColorRes @DrawableRes int background() default -1;
}