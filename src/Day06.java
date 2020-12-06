package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day06 {
	
	// set = {'a', 'b', ..., 'z'}
	private static Set<Character> generateAlphabetSet(){
		Set<Character> set = Stream.iterate('a', n -> (char)(n+1))
									.limit(26)
									.collect(Collectors.toSet());
		return set;						
	}

	public static void main(String[] args) {
		
		String src = null;
		try {
			src = Files.lines(Paths.get("inputs/day06.txt"))
					.collect(Collectors.joining("\n"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] groups = src.split("\n\n");
		int total = 0;
		
		for(String g : groups) {
			
			// For each group, the set of characters that appears in every response is equal to
			// the intersection of the sets of unique characters from each group member.
			String[] responses = g.split("\n");
			Set<Character> everyone = generateAlphabetSet();
			
			for(String res : responses) {
				Set<Character> uniqueChars = new HashSet<Character>();
				for(int i = 0; i < res.length(); i++) {
					if(Character.isAlphabetic(res.charAt(i))) {
						uniqueChars.add(res.charAt(i));
					}
				}
				everyone.retainAll(uniqueChars); // intersection
			}
			total += everyone.size();
		}
		
		System.out.println(total);
		
	}
}
