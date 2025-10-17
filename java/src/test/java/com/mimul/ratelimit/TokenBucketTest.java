package com.mimul.ratelimit;

import com.google.common.util.concurrent.Uninterruptibles;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TokenBucketTest extends TestCase {

	@Test
	public void testTokenBucket() throws Exception {
		RateLimiter tokenBucket = new TokenBucket(10);
		Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
		for (int i = 0; i < 20; i++) {
			boolean result = tokenBucket.allow();
			Uninterruptibles.sleepUninterruptibly(10, TimeUnit.MILLISECONDS);
			if(!result)
				log.info("{}, result={}", i, result);
		}
	}
}
