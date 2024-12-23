package AoC2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;

import AoC2022.Day;

public class Day19 extends Day {
	
	private Set<String> valid_tokens;
	private int min_length;
	private int max_length;
	private long cycle_counter;
	private final Map<String, Long> memoCount;
	
	public Day19() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		valid_tokens = null;
		min_length = 1;
		max_length = Integer.MAX_VALUE;
		cycle_counter = 0;
		memoCount = new HashMap<String, Long>();
	}
	
	public long tokenizer2(String part, List<String> tokens) {
		cycle_counter++;
		
		if(memoCount.containsKey(part))
			return memoCount.get(part);
		
		long count = 0;
		
		if(valid_tokens.contains(part)) {
			tokens.add(part);
			tokens.remove(tokens.size()-1);
			count++;
		}
		
		
		for (int i = min_length; i < Math.min(part.length(),max_length+1); i++) {
			
			String pre = part.substring(0,i);
			String post = part.substring(i);
			
			if(valid_tokens.contains(pre)) {
				tokens.add(pre);
				count += tokenizer2(post,tokens);
				
				tokens.remove(tokens.size()-1);
			}
		}
		memoCount.put(part, count);
		
		return count;
	}

	public void processFile() {
		String[] lines = sb.toString().split("\n");
		valid_tokens = Set.of(lines[0].split(", "));
		OptionalInt oi = valid_tokens.stream().mapToInt(s -> s.length()).max();
		if(oi.isPresent()) max_length=oi.getAsInt();
		oi = valid_tokens.stream().mapToInt(s -> s.length()).min();
		if(oi.isPresent()) min_length=oi.getAsInt();
		
		long result_part1 = 0;
		long result_part2 = 0;
		for (int i = 2; i < lines.length; i++) {
			long count = tokenizer2(lines[i],new ArrayList<String>());
			if(count>0) result_part1++;
			result_part2+=count;
			logln("Finished line:"+(i-1)+"/"+(lines.length-2)+" "+lines[i] +" found:"+count+" cycles:"+cycle_counter);
			cycle_counter=0;
		}
		logln("\nresult part1: "+result_part1); // correct: 287
		logln("result part2: "+result_part2); // correct: 571894474468161
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day19 day = new Day19();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}

