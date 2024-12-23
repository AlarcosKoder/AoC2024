package AoC2024;

import AoC2022.Day;

public class Day00 extends Day {

	public Day00() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = false;
	}

	public void processFile() {
		
		long result_part1 = 0;
		
		System.out.println("result part1: "+result_part1); // correct: 
		
		
		long result_part2 = 0;
		
		System.out.println("result part2: "+result_part2); // correct: 
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day00 day = new Day00();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
