package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;

public class Day19 {
	
	// dict -> symbolic representations, rule -> rule being computed, expr -> regex representations
	// Regex representations of 8 and 11 have been evaluated as special cases:
	// 8 = (42)+
	// 11 = (42){n}(31){n} for any n; for this input set, the max n for any match is 4
	private static String buildExpr(HashMap<String,String> dict, String rule, HashMap<String,String> expr, int flag) {
		if(expr.get(rule) != null) {
			return expr.get(rule);
		}
		if(flag == 2) {
			if(rule.equals("8")) {
				return "(" + buildExpr(dict, "42", expr, flag) + "+)";
			}else if(rule.equals("11")) {
				return "(" + buildExpr(dict, "42", expr, flag) + "{n}" + buildExpr(dict, "31", expr, flag) + "{n})";
			}
		}
		String[] subexpr = dict.get(rule).split(" ");
		String s = "(";
		for(String e : subexpr) {
			if(e.equals("|")) {
				s += "|";
			}else if(e.equals("\"a\"")) {
				return "a";
			}else if(e.equals("\"b\"")) {
				return "b";
			}else {	
				s += buildExpr(dict, e, expr, flag);
			}
		}
		s += ")";
		expr.put(rule, s);
		return s;
	}

	public static void main(String[] args) {
		
		// Build dict of rules
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day19dict.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		HashMap<String,String> dict = new HashMap<String,String>();
		for(String s : src) {
			String[] tokens = s.split(": ");
			dict.put(tokens[0], tokens[1]);
		}
		
		// Compile into regex expression
		HashMap<String,String> expr1 = new HashMap<String,String>();
		HashMap<String,String> expr2 = new HashMap<String,String>();
		String rule1 = "^" + buildExpr(dict, "0", expr1, 1) + "$";
		String rule2 = "^" + buildExpr(dict, "0", expr2, 2) + "$";
		
		// Compare against input
		String[] msg = null;
		try {
			msg = Files.lines(Paths.get("inputs/day19msg.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		/* PART 1 */
		long matches = Arrays.stream(msg).filter(s -> s.matches(rule1)).count();
		System.out.println(matches);
		
		/* PART 2 */
		matches = 0;
		for(int n = 1; n <= 4; n++) {
			String rule2n = rule2.replace("n", String.valueOf(n));
			matches += Arrays.stream(msg).filter(s -> s.matches(rule2n)).count();
		}
		System.out.println(matches);
		
	}
	
}
