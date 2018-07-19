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
package universum.studios.android.samples.fragment.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import universum.studios.android.fragment.BackPressWatcher;
import universum.studios.android.fragment.annotation.FragmentAnnotations;
import universum.studios.android.fragment.manage.FragmentController;
import universum.studios.android.samples.fragment.R;
import universum.studios.android.samples.fragment.ui.web.SampleWebFragment;
import universum.studios.android.samples.fragment.ui.welcome.WelcomeActivity;
import universum.studios.android.samples.ui.SamplesMainFragment;
import universum.studios.android.samples.ui.SamplesNavigationActivity;

/**
 * @author Martin Albedinsky
 */
public final class MainActivity extends SamplesNavigationActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "MainActivity";

	static {
		FragmentAnnotations.setEnabled(true);
	}

	private FragmentController fragmentController;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.fragmentController = new FragmentController(this);
		this.fragmentController.setViewContainerId(R.id.samples_container);
	}

	@Override
	protected boolean onHandleNavigationItemSelected(@NonNull MenuItem item) {
		switch (item.getItemId()) {
			case R.id.navigation_item_home:
				fragmentController.newRequest(new SamplesMainFragment()).replaceSame(true).execute();
				return true;
			case R.id.navigation_item_web:
				fragmentController.newRequest(new SampleWebFragment()).replaceSame(true).execute();
				return true;
			case R.id.navigation_item_welcome:
				startActivity(new Intent(this, WelcomeActivity.class));
				return false;
		}
		return super.onHandleNavigationItemSelected(item);
	}

	@Override
	protected boolean onBackPress() {
		final Fragment fragment = fragmentController.findCurrentFragment();
		return fragment instanceof BackPressWatcher && ((BackPressWatcher) fragment).dispatchBackPress();
	}
}