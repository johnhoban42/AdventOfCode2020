package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day03 {
	
	public static char[][] forest = null;
	
	public static int findTrees(int dx, int dy) {
		int x = 0;
		int y = 0;
		int trees = 0;
		while(y < forest.length) {
			if(forest[y][x] == '#') {
				trees++;
			}
			x = (x + dx) % forest[0].length;
			y += dy;
		}
		return trees;
	}
	
	public static void main(String[] args) {

		try {
			forest = Files.lines(Paths.get("inputs/day03.txt"))
					.map(s -> s.toCharArray())
					.toArray(char[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		long treeProduct = findTrees(1, 1);
		treeProduct *= findTrees(3, 1);
		treeProduct *= findTrees(5, 1);
		treeProduct *= findTrees(7, 1);
		treeProduct *= findTrees(1, 2);
		
		System.out.println(treeProduct);
		
	}
	
}
