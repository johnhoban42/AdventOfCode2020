package src;

import java.util.*;

public class Day23 {
	
	private static void game(HashMap<Long,Long> cups, int moves, long start){

		final int N = cups.size();
		long curr = start;
		for(int m = 0; m < moves; m++) {

			// Remove 3 cups
			LinkedList<Long> toRemove = new LinkedList<Long>();
			toRemove.add(cups.get(curr));
			toRemove.add(cups.get(toRemove.get(0)));
			toRemove.add(cups.get(toRemove.get(1)));
			
			// Find destination
			long dest = (curr - 1 == 0) ? N : curr - 1;
			while(toRemove.contains(dest)) {
				dest = (dest - 1 == 0) ? N : dest - 1;
			}
			
			// Replace cups at the destination
			cups.put(curr, cups.get(toRemove.get(2)));
			cups.put(toRemove.get(2), cups.get(dest));
			cups.put(dest, toRemove.get(0));
			
			// Iterate
			curr = cups.get(curr);
			
		}

	}

	public static void main(String[] args) {
		
		// Input = 589174263
		// Create map of cups: (cup number, next cup number). Forms a circular linked list
		LinkedList<Long> src = new LinkedList<Long>(List.of(5L,8L,9L,1L,7L,4L,2L,6L,3L));
		final int SRC_LEN = src.size();
		HashMap<Long,Long> cups = new HashMap<Long,Long>();
		for(int i = 0; i < SRC_LEN; i++) {
			cups.put(src.get(i), src.get((i+1) % SRC_LEN));
		}

		/* PART 1 */
		game(cups, 100, src.get(0));
		int label1 = 0;
		long next = cups.get(1L);
		for(int i = 0; i < 8; i++) {
			label1 *= 10;
			label1 += next;
			next = cups.get(next);
		}
		System.out.println(label1);
		
		/* PART 2 */
		cups = new HashMap<Long,Long>();
		for(int i = 0; i < SRC_LEN-1; i++) {
			cups.put(src.get(i), src.get((i+1) % SRC_LEN));
		}
		cups.put(src.get(SRC_LEN-1), (long)(SRC_LEN+1));
		for(long i = SRC_LEN+1; i < 1000000; i++) {
			cups.put(i, i+1);
		}
		cups.put(1000000L, src.get(0)); // completes the circular LL
		
		game(cups, 10000000, src.get(0));
		long label2 = cups.get(1L);
		label2 *= cups.get(label2);
		System.out.println(label2);
		
	}
	
}
