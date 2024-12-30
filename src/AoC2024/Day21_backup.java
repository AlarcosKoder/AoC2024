package AoC2024;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day21_backup extends Day {
	private Map<Character,Map<Character,List<String>>> hashmap_num;
	private Place[][] map_num;
	private char[] elements_num;
	
	private Map<Character,Map<Character,List<String>>> hashmap_arrow;
	private Place[][] map_arrow;
	private char[] elements_arrow;
	
	private final char _CA='X';
	private final char _ACK='A';
	private final String _ACK_STR=String.valueOf(_ACK);
	
	private final int[] dx = {-1, 0, 1, 0};
    private final int[] dy = {0, 1, 0, -1};
    private final char[] dc = {'<', 'v', '>', '^'};
    
	public Day21_backup() {
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
		hashmap_num = new HashMap<Character, Map<Character,List<String>>>();
		
		char[][] pad_arrow = new char[][]{{_CA,'^','A'},{'<','v','>'}};
		map_arrow = new Place[pad_arrow.length][pad_arrow[0].length];
		for (int j = 0; j < pad_arrow.length; j++) {
			for (int i = 0; i < pad_arrow[0].length; i++) {
				map_arrow[j][i]=new Place(i, j, pad_arrow[j][i], pad_arrow[j][i]==_CA?true:false);
			}
		}
		elements_arrow=new char[]{'<','v','>','^','A'};
		hashmap_arrow = new HashMap<Character, Map<Character,List<String>>>();
		
	}
	
	static public int calcScore(String _str) {
        int count = 0;
        for (int i = 1; i < _str.length(); i++) {
            if (_str.charAt(i) == _str.charAt(i - 1)) {
            	count++;
            } 
        }
		return count;
	}
	
	public List<ScoredPath> encodeHeuristics(ScoredPath _sp, Map<Character, Map<Character, List<String>>> _hashmap, char _ch_prev) {
		final int K = 25;
		
		List<ScoredPath> currentPaths = new ArrayList<>();
		ScoredPath new_sp = new ScoredPath(null, 0);
		new_sp.anchestors.addAll(_sp.anchestors);
	    currentPaths.add(new_sp);
		// v<A<AA>>^AvAA^<Av<<A
		
	    for (int i = 0; i < _sp.values.size(); i++) {
	    	String _value_part = _sp.memo_id_str.get(_sp.values.get(i));
	    	StringBuilder old_string = new StringBuilder();
	    	StringBuilder new_string = new StringBuilder();
	    	
	    	for (char _ch_next : _value_part.toCharArray()) {
	    		PriorityQueue<ScoredPath> queue = new PriorityQueue<>(Comparator.comparingInt(ScoredPath::getScore));
	    		
	    		old_string.append(_ch_next);
				// addig kell olvasni amig nem nem érünk egy elágazáshoz
	    		List<String> steps = _hashmap.get(_ch_prev).get(_ch_next);
				if(steps.size()==1) {
					String step = steps.get(0);
					new_string.append(step);
					if(!step.equals(_ACK_STR)) {
						new_string.append(_ACK_STR);
					}
					
				} else {
					
					for (ScoredPath scoredPath : currentPaths) {
						for (String step : steps) {
							
//							String _post = step.equals(String.valueOf(_ACK))?scoredPath.value+step:scoredPath.value+step+_ACK;
							
//							String _new_str = new_string.append(step).append(step.equals(_ACK_STR)?"":_ACK_STR).toString();
							String _temp = new_string.toString();
							String _new_str = _temp+step+(step.equals(_ACK_STR)?"":_ACK_STR);
							//v<A<AA>>^AvAA^<A
//							String _new_str = new_string.toString()+step+(step.equals(_ACK_STR)?"":_ACK_STR);
							ScoredPath _spn = new ScoredPath(scoredPath,_new_str, calcScore(_new_str));
							queue.add(_spn);
							// <vA<AA>>^AvAA^<Av<<A>>^AvA^Av<<A>>^AAvA<A^>A<A>Av<A<A>>^AAA<Av>A
							if(queue.size() > K) {
								queue.poll();
								
							}
						}
					}
					currentPaths = new ArrayList<>(queue);
					old_string = new StringBuilder();
			    	new_string = new StringBuilder();
				}
				
				
				_ch_prev = _ch_next;
				
			}
		}
		return currentPaths;
	}


	
	public Set<String> encode(String _str, Map<Character,Map<Character,List<String>>> _hashmap, char _ch_prev){
		Set<String> result = new HashSet<>();
	    
	    for (char _ch_next : _str.toCharArray()) {
	        // Get the set of paths from _ch_prev to _ch_next
	    	List<String> currentPaths = _hashmap.get(_ch_prev).get(_ch_next);
	        Set<String> newResult = new HashSet<>();
	        
	        if (result.isEmpty()) {
	            // Initialize result with the first step's paths
	            newResult.addAll(currentPaths);
	        } else {
	            // Combine existing paths in result with current paths
	        	if(!currentPaths.isEmpty()) {
	        		for (String existingPath : result) {
	            	
	            		for (String newPath : currentPaths) {
		                    String combinedPath = existingPath;
		                    combinedPath+=newPath;
		                    newResult.add(combinedPath);
		                }
	        		}
	        	} else {
            		newResult = result;
            	}
	        }
	        Set<String> newResultWitA = new HashSet<>();
	        for (String _nl : newResult) {
	        	newResultWitA.add(_nl+_ACK);
//	        	logln("parts:"+newResultWitA);
			}
	        
	        // Update result for the next iteration
	        result = newResultWitA;
	        _ch_prev = _ch_next;
	    }
	    
		return result;
	}
	
	public Set<ScoredPath> encode_possible_strings_Heuristics(Set<ScoredPath> _to_be_encoded_strs, final Map<Character,Map<Character,List<String>>> _hashmap, char _ch_prev, int _lvl){
		Set<ScoredPath> encoded_strs = new HashSet<ScoredPath>();
		for (ScoredPath _sp : _to_be_encoded_strs) {
			List<ScoredPath> chs = encodeHeuristics(_sp,_hashmap,_ch_prev);
			encoded_strs.addAll(chs);
		}
		return encoded_strs;
	}
	
	public Set<String> encode_possible_strings(Set<String> _to_be_encoded_strs, final Map<Character,Map<Character,List<String>>> _hashmap, char _ch_prev, int _lvl){
		Set<String> encoded_strs = new HashSet<String>();
		for (String _sp : _to_be_encoded_strs) {
			Set<String> chs = encode(_sp,_hashmap,_ch_prev);
			encoded_strs.addAll(chs);
		}
		return encoded_strs;
	}
	
	private String decode(ScoredPath sp, Place[][] map, char _start) {
		StringBuffer sb = new StringBuffer();
		for (Integer number : sp.values) {
			String _str = sp.memo_id_str.get(number);
			sb.append(_str);
		}
		return decode(sb.toString(),map_arrow,_start);
	}
	
	private String decode(String _chars, Place[][] map, char _start) {
		System.out.println("decode: "+_chars);
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
		int counter = 0;
		StringBuffer sb = new StringBuffer();
		char prev =0;
    	for (char c : _chars.toCharArray()) {
    		counter++;
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
			prev = c;
		}
    	System.out.println(_chars+" dechipered: "+sb.toString());
    	return sb.toString();
    }
	

	
	public void processFile() {
//		decode("v<A<AA>>^AvAA^<A", map_arrow, _ACK);
//		decode("v<A<AA>>^AvAA^<Av<<A", map_arrow, _ACK);
		
		String[] lines = sb.toString().split("\n");
		fillMap(elements_num, map_num, hashmap_num);
		fillMap(elements_arrow, map_arrow, hashmap_arrow);
		
		long result_part1 = 0;
		char _ch_prev = _ACK;
		
		for (String _string : lines) {
			int code = Integer.parseInt(_string.substring(0,_string.length()-1));
			Set<String> sets_str = encode_possible_strings(new HashSet<String>(List.of(_string)),hashmap_num,_ch_prev,0);
			
			int minLength1 = sets_str.stream()
                    .mapToInt(String::length)
                    .min()
                    .orElse(0);
			
//			sets_str=sets_str.stream()
//		            .filter(str -> str.length() == minLength1)
//		            .collect(Collectors.toSet());
			
//			int _top_scores = sets_str.stream()
//                    .mapToInt(this::calcScore)
//                    .max()
//                    .orElse(0);
//			
//			sets_str=sets_str.stream()
//		            .filter(str -> calcScore(str) == _top_scores)
//		            .collect(Collectors.toSet());
			
			Optional<String> _ostr = sets_str.stream().findFirst();
			logln("Finished 0. round, minlength : "+minLength1+" one example:"+_ostr.get());
			
			Set<ScoredPath> sets = new TreeSet<ScoredPath>(); 
			for (String _result : sets_str) {
				ScoredPath sp = new ScoredPath(_result, calcScore(_result));
				sets.add(sp);
//				System.out.println("decoded: "+decode(_result, map_num, _ACK));
			}

			
			
			
			int minLength = 0;
			for (int i = 0; i < 200; i++) {
				
				System.out.println("before econde:"+sets.toString());
//				for (ScoredPath sp : sets) {
//					System.out.println("decoded: "+decode(sp, map_arrow, _ACK));
//				}
//				sets = encode_possible_strings_Heuristics(sets,hashmap_arrow,_ch_prev,i);
				Set<ScoredPath> sets2 = encode_possible_strings_Heuristics(sets,hashmap_arrow,_ch_prev,i);
				sets2 = sets.stream()
				        .sorted(Comparator.comparingInt(ScoredPath::getLength)) // Sort in descending order
				        .limit(2000) // Limit to first 20 elements
				        .collect(Collectors.toCollection(LinkedHashSet::new));
				sets2 = encode_possible_strings_Heuristics(sets2,hashmap_arrow,_ch_prev,i);
				sets2 = sets.stream()
				        .sorted(Comparator.comparingInt(ScoredPath::getLength)) // Sort in descending order
				        .limit(2000) // Limit to first 20 elements
				        .collect(Collectors.toCollection(LinkedHashSet::new));
				sets2 = encode_possible_strings_Heuristics(sets2,hashmap_arrow,_ch_prev,i);
				System.out.println("after enconde:"+sets2.toString());
//				for (ScoredPath sp : sets) {
//					for (int j = 0; j < i+1; j++) {
//						decode(sp, map_arrow, _ACK);
//					}
//				}
				
				/*
				int l_minLength = sets.stream()
						.mapToInt(ScoredPath::getLength)
	                    .min() 
	                    .orElse(0);
				
//				sets=sets.stream()
//			            .filter(str -> str.length == l_minLength)
//			            .collect(Collectors.toSet());
//				
				int _top_scores2 = sets.stream()
						.mapToInt(ScoredPath::getScore)
	                    .max()
	                    .orElse(0);
				
//				sets=sets.stream()
//						.filter(str -> str.getScore() == _top_scores2)
//			            .collect(Collectors.toSet());
				sets = sets.stream()
//				        .sorted(Comparator.comparingInt(ScoredPath::getLength).reversed()) // Sort in descending order
				        .sorted(Comparator.comparingInt(ScoredPath::getLength)) // Sort in descending order
				        .limit(2000) // Limit to first 20 elements
				        .collect(Collectors.toCollection(LinkedHashSet::new));
				
				minLength = l_minLength;
				Optional<ScoredPath> _ostr2  = sets.stream().findFirst();
				logln("Finished "+(i+1)+". round, minlength : "+minLength+" set size: "+sets.size()+" one example:"+_ostr2.get().memo_id_str);
				*/
			}
			
			result_part1+=minLength*code;
		}
		
		
		
		logln("result part1: "+result_part1); // correct: 278568
		
		long result_part2 = 0;
		
		logln("result part2: "+result_part2); // correct: 
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day21_backup day = new Day21_backup();
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
	
	public List<String> find_shortest_from_to(Place[][] arr,char char_from, char char_to) {
		if(char_from == char_to) return new ArrayList<String>(List.of(_ACK+""));
		
		int height = arr.length;
		int width=arr[0].length;
		
		Place from = find_char_in_array(arr,char_from);
		Place to = find_char_in_array(arr,char_to);
		
		PriorityQueue<StackState> queue = new PriorityQueue<>((a, b) -> Integer.compare(heuristic(a), heuristic(b)));
		List<String> shortest_paths = new ArrayList<String>();
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
//	            	newScore+=turnScore; //TODO:benne hagyni, sokat gyorsít
	            	next.min_score = newScore;
	                queue.add(new StackState(newY, newX, newScore,s.visited+dc[i],s.from,s.to,i));
	            }
    		}
		}
		return shortest_paths;
	}
	
	public void fillMap(char[] _elements, Place[][] _map, Map<Character, Map<Character,List<String>>> _hashmap) {
		for (char from : _elements) {
			Map<Character,List<String>> row = new HashMap<Character,List<String>>();
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
	
	class ScoredPath implements Comparable<ScoredPath> {
		
		public int calcScore(String _str) {
	        int count = 0;
	        for (int i = 1; i < _str.length(); i++) {
	            if (_str.charAt(i) == _str.charAt(i - 1)) {
	            	count++;
	            } 
	        }
			return count;
		}
		List<String> anchestors;
		List<Integer> values;
		
//		BiMap<Integer, String> value_memo;
		Map<Integer, String> memo_id_str;
		Map<String, Integer> memo_str_id;
		HashMap<Integer, Integer> score_memo;
		
		int length;
		int score;
		
		public int getScore() {
			return score;
		}
		
		public int getLength() {
			return length;
		}
		
		public void store_value(String _str, int _score){
			if(_str==null) return;
			int _ID=memo_id_str.size();
			if(!memo_id_str.containsValue(_str)) {
				memo_id_str.put(_ID, _str);
				memo_str_id.put(_str, _ID);
			} else  {
				_ID = memo_str_id.get(_str);
			}
			score_memo.put(_ID, _score);
			values.add(_ID);
			score+=_score;
			length+=_str.length();
			if(values.size()>1) {
//				String _temp1 = value_memo.get(values.size()-2);
//				String _temp2 = value_memo.get(values.size()-1);
				String _temp1 = memo_id_str.get(values.get(values.size()-2));
				String _temp2 = memo_id_str.get(values.get(values.size()-1));
				if(_temp1.length()>0 && _temp2.length()>0) {
					if(_temp1.charAt(_temp1.length()-1)==_temp2.charAt(0))
						score++;
//					String _comb = _temp1.substring(_temp1.length()-1)+_temp2.substring(0,1);
//					score+=calcScore(_comb);
				}
			}
		}

		public ScoredPath(ScoredPath sp, String _str, int _score) {
			this.anchestors=new ArrayList<String>(sp.anchestors);
			values = new ArrayList<Integer>(sp.values);
			memo_id_str = new HashMap<Integer, String>(sp.memo_id_str);
			memo_str_id = new HashMap<String, Integer>(sp.memo_str_id);
			//value_memo = HashBiMap.create(sp.value_memo);
			score_memo = new HashMap<Integer, Integer>(sp.score_memo);
			this.score=sp.score;
			this.length=sp.length;
			store_value(_str,_score);
		}
		
		public ScoredPath(String _str, int _score) {
			values = new ArrayList<Integer>();
			memo_id_str = new HashMap<Integer, String>();
			memo_str_id = new HashMap<String, Integer>();
			score_memo = new HashMap<Integer, Integer>();
			anchestors = new ArrayList<String>();
			if(_str!=null)anchestors.add(_str);
//			this.score=_score;
//			this.length=_str.length();
			store_value(_str,_score);
			
		}
		
		@Override
        public String toString() {
			StringBuilder sb = new StringBuilder();
			for (Integer _ID: values) {
				sb.append(memo_id_str.get(_ID));
			}
        	return "s:"+score+" "+sb.toString();
        }
		
		@Override
		public int compareTo(ScoredPath o) {
			return this.score>o.score?1:-1;
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
