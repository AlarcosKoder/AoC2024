package AoC2024;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import AoC2022.Day;

public class Day21 extends Day {
	private Map<String,String> hashmap_num;
	
	private Map<String,String> hashmap_arrow;
	
	private final char _ACK='A';
	private final String _ACK_STR=String.valueOf(_ACK);
	
    
	public Day21() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		
		hashmap_num = new HashMap<String, String>();
		hashmap_arrow = new HashMap<String, String>();
	}
	
	
	private long countChars(String code, int depth, Map<String,String> arrow_moves, Map<Key, Long> memoMap) {
        if (depth == 0) {
            return code.length();
        }
        if (code.equals("A")) {
            return 1;
        }
        final Key key = new Key(code, depth);
        if (memoMap.containsKey(key)) {
            return memoMap.get(key);
        }

        long total = 0;
        for (final String move : code.split("A")) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i <= move.length(); i++) {
            	String _key = (i==0?"A" : String.valueOf(move.charAt(i - 1)) ) + (i==move.length()?"A": String.valueOf(move.charAt(i)));
                sb.append(arrow_moves.get(_key));
                sb.append("A");
            }
            total += countChars(sb.toString(), depth - 1, arrow_moves, memoMap);
        }
        memoMap.put(key, total);
        return total;
    }
	
	private long getNrChars(String code, Map<String,String> num_moves, Map<String,String> arrow_moves, Map<Key, Long> memoMap, int depth) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); i++) {
        	String _key = (i==0?"A" : String.valueOf(code.charAt(i - 1)) )+String.valueOf(code.charAt(i) );
            sb.append(num_moves.get(_key));
            sb.append("A");
        }
        final String start = sb.toString();

        return countChars(start, depth, arrow_moves, memoMap);
    }
	
	public void processFile() {
		final List<String> input = List.of(sb.toString().split("\n"));
		fillMaps();
		
		long result_part1 = 0;
        Map<Key, Long> moveToLength = new HashMap<>();
        for (final String code : input) {
            final long nr = Integer.parseInt(code.substring(0,code.length()-1));
            result_part1 += nr * getNrChars(code, hashmap_num, hashmap_arrow, moveToLength, 2);
        }
		
		logln("result part1: "+result_part1); // correct: 278568
		
		long result_part2 = 0;
		moveToLength.clear();
        for (final String code : input) {
            final long nr = Integer.parseInt(code.substring(0,code.length()-1));
            result_part2 += nr * getNrChars(code, hashmap_num, hashmap_arrow, moveToLength, 25);
        }
		logln("result part2: "+result_part2); // correct: 341460772681012
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day21 day = new Day21();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	

	public void fillMaps() {
		
	/*
			+---+---+
		    | ^ | A |
		+---+---+---+
		| < | v | > |
		+---+---+---+ 
	 */
		hashmap_arrow.put("AA","");
		hashmap_arrow.put("^^","");
		hashmap_arrow.put("^<","v<");
		hashmap_arrow.put("^>","v>");
		hashmap_arrow.put("^A",">");
		hashmap_arrow.put("^v","v");
		hashmap_arrow.put("<^",">^");
		hashmap_arrow.put("<<","");
		hashmap_arrow.put("<>",">>");
		hashmap_arrow.put("<A",">>^");
		hashmap_arrow.put("<v",">");
		hashmap_arrow.put(">^","<^");
		hashmap_arrow.put("><","<<");
		hashmap_arrow.put(">>","");
		hashmap_arrow.put(">A","^");
		hashmap_arrow.put(">v","<");
		hashmap_arrow.put("A^","<");
		hashmap_arrow.put("A<","v<<");
		hashmap_arrow.put("A>","v");
		hashmap_arrow.put("Av","<v");
		hashmap_arrow.put("v^","^");
		hashmap_arrow.put("v<","<");
		hashmap_arrow.put("v>",">");
		hashmap_arrow.put("vA","^>");
		hashmap_arrow.put("vv","");
/*
 	 	+---+---+---+
		| 7 | 8 | 9 |
		+---+---+---+
		| 4 | 5 | 6 |
		+---+---+---+
		| 1 | 2 | 3 |
		+---+---+---+
		    | 0 | A |
		    +---+---+ 
 */	
		hashmap_num.put("00","");
		hashmap_num.put("01","^<");
		hashmap_num.put("02","^");
		hashmap_num.put("03","^>");
		hashmap_num.put("04","^^<");
		hashmap_num.put("05","^^");
		hashmap_num.put("06","^^>");
		hashmap_num.put("07","^^^<");
		hashmap_num.put("08","^^^");
		hashmap_num.put("09","^^^>");
		hashmap_num.put("0A",">");
		hashmap_num.put("10",">v");
		hashmap_num.put("11","");
		hashmap_num.put("12",">");
		hashmap_num.put("13",">>");
		hashmap_num.put("14","^");
		hashmap_num.put("15","^>");
		hashmap_num.put("16","^>>");
		hashmap_num.put("17","^^");
		hashmap_num.put("18","^^>");
		hashmap_num.put("19","^^>>");
		hashmap_num.put("1A",">>v");
		hashmap_num.put("20","v");
		hashmap_num.put("21","<");
		hashmap_num.put("22","");
		hashmap_num.put("23",">");
		hashmap_num.put("24","<^");
		hashmap_num.put("25","^");
		hashmap_num.put("26","^>");
		hashmap_num.put("27","<^^");
		hashmap_num.put("28","^^");
		hashmap_num.put("29","^^>");
		hashmap_num.put("2A","v>");
		hashmap_num.put("30","<v");
		hashmap_num.put("31","<<");
		hashmap_num.put("32","<");
		hashmap_num.put("33","");
		hashmap_num.put("34","<<^");
		hashmap_num.put("35","<^");
		hashmap_num.put("36","^");
		hashmap_num.put("37","<<^^");
		hashmap_num.put("38","<^^");
		hashmap_num.put("39","^^");
		hashmap_num.put("3A","v");
		hashmap_num.put("40",">vv");
		hashmap_num.put("41","v");
		hashmap_num.put("42","v>");
		hashmap_num.put("43","v>>");
		hashmap_num.put("44","");
		hashmap_num.put("45",">");
		hashmap_num.put("46",">>");
		hashmap_num.put("47","^");
		hashmap_num.put("48","^>");
		hashmap_num.put("49","^>>");
		hashmap_num.put("4A",">>vv");
		hashmap_num.put("50","vv");
		hashmap_num.put("51","<v");
		hashmap_num.put("52","v");
		hashmap_num.put("53","v>");
		hashmap_num.put("54","<");
		hashmap_num.put("55","");
		hashmap_num.put("56",">");
		hashmap_num.put("57","<^");
		hashmap_num.put("58","^");
		hashmap_num.put("59","^>");
		hashmap_num.put("5A","vv>");
		hashmap_num.put("60","<vv");
		hashmap_num.put("61","<<v");
		hashmap_num.put("62","<v");
		hashmap_num.put("63","v");
		hashmap_num.put("64","<<");
		hashmap_num.put("65","<");
		hashmap_num.put("66","");
		hashmap_num.put("67","<<^");
		hashmap_num.put("68","<^");
		hashmap_num.put("69","^");
		hashmap_num.put("6A","vv");
		hashmap_num.put("70",">vvv");
		hashmap_num.put("71","vv");
		hashmap_num.put("72","vv>");
		hashmap_num.put("73","vv>>");
		hashmap_num.put("74","v");
		hashmap_num.put("75","v>");
		hashmap_num.put("76","v>>");
		hashmap_num.put("77","");
		hashmap_num.put("78",">");
		hashmap_num.put("79",">>");
		hashmap_num.put("7A",">>vvv");
		hashmap_num.put("80","vvv");
		hashmap_num.put("81","<vv");
		hashmap_num.put("82","vv");
		hashmap_num.put("83","vv>");
		hashmap_num.put("84","<v");
		hashmap_num.put("85","v");
		hashmap_num.put("86","v>");
		hashmap_num.put("87","<");
		hashmap_num.put("88","");
		hashmap_num.put("89",">");
		hashmap_num.put("8A","vvv>");
		hashmap_num.put("90","<vvv");
		hashmap_num.put("91","<<vv");
		hashmap_num.put("92","<vv");
		hashmap_num.put("93","vv");
		hashmap_num.put("94","<<v");
		hashmap_num.put("95","<v");
		hashmap_num.put("96","v");
		hashmap_num.put("97","<<");
		hashmap_num.put("98","<");
		hashmap_num.put("99","");
		hashmap_num.put("9A","vvv");
		hashmap_num.put("A0","<");
		hashmap_num.put("A1","^<<");
		hashmap_num.put("A2","<^");
		hashmap_num.put("A3","^");
		hashmap_num.put("A4","^^<<");
		hashmap_num.put("A5","<^^");
		hashmap_num.put("A6","^^");
		hashmap_num.put("A7","^^^<<");
		hashmap_num.put("A8","<^^^");
		hashmap_num.put("A9","^^^");
		hashmap_num.put("AA","");

	}
}

record Key(String code, int depth) {
};