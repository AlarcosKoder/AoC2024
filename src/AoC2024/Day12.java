package AoC2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day12 extends Day {
	private Character[][] map;
	private int width;
	private int height;
	
	private List<Coordinate> directions = Arrays.asList(
            new Coordinate(1, 0),
            new Coordinate(-1, 0),
            new Coordinate(0, 1),
            new Coordinate(0, -1)
        );
	
	public Day12() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}

	public Set<Coordinate> perimeter(Coordinate coord,Set<Coordinate> gathered,List<Coordinate> non_visited) {
		Coordinate coord2 = coord.add(directions.get(0));
		if(non_visited.contains(coord2) && !gathered.contains(coord2) && map[coord.getX()][coord.getY()]==map[coord2.getX()][coord2.getY()]) {
			gathered.add(coord2);
			non_visited.remove(coord2);
			gathered.addAll(perimeter(coord2, gathered, non_visited));
		}
		coord2 = coord.add(directions.get(1));
		if(non_visited.contains(coord2) && !gathered.contains(coord2) && map[coord.getX()][coord.getY()]==map[coord2.getX()][coord2.getY()]) {
			gathered.add(coord2);
			non_visited.remove(coord2);
			gathered.addAll(perimeter(coord2, gathered, non_visited));
		}
		coord2 = coord.add(directions.get(2));
		if(non_visited.contains(coord2) && !gathered.contains(coord2) && map[coord.getX()][coord.getY()]==map[coord2.getX()][coord2.getY()]) {
			gathered.add(coord2);
			non_visited.remove(coord2);
			gathered.addAll(perimeter(coord2, gathered, non_visited));
		}
		coord2 = coord.add(directions.get(3));
		if(non_visited.contains(coord2) && !gathered.contains(coord2) && map[coord.getX()][coord.getY()]==map[coord2.getX()][coord2.getY()]) {
			gathered.add(coord2);
			non_visited.remove(coord2);
			gathered.addAll(perimeter(coord2, gathered, non_visited));
		}
		return gathered;
	}
	
	public void processFile() {
		
		String[] lines = sb.toString().split("\n");
		width=lines[0].length();
		height=lines.length;
		
		map = new Character[height][width];
//		Map<Character, Set<Coordinate>> garden = new HashMap<Character, Set<Coordinate>>();
		Map<Character, List<Set<Coordinate>>> gardenparts = new HashMap<Character, List<Set<Coordinate>>>();
		List<Coordinate> non_visited = new ArrayList<Coordinate>();
		
		
		for(int x = 0; x < height; x++) {
			String line = lines[x];
			for(int y=0; y < width; y++) {
				Character c = line.charAt(y);
//				Set<Coordinate> coords;
//				if(!garden.containsKey(c)) {
//					coords = new HashSet<Coordinate>();
//					garden.put(c, coords);
//				} else
//					coords = garden.get(c);
				
				map[x][y]= c;
				Coordinate coord = new Coordinate(x, y);
				non_visited.add(coord);
//				coords.add(coord);
			}
		}
		
		long result_part1 = 0;
		
		while(non_visited.size()>0) {
			Coordinate c = non_visited.remove(0);
			
			List<Set<Coordinate>> list = gardenparts.getOrDefault(map[c.getX()][c.getY()], new ArrayList<Set<Coordinate>>());
			Set<Coordinate> gathered = new HashSet<Coordinate>();
			list.add(gathered);
			gathered.add(c);
			
			List<Coordinate> tocheck = new ArrayList<Coordinate>();
			tocheck.add(c);
			
			while(tocheck.size()>0) {
				c = tocheck.remove(0);
				for (Coordinate dir : directions) {
					Coordinate c2 = c.add(dir);
					if(non_visited.contains(c2) && map[c.getX()][c.getY()] == map[c2.getX()][c2.getY()]) {
						non_visited.remove(c2);
						gathered.add(c2);
						tocheck.add(c2);
					}
				}
			}
			
			gardenparts.put(map[c.getX()][c.getY()], list);
			
		}
		
		for (Character ch : gardenparts.keySet()) {
			List<Set<Coordinate>> list = gardenparts.get(ch);
			
			for (Set<Coordinate> coords : list) {
				int size = coords.size();
				int wall = 0;
				for (Coordinate c : coords) {
					if(!coords.contains(c.add(directions.get(0)))) wall++;
					if(!coords.contains(c.add(directions.get(1)))) wall++;
					if(!coords.contains(c.add(directions.get(2)))) wall++;
					if(!coords.contains(c.add(directions.get(3)))) wall++;
				}
				result_part1 += size*wall;
			}
		}
		System.out.println("result part1: "+result_part1); // correct: 1522850
		
		
		long result_part2 = 0;
		for (Character ch : gardenparts.keySet()) {
			List<Set<Coordinate>> list = gardenparts.get(ch);
			
			for (Set<Coordinate> coords : list) {
				int size = coords.size();
				int side = calculateSides(coords);
				result_part2 += size*side;
			}
		}
		//TODO: implement this part
		System.out.println("result part2: "+result_part2); // correct: 953738
	}
	
	public int calculateSides(Set<Coordinate> region) {
		int fences = 0;
        Set<Edge> seen = new HashSet<>();
        
        
        for (Coordinate position : region) {
            for (Coordinate outside : directions) {
                if (region.contains(position.add(outside))) {
                    continue;
                }

                Edge current = new Edge(position, outside);
                Coordinate direction = new Coordinate(outside.getY(), -outside.getX());

                while (!seen.contains(current)) {
                    seen.add(current);

                    if (region.contains(current.position.add(direction))) {
                        if (region.contains(current.position.add(direction).add(current.outside))) {
                            fences++;
                            current = new Edge(
                                current.position.add(direction).add(current.outside),
                                new Coordinate(-current.outside.getY(), current.outside.getX())
                            );
                            direction = new Coordinate(-direction.getY(), direction.getX());
                        } else {
                            current = new Edge(current.position.add(direction), current.outside);
                        }
                    } else {
                        fences++;
                        current = new Edge(current.position, new Coordinate(current.outside.getY(), -current.outside.getX()));
                        direction = new Coordinate(direction.getY(), -direction.getX());
                    }
                }
            }
        }

	    return fences;
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day12 day = new Day12();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	static class Edge {
        Coordinate position;
        Coordinate outside;

        Edge(Coordinate position, Coordinate outside) {
            this.position = position;
            this.outside = outside;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Edge edge = (Edge) o;
            return Objects.equals(position, edge.position) && Objects.equals(outside, edge.outside);
        }

        @Override
        public int hashCode() {
            return Objects.hash(position, outside);
        }
    }
}