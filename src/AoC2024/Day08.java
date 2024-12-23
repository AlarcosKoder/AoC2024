package AoC2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day08 extends Day {

	private char[][] map;
    private int rows, cols;
	
	public Day08() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}

	private boolean isRealCoordinate(Coordinate c) {
		return c.getX()>=0 && c.getY()>=0 && c.getX() < rows && c.getY() < cols;
	}
	
	public void processFile() {
		
		HashMap<Character, List<Coordinate>> values = new HashMap<Character, List<Coordinate>>();
		HashSet<Coordinate>	antinodes = new HashSet<Coordinate>();
		HashSet<Coordinate>	antinodes_part2 = new HashSet<Coordinate>();
		String[] lines = sb.toString().split("\n");
        map = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
            
            for (int j = 0; j < map[i].length; j++) {
            	Character ch = Character.valueOf(map[i][j]);
            	if(ch=='.') continue;
            	Coordinate coord = new Coordinate(i, j);
            	if(!values.containsKey(ch)) {
            		values.put(ch, new ArrayList<Coordinate>(Collections.singleton(coord)));
    			} else  {
    				values.get(ch).add(coord);
    			}
			}
             	
        }
        rows = map.length;
        cols = map[0].length;
        
        for (List<Coordinate> c_list : values.values()) {
        	for (Coordinate c1 : c_list) {
        		for (Coordinate c2 : c_list) {
            		if(c1==c2) continue;
            		int diffX = c1.getX()-c2.getX();
            		int diffY = c1.getY()-c2.getY();
            		
            		int m1 = diffX==0?0:rows/Math.abs(diffX);
            		int m2 = diffY==0?0:cols/Math.abs(diffY);
            		int m = Math.max(m1, m2);
            		
            		
            		for (int i = 1; i < m; i++) {
            			Coordinate a1 = new Coordinate(c1.getX()+i*diffX, c1.getY()+i*diffY);
	            		Coordinate a2 = new Coordinate(c2.getX()-i*diffX, c2.getY()-i*diffY);
	            		if(isRealCoordinate(a1)) {
	            			if(i==1) antinodes.add(a1);
	            			antinodes_part2.add(a1);
	            		}
	            		if(isRealCoordinate(a2)) {
	            			if(i==1) antinodes.add(a2);
	            			antinodes_part2.add(a2);
	            		}
					}
    			}
        		antinodes_part2.add(c1);
			}
		}
        
		long result_part1 = antinodes.size();
		
		System.out.println("result part1: "+result_part1); // correct: 423
		
		
		long result_part2 = antinodes_part2.size();
//		visualize(antinodes_part2);
		System.out.println("result part2: "+result_part2); // correct: 1287
	}
	
	public void visualize(HashSet<Coordinate> antinodes) {
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				Coordinate c = new Coordinate(i, j);
				log(antinodes.contains(c)?"#":map[i][j]+"");
			}
			logln("");
		}
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day08 day = new Day08();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
    }
}
