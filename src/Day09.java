package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Day09 {

	public static void main(String[] args) {

		long[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day09.txt"))
					.mapToLong(s -> Long.parseLong(s))
					.toArray();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		// Build initial 25x25 tableau from preamble, organized as an upper triangular
		// addition table with entries not in the upper triangle being ignored to save time
		long[][] tableau = new long[25][25];
		for(int i = 0; i < 25; i++) {
			for(int j = i+1; j < 25; j++) {
				tableau[i][j] = src[i] + src[j];
			}
		}
		
		// Now that we've constructed the tableau, whenever the last element in the
		// current preamble gets replaced, only the entries in that element's row and column 
		// need to be updated, instead of re-computing the entire set of additive combinations.
		// Elements are updated by the difference of the new and old entries.
		int i;
		for(i = 25; i < src.length; i++) {
			if(i > 25) {
				// Update tableau ((i-1)%25 is the new element's index in the tableau)
				int idx = (i-1)%25;
				for(int row = 0; row < idx; row++) {
					tableau[row][idx] += (src[i-1] - src[i-26]);
				}
				for(int col = idx + 1; col < 25; col++) {
					tableau[idx][col] += (src[i-1] - src[i-26]);
				}
			}
			// Search for value
			boolean valid = false;
			for(int row = 0; row < 25; row++) {
				for(int col = row+1; col < 25; col++) {
					if(src[i] == tableau[row][col]) {
						valid = true;
					}
				}
			}
			if(!valid) {
				break;
			}
		}
		System.out.println(src[i]);
		
		/* PART 2 */
		// Start with the first 2 elements of the list in a set S.
		// If sum(S) < src[i], add next element to S.
		// If sum(S) > src[i], remove last element from S.
		// Continue until sum(S) = src[i].
		ArrayList<Long> s = new ArrayList<Long>();
		s.add(src[0]);
		s.add(src[1]);
		int maxIndex = 1;
		long sum = src[0] + src[1];
		do {
			if(sum < src[i]) {
				maxIndex++;
				s.add(src[maxIndex]);
				sum += src[maxIndex];
			}else if(sum > src[i]) {
				sum -= s.remove(0);
			}
		}while(sum != src[i]);
		
		long min = s.stream().mapToLong(l -> l).max().getAsLong();
		long max = s.stream().mapToLong(l -> l).min().getAsLong();
		System.out.println(min + max);
		
	}
	
}
