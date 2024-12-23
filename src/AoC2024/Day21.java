package AoC2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import AoC2022.Day;
import AoC2024.Day21_1223_backup.Place;
import AoC2024.utils.Coordinate;

public class Day21 extends Day {
	
	private Map<Character,Map<Character,Set<String>>> hashmap_num;
	private Place[][] map_num;
	private char[] elements_num;
	
	private Map<Character,Map<Character,Set<String>>> hashmap_arrow;
	private Place[][] map_arrow;
	private char[] elements_arrow;
	
	private final char _CA='X';
	private final char _ACK='A';
	
	private final int[] dx = {-1, 0, 1, 0};
    private final int[] dy = {0, 1, 0, -1};
    private final char[] dc = {'<', 'v', '>', '^'};
    
	public Day21() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = false;
		
		char[][] pad_num = new char[][]{{'7','8','9'},{'4','5','6'},{'1','2','3'},{_CA,'0','A'}};
		map_num = new Place[pad_num.length][pad_num[0].length];
		for (int j = 0; j < pad_num.length; j++) {
			for (int i = 0; i < pad_num[0].length; i++) {
				map_num[j][i]=new Place(i, j, pad_num[j][i], pad_num[j][i]==_CA?true:false);
			}
		}
		elements_num=new char[]{'0','1','2','3','4','5','6','7','8','9','A'};
		hashmap_num = new HashMap<Character, Map<Character,Set<String>>>();
		
		char[][] pad_arrow = new char[][]{{_CA,'^','A'},{'<','v','>'}};
		map_arrow = new Place[pad_arrow.length][pad_arrow[0].length];
		for (int j = 0; j < pad_arrow.length; j++) {
			for (int i = 0; i < pad_arrow[0].length; i++) {
				map_arrow[j][i]=new Place(i, j, pad_arrow[j][i], pad_arrow[j][i]==_CA?true:false);
			}
		}
		elements_arrow=new char[]{'<','v','>','^','A'};
		hashmap_arrow = new HashMap<Character, Map<Character,Set<String>>>();
	}
	
	public int calcScore(String _str) {
        int count = 0;
        for (int i = 1; i < _str.length(); i++) {
            if (_str.charAt(i) == _str.charAt(i - 1)) {
            	count++;
            } 
        }
		return count;
	}
	
	public void processFile() {
		String[] lines = sb.toString().split("\n");
		fillMap(elements_num, map_num, hashmap_num);
		fillMap(elements_arrow, map_arrow, hashmap_arrow);
		

		for (char from : elements_arrow) {
			for (char to : elements_arrow) {
				String ft = ""+from+to;
				System.out.print(ft+" -> ");
				List<String> l = hashmap_arrow.get(from).get(to).stream().collect(Collectors.toList());
				for (int i = 0; i < l.size(); i++) {
					String s = l.get(i);
					if(!s.endsWith(_ACK+"")) s+=_ACK;
						
					if(i!=l.size()-1)
						System.out.print(s+" | ");
					else
						System.out.print(s);
				}
				System.out.println();
				
			}
		}
		System.out.println();

		
		long result_part1 = 0;
		
		
		logln("result part1: "+result_part1); // correct: 278568
		
		long result_part2 = 0;
		
		logln("result part2: "+result_part2); // correct: 
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
	
	public Place find_char_in_array(Place[][] arr,char _ch) {
		for (int j = 0; j < arr.length; j++) { //y
			for (int i = 0; i < arr[j].length; i++) { //x
				if(arr[j][i].ch==_ch) return arr[j][i];
			}
		}
		return null;
	}
	
	public Set<String> find_shortest_from_to(Place[][] arr,char char_from, char char_to) {
		if(char_from == char_to) return new HashSet<String>(List.of(_ACK+""));
		
		int height = arr.length;
		int width=arr[0].length;
		
		Place from = find_char_in_array(arr,char_from);
		Place to = find_char_in_array(arr,char_to);
		
		PriorityQueue<StackState> queue = new PriorityQueue<>((a, b) -> Integer.compare(heuristic(a), heuristic(b)));
		Set<String> shortest_paths = new HashSet<String>();
		queue.add(new StackState(from.y, from.x, 0,"",from,to,0));
		int min_score = Integer.MAX_VALUE;
		
		while (!queue.isEmpty()) {
	        StackState s = queue.poll();
	        if (s.y == to.y && s.x == to.x) {
	            if (s.score < min_score) {
	            	shortest_paths.clear();
	            	min_score = s.score;
	            	shortest_paths.add(s.visited);
	            } else if(s.score == min_score) {
	            	shortest_paths.add(s.visited);
	            }
	            continue;
	        }
	        
	        for (int i = 0; i < dx.length; i++) {
	        	int newX = s.x + dx[i];
	            int newY = s.y + dy[i];
	            
	            if (newX < 0 || newX >= width || newY < 0 || newY >= height || arr[newY][newX].wall) continue;
	            Place next = arr[newY][newX];
	            
	            int newScore = s.score + 1;
	            
	            if (newScore <= next.min_score) {
	            	int turnScore = (s.dir != i && s.dir != -1) ? 1 : 0;
	            	newScore+=turnScore; //TODO:benne hagyni, sokat gyorsít
	            	next.min_score = newScore;
	                queue.add(new StackState(newY, newX, newScore,s.visited+dc[i],s.from,s.to,i));
	            }
    		}
		}
		return shortest_paths;
	}
	
	public void fillMap(char[] _elements, Place[][] _map, Map<Character, Map<Character,Set<String>>> _hashmap) {
		for (char from : _elements) {
			Map<Character,Set<String>> row = new HashMap<Character,Set<String>>();
			for (char to : _elements) {
				row.put(to, find_shortest_from_to(_map,from,to));
				resetDist(_map);
			}
			_hashmap.put(Character.valueOf(from), row);
		}
	}
	
	private void resetDist(Place[][] _map) {
		for (int j = 0; j < _map.length; j++) { //y
			for (int i = 0; i < _map[j].length; i++) { //x
				_map[j][i].min_score=Integer.MAX_VALUE;
			}
		}
	}
	
	private int heuristic(StackState state) {
	    return Math.abs(state.to.x - state.x) + Math.abs(state.to.y - state.y);
	}
	
	private String decode(String _chars, Place[][] map, char _start) {
		Place start_p = null;
		for (int j = 0; j < map.length; j++) {
			for (int i = 0; i < map[j].length; i++) {
				if(map[j][i].ch==_start) {
					start_p = map[j][i];
					break;
				}
					 
			}
		}
		
		int newX = start_p.x;
		int newY = start_p.y;
		
		StringBuffer sb = new StringBuffer();
    	for (char c : _chars.toCharArray()) {
    		
    		int _dir=-2;
    		switch (c) {
				case '<':_dir=0; break;
				case 'v':_dir=1; break;
				case '>':_dir=2; break;
				case '^':_dir=3; break;
				case _ACK:_dir=-1; break;
			}
			if(_dir != -1) {
				newX += dx[_dir];
				newY += dy[_dir];
			} else {
				sb.append(map[newY][newX].ch);
			}
		}
    	System.out.println(_chars+" dechipered: "+sb.toString());
    	return sb.toString();
    }
	
	class ScoredPath {
		String value;
		HashMap<String, String> memo;
		
		public String getValue() {
			return value;
		}

		int score;
		
		public int getScore() {
			return score;
		}

		public ScoredPath(String _str, int _score) {
			this.value=_str;
			this.score=_score;
			memo=new HashMap<String, String>();
		}
		
		@Override
        public String toString() {
        	return value+" s:"+score;
        }
	}
	
	class StackState {
		Place from;
		Place to;
		int y = 0;
        int x = 0;
        int score = 0;
        int dir = 0;
        String visited = null;
        
        public StackState(int _y, int _x, int _score, String _visited, Place _from, Place _to, int _dir) {
        	this.y=_y;
        	this.x=_x;
        	this.score=_score;
        	this.visited=_visited;
        	this.from=_from;
        	this.to=_to;
        	this.dir=_dir;
        }
        
        @Override
        public String toString() {
        	return "("+y+","+x+") s:"+score;
        }
	}
	
	/*	+---+---+---+
		| 7 | 8 | 9 |
		+---+---+---+
		| 4 | 5 | 6 |
		+---+---+---+
		| 1 | 2 | 3 |
		+---+---+---+
		    | 0 | A |
		    +---+---+
		    
			+---+---+
		    | ^ | A |
		+---+---+---+
		| < | v | > |
		+---+---+---+ 
	 */
	
	class Place extends Coordinate {
		boolean wall;
		char ch;
		int min_score;
		
		public Place(int x, int y, char _ch, boolean _wall) {
			super(x, y);
			this.ch=_ch;
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
