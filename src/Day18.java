package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Day18 {
	
	// Find the ending token of the subexpression given an index with a starting parenthesis
	private static int getEndOfSubexpression(String expr, int start) {
		int depth = 0; // levels of parentheses
		do {
			char c = expr.charAt(start);
			if(c == '(') {
				depth++;
			}else if(c == ')') {
				depth--;
			}
			start++;
		}while(depth > 0);
		return --start;
	}
	
	// Recursively evaluate the expression. This method gets called on the entire expression,
	// then again for any sub-expressions in parentheses. Relies on the convenient assumptions
	// that all tokens are a single character and the expression is well-formatted.
	private static long evaluate(String expr, int flag) {
		Long total = null;
		char op = '\0';
		int i = 0;
		while(i < expr.length()) {
			
			char c = expr.charAt(i);
			long val;
			
			// Store operator
			if(c == '+') {
				op = c;
				i++;
				continue;
				
			// PART 2: Evaluate the rest of the expression before evaluating this multiplication
			}else if(c == '*') {
				op = c;
				if(flag == 2) {
					String subexpr = expr.substring(i+1);
					val = evaluate(subexpr, flag);
					i = expr.length();
				}else {
					i++;
					continue;
				}
				
			// On a parenthesis, parse the contained sub-expression and evaluate it
			}else if(c == '(') {
				int end = getEndOfSubexpression(expr, i);
				String subexpr = expr.substring(i+1, end);
				val = evaluate(subexpr, flag);
				i = end+1;
			
			// Parse a constant
			}else {
				val = c - '0';
				i++;
			}
			
			// Store number if there is no running total
			// Otherwise, evaluate this binary operation based on the operator
			if(total == null) {
				total = val;
			}else {
				if(op == '+') {
					total += val;
				}else {
					total *= val;
				}
			}
		}
		return total;
	}

	public static void main(String[] args) {
		
		String[] src = null;
		try {
			src = Files.lines(Paths.get("inputs/day18.txt"))
					.toArray(String[]::new);
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		long ans1 = 0; long ans2 = 0;
		for(String expr : src) {
			// Strip tokens (0-9, +, *, parentheses) from source
			expr = expr.replace(" ", "");
			ans1 += evaluate(expr, 1);
			ans2 += evaluate(expr, 2);
		}
		System.out.println(ans1);
		System.out.println(ans2);
		
	}
	
}
