package com.mimul.ratelimit;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class SlidingWindowTest {

	@Test
	public void testSlidingWindow() throws Exception {
		RateLimiter slidingWindow = new SlidingWindow(10, 1000 * 1000);
		for (int i = 0; i < 20; i++) {
			boolean result = slidingWindow.allow();
			Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
			if(!result)
				log.info("{}, result={}", i, result);
		}
	}
}
