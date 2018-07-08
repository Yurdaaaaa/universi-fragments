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

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import universum.studios.android.fragment.util.FragmentUtils;

/**
 * ActionBarDelegate is used to wrap an instance of {@link ActionBar} or {@link android.support.v7.app.ActionBar}
 * in order to hide some implementation details when using ActionBar within fragments.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public abstract class ActionBarDelegate {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ActionBarDelegate";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * <b>This field has been deprecated and will be made private in version 1.4.0.</b>
	 * <p>
	 * Instance of context used to access application data.
	 *
	 * @deprecated Use {@link #getContext()} instead.
	 */
	@Deprecated
	@NonNull protected final Context mContext;

	/*
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ActionBarDelegate with the given <var>context</var>.
	 *
	 * @param context The context used to access application data.
	 */
	protected ActionBarDelegate(@NonNull final Context context) {
		this.mContext = context;
	}

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Wraps an ActionBar of the given <var>activity</var> to its corresponding delegate depending
	 * on the activity's implementation.
	 * <p>
	 * <b>Note</b>, that this method will return {@code null} if the specified activity does not have
	 * its ActionBar available at the time.
	 *
	 * @param activity The activity of which action bar to wrap.
	 * @return Instance of ActionBarDelegate for ActionBar of the specified activity.
	 *
	 * @see Activity#getActionBar()
	 * @see AppCompatActivity#getSupportActionBar()
	 */
	@Nullable public static ActionBarDelegate create(@NonNull final Activity activity) {
		if (activity instanceof AppCompatActivity) {
			final android.support.v7.app.ActionBar actionBar = ((AppCompatActivity) activity).getSupportActionBar();
			return actionBar == null ? null : create(activity, actionBar);
		}
		final ActionBar actionBar = activity.getActionBar();
		return actionBar == null ? null : create(activity, actionBar);
	}

	/**
	 * Wraps the given <var>actionBar</var> into its corresponding delegate.
	 *
	 * @param context   Context used to access application data.
	 * @param actionBar The desired action bar to wrap. May be {@code null} to create mock delegate.
	 * @return New instance of ActionBarDelegate with the given action bar.
	 */
	@NonNull public static ActionBarDelegate create(@NonNull final Context context, @Nullable final ActionBar actionBar) {
		return new Impl(context, actionBar);
	}

	/**
	 * Wraps the given support <var>actionBar</var> into its corresponding delegate.
	 *
	 * @param context   Context used to access application data.
	 * @param actionBar The desired action bar to wrap. May be {@code null} to create mock delegate.
	 * @return New instance of ActionBarDelegate with the given action bar.
	 */
	@NonNull public static ActionBarDelegate create(@NonNull final Context context, @Nullable final android.support.v7.app.ActionBar actionBar) {
		return new SupportImpl(context, actionBar);
	}

	/**
	 * Returns the context this delegate for created with.
	 *
	 * @return This delegate's context.
	 *
	 * @see #ActionBarDelegate(Context)
	 */
	@NonNull protected Context getContext() {
		return mContext;
	}

	/**
	 * Delegates to {@link ActionBar#setDisplayHomeAsUpEnabled(boolean)}.
	 */
	public abstract void setDisplayHomeAsUpEnabled(boolean enabled);

	/**
	 * Delegates to {@link ActionBar#setHomeAsUpIndicator(int)}.
	 */
	public abstract void setHomeAsUpIndicator(@DrawableRes int resId);

	/**
	 * Delegates to {@link ActionBar#setHomeAsUpIndicator(int)} for vector drawable indicator.
	 */
	public abstract void setHomeAsUpVectorIndicator(@DrawableRes int resId);

	/**
	 * Delegates to {@link ActionBar#setHomeAsUpIndicator(Drawable)}.
	 */
	public abstract void setHomeAsUpIndicator(@Nullable Drawable indicator);

	/**
	 * Delegates to {@link ActionBar#setIcon(int)}.
	 */
	public abstract void setIcon(@DrawableRes int resId);

	/**
	 * Delegates to {@link ActionBar#setIcon(Drawable)}.
	 */
	public abstract void setIcon(@Nullable Drawable icon);

	/**
	 * Delegates to {@link ActionBar#setTitle(int)}.
	 */
	public abstract void setTitle(@StringRes int resId);

	/**
	 * Delegates to {@link ActionBar#setTitle(CharSequence)}.
	 */
	public abstract void setTitle(@Nullable CharSequence title);

	/*
	 * Inner classes ===============================================================================
	 */

	/**
	 * An {@link ActionBarDelegate} implementation used to wrap {@link ActionBar}.
	 */
	@VisibleForTesting static final class Impl extends ActionBarDelegate {

		/**
		 * Wrapped action bar instance.
		 */
		private final ActionBar actionBar;

		/**
		 * Creates a new instance of Impl to wrap the given <var>actionBar</var>.
		 *
		 * @param context   Context used to access application data.
		 * @param actionBar The native action bar to be wrapped.
		 */
		Impl(final Context context, final ActionBar actionBar) {
			super(context);
			this.actionBar = actionBar;
		}

		/**
		 */
		@Override public void setDisplayHomeAsUpEnabled(final boolean enabled) {
			if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(enabled);
		}

		/**
		 */
		@Override public void setHomeAsUpIndicator(@DrawableRes final int resId) {
			if (actionBar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
				actionBar.setHomeAsUpIndicator(resId);
		}

		/**
		 */
		@Override public void setHomeAsUpVectorIndicator(@DrawableRes final int resId) {
			setHomeAsUpIndicator(FragmentUtils.getVectorDrawable(
					mContext.getResources(),
					resId,
					mContext.getTheme()
			));
		}

		/**
		 */
		@Override public void setHomeAsUpIndicator(@Nullable final Drawable indicator) {
			if (actionBar != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2)
				actionBar.setHomeAsUpIndicator(indicator);
		}

		/**
		 */
		@Override public void setTitle(@StringRes final int resId) {
			setTitle(mContext.getText(resId));
		}

		/**
		 */
		@Override public void setTitle(@Nullable final CharSequence title) {
			if (actionBar != null) actionBar.setTitle(title);
		}

		/**
		 */
		@Override public void setIcon(@DrawableRes final int resId) {
			if (actionBar != null) actionBar.setIcon(resId);
		}

		/**
		 */
		@Override public void setIcon(@Nullable final Drawable icon) {
			if (actionBar != null) actionBar.setIcon(icon);
		}
	}

	/**
	 * An {@link ActionBarDelegate} implementation used to wrap {@link android.support.v7.app.ActionBar}.
	 */
	@VisibleForTesting static final class SupportImpl extends ActionBarDelegate {

		/**
		 * Wrapped support action bar instance.
		 */
		private final android.support.v7.app.ActionBar actionBar;

		/**
		 * Creates a new instance of SupportImpl to wrap the given <var>actionBar</var>.
		 *
		 * @param context   Context used to access application data.
		 * @param actionBar The support action bar to be wrapped.
		 */
		SupportImpl(final Context context, final android.support.v7.app.ActionBar actionBar) {
			super(context);
			this.actionBar = actionBar;
		}

		/**
		 */
		@Override public void setDisplayHomeAsUpEnabled(final boolean enabled) {
			if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(enabled);
		}

		/**
		 */
		@Override public void setHomeAsUpIndicator(@DrawableRes final int resId) {
			if (actionBar != null) actionBar.setHomeAsUpIndicator(resId);
		}

		/**
		 */
		@Override public void setHomeAsUpVectorIndicator(@DrawableRes final int resId) {
			setHomeAsUpIndicator(FragmentUtils.getVectorDrawable(
					mContext.getResources(),
					resId,
					mContext.getTheme()
			));
		}

		/**
		 */
		@Override public void setHomeAsUpIndicator(@Nullable final Drawable indicator) {
			if (actionBar != null) actionBar.setHomeAsUpIndicator(indicator);
		}

		/**
		 */
		@Override public void setIcon(@DrawableRes final int resId) {
			if (actionBar != null) actionBar.setIcon(resId);
		}

		/**
		 */
		@Override public void setIcon(@Nullable final Drawable icon) {
			if (actionBar != null) actionBar.setIcon(icon);
		}

		/**
		 */
		@Override public void setTitle(@StringRes final int resId) {
			setTitle(mContext.getText(resId));
		}

		/**
		 */
		@Override public void setTitle(@Nullable final CharSequence title) {
			if (actionBar != null) actionBar.setTitle(title);
		}
	}
}