package AoC2024;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day06 extends Day {
	
	private class CoordinateDir extends Coordinate {
		private final int d;
		public CoordinateDir(int x, int y, int direction) {
			super(x,y);
			d=direction;
		}
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        CoordinateDir that = (CoordinateDir) o;
	        return x == that.getX() && y == that.getY() && d == that.d;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(x, y,d);
	    }

	    @Override
	    public String toString() {
	        return "(" + x + "," + y + ","+ d +")";
	    }
	    public int getD() {
	        return d;
	    }
	}
    
	private char[][] map;
    private int rows, cols;

    public Day06() {
    	super();
    	INPUT_REAL = true;
    }

    public void processFile() {
        map = readMap();
        rows = map.length;
        cols = map[0].length;

        // Directions: Up, Right, Down, Left (clockwise rotation)
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, 1, 0, -1};

        // Find the guard's initial position and direction
        int guardX=0, o_guardX = 0, guardY=0, o_guardY = 0, direction = -1; // 0 = Up
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == '^') {
                	o_guardX = guardX = i;
                	o_guardY = guardY = j;
                    direction = 0; // Facing up
                    break;
                }
            }
            if(direction==0)break;
        }

        // Set to track distinct visited positions
        Set<Coordinate> visited = new HashSet<>();
        visited.add(new Coordinate(o_guardX, o_guardY));

        while (true) {
            int nextX = guardX + dx[direction];
            int nextY = guardY + dy[direction];

            // Check if the guard leaves the map
            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
                break;
            }

            // Check if the next position is an obstacle
            if (map[nextX][nextY] == '#') {
                // Turn right 90 degrees
                direction = (direction + 1) % 4;
            } else {
                // Move forward
                guardX = nextX;
                guardY = nextY;
                visited.add(new Coordinate(guardX,guardY));
            }
        }
        
        long result_part1 = visited.size();
        logln("result part1: " + result_part1); // Correct: 5453
        
        
        Set<Coordinate> check4loops = new HashSet<>(visited);
        long result_part2 = 0;
        
        for (Coordinate c : check4loops) {
			int i = c.getX();
			int j = c.getY();
				
			guardX = o_guardX;
			guardY = o_guardY;
			direction = 0;
			
			if(i == guardX && j == guardY) continue;
			
			char original = map[i][j];
			map[i][j]='#';
			
			
			Set<CoordinateDir> loop_detector = new HashSet<>();
	        loop_detector.add(new CoordinateDir(guardX,guardY,direction));

	        while (true) {
	            int nextX = guardX + dx[direction];
	            int nextY = guardY + dy[direction];

	            if (nextX < 0 || nextX >= rows || nextY < 0 || nextY >= cols) {
	                break;
	            }

	            if (map[nextX][nextY] == '#') {
	                direction = (direction + 1) % 4;
	            } else {
	                guardX = nextX;
	                guardY = nextY;
	                CoordinateDir cd = new CoordinateDir(guardX,guardY,direction);
	                if(loop_detector.contains(cd)) {
	                	result_part2++;
	                	break;
	                }
	                loop_detector.add(cd);
	            }
	        }
	        map[i][j]=original;
		        
		}
        logln("result part2: " + result_part2); // Correct: 2188
        
    }

    private char[][] readMap() {
        String[] lines = sb.toString().split("\n");
        char[][] map = new char[lines.length][lines[0].length()];
        for (int i = 0; i < lines.length; i++) {
            map[i] = lines[i].toCharArray();
        }
        return map;
    }

    public static void main(String[] args) {
        long time_start = System.currentTimeMillis();
        Day06 day = new Day06();
        day.readFile();
        day.processFile();
        long time_end = System.currentTimeMillis();
        day.logln("Execution time: " + (time_end - time_start) + " msec");
        day.flush();
    }
}

