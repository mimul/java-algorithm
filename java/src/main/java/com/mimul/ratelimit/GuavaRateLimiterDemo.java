package com.mimul.ratelimit;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GuavaRateLimiterDemo {
	private static final int maxInt = 10;

	public static void main(String[] args) {
		testNoRateLimiter();
		testWithRateLimiter();
	}

	public static void testNoRateLimiter() {
		long start = System.currentTimeMillis();
		for (int i = 0; i < maxInt; i++) {
			log.info("call execute. i={}", i);
		}
		long end = System.currentTimeMillis();
		log.info("elapsedTime={}", (end - start));
	}

	public static void testWithRateLimiter() {
		long start = System.currentTimeMillis();
		RateLimiter limiter = RateLimiter.create(10.0); // 초당 10개 미만

		for (int i = 0; i < maxInt; i++) {
			limiter.acquire();
			System.out.println("call execute. i=" + i);
		}
		long end = System.currentTimeMillis();
		log.info("elapsedTime={}", (end - start));
	}
}
