package AoC2024;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import AoC2022.Day;

public class Day07 extends Day {

	public Day07() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}

	private boolean generateResult(long result,Long current,List<Long> remainingList, boolean part2) {
		
		if(remainingList.size() > 1) {
			Long a = remainingList.remove(0);
			Long b = remainingList.remove(0);
			
			Long add = a+b;
			remainingList.add(0, add);
			if(generateResult(result,add,remainingList,part2)) {
				return true;
			}
			remainingList.remove(0);
			
			Long mul = a*b;
			remainingList.add(0, mul);
			
			if(generateResult(result,mul,remainingList,part2)) {
				return true;
			}
			remainingList.remove(0);
			
			if(part2) {
				Long concat = Long.parseLong(String.valueOf(a)+String.valueOf(b));
				remainingList.add(0, concat);
				if(generateResult(result,concat,remainingList,part2)) {
					return true;
				}
				remainingList.remove(0);
			}
			remainingList.add(0,b);
			remainingList.add(0,a);
			
		} else if(remainingList.size() == 1) {
			if(result==current) return true;
			else return false;
		}
		
		return false;
	}
	
	public void processFile() {
		
		long result_part1 = 0;
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			long result = Long.parseLong(line.split("\\:")[0]);
			List<Long> numbers =  Arrays.stream(line.split("\\:")[1].trim().split(" ")).map(Long::parseLong).collect(Collectors.toList());
			if(generateResult(result, 0L, numbers,false))
				result_part1 += result;
		}
		logln("result part1: "+result_part1); // correct: 4122618559853
		
		long result_part2 = 0;
		lines = sb.toString().split("\n");
		for (String line : lines) {
			long result = Long.parseLong(line.split("\\:")[0]);
			List<Long> numbers =  Arrays.stream(line.split("\\:")[1].trim().split(" ")).map(Long::parseLong).collect(Collectors.toList());
			if(generateResult(result, 0L, numbers,true))
				result_part2 += result;
		}
		logln("result part2: "+result_part2); // correct: 227615740238334
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day07 day = new Day07();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}


}

