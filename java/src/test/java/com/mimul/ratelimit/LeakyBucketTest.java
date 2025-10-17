package com.mimul.ratelimit;

import com.google.common.util.concurrent.Uninterruptibles;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class LeakyBucketTest {
	@Test
	public void testLeakyBucket() throws Exception {
		RateLimiter leakyBucket = new LeakyBucket(10);
		for (int i = 0; i < 20; i++) {
			boolean result = leakyBucket.allow();
			Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
			if(!result)
				log.info("{}, result={}", i, result);
		}
	}
}
