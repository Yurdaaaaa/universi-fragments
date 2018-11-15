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

import android.util.Log;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

/**
 * @author Martin Albedinsky
 */
public final class FragmentsLoggingTest extends RobolectricTestCase {

	@SuppressWarnings("unused") private static final String LOG_TAG = "LoggingTest";

	@Override public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		FragmentsLogging.setLogger(null);
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		FragmentsLogging.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<FragmentsLogging> constructor = FragmentsLogging.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testDefaultLogger() {
		// Act:
		final Logger logger = FragmentsLogging.getLogger();
		// Assert:
		assertThat(logger, is(notNullValue()));
		assertThat(logger.getLogLevel(), is(Log.ASSERT));
	}

	@Test public void testLogger() {
		// Arrange:
		final Logger logger = new SimpleLogger(Log.DEBUG);
		// Act:
		FragmentsLogging.setLogger(logger);
		assertThat(FragmentsLogging.getLogger(), is(logger));
		FragmentsLogging.setLogger(null);
		assertThat(FragmentsLogging.getLogger(), is(notNullValue()));
	}

	@Test public void testV() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.v(LOG_TAG, "message.verbose");
		FragmentsLogging.v(LOG_TAG, "message.verbose", null);
		// Assert:
		verify(mockLogger).v(LOG_TAG, "message.verbose");
		verify(mockLogger).v(LOG_TAG, "message.verbose", null);
		verifyNoMoreInteractions(mockLogger);
	}

	@Test public void testD() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.d(LOG_TAG, "message.debug");
		FragmentsLogging.d(LOG_TAG, "message.debug", null);
		// Assert:
		verify(mockLogger).d(LOG_TAG, "message.debug");
		verify(mockLogger).d(LOG_TAG, "message.debug", null);
		verifyNoMoreInteractions(mockLogger);
	}

	@Test public void testI() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.i(LOG_TAG, "message.info");
		FragmentsLogging.i(LOG_TAG, "message.info", null);
		// Assert:
		verify(mockLogger).i(LOG_TAG, "message.info");
		verify(mockLogger).i(LOG_TAG, "message.info", null);
		verifyNoMoreInteractions(mockLogger);
	}

	@Test public void testW() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.w(LOG_TAG, "message.warn");
		FragmentsLogging.w(LOG_TAG, "message.warn", null);
		FragmentsLogging.w(LOG_TAG, (Throwable) null);
		// Assert:
		verify(mockLogger).w(LOG_TAG, "message.warn");
		verify(mockLogger).w(LOG_TAG, "message.warn", null);
		verify(mockLogger).w(LOG_TAG, (Throwable) null);
		verifyNoMoreInteractions(mockLogger);
	}

	@Test public void testE() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.e(LOG_TAG, "message.error");
		FragmentsLogging.e(LOG_TAG, "message.error", null);
		// Assert:
		verify(mockLogger).e(LOG_TAG, "message.error");
		verify(mockLogger).e(LOG_TAG, "message.error", null);
		verifyNoMoreInteractions(mockLogger);
	}

	@Test public void testWTF() {
		// Arrange:
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		// Act:
		FragmentsLogging.wtf(LOG_TAG, "message.wtf");
		FragmentsLogging.wtf(LOG_TAG, "message.wtf", null);
		FragmentsLogging.wtf(LOG_TAG, (Throwable) null);
		// Assert:
		verify(mockLogger).wtf(LOG_TAG, "message.wtf");
		verify(mockLogger).wtf(LOG_TAG, "message.wtf", null);
		verify(mockLogger).wtf(LOG_TAG, (Throwable) null);
		verifyNoMoreInteractions(mockLogger);
	}
}