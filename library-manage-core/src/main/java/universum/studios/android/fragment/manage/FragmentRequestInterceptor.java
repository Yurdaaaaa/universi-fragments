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
package universum.studios.android.fragment.manage;

import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface that may be used to intercept a specific {@link FragmentRequest} when it is being executed
 * via its associated {@link FragmentController}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see FragmentController#setRequestInterceptor(FragmentRequestInterceptor)
 */
public interface FragmentRequestInterceptor {

	/**
	 * Called to allow this request interceptor to intercept execution of the given fragment <var>request</var>.
	 * <p>
	 * Interceptor may also just change configuration of the request and return {@code null} to indicate
	 * that the associated fragment controller should handle the execution.
	 *
	 * @param request The request to be executed.
	 * @return Fragment associated with the request as result of the handled execution, {@code null}
	 * to let the fragment controller handle the execution.
	 */
	@Nullable Fragment interceptFragmentRequest(@NonNull FragmentRequest request);
}