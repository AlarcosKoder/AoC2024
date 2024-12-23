package AoC2024;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import AoC2022.Day;

public class Day03 extends Day {

	public Day03() {
		super();
	}
	
	public void processFile() {
		
		long result_part1 = 0;
		// mul()
		Pattern p = Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
		Matcher m = p.matcher(sb.toString());
		while(m.find()) {
			int a = Integer.parseInt(m.group(1));
			int b = Integer.parseInt(m.group(2));
			result_part1 += a*b;
		}	
		logln("result part1: "+result_part1); // correct: 179571322
		
		
		long result_part2 = 0;
		String newValue = sb.toString().replaceAll("(?s)don't\\(\\).*?do\\(\\)", ""); //szajbakurt szar \n megbaszta a replaceAll-t
		newValue=newValue.replaceAll("(?s)don't\\(\\).*", ""); // ha a végén lenne egy don't()
		p= Pattern.compile("mul\\((\\d{1,3}),(\\d{1,3})\\)");
		m = p.matcher(newValue);
		while(m.find()) {
			int a = Integer.parseInt(m.group(1));
			int b = Integer.parseInt(m.group(2));
			result_part2 += a*b;
		}
		logln("result part2: "+result_part2); // correct: 103811193
	}
	
	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day03 day = new Day03();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}

}
