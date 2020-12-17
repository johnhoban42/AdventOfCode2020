package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day17 {
	
	static int N, h, v, X, Y, Z, W;
	
	private static boolean areValidCoordinates3D(int x, int y, int z) {
		return (0 <= x && x < X) && (0 <= y && y < Y) && (0 <= z && z < Z);
	}
	
	private static boolean areValidCoordinates4D(int x, int y, int z, int w) {
		return (0 <= x && x < X) && (0 <= y && y < Y) && (0 <= z && z < Z) && (0 <= w && w < W);
	}
	
	// Iterate one state cycle in the same style as Day 11
	private static char[][][] iterate3D(char[][][] state){
		
		int[][][] adj = new int[Z][Y][X];
		for(int z = 0; z < Z; z++) {
			for(int y = 0; y < Y; y++) {
				for(int x = 0; x < X; x++) {
					
					if(state[z][y][x] == '#') {
						
						for(int dz = -1; dz <= 1; dz++) {
							for(int dy = -1; dy <= 1; dy++) {
								for(int dx = -1; dx <= 1; dx++) {
									
									if(!(dz == 0 && dy == 0 && dx == 0) && areValidCoordinates3D(x+dx, y+dy, z+dz)) {
										adj[z+dz][y+dy][x+dx]++;
									}}}}}}}}
		
		char[][][] next = new char[Z][Y][X];
		for(int z = 0; z < Z; z++) {
			for(int y = 0; y < Y; y++) {
				for(int x = 0; x < X; x++) {
					if(state[z][y][x] == '.') {
						next[z][y][x] = (adj[z][y][x] == 3) ? '#' : '.';
					}else {
						next[z][y][x] = (adj[z][y][x] == 2 || adj[z][y][x] == 3) ? '#' : '.';
					}}}}
		return next;
		
	}
	
	private static char[][][][] iterate4D(char[][][][] state) {
		
		int[][][][] adj = new int[W][Z][Y][X];
		for(int w = 0; w < W; w++) {
			for(int z = 0; z < Z; z++) {
				for(int y = 0; y < Y; y++) {
					for(int x = 0; x < X; x++) {
						
						if(state[w][z][y][x] == '#') {
							
							for(int dw = -1; dw <= 1; dw++) {
								for(int dz = -1; dz <= 1; dz++) {
									for(int dy = -1; dy <= 1; dy++) {
										for(int dx = -1; dx <= 1; dx++) {
											
											if(!(dw == 0 && dz == 0 && dy == 0 && dx == 0) && areValidCoordinates4D(x+dx, y+dy, z+dz, w+dw)) {
												adj[w+dw][z+dz][y+dy][x+dx]++;
											}}}}}}}}}}
		
		char[][][][] next = new char[W][Z][Y][X];
		for(int w = 0; w < W; w++) {
			for(int z = 0; z < Z; z++) {
				for(int y = 0; y < Y; y++) {
					for(int x = 0; x < X; x++) {
						if(state[w][z][y][x] == '.') {
							next[w][z][y][x] = (adj[w][z][y][x] == 3) ? '#' : '.';
						}else {
							next[w][z][y][x] = (adj[w][z][y][x] == 2 || adj[w][z][y][x] == 3) ? '#' : '.';
						}}}}}
		return next;
		
	}

	public static void main(String[] args) {
		
		char[][] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day17.txt"))
					.map(s -> s.toCharArray())
					.toArray(char[][]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* CONSTANTS */
		N = 6; // number of cycles
		h = src[0].length; // size in X of initial state
		v = src.length; // size in Y of initial state
		X = 2*N + h; // size in X of total state
		Y = 2*N + v; // size in Y of total state
		Z = 2*N + 1; // size in Z of total state
		W = 2*N + 1; // size in W of total state
		
		/* PART 1 */
		// Construct the pocket dimension based on the size of the input and
		// the expected number of cycles by buffering the initial active cube state
		// with N inactive units on all sides in all dimensions
		char[][][] state = new char[Z][Y][X];
		for(int z = 0; z < Z; z++) {
			for(int y = 0; y < Y; y++) {
				for(int x = 0; x < X; x++) {
					state[z][y][x] = '.';
				}
			}
		}
		// Middle z-layer (initial state)
		for(int y = N; y < N+v; y++) {
			for(int x = N; x < N+h; x++) {
				state[Z/2][y][x] = src[y-N][x-N];
			}
		}
		
		// Iterate and count active cubes
		for(int i = 0; i < 6; i++) {
			state = iterate3D(state);
		}
		int active = 0;
		for(char[][] z : state) {
			for(char[] y : z) {
				for(char x : y) {
					active = (x == '#') ? active+1 : active;
				}
			}
		}
		System.out.println(active);
		
		/* PART 2 */
		// Same algorithm as part 1, but in 4D
		char[][][][] state2 = new char[W][Z][Y][X];
		for(int w = 0; w < W; w++) {
			for(int z = 0; z < Z; z++) {
				for(int y = 0; y < Y; y++) {
					for(int x = 0; x < X; x++) {
						state2[w][z][y][x] = '.';
					}
				}
			}
		}
		for(int y = N; y < N+v; y++) {
			for(int x = N; x < N+h; x++) {
				state2[W/2][Z/2][y][x] = src[y-N][x-N];
			}
		}
		for(int i = 0; i < 6; i++) {
			state2 = iterate4D(state2);
		}
		active = 0;
		for(char[][][] w : state2) {
			for(char[][] z : w) {
				for(char[] y : z) {
					for(char x : y) {
						active = (x == '#') ? active+1 : active;
					}
				}
			}
		}
		System.out.println(active);
		
	}
	
}
