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

import android.support.annotation.IntDef;
import android.support.annotation.MenuRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an annotation for determining how a {@link android.view.Menu Menu} should be set.
 *
 * @author Martin Albedinsky
 */
@Inherited
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MenuOptions {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Fag indicating that a default menu set up should be used.
	 */
	int DEFAULT = 0x00000000;

	/**
	 * Flag indicating that the creation of a super's menu should be ignored.
	 */
	int IGNORE_SUPER = 0x00000001;

	/**
	 * Flag indicating that a menu should be created before a super's one.
	 */
	int BEFORE_SUPER = 0x00000001 << 1;

	/**
	 * Defines an annotation for determining set of allowed flags for {@link #flags()} attribute.
	 */
	@Retention(RetentionPolicy.SOURCE)
	@IntDef({DEFAULT, IGNORE_SUPER, BEFORE_SUPER})
	@interface Flags {
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * A resource id of the desired xml menu.
	 * <p>
	 * Default value: <b>0</b>
	 */
	@MenuRes
	int value() default 0;

	/**
	 * Flag indicating whether to clear the already created menu or not.
	 */
	boolean clear() default false;

	/**
	 * Flags for determining a menu set up.
	 */
	@Flags
	int flags() default DEFAULT;
}