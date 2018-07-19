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

import android.support.annotation.StringRes;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines an annotation for determining a content to be loaded into {@link android.webkit.WebView WebView}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface WebContent {

	/**
	 * The desired content to be loaded into {@link android.webkit.WebView WebView}. May be a raw
	 * <b>HTML</b>, web <b>URL</b> or path to a <b>FILE</b> with the desired HTML content.
	 * <p>
	 * Default value: <b>""</b>
	 */
	String value() default "";

	/**
	 * Like {@link #value()}, but this specifies a resource id of the desired web content.
	 * <p>
	 * Default value: <b>-1</b>
	 */
	@StringRes int valueRes() default -1;
}