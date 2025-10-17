package com.mimul.bloomfilter;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.PrimitiveSink;
import java.util.HashSet;
import java.util.Set;

public class GuavaBloomFilterDemo {
	private static final int maxInt = 500;

	public static void main(String[] args) {

		final Funnel<Integer> funnel = (Integer x, PrimitiveSink into) -> into.putInt(x);
		final BloomFilter<Integer> bloomFilter = BloomFilter.create(funnel, maxInt);

		// 0에서 998까지의 짝수를 Bloom Filter 에 추가
		for (int i = 0; i < maxInt * 2; i += 2) {
			bloomFilter.put(i);
		}

		// 998까지 모든 짝수가 positive 임
		for (int i = 0; i < maxInt * 2; i += 2) {
			if (!bloomFilter.mightContain(i)) {
				System.out.printf("%d should be contained.\n", i);
			}
		}

		// 1에서 999까지의 홀수로 false positive가 되는 값 추출
		final Set<Integer> falsePositives = new HashSet<>();
		for (int i = 1; i < maxInt * 2; i += 2) {
			if (bloomFilter.mightContain(i)) {
				falsePositives.add(i);
			}
		}

		System.out.printf("예상 false positive 확률: %f\n", bloomFilter.expectedFpp());
		System.out.printf("실제 false positive 확률: %f\n", (double) falsePositives.size() / maxInt);
		System.out.printf("false-positive 값: %s", falsePositives);
	}
}
