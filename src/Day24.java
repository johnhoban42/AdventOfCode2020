package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Day24 {
	
	// Iterate one state cycle in the same style as Day 11
	private static char[][] iterate(char[][] lobby){
		
		// Find how many occupied seats are adjacent to each cell
		int[][] adj = new int[lobby.length][lobby.length];
		for(int x = 0; x < lobby.length; x++) {
			for(int y = 0; y < lobby.length; y++) {
				if(lobby[x][y] == '#') {
					adj[x][y+1]++;		// NW
					adj[x+1][y+1]++;	// NE
					adj[x+1][y]++;		// E
					adj[x][y-1]++;		// SE
					adj[x-1][y-1]++;	// SW
					adj[x-1][y]++;		// W
				}
			}
		}
		
		// Build new seat layout from adjacency matrix
		char[][] next = new char[lobby.length][lobby.length];
		for(int x = 0; x < lobby.length; x++) {
			for(int y = 0; y < lobby.length; y++) {
				if(lobby[x][y] == '.') {
					next[x][y] = (adj[x][y] == 2) ? '#' : '.';
				}else if(lobby[x][y] == '#') {
					next[x][y] = (adj[x][y] == 0 || adj[x][y] > 2) ? '.' : '#';
				}
			}
		}
		return next;
	}

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day24.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		// Map the hex grid to (x,y) coordinates and keep track of which tiles have been
		// flipped an odd number of times
		HashSet<List<Integer>> black = new HashSet<List<Integer>>();
		for(String s : src) {
			List<Integer> tile = new ArrayList<Integer>(List.of(0,0)); // (0,0)
			int i = 0;
			while(i < s.length()) {
				int x = tile.get(0);
				int y = tile.get(1);
				if(s.charAt(i) == 'e') {
					tile.set(0, ++x);
					i++;
				}else if(s.charAt(i) == 'w') {
					tile.set(0, --x);
					i++;
				}else if(s.substring(i, i+2).equals("nw")){
					tile.set(1, ++y);
					i += 2;
				}else if(s.substring(i, i+2).equals("ne")) {
					tile.set(0, ++x);
					tile.set(1, ++y);
					i += 2;
				}else if(s.substring(i, i+2).equals("se")) {
					tile.set(1, --y);
					i += 2;
				}else if(s.substring(i, i+2).equals("sw")) {
					tile.set(0, --x);
					tile.set(1, --y);
					i += 2;
				}
			}
			if(black.contains(tile)) {
				black.remove(tile);
			}else {
				black.add(tile);
			}
		}
		System.out.println(black.size());
		
		/* PART 2 */
		// Lots of game of life puzzles this year...
		// Center the flipped tiles on a 200x200 (x,y) grid, then iterate.
		char[][] lobby = new char[200][200];
		for(int x = 0; x < 200; x++) {
			for(int y = 0; y < 200; y++) {
				lobby[x][y] = '.';
			}
		}
		for(List<Integer> tile : black) {
			int x = tile.get(0) + 100;
			int y = tile.get(1) + 100;
			lobby[x][y] = '#';
		}
		for(int i = 0; i < 100; i++) {
			lobby = iterate(lobby);
		}
		int count = 0;
		for(char[] r : lobby) {
			for(char c : r) {
				count = (c == '#') ? count+1 : count;
			}
		}
		System.out.println(count);
		
	}
	
}
