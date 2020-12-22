package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {
	
	// Dimension of tiles and image
	public static final int TILE_DIM = 10;
	public static int IMG_DIM;
	
	// Reverse an N-bit hash
	private static int reverse(int i) {
		int r = 0;
		for(int b = 0; b < TILE_DIM; b++) {
			r = r << 1;
			r += (i % 2);
			i = i >> 1;
		}
		return r;
	}
	
	// Rotate a grid of characters clockwise
	private static String[] rotateGrid(String[] grid) {
		String[] tmp = new String[grid.length];
		for(int col = 0; col < grid.length; col++) {
			tmp[col] = "";
			for(int row = grid.length-1; row >= 0; row--) {
				tmp[col] += grid[row].charAt(col);
			}
		}
		return tmp;
	}
	
	// Flip a grid vertically
	private static String[] flipGridVertical(String[] grid) {
		String[] tmp = new String[grid.length];
		for(int row = 0; row < grid.length; row++) {
			tmp[grid.length - (row+1)] = grid[row];
		}
		return tmp;
	}
	
	private static class Tile {
		
		private long id;
		private String[] data;
		
		public long getID() {return id;}
		public String[] getData() {return data;}
		
		// Border hash codes to check for matches
		// Computed by scanning the binary string along a border clockwise and converting it to an integer
		private int tHash; // top
		private int rHash; // right
		private int bHash; // bottom
		private int lHash; // left
		
		public int getTHash() {return tHash;}
		public int getRHash() {return rHash;}
		public int getBHash() {return bHash;}
		public int getLHash() {return lHash;}
		public List<Integer> getHashes() {return List.of(tHash, rHash, bHash, lHash);}
		public List<Integer> getReverseHashes() {return List.of(reverse(tHash), reverse(rHash), reverse(bHash), reverse(lHash));}
		
		private void computeHashes() {
			tHash = Integer.parseInt(data[0], 2);
			bHash = reverse(Integer.parseInt(data[TILE_DIM-1], 2));
			String lStr = "", rStr = "";
			for(int i = 0; i < TILE_DIM; i++) {
				lStr += data[i].charAt(0);
				rStr += data[i].charAt(TILE_DIM-1);
			}
			lHash = reverse(Integer.parseInt(lStr, 2));
			rHash = Integer.parseInt(rStr, 2);
		}
		
		// Rotate the tile clockwise n times
		public void rotate(int n) {
			while(n > 0) {
				this.data = rotateGrid(data);
				n--;
			}
			computeHashes();
		}
		
		// Flip the tile horizontally
		public void flipHorizontal() {
			for(int row = 0; row < TILE_DIM; row++) {
				String tmp = "";
				for(int col = TILE_DIM-1; col >= 0; col--) {
					tmp += this.data[row].charAt(col);
				}
				this.data[row] = tmp;
			}
			computeHashes();
		}
		
		// Flip the tile vertically
		public void flipVertical() {
			this.data = flipGridVertical(data);
			computeHashes();
		}
		
		public Tile(long id, String[] data) {
			this.id = id;
			this.data = data;
			computeHashes();
		}
		
	}
	
	// Keeps track of how many times a hash has been put into the map
	private static void putTileHash(HashMap<Integer,Integer> hashes, int hash) {
		Integer freq = hashes.remove(hash);
		hashes.put(hash, (freq == null) ? 1 : freq+1);
	}
	
	// A tile is a corner tile if two of its hashes appear only once in the set of all hashes
	private static boolean isCornerTile(Tile t, List<Integer> uniqueHashes) {
		int unique = 0;
		if(uniqueHashes.contains(t.getTHash())){
			unique++;
		}if(uniqueHashes.contains(t.getRHash())){
			unique++;
		}if(uniqueHashes.contains(t.getBHash())){
			unique++;
		}if(uniqueHashes.contains(t.getLHash())){
			unique++;
		}
		return unique == 2;
	}
	
	// Find a sea monster from a given starting position. Does not check for valid array indices.
	private static boolean findMonster(String[] sea, int row, int col) {
		return  sea[row]	.charAt(col) 	== '1' &&
				sea[row+1]	.charAt(col+1)	== '1' &&
				sea[row+1]	.charAt(col+4) 	== '1' &&
				sea[row]  	.charAt(col+5) 	== '1' &&
				sea[row]	.charAt(col+6) 	== '1' &&
				sea[row+1]	.charAt(col+7) 	== '1' &&
				sea[row+1]	.charAt(col+10) == '1' &&
				sea[row]	.charAt(col+11) == '1' &&
				sea[row]	.charAt(col+12) == '1' &&
				sea[row+1]	.charAt(col+13) == '1' &&
				sea[row+1]	.charAt(col+16) == '1' &&
				sea[row]	.charAt(col+17) == '1' &&
				sea[row]	.charAt(col+18) == '1' &&
				sea[row-1]	.charAt(col+18) == '1' &&
				sea[row]	.charAt(col+19)	== '1';
	}
	
	// Mark a monster in the monster tracker
	private static void markMonster(int[][] tracker, int row, int col) {
		tracker[row][col]++;
		tracker[row+1][col+1]++;
		tracker[row+1][col+4]++;
		tracker[row][col+5]++;
		tracker[row][col+6]++;
		tracker[row+1][col+7]++;
		tracker[row+1][col+10]++;
		tracker[row][col+11]++;
		tracker[row][col+12]++;
		tracker[row+1][col+13]++;
		tracker[row+1][col+16]++;
		tracker[row][col+17]++;
		tracker[row][col+18]++;
		tracker[row-1][col+18]++;
		tracker[row][col+19]++;
	}

	public static void main(String[] args) {
		
		// Create tiles from input, represented in binary
		// '.' -> 0, '#' -> 1
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day20.txt"))
					.map(s -> s.replace(".", "0").replace("#", "1"))
					.collect(Collectors.joining("\n"))
					.split("\n\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
		List<Tile> tiles = new ArrayList<Tile>();
		for(String s : src) {
			String[] tile = s.split(":\n");
			long id = Long.parseLong(tile[0].substring(5));
			String[] data = tile[1].split("\n");
			tiles.add(new Tile(id, data));
		}
		
		/* PART 1 */
		// Compile all hashes and their mirror images, each paired with its frequency
		HashMap<Integer,Integer> hashes = new HashMap<Integer,Integer>();
		for(Tile t : tiles) {
			List<Integer> h = t.getHashes(); List<Integer> hr = t.getReverseHashes();
			for(int i = 0; i < h.size(); i++) {
				putTileHash(hashes, h.get(i));
				putTileHash(hashes, hr.get(i));
			}
		}
		
		// The corner tiles are the 4 tiles such that 2 of their edge hashes match no other hashes
		// Find these tiles and then the product of their IDs
		List<Integer> uniqueHashes = hashes.keySet().stream()
													.filter(i -> hashes.get(i) == 1)
													.collect(Collectors.toList());
		long ans = tiles.stream()
						.filter(t -> isCornerTile(t, uniqueHashes))
						.mapToLong(Tile::getID)
						.reduce(1, (x,y) -> x*y);
		System.out.println(ans);
		
		// Assemble the image.
		// Start by placing any of the 4 corner pieces in the top left corner.
		// Build the first row by matching another tile's edge hash with the rightmost tile's right edge hash.
		// For subsequent rows, match a tile's edge hash with the bottom hash of the tile above it.
		IMG_DIM = (int)Math.sqrt(tiles.size());
		Tile[][] img = new Tile[IMG_DIM][IMG_DIM];
		img[0][0] = tiles.stream()
						 .filter(t -> isCornerTile(t, uniqueHashes))
						 .findAny()
						 .get();
		while(!(uniqueHashes.contains(img[0][0].getLHash()) && uniqueHashes.contains(img[0][0].getTHash()))) {
			img[0][0].rotate(1);
		}
		tiles.remove(img[0][0]);
		
		// First row
		for(int col = 1; col < IMG_DIM; col++) {
			Iterator<Tile> it = tiles.iterator();
			Tile target = null;
			int rHash = img[0][col-1].getRHash();
			while(target == null) {
				// If the tile has a matching hash, flip it such that the existing tile's right hash
				// equals the new tile's reverse left hash
				Tile t = it.next();
				if(rHash == t.getTHash()) {
					t.flipVertical();
					t.rotate(1);
					target = t;
					
				}else if(rHash == reverse(t.getTHash())) {
					t.rotate(3);
					target = t;
				
				}else if(rHash == t.getRHash()) {
					t.flipHorizontal();
					target = t;
				
				}else if(rHash == reverse(t.getRHash())) {
					t.rotate(2);
					target = t;
				
				}else if(rHash == t.getBHash()) {
					t.flipHorizontal();
					t.rotate(1);
					target = t;
				
				}else if(rHash == reverse(t.getBHash())) {
					t.rotate(1);
					target = t;
				
				}else if(rHash == t.getLHash()) {
					t.flipVertical();
					target = t;
				
				}else if(rHash == reverse(t.getLHash())) {
					// do nothing
					target = t;
				}	
			}
			img[0][col] = target;
			tiles.remove(target);
		}
		
		// Rest of the picture
		for(int row = 1; row < IMG_DIM; row++) {
			for(int col = 0; col < IMG_DIM; col++) {
				Iterator<Tile> it = tiles.iterator();
				Tile target = null;
				int bHash = img[row-1][col].getBHash();
				while(target == null) {
					// If the tile has a matching hash, flip it such that the existing tile's bottom hash
					// equals the new tile's reverse top hash
					Tile t = it.next();
					if(bHash == t.getTHash()) {
						t.flipHorizontal();
						target = t;
						
					}else if(bHash == reverse(t.getTHash())) {
						// do nothing
						target = t;
					
					}else if(bHash == t.getRHash()) {
						t.flipHorizontal();
						t.rotate(1);
						target = t;
					
					}else if(bHash == reverse(t.getRHash())) {
						t.rotate(3);
						target = t;
					
					}else if(bHash == t.getBHash()) {
						t.flipVertical();
						target = t;
					
					}else if(bHash == reverse(t.getBHash())) {
						t.rotate(2);
						target = t;
					
					}else if(bHash == t.getLHash()) {
						t.flipVertical();
						t.rotate(1);
						target = t;
					
					}else if(bHash == reverse(t.getLHash())) {
						t.rotate(1);
						target = t;
					}	
				}
				img[row][col] = target;
				tiles.remove(target);
			}
		}
		
		// Build image
		String[] sea = new String[IMG_DIM * (TILE_DIM-2)];
		for(int irow = 0; irow < IMG_DIM; irow++) {
			for(int trow = 1; trow < TILE_DIM-1; trow++) {
				final int TROW = trow;
				sea[irow*(TILE_DIM-2) + (trow-1)] = Arrays.stream(img[irow])
													.map(tile -> tile.getData()[TROW].substring(1, TILE_DIM-1))
													.collect(Collectors.joining());
			}
		}
		
		/* PART 2 */
		// Rotate and flip the sea image until at least one monster is found
		int[][] monsters = new int[sea.length][sea.length];
		int iter = 1; boolean done = false;
		while(iter <= 8 && !done) {
			for(int row = 1; row < sea.length-1; row++) {
				for(int col = 0; col < sea.length-20; col++) {
					if(findMonster(sea, row, col)) {
						done = true;
						markMonster(monsters, row, col);
					}
				}
			}
			if(done) {break;}
			// Iterate
			if(iter == 4) {
				sea = flipGridVertical(sea);
			}else {
				sea = rotateGrid(sea);
			}
			iter++;
		}

		// Count unused 1's in the original image
		final String[] SEA = sea;
		long roughness = IntStream.rangeClosed(0, SEA.length-1)
							.flatMap(i -> IntStream.rangeClosed(0, SEA.length-1)
												.filter(j -> SEA[i].charAt(j) == '1' && monsters[i][j] == 0))
							.count();
		System.out.println(roughness);
		
	}
	
}
