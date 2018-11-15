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

import androidx.fragment.app.Fragment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import universum.studios.android.fragment.manage.BaseFragmentFactory;

/**
 * Annotation type used to mark an <b>int</b> field that specifies an id of fragment provided by a
 * specific {@link universum.studios.android.fragment.manage.BaseFragmentFactory BaseFragmentFactory}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@Documented
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface FactoryFragment {

	/**
	 * Class of the desired fragment of which instance should be instantiated for this id.
	 *
	 * @see BaseFragmentFactory#createFragment(int)
	 */
	Class<? extends Fragment> value() default Fragment.class;

	/**
	 * Name of the associated fragment to be placed into its TAG.
	 *
	 * @see BaseFragmentFactory#createFragmentTag(Class, String)
	 */
	String taggedName() default "";
}