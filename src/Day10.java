package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 {
	
	// Recursively create a decision tree of possible chains, counting a chain when it
	// reaches the end of the list. Store sub-results dynamically in the cache to save time.
	private static long findChains(List<Integer> chain, int start, long[] cache) {
		// Reached the end, count this chain
		if(start == chain.size()-1) {
			return 1;
		}
		// Check the cache
		if(cache[start] > 0) {
			return cache[start];
		}
		// Branch and build more chains
		long count = 0;
		int current = chain.get(start);
		for(int i = 1; i <= 3; i++) {
			if(chain.contains(current + i)) {
				count += findChains(chain, chain.indexOf(current + i), cache);
			}
		}
		cache[start] = count;
		return count;
	}

	public static void main(String[] args) {
		
		List<Integer> src = null;
		try {
			src = Files.lines(Paths.get("inputs/day10.txt"))
					.map(s -> Integer.parseInt(s))
					.collect(Collectors.toList());
		}catch(IOException e) {
			e.printStackTrace();
		}

		Collections.sort(src);
		
		/* PART 1 */
		int diff1 = (src.get(0) == 1) ? 1 : 0;
		int diff3 = (src.get(0) == 3) ? 2 : 1; // includes final adapter to device connection
		for(int i = 0; i < src.size()-1; i++) {
			int d = src.get(i+1) - src.get(i);
			if(d == 1) {
				diff1++;
			}else if(d == 3) {
				diff3++;
			}
		}
		System.out.println(diff1 * diff3);
		
		/* PART 2 */
		// Find all chains from all starting points (i.e., 1, 2, 3)
		long chains = 0;
		long[] cache = new long[src.size()];
		for(int start = 1; start <= 3; start++) {
			if(src.contains(start)) {
				chains += findChains(src, src.indexOf(start), cache);
			}
		}
		System.out.println(chains);
		
	}
	
}
