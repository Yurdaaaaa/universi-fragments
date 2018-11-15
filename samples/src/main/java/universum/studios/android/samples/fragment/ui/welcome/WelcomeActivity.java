/*
 * *************************************************************************************************
 *                                 Copyright 2017 Universum Studios
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
package universum.studios.android.samples.fragment.ui.welcome;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import universum.studios.android.fragment.ViewClickWatcher;
import universum.studios.android.fragment.manage.FragmentController;
import universum.studios.android.fragment.manage.FragmentRequest;
import universum.studios.android.fragment.manage.FragmentRequestInterceptor;
import universum.studios.android.fragment.transition.FragmentTransitions;
import universum.studios.android.samples.fragment.R;
import universum.studios.android.samples.ui.SamplesActivity;

/**
 * @author Martin Albedinsky
 */
public final class WelcomeActivity extends SamplesActivity implements FragmentRequestInterceptor {

	private FragmentController fragmentController;

	@Override protected void onCreate(@Nullable final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		this.fragmentController = new FragmentController(this);
		this.fragmentController.setViewContainerId(R.id.container);
		this.fragmentController.setFactory(new WelcomeFragments());
		if (savedInstanceState == null) {
			fragmentController.newRequest(WelcomeFragments.WELCOME).immediate(true).execute();
		}
	}

	@Override @Nullable public Fragment interceptFragmentRequest(@NonNull final FragmentRequest request) {
		switch (request.fragmentId()) {
			case WelcomeFragments.SIGN_IN:
				request.transition(FragmentTransitions.SLIDE_TO_LEFT).addToBackStack(true);
				break;
			case WelcomeFragments.SIGN_UP:
				request.transition(FragmentTransitions.SLIDE_TO_RIGHT).addToBackStack(true);
				break;
			case WelcomeFragments.LOST_PASSWORD:
				request.transition(FragmentTransitions.CROSS_FADE).addToBackStack(true);
				break;
		}
		return null;
	}

	@SuppressWarnings("unused") public void onViewClick(@NonNull final View view) {
		final Fragment fragment = fragmentController.findCurrentFragment();
		if (fragment instanceof ViewClickWatcher && ((ViewClickWatcher) fragment).dispatchViewClick(view)) {
			return;
		}
		switch (view.getId()) {
			case R.id.sign_in:
				fragmentController.newRequest(WelcomeFragments.SIGN_IN).execute();
				break;
			case R.id.sign_up:
				fragmentController.newRequest(WelcomeFragments.SIGN_UP).execute();
				break;
			case R.id.lost_password:
				fragmentController.newRequest(WelcomeFragments.LOST_PASSWORD).execute();
				break;
		}
	}
}