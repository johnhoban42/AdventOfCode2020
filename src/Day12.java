package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day12 {
	
	// c : coordinates, m : rotation matrix, p : # of rotations
	private static int[] rotate(int[] c, int[][] m, int p) {
		for(int i = 0; i < p; i++) {
			int[] d = new int[2];
			d[0] = m[0][0]*c[0] + m[0][1]*c[1];
			d[1] = m[1][0]*c[0] + m[1][1]*c[1];
			c = d;
		}
		return c;
	}

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day12.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		int x = 0; int y = 0;
		int[] dir = new int[] {1,0}; // dx, dy
		final int[][] L = new int[][] {new int[] {0,-1}, new int[] {1,0}}; // left 90deg rotation matrix
		final int[][] R = new int[][] {new int[] {0,1}, new int[] {-1,0}}; // right 90deg rotation matrix
		for(String move : src) {
			char c = move.charAt(0);
			int val = Integer.parseInt(move.substring(1));
			switch(c) {
				case 'N':
					y += val;
					break;
				case 'S':
					y -= val;
					break;
				case 'E':
					x += val;
					break;
				case 'W':
					x -= val;
					break;
				case 'L':
					dir = rotate(dir, L, val/90);
					break;
				case 'R':
					dir = rotate(dir, R, val/90);
					break;
				case 'F':
					x += val * dir[0];
					y += val * dir[1];
					break;
			}
		}
		System.out.println(Math.abs(x) + Math.abs(y));
		
		/* PART 2 */
		int[] w = new int[] {10,1}; // waypoint
		int sx = 0; int sy = 0; // ship
		for(String move : src) {
			char c = move.charAt(0);
			int val = Integer.parseInt(move.substring(1));
			switch(c) {
				case 'N':
					w[1] += val;
					break;
				case 'S':
					w[1] -= val;
					break;
				case 'E':
					w[0] += val;
					break;
				case 'W':
					w[0] -= val;
					break;
				case 'L':
					w = rotate(w, L, val/90);
					break;
				case 'R':
					w = rotate(w, R, val/90);
					break;
				case 'F':
					sx += val * w[0];
					sy += val * w[1];
					break;
			}
		}
		System.out.println(Math.abs(sx) + Math.abs(sy));
		
	}
	
}
