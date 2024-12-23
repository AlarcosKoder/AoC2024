package AoC2024;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AoC2022.Day;

public class Day04 extends Day {

	public Day04() {
	}
	
	private char[][] charArray;
	private String xmas = "XMAS";
	
	public boolean wanderArray(int i, int j, int step_i, int step_j, String str) {
		char check = charArray[i][j];
		try {
			if(str.charAt(0)==check) {
				if(str.length() == 1) return true;
				else return wanderArray(i+step_i,j+step_j,step_i,step_j,str.substring(1));
			}
		} catch (Exception e) {
		}
		
		return false;
	}
	
	public void processFile() {
		
		String[] rows = sb.toString().split("\n");
		// array[x][y] x: vertical, y: horizontal
		
		charArray = new char[rows.length][rows[0].length()];
		
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[0].length(); j++) {
				charArray[i][j] = rows[i].charAt(j);
			}
		}
		
		
		long result_part1 = 0;
		
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[0].length(); j++) {
				if (wanderArray(i, j, 0, 1, xmas)) result_part1++;
				if (wanderArray(i, j, 1, 0, xmas)) result_part1++;
				if (wanderArray(i, j, 1, 1, xmas)) result_part1++;
				if (wanderArray(i, j, 0, -1, xmas)) result_part1++;
				if (wanderArray(i, j, -1, 0, xmas)) result_part1++;
				if (wanderArray(i, j, -1, -1, xmas)) result_part1++;
				if (wanderArray(i, j, -1, 1, xmas)) result_part1++;
				if (wanderArray(i, j, 1, -1, xmas)) result_part1++;
			}
		}
		logln("result part1: "+result_part1); // correct: 2378
		
		
		long result_part2 = 0;
		
		for (int i = 0; i < rows.length; i++) {
			for (int j = 0; j < rows[0].length(); j++) {
				try {
					if('A' == charArray[i][j] && 'M' == charArray[i-1][j-1] && 'S' == charArray[i-1][j+1] && 'M' == charArray[i+1][j-1] && 'S' == charArray[i+1][j+1]) result_part2++;
					else if('A' == charArray[i][j] && 'S' == charArray[i-1][j-1] && 'S' == charArray[i-1][j+1] && 'M' == charArray[i+1][j-1] && 'M' == charArray[i+1][j+1]) result_part2++;
					else if('A' == charArray[i][j] && 'M' == charArray[i-1][j-1] && 'M' == charArray[i-1][j+1] && 'S' == charArray[i+1][j-1] && 'S' == charArray[i+1][j+1]) result_part2++;
					else if('A' == charArray[i][j] && 'S' == charArray[i-1][j-1] && 'M' == charArray[i-1][j+1] && 'S' == charArray[i+1][j-1] && 'M' == charArray[i+1][j+1]) result_part2++;
				} catch (Exception e) {
				}
			}
		}
		
		logln("result part2: "+result_part2); // correct: 1796
	}

	
	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day04 day = new Day04();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
}
