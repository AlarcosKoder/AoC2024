package AoC2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AoC2022.Day;

public class Day05 extends Day {

	public Day05() {
		super();
	}
	
	public void processFile() {
		
		
		HashMap<Integer, TreeSet<Integer>> rules = new HashMap<Integer, TreeSet<Integer>>();
		
		List<List<Integer>> page_numbers = new ArrayList<>();
		List<List<Integer>> incorrect_page_numbers = new ArrayList<>();
		
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			if(line.contains("|")) {
				Integer a = Integer.parseInt(line.split("\\|")[0]);
				Integer b = Integer.parseInt(line.split("\\|")[1]);
				if(!rules.containsKey(a)) {
					rules.put(a, new TreeSet<>(Collections.singleton(b)));
				} else  {
					rules.get(a).add(b);
				}
			}
			
			if(line.contains(",")) {
				String[] numbers = line.split(",");
                List<Integer> row = new ArrayList<>();
                for (String number : numbers) {
                    row.add(Integer.parseInt(number.trim()));
                }
                page_numbers.add(row);
			}
		}
		
		long result_part1 = 0;
		for (List<Integer> page_number : page_numbers) {
			boolean correct_order = true;
			for (int i = 0; i < page_number.size()-1; i++) {
				Integer a = page_number.get(i);
				Integer b = page_number.get(i+1);
				
				if (rules.get(a)==null || !rules.get(a).contains(b)) {
					correct_order = false;
					break;
				}
			}
			if(correct_order) {
				result_part1 += page_number.get(page_number.size()/2).intValue();
			} else {
				incorrect_page_numbers.add(page_number);
			}
				
		}
		logln("result part1: "+result_part1); // correct: 4281
		
		long result_part2 = 0;
		for (List<Integer> icpn : incorrect_page_numbers) {
			List<Integer> newList = new ArrayList<Integer>();
			for (Integer number : icpn) {
				if(newList.size()==0) {
					newList.add(number);
					continue;
				} else {
					for (int i = 0; i < newList.size(); i++) {
						Integer a = newList.get(i); // a=75, number=97
						if(rules.containsKey(number) && rules.get(number).contains(a)) {
							newList.add(i, number);
							break;
						}
					}
					if(!newList.contains(number)) {
						newList.add(number);
					}
					
				}
			}
			result_part2 += newList.get(newList.size()/2).intValue();
			
		}
		
		logln("result part2: "+result_part2); // correct: 5466
    }

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day05 day = new Day05();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}

}
