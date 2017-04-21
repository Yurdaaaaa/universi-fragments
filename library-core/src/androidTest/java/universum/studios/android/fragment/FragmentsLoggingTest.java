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

import universum.studios.android.logging.Logger;
import universum.studios.android.logging.SimpleLogger;
import universum.studios.android.test.BaseInstrumentedTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author Martin Albedinsky
 */
@RunWith(AndroidJUnit4.class)
public final class FragmentsLoggingTest extends BaseInstrumentedTest {

	@SuppressWarnings("unused")
	private static final String TAG = "FragmentsLoggingTest";

	private Logger mMockLogger;

	@Override
	public void beforeTest() throws Exception {
		super.beforeTest();
		this.mMockLogger = mock(Logger.class);
	}

	@Override
	public void afterTest() throws Exception {
		super.afterTest();
		// Ensure that the logging class has default logger.
		FragmentsLogging.setLogger(null);
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
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.v(TAG, "");
		FragmentsLogging.v(TAG, "", null);
	}

	@Test
	public void testD() {
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.d(TAG, "message.debug");
		verify(mMockLogger, times(1)).d(TAG, "message.debug");
		FragmentsLogging.d(TAG, "message.debug", null);
		verify(mMockLogger, times(1)).d(TAG, "message.debug", null);
	}

	@Test
	public void testI() {
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.i(TAG, "message.info");
		verify(mMockLogger, times(1)).i(TAG, "message.info");
		FragmentsLogging.i(TAG, "message.info", null);
		verify(mMockLogger, times(1)).i(TAG, "message.info", null);
	}

	@Test
	public void testW() {
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.w(TAG, "message.warn");
		verify(mMockLogger, times(1)).w(TAG, "message.warn");
		FragmentsLogging.w(TAG, "message.warn", null);
		verify(mMockLogger, times(1)).w(TAG, "message.warn", null);
		FragmentsLogging.w(TAG, (Throwable) null);
		verify(mMockLogger, times(1)).w(TAG, (Throwable) null);
	}

	@Test
	public void testE() {
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.e(TAG, "message.error");
		verify(mMockLogger, times(1)).e(TAG, "message.error");
		FragmentsLogging.e(TAG, "message.error", null);
		verify(mMockLogger, times(1)).e(TAG, "message.error", null);
	}

	@Test
	public void testWTF() {
		FragmentsLogging.setLogger(mMockLogger);
		FragmentsLogging.wtf(TAG, "message.wtf");
		verify(mMockLogger, times(1)).wtf(TAG, "message.wtf");
		FragmentsLogging.wtf(TAG, "message.wtf", null);
		verify(mMockLogger, times(1)).wtf(TAG, "message.wtf", null);
		FragmentsLogging.wtf(TAG, (Throwable) null);
		verify(mMockLogger, times(1)).wtf(TAG, (Throwable) null);
	}
}
