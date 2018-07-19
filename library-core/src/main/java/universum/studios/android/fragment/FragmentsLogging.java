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
package universum.studios.android.fragment;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;

/**
 * Utility class used by the <b>Fragments</b> library for logging purpose.
 * <p>
 * Custom {@link Logger} may be specified via {@link #setLogger(Logger)} which may be used to control
 * logging outputs of the library.
 * <p>
 * Default logger used by this class has specified {@link Log#ASSERT} log level which means the the
 * library by default does not print out any logs.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class FragmentsLogging {

	/**
	 * Default logger used by the library for logging purpose.
	 */
	private static final Logger LOGGER = new SimpleLogger(Log.ASSERT);

	/**
	 * Logger to which is this logging utility class delegating all log related requests.
	 */
	@NonNull private static Logger logger = LOGGER;

	/**
	 */
	private FragmentsLogging() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}

	/**
	 * Sets a logger to be used by this logging class to print out logs into console.
	 *
	 * @param logger The desired logger. May by {@code null} to use default logger.
	 *
	 * @see #getLogger()
	 */
	public static void setLogger(@Nullable final Logger logger) {
		FragmentsLogging.logger = logger == null ? LOGGER : logger;
	}

	/**
	 * Returns the logger used by this logging class.
	 *
	 * @return Either default or custom logger.
	 *
	 * @see #setLogger(Logger)
	 */
	@NonNull public static Logger getLogger() {
		return logger;
	}

	/**
	 * Delegates to {@link Logger#d(String, String)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String message) {
		logger.d(tag, message);
	}

	/**
	 * Delegates to {@link Logger#d(String, String, Throwable)}.
	 */
	public static void d(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.d(tag, message, throwable);
	}

	/**
	 * Delegates to {@link Logger#v(String, String)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String message) {
		logger.v(tag, message);
	}

	/**
	 * Delegates to {@link Logger#v(String, String, Throwable)}.
	 */
	public static void v(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.v(tag, message, throwable);
	}

	/**
	 * Delegates to {@link Logger#i(String, String)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String message) {
		logger.i(tag, message);
	}

	/**
	 * Delegates to {@link Logger#i(String, String, Throwable)}.
	 */
	public static void i(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.i(tag, message, throwable);
	}

	/**
	 * Delegates to {@link Logger#w(String, String)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String message) {
		logger.w(tag, message);
	}

	/**
	 * Delegates to {@link Logger#w(String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @Nullable final Throwable throwable) {
		logger.w(tag, throwable);
	}

	/**
	 * Delegates to {@link Logger#w(String, String, Throwable)}.
	 */
	public static void w(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.w(tag, message, throwable);
	}

	/**
	 * Delegates to {@link Logger#e(String, String)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String message) {
		logger.e(tag, message);
	}

	/**
	 * Delegates to {@link Logger#e(String, String, Throwable)}.
	 */
	public static void e(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.e(tag, message, throwable);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String message) {
		logger.wtf(tag, message);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @Nullable final Throwable throwable) {
		logger.wtf(tag, throwable);
	}

	/**
	 * Delegates to {@link Logger#wtf(String, String, Throwable)}.
	 */
	public static void wtf(@NonNull final String tag, @NonNull final String message, @Nullable final Throwable throwable) {
		logger.wtf(tag, message, throwable);
	}
}