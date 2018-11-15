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
package universum.studios.android.fragment;

import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

/**
 * The ViewClickWatcher interface specifies one callback that may be used to dispatch a view click
 * event to a watcher that implements this interface.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface ViewClickWatcher {

	/**
	 * Called to dispatch a view click event to this watcher instance.
	 *
	 * @param view The clicked view.
	 * @return {@code True} if this watcher processed the click event for the specified <var>view</var>,
	 * {@code false} otherwise.
	 */
	@CheckResult boolean dispatchViewClick(@NonNull View view);
}