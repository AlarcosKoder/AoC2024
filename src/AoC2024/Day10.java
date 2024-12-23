package AoC2024;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day10 extends Day {
	
	private int width;
	private int height;
	
	public Day10() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		width = 0;
		height = 0;
	}
	
	public int bfsTrail(int[][] map, int value,int x, int y, HashMap<Coordinate, Set<Coordinate>> scores, Coordinate th) {
		int nextValue = value+1;
		if(map[x][y]==9 && nextValue==10) {
			scores.get(th).add(new Coordinate(x, y));
			return 1;
		}
		int sum = 0;
		if(x > 0 && map[x-1][y]==nextValue) { //up
			sum+=bfsTrail(map,nextValue,x-1,y,scores,th);
		}
		if(x < height-1 && map[x+1][y]==nextValue) { //down
			sum+=bfsTrail(map,nextValue,x+1,y,scores,th);
		}
		if(y > 0 && map[x][y-1]==nextValue) { //left
			sum+=bfsTrail(map,nextValue,x,y-1,scores,th);
		}
		if(y < width-1 && map[x][y+1]==nextValue) { //right
			sum+=bfsTrail(map,nextValue,x,y+1,scores,th);
		}
		return sum;
	}
	
    public int dfsTrail(int[][] map, int x, int y, Map<Coordinate, Set<Coordinate>> scores, Coordinate trailhead) {
        Stack<int[]> stack = new Stack<>();
        int trailsCount = 0;

        stack.push(new int[]{x, y, 0});

        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int cx = current[0];
            int cy = current[1];
            int value = current[2];

            if (map[cx][cy] == 9 && value + 1 == 10) {
                scores.get(trailhead).add(new Coordinate(cx, cy));
                trailsCount++;
                continue;
            }

            int nextValue = value + 1;
            if (cx > 0 && map[cx - 1][cy] == nextValue) {
                stack.push(new int[]{cx - 1, cy, nextValue});
            }
            if (cx < height - 1 && map[cx + 1][cy] == nextValue) {
                stack.push(new int[]{cx + 1, cy, nextValue});
            }
            if (cy > 0 && map[cx][cy - 1] == nextValue) {
                stack.push(new int[]{cx, cy - 1, nextValue});
            }
            if (cy < width - 1 && map[cx][cy + 1] == nextValue) {
                stack.push(new int[]{cx, cy + 1, nextValue});
            }
        }

        return trailsCount;
    }

	public void processFile() {
		
		String[] lines = sb.toString().split("\n");
		width=lines[0].length();
		height=lines.length;
		
		int[][] map= new int[height][width];
		HashMap<Coordinate, Set<Coordinate>> scores = new HashMap<Coordinate, Set<Coordinate>>();
		
		for(int x = 0; x < height; x++) {
			String line = lines[x];
			for(int y=0; y < width; y++) {
				int value = Character.getNumericValue(line.charAt(y));
				map[x][y] = value;
				if(value==0) {
					scores.put(new Coordinate(x, y), new HashSet<Coordinate>());
				}
			}
		}

		long result_part1 = 0;
		long result_part2 = 0;
		for (Coordinate c : scores.keySet()) {
//			result_part2 += bfsTrail(map,0,c.getX(),c.getY(),scores,c);
			result_part2 += dfsTrail(map, c.getX(), c.getY(), scores, c);
		}
		for (Coordinate c : scores.keySet()) {
			result_part1+=scores.get(c).size();
		}
		System.out.println("result part1: "+result_part1); // correct: 617	
		System.out.println("result part2: "+result_part2); // correct: 1477
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day10 day = new Day10();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
