package src;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.regex.*;

public class Day04 {
	
	private static class Passport {
		
		int byr = -1;
		int iyr = -1;
		int eyr = -1;
		int hgt = -1;
		String hgt_type = null;
		String hcl = null;
		String ecl = null;
		String pid = null;
		
		public boolean isValid() {
			
			if(hgt_type == null || hcl == null || ecl == null || pid == null) {
				return false;
			}
			
			Matcher mHcl = Pattern.compile("^#[a-f0-9]{6}$").matcher(hcl);
			Matcher mEcl = Pattern.compile("^((amb)|(blu)|(brn)|(gry)|(grn)|(hzl)|(oth))$").matcher(ecl);
			Matcher mPid = Pattern.compile("^[0-9]{9}$").matcher(pid);
			
			return	(1920 <= byr && byr <= 2002) &&
					(2010 <= iyr && iyr <= 2020) && 
					(2020 <= eyr && eyr <= 2030) && 
					((hgt_type.equals("cm") && 150 <= hgt && hgt <= 193) || (hgt_type.equals("in") && 59 <= hgt && hgt <= 76)) &&
					mHcl.matches() &&
					mEcl.matches() &&
					mPid.matches();
					
		}
		
	}

	public static void main(String[] args) {
		
		String src = null;
		try {
			src = Files.lines(Paths.get("inputs/day04.txt"))
					.collect(Collectors.joining("\n"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		String[] passports = src.split("\n\n");
		int valid = 0;
		for(String p : passports) {
			Passport pass = new Passport();
			String[] tokens = p.split("[: \n]");
			for(int i = 0; i < tokens.length; i += 2) {
				if(tokens[i].equals("byr")) {
					pass.byr = Integer.parseInt(tokens[i+1]);
				}else if(tokens[i].equals("iyr")) {
					pass.iyr = Integer.parseInt(tokens[i+1]);
				}else if(tokens[i].equals("eyr")) {
					pass.eyr = Integer.parseInt(tokens[i+1]);
				}else if(tokens[i].equals("hgt")) {
					int l = tokens[i+1].length() - 2; // length of numeric value
					if(l > 1) {
						pass.hgt_type = tokens[i+1].substring(l);
						pass.hgt = Integer.parseInt(tokens[i+1].substring(0, l));
					}
				}else if(tokens[i].equals("hcl")) {
					pass.hcl = tokens[i+1];
				}else if(tokens[i].equals("ecl")) {
					pass.ecl = tokens[i+1];
				}else if(tokens[i].equals("pid")) {
					pass.pid = tokens[i+1];
				}
			}
			if(pass.isValid()) {
				valid++;
			}
		}
		
		System.out.println(valid);
		
	}
	
}
