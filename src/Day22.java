package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Day22 {
	
	// Simulate combat
	// PART 1: Check for the higher card only.
	// PART 2: Play a recursive game if both players have enough cards, or check for the higher card.
	// Also ends the game immediately if the sub-game repeats its state.
	private static LinkedList<Integer> combat(LinkedList<Integer> p1, LinkedList<Integer> p2, int flag){
		
		Set<List<Integer>> states = new HashSet<List<Integer>>();
		while(p1.size() > 0 && p2.size() > 0) {
			
			// Check for duplicate states
			if(flag == 2) {
				List<Integer> newState = new ArrayList<Integer>();
				newState.addAll(p1);
				newState.add(-1);
				newState.addAll(p2);
				if(!states.add(newState)) {
					return p1;
				}
			}
			
			// Play a round, recursing in part 2 if possible
			int card1 = p1.removeFirst();
			int card2 = p2.removeFirst();
			if(flag == 2 && p1.size() >= card1 && p2.size() >= card2) {
				LinkedList<Integer> subp1 = new LinkedList<Integer>();
				subp1.addAll(p1.subList(0, card1));
				LinkedList<Integer> subp2 = new LinkedList<Integer>();
				subp2.addAll(p2.subList(0, card2));
				LinkedList<Integer> winner = combat(subp1, subp2, flag);
				if(winner == subp1) {
					p1.addLast(card1);
					p1.addLast(card2);
				}else if(winner == subp2){
					p2.addLast(card2);
					p2.addLast(card1);
				}
			}else if(card1 > card2) {
				p1.addLast(card1);
				p1.addLast(card2);
			}else {
				p2.addLast(card2);
				p2.addLast(card1);
			}
		}
		return (p1.size() > 0) ? p1 : p2;
		
	}
	
	// Score a winning deck
	private static int score(LinkedList<Integer> winner) {
		int score = 0;
		for(int i = 0; i < winner.size(); i++) {
			score += winner.get(i) * (winner.size() - i);
		}
		return score;
	}

	public static void main(String[] args) {
		
		// Create decks
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day22.txt"))
					.map(s -> s.replace(".", "0").replace("#", "1"))
					.collect(Collectors.joining("\n"))
					.split("\n\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
		LinkedList<Integer> player1 = new LinkedList<Integer>();
		String[] cards = src[0].split("\n");
		for(int i = 1; i < cards.length; i++) {
			player1.addLast(Integer.parseInt(cards[i]));
		}
		LinkedList<Integer> player2 = new LinkedList<Integer>();
		cards = src[1].split("\n");
		for(int i = 1; i < cards.length; i++) {
			player2.addLast(Integer.parseInt(cards[i]));
		}
		
		// Playing combat and scoring
		LinkedList<Integer> p1 = new LinkedList<Integer>();
		p1.addAll(player1);
		LinkedList<Integer> p2 = new LinkedList<Integer>();
		p2.addAll(player2);
		int score1 = score(combat(player1, player2, 1));
		int score2 = score(combat(p1, p2, 2));
		System.out.println(score1);
		System.out.println(score2);
		
	}
	
}
