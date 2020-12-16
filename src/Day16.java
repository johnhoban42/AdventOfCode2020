package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Day16 {

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day16.txt"))
					.collect(Collectors.joining("\n"))
					.split("(\n\n[a-z ]+:\n)");
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String fieldsSrc = src[0];
		String ticketSrc = src[1];
		String nearbySrc = src[2];
		
		/* PART 1 */
		// The valid field ranges overlap to form one uninterrupted range; check all ticket fields against this range
		// Also, remove invalid tickets in preparation for Part 2
		final int lo = 25;
		final int hi = 968;
		int error = 0;
		ArrayList<String> nearby = new ArrayList<String>(List.of(nearbySrc.split("\n")));
		ArrayList<int[]> nearbyData = new ArrayList<int[]>();
		for(String s : nearby) {
			int[] d = Arrays.stream(s.split(",")).mapToInt(Integer::parseInt).toArray();
			boolean keep = true;
			for(int i : d) {
				if(i < lo || i > hi) {
					keep = false;
					error += i;
				}
			}
			if(keep) {
				nearbyData.add(d);
			}
		}
		System.out.println(error);
		
		/* PART 2 */
		// Build N field range-checkers
		String[] fields = fieldsSrc.split("\n");
		final int N = fields.length;
		ArrayList<Predicate<Integer>> valid = new ArrayList<Predicate<Integer>>();
		for(String f : fields) {
			String[] bounds = f.split("([a-z :\\-]+)");
			valid.add(k -> (Integer.parseInt(bounds[1]) <= k && k <= Integer.parseInt(bounds[2])) ||
						   (Integer.parseInt(bounds[3]) <= k && k <= Integer.parseInt(bounds[4])));
		}
		
		// For each entry in each nearby ticket, check it against every field boundary
		// b[i][j] = true => field i is legal at index j
		boolean[][] b = new boolean[N][N];
		for(int i = 0; i < N; i++) {
			for(int j = 0; j < N; j++) {
				b[i][j] = true;
			}
		}
		for(int[] ticket : nearbyData) {
			for(int i = 0; i < N; i++) {
				for(int j = 0; j < N; j++) {
					b[i][j] = b[i][j] && valid.get(i).test(ticket[j]);
				}
			}
		}
		
		// Find the optimal assignment
		int[] indices = new int[N]; // indices[k] = valid index for field k
		for(int i = 0; i < N; i++) {
			indices[i] = -1;
		}
		ArrayList<Integer> assigned = new ArrayList<Integer>(); // list of indices taken by fields
		int i = 0;
		while(assigned.size() < N) {
			if(indices[i] == -1) {
				int choices = 0;
				int indexOfTrue = -1;
				for(int j = 0; j < N; j++) {
					// If index j works for field i and field i hasn't been assigned, count it as a choice
					if(b[i][j] && !assigned.contains(j)) {
						choices++;
						indexOfTrue = j;
					}
				}
				// If there is only one choice for this field, assign it to its index
				if(choices == 1) {
					indices[i] = indexOfTrue;
					assigned.add(indexOfTrue);
				}
			}
			i = (i + 1) % N;
		}
		
		// Find product of departure fields on your ticket
		// Departure fields are the first 6 fields on the ticket (0-5)
		long[] ticket = Arrays.stream(ticketSrc.split(",")).mapToLong(Long::parseLong).toArray();
		long ans = ticket[indices[0]] * ticket[indices[1]] * ticket[indices[2]] *
					ticket[indices[3]] * ticket[indices[4]] * ticket[indices[5]];
		System.out.println(ans);

	}
	
}
