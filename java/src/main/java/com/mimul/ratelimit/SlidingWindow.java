package com.mimul.ratelimit;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SlidingWindow extends RateLimiter {
  private final ConcurrentMap<Long, AtomicInteger> windows = new ConcurrentHashMap<>();
  private final int windowSizeInMs;

  protected SlidingWindow(int maxRequestPerSec, int windowSizeInMs) {
    super(maxRequestPerSec);
    this.windowSizeInMs = windowSizeInMs;
  }

  @Override
  boolean allow() {
    long now = System.currentTimeMillis();
    long curWindowKey = now / windowSizeInMs;
    windows.putIfAbsent(curWindowKey, new AtomicInteger(0));
    long preWindowKey = curWindowKey - 1000;
    AtomicInteger preCount = windows.get(preWindowKey);
    if (preCount == null) {
      return windows.get(curWindowKey).incrementAndGet() <= maxRequestPerSec;
    }
    double preWeight = 1 - (now - curWindowKey) / 1000.0;
    long count = (long) (preCount.get() * preWeight + windows.get(curWindowKey).incrementAndGet());
    return count <= maxRequestPerSec;
  }
}
