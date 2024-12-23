package AoC2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import AoC2022.Day;

public class Day01 extends Day {
	
	protected static boolean INPUT_REAL = true;
	
	private List<Integer> part1_a;
	private List<Integer> part1_b;
	
	private List<Integer> part2_a;
	private List<Integer> part2_b;
	
	private long result_part1;
	private long result_part2;
	
	public Day01() {
		super();
		part1_a = new ArrayList<Integer>();
		part1_b = new ArrayList<Integer>();
		result_part1 = 0;
		result_part2 = 0;
	}
	
	public void processFile() {
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			
			Pattern p = Pattern.compile("(\\d+)\\s*(\\d+)");
			Matcher m = p.matcher(line);
			if(m.find()) {
				Integer a = Integer.parseInt(m.group(1));
				Integer b = Integer.parseInt(m.group(2));
				part1_a.add(a);
				part1_b.add(b);
			}
		}
		
		List<Integer> sortedLeft = part1_a.stream().sorted().collect(Collectors.toList());
        List<Integer> sortedRight = part1_b.stream().sorted().collect(Collectors.toList());

        result_part1 = IntStream.range(0, Math.min(sortedLeft.size(), sortedRight.size()))
                .map(i -> Math.abs(sortedLeft.get(i) - sortedRight.get(i)))
                .sum();
		
//		for (int i = 0; i < lines.length; i++) {
//			int min_a = getIndexOfMin(part1_a);
//			int min_b = getIndexOfMin(part1_b);
//			int value_a = part1_a.get(min_a);
//			int value_b = part1_b.get(min_b);
//			
//			result_part1 +=Math.abs(value_a-value_b);
//			part1_a.remove(min_a);
//			part1_b.remove(min_b);
//		}
		logln("result part1: "+result_part1); // correct: 1151792
		
		
		Map<Integer, Long> rightCountMap = part1_b.stream()
                .collect(Collectors.groupingBy(n -> n, Collectors.counting()));

		result_part2 = part1_a.stream()
                .mapToInt(num -> num * rightCountMap.getOrDefault(num, 0L).intValue())
                .sum();
		
//		for (int i = 0; i < part2_a.size(); i++) {
//			
//			Integer value_a = part2_a.get(i);
//			int counter=0;
//			for (Integer value_b : part2_b) {
//				if(value_a!= null && value_b!= null && value_a.equals(value_b))counter++;
//			}
//			
//			result_part2 +=value_a * counter;
//		}
		
		logln("result part2: "+result_part2); // correct: 21790168
	}
	
	public int getIndexOfMin(List<Integer> data) {
	    int min = Integer.MAX_VALUE;
	    int index = -1;
	    for (int i = 0; i < data.size(); i++) {
	    	Integer l = data.get(i);
	        if (Integer.compare(l.intValue(), min) < 0) {
	            min = l.intValue();
	            index = i;
	        }
	    }
	    return index;
	}
	
	
	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day01 day = new Day01();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}

}
