package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day08 {
	
	// Returns accumulator value if the program terminates normally, or 
	// if an infinite loop is detected.
	private static int run(String[][] commands) throws IllegalArgumentException {
		int accumulator = 0;
		int line = 0;
		boolean[] visited = new boolean[commands.length];
		while(line < commands.length && !visited[line]) {
			visited[line] = true;
			if(commands[line][0].equals("acc")) {
				accumulator += Integer.parseInt(commands[line][1]);
				line++;
			}else if(commands[line][0].equals("jmp")) {
				line += Integer.parseInt(commands[line][1]);
			}else {
				line++;
			}
		}
		if(line >= commands.length) {
			return accumulator;
		}
		throw new IllegalArgumentException();
	}

	public static void main(String[] args) {
		
		String[][] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day08.txt"))
					.map(s -> s.split(" "))
					.toArray(String[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// Brute force - change one instruction at a time and re-run the code
		int result = 0;
		for(String[] line : src) {
			if(line[0].equals("jmp")) {
				line[0] = "nop";
				try {
					result = run(src);
					break;
				}catch(IllegalArgumentException e) {
					line[0] = "jmp";
				}
			}else if(line[0].equals("nop")) {
				line[0] = "jmp";
				try {
					result = run(src);
					break;
				}catch(IllegalArgumentException e) {
					line[0] = "nop";
				}
			}
		}
		
		System.out.println(result);
		
	}
	
}
