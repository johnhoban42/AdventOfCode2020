package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day13 {

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day13.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		int time = Integer.parseInt(src[0]);
		int[] id = Arrays.stream(src[1].split("[,x]+"))
				.mapToInt(Integer::parseInt)
				.toArray();
		boolean done = false;
		int t = time; int ans = 0;
		while(!done) {
			for(int i : id) {
				if(t % i == 0) {
					ans = (t - time) * i;
					done = true;
					break;
				}
			}
			t++;
		}
		System.out.println(ans);
		
		/* PART 2 */
		String[] bus = src[1].split(",");
		long[] T = new long[bus.length]; // T[k] -> earliest departure time for first i buses
		long p = Integer.parseInt(bus[0]); // product of all primes encountered so far
		for(int i = 1; i < T.length; i++) {
			if(bus[i].equals("x")) {
				T[i] = T[i-1];
			}else {
				// Loop through "cycles" of the first i-1 buses, each of length p,
				// assuming that bus i always leaves i minutes after the first bus. Continue
				// until bus i leaves at a valid time.
				int b = Integer.parseInt(bus[i]);
				T[i] = T[i-1];
				while((T[i] + i) % b != 0) {
					T[i] += p;
				}
				p *= b;
			}
		}
		System.out.println(T[T.length-1]);
		
	}
	
}
