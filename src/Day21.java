package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day21 {

	public static void main(String[] args) {
		
		String[][] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day21.txt"))
					.map(s -> s.split("( \\(contains )|\\)"))
					.toArray(String[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		// An ingredient might contain an allergen if it appears in every list that has a certain allergen
		HashMap<String,Integer> ingredients = new HashMap<String,Integer>();
		HashMap<String, Set<String>> allergens = new HashMap<String, Set<String>>();
		for(String[] line : src) {
			List<String> ing = Arrays.asList(line[0].split(" "));
			List<String> all = Arrays.asList(line[1].split(", "));
			for(String i : ing) {
				Integer ingCount = ingredients.get(i);
				ingredients.put(i, (ingCount == null) ? 1 : ingCount+1);
			}
			for(String a : all) {
				if(allergens.get(a) == null) {
					Set<String> s = new HashSet<String>();
					s.addAll(ing);
					allergens.put(a, s);
				}else {
					allergens.get(a).retainAll(ing);
				}
			}
		}
		
		Set<String> badFoods = new HashSet<String>();
		for(Set<String> s : allergens.values()) {
			badFoods.addAll(s);
		}
		int ans = ingredients.keySet().stream()
										.filter(str -> !badFoods.contains(str))
										.mapToInt(str -> ingredients.get(str))
										.sum();
		System.out.println(ans);
		
		/* PART 2 */
		// Determine which foods contain which allergens by finding the optimal 1:1 assignment
		// Based on the algorithm from Day 16 Part 2
		String[] ing = badFoods.toArray(String[]::new);
		String[] all = allergens.keySet().stream().sorted().toArray(String[]::new);
		
		// inFood[i][j] -> food i contains allergen j
		boolean[][] inFood = new boolean[ing.length][all.length];
		for(int i = 0; i < ing.length; i++) {
			for(int j = 0; j < all.length; j++) {
				inFood[i][j] = allergens.get(all[j]).contains(ing[i]);
			}
		}
		
		// Find the optimal assignment
		final int N = all.length;
		int[] indices = new int[N]; // indices[k] = valid index for food k
		for(int i = 0; i < N; i++) {
			indices[i] = -1;
		}
		ArrayList<Integer> assigned = new ArrayList<Integer>(); // list of indices taken by foods
		int i = 0;
		while(assigned.size() < N) {
			if(indices[i] == -1) {
				int choices = 0;
				int indexOfTrue = -1;
				for(int j = 0; j < N; j++) {
					// If food i contains allergen j and allergen j hasn't been assigned, count it as a choice
					if(inFood[i][j] && !assigned.contains(j)) {
						choices++;
						indexOfTrue = j;
					}
				}
				// If there is only one choice for this food, assign the allergen to it
				if(choices == 1) {
					indices[i] = indexOfTrue;
					assigned.add(indexOfTrue);
				}
			}
			i = (i + 1) % N;
		}
		
		// Compile the canonical dangerous ingredient list
		String CDIL = "";
		List<Integer> iList = Arrays.stream(indices).boxed().collect(Collectors.toList());
		for(int a = 0; a < all.length; a++) {
			CDIL += (ing[iList.indexOf(a)] + ",");
		}
		System.out.println(CDIL.substring(0, CDIL.length()-1));
		
	}
	
}
