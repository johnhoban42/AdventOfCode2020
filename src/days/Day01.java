package days;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day01 {

	public static void main(String[] args) {
		
		int[] arr = null;
		try {
			arr = Files.lines(Paths.get("inputs/day01.txt"))
					.mapToInt((String s) -> Integer.valueOf(s))
					.toArray();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < arr.length; i++) {
			for(int j = i+1; j < arr.length; j++) {
				for(int k = j+1; k < arr.length; k++) {
					if(arr[i] + arr[j] + arr[k] == 2020) {
						long ans = arr[i] * arr[j] * arr[k];
						System.out.println(ans);
						return;
					}
				}
			}
		}
		
	}
	
}
