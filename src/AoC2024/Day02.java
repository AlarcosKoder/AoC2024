package AoC2024;

import AoC2022.Day;

public class Day02 extends Day {

	public Day02() {
		super();
	}
	
	public boolean checkLine(String line) {
		String[] splits = line.split(" ");
		
		long current;
		long next;
		
		Boolean increasing = null;
		
		int i = 1;
		for (i = 1; i < splits.length; i++) {
			current = Long.parseLong(splits[i-1]);
			next = Long.parseLong(splits[i]);
			
			if(increasing==null) increasing = Boolean.valueOf(next>current);
			if(current==next) break;
			
			if(increasing) {
				if(next-current<=0) break;
				if(next-current>3) break;
			} else {
				if(current-next<=0) break;
				if(current-next>3) break;
			}
		}
		return i==splits.length;
	}
	
	public void processFile() {
		String[] lines = sb.toString().split("\n");
		long result_part1 = 0;
		for (String line : lines) {
			
			if(checkLine(line)) result_part1++; // corect: 606
			
		}
		
		logln("result part1: "+result_part1); // correct: 202
		
		
		lines = sb.toString().split("\n");
		long result_part2 = 0;
		for (String line : lines) {
			
			if(checkLine(line)) {
				result_part2++;
			} else {
				String[] splits = line.split(" ");
				
				String newLine="";
				for (int i = 0; i < splits.length; i++) {
					newLine="";
					for (int j = 0; j < splits.length; j++) {
						if(i!=j) {
							newLine += splits[j];
							newLine += " ";
						}
					}
					
					if(checkLine(newLine)) {
						result_part2++;
						break;
					}
					
				}
			}
			
		}
		
		logln("result part2: "+result_part2); // correct: 644
		
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day02 day = new Day02();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}

}
