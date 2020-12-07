package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

public class Day07 {
	
	private static class Bag {
		
		// Bag = color that contains this bag, Integer = how many bags are contained
		public HashMap<Bag, Integer> containers = new HashMap<Bag, Integer>();
		
		// Bag = color inside this bag, Integer = how many bags are contained 
		public HashMap<Bag, Integer> innerBags = new HashMap<Bag, Integer>();
		
		public String color;
		
		// Used in DFS
		public boolean visited = false;
		
		public Bag(String color) {
			this.color = color;
		}
		
		public void addContainingBag(Bag container, int bagCount) {
			containers.put(container, bagCount);
		}
		
		public void addInnerBag(Bag inner, int bagCount) {
			innerBags.put(inner,  bagCount);
		}
		
	}
	
	
	private static class BagGraph {
		
		public ArrayList<Bag> graph = new ArrayList<Bag>();
		
		public Bag getBagByColor(String color) {
			Bag b = null;
			for(Bag bag : graph) {
				if(bag.color.equals(color)){
					b = bag;
				}
			}
			return b;
		}
		
		// Find the number of possible containing bags for this color using depth-first search
		public int findContainingBags(Bag bag) {
			int count = 0;
			for(Bag container : bag.containers.keySet()) {
				if(!container.visited) {
					count++;
					container.visited = true;
					count += findContainingBags(container);
				}
			}
			return count;
		}
		
		// Find the total bags inside this bag
		public int findInnerBags(Bag bag) {
			int count = 0;
			for(Bag inner : bag.innerBags.keySet()) {
				int freq = bag.innerBags.get(inner);
				count += freq;
				count += freq * findInnerBags(inner);
			}
			return count;
		}
		
	}

	
	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day07.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		// Build a relationship graph of which bags contain others
		BagGraph bg = new BagGraph();
		for(String bag : src) {
			// Isolate each colors and numbers, first color is containing bag
			String[] tokens = bag.split("( bags contain )|( bags?[.,] ?)");
			// Get/create outer bag
			String color = tokens[0];
			Bag outer = bg.getBagByColor(color);
			if(outer == null) {
				outer = new Bag(color);
				bg.graph.add(outer);
			}
			// Get/create inner bags, build dependencies
			for(int i = 1; i < tokens.length; i++) {
				// Empty bag
				if(tokens[i].equals("no other")) {
					break;
				}
				// Non-empty bag
				String[] bagData = tokens[i].split("(?<=[0-9]+) ");
				int count = Integer.valueOf(bagData[0]);
				String innerColor = bagData[1];
				Bag inner = bg.getBagByColor(innerColor);
				if(inner == null) {
					inner = new Bag(innerColor);
					bg.graph.add(inner);
				}
				inner.addContainingBag(outer, count);
				outer.addInnerBag(inner, count);
			}
		}
		
		// PART 1: Traverse the graph, find the containers
		Bag shinyGold = bg.getBagByColor("shiny gold");
		int containers = bg.findContainingBags(shinyGold);
		System.out.println(containers);
		
		// PART 2: Traverse the graph in reverse, find the contained bags
		int innerBags = bg.findInnerBags(shinyGold);
		System.out.println(innerBags);
		
	}
	
}
