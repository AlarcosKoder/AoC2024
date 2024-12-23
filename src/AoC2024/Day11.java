package AoC2024;

import java.util.HashMap;
import java.util.Map;

import AoC2022.Day;

public class Day11 extends Day {
	public Day11() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	
	public long blink(HashMap<Long, Long> stones, int iter) {
		for (int i = 0; i < iter; i++) {
			
			HashMap<Long, Long> new_stones = new HashMap<Long, Long>();
			
			for (Map.Entry<Long, Long> entry : stones.entrySet()) {
                long stone = entry.getKey();
                long count = entry.getValue();
				
				if(stone==0) {
					new_stones.put(1L, new_stones.getOrDefault(1L, 0L)+count);
				} else if(hasEvenDigits(stone)) {
					String[] stone_parts = splitStone(stone);
					Long stone1 = Long.parseLong(stone_parts[0]);
					Long stone2 = Long.parseLong(stone_parts[1]);
					
					new_stones.put(stone1, new_stones.getOrDefault(stone1, 0L)+count);
					new_stones.put(stone2, new_stones.getOrDefault(stone2, 0L)+count);
				} else {
					long new_stone = stone*2024; 
					new_stones.put(new_stone, new_stones.getOrDefault(new_stone, 0L)+count);
				}
			}
			stones = new_stones;
		}
		return stones.values().stream().mapToLong(Long::longValue).sum();
	}

	public void processFile() {
		
		HashMap<Long, Long> initial_stones = new HashMap<Long, Long>();
		
		String[] initial_stones_str = sb.toString().trim().split("\\s+");
		for (String string : initial_stones_str) {
			Long stone = Long.parseLong(string);
			initial_stones.put(stone, initial_stones.getOrDefault(initial_stones,0L)+1);
		}
		
		HashMap<Long, Long> stones = new HashMap<Long, Long>(initial_stones);
		
		long result_part1 = blink(stones, 25);
		System.out.println("result part1: "+result_part1); // correct: 213625
		
		stones = new HashMap<Long, Long>(initial_stones);
		long result_part2 = blink(stones, 75);
		System.out.println("result part2: "+result_part2); // correct: 252442982856820
	}
	
	private boolean hasEvenDigits(Long stone) {
		return String.valueOf(Math.abs(stone)).length()%2==0;
	}
	
	private String[] splitStone(Long stone) {
		String str = stone.toString();
		int middle = str.length()/2;
		String part1 = str.substring(0, middle);
		String part2 = str.substring(middle);
		return new String[]{part1,part2};
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day11 day = new Day11();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
