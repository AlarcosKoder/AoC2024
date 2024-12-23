package AoC2024;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day18 extends Day {
	private int STEPS;
	Place[][] map;
	Place[][] map2;
	Place start;
	Place end;
	private MapPanel mapPanel;
	private Set<Place> shortest_paths;
	private int min_score;
	private int width;
	private int height;
	
//	int[] dx = {1, -1, 0, 0};
//    int[] dy = {0, 0, 1, -1};
	int[] dx = {-1, 0, 1, 0};
    int[] dy = {0, 1, 0, -1};
	
	public Day18() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		
		width=INPUT_REAL?71:7;
		height=INPUT_REAL?71:7;
		
		STEPS = INPUT_REAL?1024:12;
		map = new Place[height][width];
		map2 = new Place[height][width];
//		shortest_paths = new HashSet<Place>();
		min_score=Integer.MAX_VALUE;
	}

	public void processFile() {
		
//		List<Integer[]> fallen = Arrays.stream(sb.toString().split("\n"))
//			    .map(s -> s.split(","))
//			    .map(arr -> Arrays.stream(arr).map(Integer::parseInt).toArray(Integer[]::new))
//			    //.peek(pair -> map[pair[1]][pair[0]]=true)
//			    //.peek(e -> System.out.println("Mapped value: (" + e[1]+","+e[0]+")"))
//			    .collect(Collectors.toList());
		//fallen.stream().forEach(pair -> map[pair[1]][pair[0]]=true);
		
		for (int j = 0; j < map.length; j++) {
			for (int i = 0; i < map[j].length; i++) {
				map[j][i]=new Place(i, j, false);
			}
		}
		start = map[0][0];
		end = map[width-1][height-1];
		
//	    SwingUtilities.invokeLater(() -> {
//			JFrame frame = new JFrame("Day15 Map Display");
//			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//			mapPanel = new MapPanel(width, height);
//			frame.add(mapPanel);
//			frame.pack();
//			frame.setVisible(true);
//			new Thread(() -> visualize()).start();
//	    });
		
		String[] lines = sb.toString().split("\n");
		int counter = 0;
		for (String string : lines) {
			map[Integer.parseInt(string.split(",")[1])][Integer.parseInt(string.split(",")[0])].wall=true;
			if(++counter>=STEPS) {
				break;
			}
		}
		
		displayMap();
		
//		try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
		
		
		escape();
		
		long result_part1 = min_score;
		System.out.println("result part1: "+(result_part1)); // correct: 316
		
		
		String result_part2="";
		for (int i = STEPS; i < lines.length; i++) {
			System.out.println("Checking:"+i);
			int newY = Integer.parseInt(lines[i].split(",")[1]);
			int newX = Integer.parseInt(lines[i].split(",")[0]);
			map[newY][newX].wall=true;
			escape();
			if(min_score==Integer.MAX_VALUE) {
				result_part2 = newX+","+newY;
				break;
			}
				
			reset();
		}
		
		System.out.println("result part2: "+result_part2); // correct: 45,18
	}
	
	public void reset() {
		shortest_paths.clear();
		min_score=Integer.MAX_VALUE;
		for (int j = 0; j < map.length; j++) {
			for (int i = 0; i < map[j].length; i++) {
				map[j][i].shortest_path=false;
				map[j][i].min_score=Integer.MAX_VALUE;
			}
		}
	}
	
	
	public void escape() {
		Set<String> states = new HashSet<>();
		PriorityQueue<StackState> queue = new PriorityQueue<>((a, b) -> Integer.compare(heuristic(a), heuristic(b)));
		List<Place> startList = new ArrayList<Place>();
		startList.add(start);
//		queue.add(new StackState(start.y, start.x, 0, startList));
		queue.add(new StackState(start.y, start.x, 0));
		
		while (!queue.isEmpty()) {
	        StackState s = queue.poll();
	        if (s.y == end.y && s.x == end.x) {
	            if (s.score < min_score) {
	            	min_score = s.score;
	            	
//	            	shortest_paths.clear();
//	            	shortest_paths.addAll(s.visited);
//	            	System.out.println("Found shortest path length: "+min_score+" path:"+s.visited);
//	            	System.out.println("Found shortest path length: "+min_score);
	            	visualize();
	            }
	            continue;
	        }
	        
	        for (int i = 0; i < dx.length; i++) {
	        	int newX = s.x + dx[i];
	            int newY = s.y + dy[i];
	            
	            if (newX < 0 || newX >= map[0].length || newY < 0 || newY >= map.length) continue;
	            Place next = map[newY][newX];
	            if (next.wall) continue;
            	
	            int newScore = s.score + 1;
	            if (newScore <= next.min_score) {
	            	next.min_score = newScore;
//	                List<Place> visited = new ArrayList<Place>(s.visited);
//    				visited.add(next);           
	            	
	            	String stateKey = s.y + "," + s.x+","+i+","+newScore;
	            	if (states.contains(stateKey)) 
	            		continue;
	            		
	            	states.add(stateKey);
	            	
	                queue.add(new StackState(newY, newX, newScore));
//	                queue.add(new StackState(newY, newX, newScore, visited));
	            }
    		}
		 }
	}
	
	private int heuristic(StackState state) {
	    return Math.abs(end.x - state.x) + Math.abs(end.y - state.y);
	}
	
	private void visualize() {
		if(mapPanel==null) return;
		mapPanel.repaint();
		
		for (int j = 0; j < map.length; j++) {
			for (int i = 0; i < map[j].length; i++) {
				map[j][i].shortest_path=false;
			}
		}
		
		for (Place p:shortest_paths) {
			p.shortest_path=true;
			start.shortest_path=true;
		}
		
		if (mapPanel != null) {
			mapPanel.updatePlaces(Arrays.stream(map)
				    .flatMap(Arrays::stream)
				    .collect(Collectors.toList()));
        }
		
		try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}
	
	class Place extends Coordinate {
		boolean wall;
		int min_score;
//		int[] min_score;
		boolean shortest_path;
		
		public Place(int x, int y, boolean _wall) {
			super(x, y);
			this.wall = _wall;
//			min_score = new int[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
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
	
	class StackState {
		int y = 0;
        int x = 0;
        int score = 0;
//        List<Place> visited;
        
        public StackState(int _y, int _x, int _score) {
        	this.y=_y;
        	this.x=_x;
        	this.score=_score;
        }
        
//        public StackState(int _y, int _x, int _score, List<Place> _visited) {
//        	this.y=_y;
//        	this.x=_x;
//        	this.score=_score;
//        	visited=_visited;
//        }
        
        @Override
        public String toString() {
        	return "("+y+","+x+") s:"+score;
        }
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day18 day = new Day18();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	public void displayMap() {
		for (int j = 0; j < map.length; j++) {
			for (int i = 0; i < map[j].length; i++) {
				
				log(map[j][i].wall?"#":".");
			}
			logln("");
		}
		logln("");
	}
	
	class MapPanel extends JPanel {
		
        private final int panelWidth;
        private final int panelHeight;
        private final int pixelSize = INPUT_REAL?5:25;
        
        private List<Place> pplaces = null;

        public MapPanel(int width, int height) {
            this.panelWidth = width;
            this.panelHeight = height;
//            this.map = new Place[height][width];
            pplaces = new ArrayList<Place>();
            setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
        }
        
        public void updatePlaces(List<Place> _places) {
            synchronized (this.pplaces) {
                this.pplaces.clear();
                this.pplaces.addAll(_places);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panelWidth * pixelSize, panelHeight * pixelSize);
            
            Font currentFont = g.getFont();
            Font newFont = currentFont.deriveFont(currentFont.getSize() * 0.8F);
            g.setFont(newFont);
            synchronized (this.pplaces) {
	            for (Place p : pplaces) {
					if(p.wall)
	            		g.setColor(Color.BLACK);
	            	else if(p.shortest_path)
	            		g.setColor(Color.GREEN);
	            	else if(!p.wall)
	            		g.setColor(Color.WHITE);
					
					g.fillRect(p.getX() * pixelSize, p.getY() * pixelSize, pixelSize, pixelSize);
				}
            }
        }
    }
}
