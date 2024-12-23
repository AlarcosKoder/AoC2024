package AoC2022;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day04 extends Day {

	public Day04() {
		super();
	}
	
	public void execute() {
//		2-4,6-8
//		2-3,4-5
//		5-7,7-9
//		solution:550
		
		int solution = 0;
		int solution2 = 0;
		String[] lines=sb.toString().split("\n");
		Set<String> missing=new HashSet<>();
		for (String line : lines) {
			Pattern p = Pattern.compile("(\\d+)\\-(\\d+)\\,(\\d+)\\-(\\d+)");
			Matcher m = p.matcher(line);
			boolean found = m.find();
			int count = m.groupCount();
			if(count==4){
//				System.out.println("group"+i+" "+m.group(i+1));	
				
				
				long i1_lower = Long.parseLong(m.group(1));
				long i1_upper = Long.parseLong(m.group(2));
				long i2_lower = Long.parseLong(m.group(3));
				long i2_upper = Long.parseLong(m.group(4));
				
				if(i1_lower>=i2_lower && i1_upper<=i2_upper
						||
					i2_lower>=i1_lower && i2_upper<=i1_upper
						) {
					solution++;
				}
				//960 tul sok
				//748 tul keves
				//627 tul kevés
				if(line.equals("22-22,21-98"))
					System.out.println("");
				long totalRange = Math.max(i1_upper, i2_upper) - Math.min(i1_lower, i2_lower);
				long sumOfRanges = (i1_upper - i1_lower) + (i2_upper - i2_lower);

				if (sumOfRanges >= totalRange) { // means they overlap
					System.out.println("sol for b:"+line);
					solution2++;
				} else 
					missing.add(line);
				
			}
		}
		
		for (String string : missing) {
			System.out.println("not overlapping: "+string);
		}
		
		System.out.println("solution:"+solution);
		System.out.println("solution2:"+solution2);
	}
	
	public static void main(String[] args) {
		Day04 d04 = null;
		d04 = new Day04();
		d04.readFile();
		d04.execute();
	}

}
