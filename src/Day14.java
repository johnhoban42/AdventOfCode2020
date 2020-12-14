package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

public class Day14 {
	
	private static void write(HashMap<Long, Long> mem, long addr, long val) {
		if(mem.containsKey(addr)) {
			mem.remove(addr);
		}
		mem.put(addr, val);
	}
	
	// Handling floating bits in part 2
	// NEW MASK VALUES:
	// 0 : no change (static 0)
	// 1 : force 1 (static/floating 1)
	// 2 : force 0 (floating 0)
	private static void floatingAddrWrite(HashMap<Long, Long> mem, long addr, long val, String mask) {
		// If there are no floating bits, write to memory
		// Similar to part 1, the single mask gets split into an OR mask and an AND mask
		if(!mask.contains("X")) {
			String orStr = mask.replace('2', '0');
			String andStr = mask.replace('0', '1').replace('2', '0');
			long or = Long.parseLong(orStr, 2);
			long and = Long.parseLong(andStr, 2);
			write(mem, (addr | or) & and, val);
			return;
		}
		// Branch on the first floating bit, creating two possible addresses
		floatingAddrWrite(mem, addr, val, mask.replaceFirst("X", "2"));
		floatingAddrWrite(mem, addr, val, mask.replaceFirst("X", "1"));
	}

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day14.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// For part 1, input mask characters get split into 2 masks:
		// X = (X | 0) & 1
		// 0 = (X | 0) & 0
		// 1 = (X | 1) & 1
		long or = 0L; long and = 0L;
		String mask = new String();
		HashMap<Long, Long> mem1 = new HashMap<Long, Long>();
		HashMap<Long, Long> mem2 = new HashMap<Long, Long>();
		for(String line : src) {
			if(line.startsWith("mask")) {
				mask = line.substring(7);
				// Update part 1 masks
				or = Long.parseLong(mask.replace('X', '0'), 2);
				and = Long.parseLong(mask.replace('X', '1'), 2);
			}else {
				String[] cmd = line.split("[a-z=\\[\\] ]+"); // cmd[0] is an empty string
				long addr = Long.parseLong(cmd[1]);
				long val = Long.parseLong(cmd[2]);
				// Part 1
				write(mem1, addr, (val | or) & and);
				// Part 2
				floatingAddrWrite(mem2, addr, val, mask);
			}
		}
		long sum1 = mem1.values().stream().mapToLong(l -> l).sum();
		long sum2 = mem2.values().stream().mapToLong(l -> l).sum();
		System.out.println(sum1);
		System.out.println(sum2);
		
	}
	
}
