package AoC2024;

import AoC2022.Day;

public class Day25 extends Day {
	public Day25() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	
	public boolean[] parse_line(String _str) {
		Boolean[] boxed_result = _str.chars().mapToObj(c -> c == '#').toArray(Boolean[]::new);
		
		boolean[] result = new boolean[boxed_result.length];
		for (int l = 0; l < boxed_result.length; l++) {
		    result[l] = boxed_result[l];  // Auto-unboxing occurs here
		}
		return result;
	}
	
	public boolean evaluate_bytes(boolean[][] lock, boolean[][] key) {
		for (int j = 0; j < lock.length; j++) {
			for (int i = 0; i < lock[j].length; i++) {
				if((lock[j][i] & key[j][i])) {
					return false;
				}
			}
		}
		return true;
	}

	public void processFile() {
		
		boolean[][][] locks = new boolean[0][7][5]; 
		boolean[][][] keys  = new boolean[0][7][5];
		
		String[] lines = sb.toString().split("\n");
		for (int i = 0; i < lines.length; i+=8) {
			// locks
			if(lines[i].equals("#####")) {
				boolean[][][] locks_new = new boolean[locks.length+1][7][5];
				System.arraycopy(locks, 0, locks_new, 0, locks.length);
				locks_new[locks_new.length-1][0]= parse_line(lines[i]);
				locks_new[locks_new.length-1][1]= parse_line(lines[i+1]);
				locks_new[locks_new.length-1][2]= parse_line(lines[i+2]);
				locks_new[locks_new.length-1][3]= parse_line(lines[i+3]);
				locks_new[locks_new.length-1][4]= parse_line(lines[i+4]);
				locks_new[locks_new.length-1][5]= parse_line(lines[i+5]);
				locks_new[locks_new.length-1][6]= parse_line(lines[i+6]);
				locks=locks_new;
			// keys
			} else {
				boolean[][][] locks_new = new boolean[keys.length+1][7][5];
				System.arraycopy(keys, 0, locks_new, 0, keys.length);
				locks_new[locks_new.length-1][0]= parse_line(lines[i]);
				locks_new[locks_new.length-1][1]= parse_line(lines[i+1]);
				locks_new[locks_new.length-1][2]= parse_line(lines[i+2]);
				locks_new[locks_new.length-1][3]= parse_line(lines[i+3]);
				locks_new[locks_new.length-1][4]= parse_line(lines[i+4]);
				locks_new[locks_new.length-1][5]= parse_line(lines[i+5]);
				locks_new[locks_new.length-1][6]= parse_line(lines[i+6]);
				keys=locks_new;
			}
		}
		
//		System.out.println("locks:");
//		System.out.println(locks);
//		System.out.println("keys:");
//		System.out.println(keys);
		long result_part1 = 0;
		for (int i = 0; i < locks.length; i++) {
			for (int j = 0; j < keys.length; j++) {
				if(evaluate_bytes(locks[i],keys[j])) {
					result_part1++;
				}
			}
		}
		
		System.out.println("result part1: "+result_part1); // correct: 3365 (sample: 3)
		
		
		long result_part2 = 0;
		
		System.out.println("result part2: "+result_part2); // correct: 
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day25 day = new Day25();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
