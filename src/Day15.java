package src;

import java.util.HashMap;

public class Day15 {

	public static void main(String[] args) {
		
		// Input : [0, 14, 6, 20, 1, 4]
		final int[] src = new int[] {0,14,6,20,1};
		
		// At each turn, maintain a map of <value, lastIndexOf>
		// If the last value (prev) has been seen, return its age and update its last index
		// If the value has not been seen, return 0 and create a new map entry for it
		HashMap<Integer, Integer> game = new HashMap<Integer, Integer>();
		for(int i = 0; i < src.length; i++) {
			game.put(src[i], i);
		}
		int prev = 4;
		for(int i = src.length; i < 30000000; i++) {
			Integer val = game.get(prev);
			int next = (val == null) ? 0 : i - val;
			game.put(prev, i);
			if(i == 2019 || i == 29999999) {
				System.out.println(prev);
			}
			prev = next;
		}

	}
	
}
