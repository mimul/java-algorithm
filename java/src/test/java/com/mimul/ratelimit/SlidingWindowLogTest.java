package com.mimul.ratelimit;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SlidingWindowLogTest {
	@Test
	public void testSlidingWindowLog() throws Exception {
		RateLimiter slidingWindowLog = new SlidingWindowLog(10);
		for (int i = 0; i < 20; i++) {
			boolean result = slidingWindowLog.allow();
			Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
			if (!result)
				log.info("{}, result={}", i, result);
		}
	}
}
