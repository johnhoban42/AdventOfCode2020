package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day02 {

	public static void main(String[] args) {
		
		List<String[]> tokens = null;
		try {
			tokens = Files.lines(Paths.get("inputs/day02.txt"))
					.map(s -> s.split(" "))
					.collect(Collectors.toList());
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		for(String[] line : tokens) {
			String[] bounds = line[0].split("-");
			int lower = Integer.parseInt(bounds[0]);
			int upper = Integer.parseInt(bounds[1]);
			char c = line[1].charAt(0);
			if(c == line[2].charAt(lower-1) ^ c == line[2].charAt(upper-1)) {
				count++;
			}
		}
		
		System.out.println(count);
		
	}
	
}
