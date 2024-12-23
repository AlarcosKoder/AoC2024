package AoC2024;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeMap;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day20 extends Day {
	
	private int width;
	private int height;
	private Place[][] map;
	private Place start;
    private Place end;
    private final int[] dx = {-1, 0, 1, 0};
    private final int[] dy = {0, 1, 0, -1};
	
	public Day20() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		start = null;
		end = null;
		
	}
	
	public List<Place> escape_no_cheat() {
		PriorityQueue<StackState> queue = new PriorityQueue<>((a, b) -> Integer.compare(heuristic(a), heuristic(b)));
		List<Place> shortest_paths = new ArrayList<Place>();
		queue.add(new StackState(start.y, start.x, 0,List.of(start)));
		int min_score = Integer.MAX_VALUE;
		
		while (!queue.isEmpty()) {
	        StackState s = queue.poll();
	        if (s.y == end.y && s.x == end.x) {
	            if (s.score < min_score) {
	            	min_score = s.score;
	            	shortest_paths=s.visited;
	            }
	            continue;
	        }
	        
	        for (int i = 0; i < dx.length; i++) {
	        	int newX = s.x + dx[i];
	            int newY = s.y + dy[i];
	            
	            if (newX < 0 || newX >= width || newY < 0 || newY >= height) continue;
	            Place next = map[newY][newX];
	            
	            int newScore = s.score + 1;
	            
	            if (next.wall) continue;
	            
	            if (newScore <= next.min_score) {
	            	next.min_score = newScore;
	            	
	            	List<Place> new_list = new ArrayList<Place>(s.visited);
	            	new_list.add(next);
	            	
	                queue.add(new StackState(newY, newX, newScore,new_list));
	            }
    		}
		 }
		return shortest_paths;
	}
	
	private int heuristic(StackState state) {
	    return Math.abs(end.x - state.x) + Math.abs(end.y - state.y);
	}
	
	public void processFile() {
		
		final char char_wall = "#".charAt(0);
		final char char_free = ".".charAt(0);
		final char char_start = "S".charAt(0);
		final char char_end = "E".charAt(0);
		
		String[] lines = sb.toString().split("\n");
		width = lines[0].length();
		height = lines.length;
		map = new Place[height][width];
		
		for (int j = 0; j < map.length; j++) { //y
			for (int i = 0; i < map[j].length; i++) { //x
				char c = lines[j].charAt(i);
				if(c==char_wall) {
					map[j][i] = new Place(i, j, true);
				} else if(c==char_free) {
					map[j][i] = new Place(i, j, false);
				} else if(c==char_start) {
					map[j][i] = new Place(i, j, false);
					start = map[j][i];
				} else if(c==char_end) {
					map[j][i]=new Place(i, j, false);
					end = map[j][i];
				}
			}
		}
		//shortest no cheat:9372
		//shortest with cheating: 244
		List<Place> shortest_path = escape_no_cheat();
		System.out.println("result part1 length: "+shortest_path.size()+" path:"+shortest_path);
		for (int i = 0; i < shortest_path.size(); i++) {
			Place p = shortest_path.get(i);
			p.score_from_start=i;
			p.score_to_end=shortest_path.size()-i;
		}
		
		Map<Integer,Integer> scores = new TreeMap<Integer, Integer>();
		
		for (int i = 0; i < shortest_path.size(); i++) {
			Place place = shortest_path.get(i);
			Set<Place> potential_places = getNonWallsInDistance(place, 2, width, height);
			for (Place pot : potential_places) {
				if(pot!=place&& !pot.wall) {
					int m_dist = manhattanDistance(pot, place);
	            	if(pot.score_to_end < place.score_to_end+m_dist) {
	            		int score = place.score_to_end-m_dist-pot.score_to_end;
	            		if(score>=100)scores.put(score, scores.getOrDefault(score, 0)+1);
	            	}
	            }
			}
		}
		
		
		long result_part1 = scores.values().stream().reduce(0, Integer::sum);
		System.out.println("result part1: "+result_part1); // correct: 
		scores.clear();
		
		for (int i = 0; i < shortest_path.size(); i++) {
			Place place = shortest_path.get(i);
			Set<Place> potential_places = getNonWallsInDistance(place, 20, width, height);
			for (Place pot : potential_places) {
				if(pot!=place && !pot.wall) {
					int m_dist = manhattanDistance(pot, place);
	            	if(pot.score_to_end < place.score_to_end+m_dist) {
	            		int score = place.score_to_end-m_dist-pot.score_to_end;
	            		if(score>=100)scores.put(score, scores.getOrDefault(score, 0)+1);
	            	}
	            }
			}
		}
		
		long result_part2 = scores.values().stream().reduce(0, Integer::sum);
		
		System.out.println("result part2: "+result_part2); // correct: 1013106
	}
	
	 public int manhattanDistance(Coordinate c1, Coordinate c2) {
	        return Math.abs(c1.x - c2.x) + Math.abs(c1.y - c2.y);
	    }
	
	public Set<Place> getNonWallsInDistance(Place origin, int dist, int width, int height){
		Set<Place> result = new HashSet<>();

        for (int dx = -dist; dx <= dist; dx++) {
            for (int dy = -dist; dy <= dist; dy++) {
                if (Math.abs(dx) + Math.abs(dy) <= dist) {
                    int newX = origin.x + dx;
                    int newY = origin.y + dy;
                    
                    if (newX < 0 || newX >= width || newY < 0 || newY >= height) continue;
                    Place p = map[newY][newX];
                    result.add(p);
                }
            }
        }

        return result;
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day20 day = new Day20();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	class StackState {
		int y = 0;
        int x = 0;
        int score = 0;
        List<Place> visited = null;
        
        public StackState(int _y, int _x, int _score, List<Place> _visited) {
        	this.y=_y;
        	this.x=_x;
        	this.score=_score;
        	this.visited=_visited;
        }
        
        @Override
        public String toString() {
        	return "("+y+","+x+") s:"+score;
        }
	}
	
	class Place extends Coordinate {
		boolean wall;
		int min_score;
		int score_from_start;
		int score_to_end;
		
		public Place(int x, int y, boolean _wall) {
			super(x, y);
			this.wall = _wall;
			min_score = Integer.MAX_VALUE;
		}
		@Override
		public String toString() {
			return (wall?"#":".")+super.toString();
		}
		public void step() {
		}
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Place that = (Place) o;
	        return x == that.x && y == that.y;
	    }
	}
}
