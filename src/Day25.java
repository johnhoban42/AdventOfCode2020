package src;

public class Day25 {

	public static void main(String[] args) {
		
		final int CARD = 9789649;
		final int DOOR = 3647239;
		final int DIV = 20201227;
		
		/* PART 1 */
		long subject = 1;
		long key = 1;
		while(subject != CARD) {
			subject = (subject * 7) % DIV;
			key = (key * DOOR) % DIV;
		}
		System.out.println(key);
	}
	
}
