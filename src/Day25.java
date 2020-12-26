package src;

public class Day25 {

	public static void main(String[] args) {
		
		final int CARD = 9789649;
		final int DOOR = 3647239;
		final int DIV = 20201227;
		
		/* PART 1 */
		int subject = 1;
		while(subject != CARD) {
			subject = (subject * 7) % DIV;
		}
		
		subject = 1;
		int doorLoops = 0;
		while(subject != DOOR) {
			subject = (subject * 7) % DIV;
			doorLoops++;
		}
		
		long key = 1;
		for(int i = 0; i < doorLoops; i++) {
			key = (key * CARD) % DIV;
		}
		System.out.println(key);
		
	}
	
}
