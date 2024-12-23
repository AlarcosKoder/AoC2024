package AoC2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AoC2022.Day;

public class Day22 extends Day {
	public Day22() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	/*
	    To mix a value into the secret number, calculate the bitwise XOR of the given value and the 
	    secret number. Then, the secret number becomes the result of that operation. 
	    (If the secret number is 42 and you were to mix 15 into the secret number, 
	    the secret number would become 37.)
	 
		To prune the secret number, calculate the value of the secret number modulo 16777216. 
		Then, the secret number becomes the result of that operation. 
		(If the secret number is 100000000 and you were to prune the secret number, 
		the secret number would become 16113920.)
	 */
	
	public long[] random(long[] _arr) {
		for (int i = 0; i < _arr.length; i++) {
			_arr[i]=random(_arr[i]);
		}
		return _arr;
	}
	
	public long banana_count(String s, List<Map<String,Integer>> number_seq_map) {
		int _c = 0;
		for (Map<String, Integer> map : number_seq_map) {
			if(map.get(s) != null)
				_c += map.get(s);
		}
		return _c;
	}
	
	public long random(long number) {
		long mul_64 = number * 64; // Calculate the result of multiplying the secret number by 64
		number ^= mul_64; // Then, mix this result into the secret number
		number %= 16777216; // Finally, prune the secret number
		
		long div = Math.floorDiv(number, 32); // Calculate the result of dividing the secret number by 32. Round the result down to the nearest integer.
		number ^= div; // Then, mix this result into the secret number. Finally, prune the secret number.
		
		long mul_2048 = number * 2048; //Calculate the result of multiplying the secret number by 2048
		number ^= mul_2048; // Then, mix this result into the secret number.
		number %= 16777216; // Finally, prune the secret number.
		return number;
	}

	public void processFile() {
		String[] lines = sb.toString().split("\n");
		long[] numbers = new long[lines.length];
		long[][] numbers_hist = new long[lines.length][2000];
		int[][] numbers_diff = new int[lines.length][2000];
		
		
		List<Map<String,Integer>> number_seq_map = new ArrayList<Map<String,Integer>>();
		
		for (int i = 0; i < lines.length; i++) {
			numbers[i]=Long.parseLong(lines[i]);
			number_seq_map.add(new LinkedHashMap<String, Integer>());
		}
		
		for (int i = 0; i < 2000; i++) {
			for (int j = 0; j < numbers_hist.length; j++) {
				numbers[j] = random(numbers[j]);
				numbers_hist[j][i]=numbers[j];
				if(i>0)
					numbers_diff[j][i]=(int)numbers_hist[j][i-1]%10- (i==1?Integer.parseInt(lines[j])%10:(int)numbers_hist[j][i-2]%10); //TODO: meh, grr, not proud of this i==1 sh*t
				else
					numbers_diff[j][i]=Integer.MIN_VALUE;
				
				// fill sequences map
				if(i>2) {
					String _str = numbers_diff[j][i-3] +","+ numbers_diff[j][i-2]+","+ numbers_diff[j][i-1]+","+ numbers_diff[j][i];
					number_seq_map.get(j).putIfAbsent(_str, (int)numbers_hist[j][i-1]%10);
				}
			}
		}
		
		long result_part1 = Arrays.stream(numbers).sum();
		logln("result part1: "+result_part1); // correct: 20215960478 [sample: 37327623] 
		
		Set<String> keys = new HashSet<String>();
		for (Map<String, Integer> map : number_seq_map) {
			keys.addAll(map.keySet());
		}
		logln("key set size: "+keys.size());
		
		long result_part2 = Integer.MIN_VALUE;
		String max_key = "";
		for (String key : keys) {
			long _count = banana_count(key,number_seq_map);
			if(_count > result_part2) {
				result_part2 = _count;
				max_key = key;
			}
		}

		logln("result part2: "+result_part2 +" for sequence:"+max_key); // correct: 2221 [sample: 23] 
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day22 day = new Day22();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
