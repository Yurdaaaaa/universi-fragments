/*
 * =================================================================================================
 *                             Copyright (C) 2017 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License 
 * you may obtain at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You can redistribute, modify or publish any part of the code written within this file but as it 
 * is described in the License, the software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.fragment;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.hamcrest.core.IsNot;
import org.hamcrest.core.IsNull;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.instrumented.InstrumentedTestCase;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentsLoggingTest extends InstrumentedTestCase {

	@SuppressWarnings("unused")
	private static final String TAG = "FragmentsLoggingTest";

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		FragmentsLogging.setLogger(null);
	}

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		FragmentsLogging.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		final Constructor<FragmentsLogging> constructor = FragmentsLogging.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		constructor.newInstance();
	}

	@Test
	public void testGetDefaultLogger() {
		final Logger logger = FragmentsLogging.getLogger();
		assertThat(logger, is(IsNot.not(IsNull.nullValue())));
		assertThat(logger.getLogLevel(), is(Log.ASSERT));
	}

	@Test
	public void testSetLogger() {
		FragmentsLogging.setLogger(null);
		assertThat(FragmentsLogging.getLogger(), is(IsNot.not(IsNull.nullValue())));
		final Logger logger = new SimpleLogger(Log.DEBUG);
		FragmentsLogging.setLogger(logger);
		assertThat(FragmentsLogging.getLogger(), is(logger));
	}

	@Test
	public void testV() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.v(TAG, "");
		FragmentsLogging.v(TAG, "", null);
	}

	@Test
	public void testD() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.d(TAG, "message.debug");
		verify(mockLogger, times(1)).d(TAG, "message.debug");
		FragmentsLogging.d(TAG, "message.debug", null);
		verify(mockLogger, times(1)).d(TAG, "message.debug", null);
	}

	@Test
	public void testI() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.i(TAG, "message.info");
		verify(mockLogger, times(1)).i(TAG, "message.info");
		FragmentsLogging.i(TAG, "message.info", null);
		verify(mockLogger, times(1)).i(TAG, "message.info", null);
	}

	@Test
	public void testW() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.w(TAG, "message.warn");
		verify(mockLogger, times(1)).w(TAG, "message.warn");
		FragmentsLogging.w(TAG, "message.warn", null);
		verify(mockLogger, times(1)).w(TAG, "message.warn", null);
		FragmentsLogging.w(TAG, (Throwable) null);
		verify(mockLogger, times(1)).w(TAG, (Throwable) null);
	}

	@Test
	public void testE() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.e(TAG, "message.error");
		verify(mockLogger, times(1)).e(TAG, "message.error");
		FragmentsLogging.e(TAG, "message.error", null);
		verify(mockLogger, times(1)).e(TAG, "message.error", null);
	}

	@Test
	public void testWTF() {
		final Logger mockLogger = mock(Logger.class);
		FragmentsLogging.setLogger(mockLogger);
		FragmentsLogging.wtf(TAG, "message.wtf");
		verify(mockLogger, times(1)).wtf(TAG, "message.wtf");
		FragmentsLogging.wtf(TAG, "message.wtf", null);
		verify(mockLogger, times(1)).wtf(TAG, "message.wtf", null);
		FragmentsLogging.wtf(TAG, (Throwable) null);
		verify(mockLogger, times(1)).wtf(TAG, (Throwable) null);
	}
}
