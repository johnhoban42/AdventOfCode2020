package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day11 {
	
	private static boolean areValidCoordinates(char[][] seats, int row, int col) {
		return row >= 0 && row < seats.length && col >= 0 && col < seats[0].length;
	}
	
	private static char[][] iteratePart1(char[][] seats){
		
		// Find how many occupied seats are adjacent to each cell
		int[][] adj = new int[seats.length][seats[0].length];
		for(int r = 0; r < seats.length; r++) {
			for(int c = 0; c < seats[0].length; c++) {
				if(seats[r][c] == '#') {
					for(int row = r-1; row <= r+1; row++) {
						for(int col = c-1; col <= c+1; col++) {
							if(!(row == r && col == c) && areValidCoordinates(seats, row, col)) {
								adj[row][col]++;
							}
						}
					}
				}
			}
		}
		
		// Build new seat layout from adjacency matrix
		char[][] next = new char[seats.length][seats[0].length];
		for(int r = 0; r < seats.length; r++) {
			for(int c = 0; c < seats[0].length; c++) {
				if(seats[r][c] == 'L') {
					next[r][c] = (adj[r][c] == 0) ? '#' : 'L';
				}else if(seats[r][c] == '#') {
					next[r][c] = (adj[r][c] >= 4) ? 'L' : '#';
				}else {
					next[r][c] = '.';
				}
			}
		}
		return next;
	}
	
	private static char[][] iteratePart2(char[][] seats) {
		// Find how many occupied seats are visible from every seat
		int[][] vis = new int[seats.length][seats[0].length];
		for(int r = 0; r < seats.length; r++) {
			for(int c = 0; c < seats[0].length; c++) {
				// If this seat is full, update all seats that can see it
				if(seats[r][c] == '#') {
					for(int dx = -1; dx <= 1; dx++) {
						for(int dy = -1; dy <= 1; dy++) {
							if(dx == 0 && dy == 0) {
								continue;
							}
							int x = c+dx;
							int y = r+dy;
							while(areValidCoordinates(seats, y, x)) {
								if(seats[y][x] == '#' || seats[y][x] == 'L') {
									vis[y][x]++;
									break;
								}
								x += dx;
								y += dy;
							}
						}
					}
				}
			}
		}
		
		// Build new seat layout from adjacency matrix
		char[][] next = new char[seats.length][seats[0].length];
		for(int r = 0; r < seats.length; r++) {
			for(int c = 0; c < seats[0].length; c++) {
				if(seats[r][c] == 'L') {
					next[r][c] = (vis[r][c] == 0) ? '#' : 'L';
				}else if(seats[r][c] == '#') {
					next[r][c] = (vis[r][c] >= 5) ? 'L' : '#';
				}else {
					next[r][c] = '.';
				}
			}
		}
		return next;
	}
	
	private static boolean sameState(char[][] curr, char[][] next) {
		if(curr == null || next == null) {return false;}
		for(int r = 0; r < curr.length; r++) {
			for(int c = 0; c < curr[0].length; c++) {
				if(curr[r][c] != next[r][c]) {
					return false;
				}
			}
		}
		return true;
	}
	
	private static char[][] copyMatrix(char[][] m) {
		char[][] n = new char[m.length][m[0].length];
		for(int i = 0; i < m.length; i++) {
			for(int j = 0; j < m[0].length; j++) {
				n[i][j] = m[i][j];
			}
		}
		return n;
	}

	public static void main(String[] args) {
		
		char[][] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day11.txt"))
					.map(s -> s.toCharArray())
					.toArray(char[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		char[][] curr = copyMatrix(src);
		char[][] next = copyMatrix(src);
		do {
			curr = copyMatrix(next);
			next = iteratePart1(curr);
		}while(!sameState(curr, next));
		int seats = 0;
		for(char[] r : next) {
			for(char c : r) {
				seats = (c == '#') ? seats+1 : seats;
			}
		}
		System.out.println(seats);
		
		/* PART 2 */
		curr = copyMatrix(src);
		next = copyMatrix(src);
		do {
			curr = copyMatrix(next);
			next = iteratePart2(curr);
		}while(!sameState(curr, next));
		seats = 0;
		for(char[] r : next) {
			for(char c : r) {
				seats = (c == '#') ? seats+1 : seats;
			}
		}
		System.out.println(seats);
		
	}
	
}
