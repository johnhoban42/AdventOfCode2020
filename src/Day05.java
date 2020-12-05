package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Day05 {

	public static void main(String[] args) {
		
		char[][] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day05.txt"))
					.map(s -> s.toCharArray())
					.toArray(char[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		int[] id_list = new int[src.length];
		for(int i = 0; i < src.length; i++) {
			char[] pass = src[i];
			int row = 0;
			for(int c = 0; c < 7; c++) {
				if(pass[c] == 'B') {
					row += (int)Math.pow(2, 6-c);
				}
			}
			int col = 0;
			for(int c = 0; c < 3; c++) {
				if(pass[c+7] == 'R') {
					col += (int)Math.pow(2, 2-c);
				}
			}
			int id = 8*row + col;
			id_list[i] = id;
		}
		
		Arrays.sort(id_list);
		int i = 0;
		while(id_list[i+1] == id_list[i] + 1) {
			i++;
		}
		System.out.println(id_list[i] + 1);
		
	}
	
}
