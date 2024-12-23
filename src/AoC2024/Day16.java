package AoC2024;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day16 extends Day {
	
	private int width;
	private int height;
	private Place[][] map;
	private MapPanel mapPanel;
	private List<Place> places;
	
	private int min_score;
	private Set<Place> min_scores;
	
	int[] dx = {-1, 0, 1, 0};
    int[] dy = {0, 1, 0, -1};
    private Place start;
    private Place end;
	
	public Day16() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		start=null;
		end=null;
		min_score = Integer.MAX_VALUE;
		min_scores = new HashSet<Place>();
	}

	public void processFile() {
		
		char char_wall = "#".charAt(0);
		char char_free = ".".charAt(0);
		char char_start = "S".charAt(0);
		char char_end = "E".charAt(0);
		
		String[] lines = sb.toString().split("\n");
		width = lines[0].length();
		height = lines.length;
		map = new Place[width][height];
		places = new ArrayList<Place>();
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				char c = lines[i].charAt(j);
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
				places.add(map[j][i]);
			}
		}
		
		traverseMaze();
		long result_part1 = min_score;
		logln("result part1: "+result_part1); // correct: 160624 //47077 msec with List; 527518 msec with hashset
		
		long result_part2 = min_scores.size();
		logln("result part2: "+result_part2); // correct: 692
		
		// Setup the JFrame and MapPanel
	      SwingUtilities.invokeLater(() -> {
	          JFrame frame = new JFrame("Day15 Map Display");
	          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	          mapPanel = new MapPanel(width, height);
	          frame.add(mapPanel);
	          frame.pack();
	          frame.setVisible(true);
	
	          // Start the simulation after initializing the GUI
	          new Thread(() -> visualize()).start();
	      });
		
	}
	private void visualize() {
		mapPanel.repaint();
		
		for (Place p:min_scores) {
			p.shortest_path=true;
		}
		
		if (mapPanel != null) {
			mapPanel.updatePlaces(places);
        }
		
		try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
	}
	
	private void traverseMaze() {
        
		PriorityQueue<StackState> stack = new PriorityQueue<>((a, b) -> Integer.compare(a.score + heuristic(a), b.score + heuristic(b)));
        start.min_score[1]=1;
        stack.add(new StackState(start.y, start.x, 1, 0, List.of(start)));

        while (!stack.isEmpty()) {
        	StackState s = stack.poll();
            
            if (s.x == end.x && s.y == end.y) {
            	if(s.score < min_score) {
            		min_score = s.score;
//            		System.out.println("new min_score: "+min_score);
            		min_scores.clear();
            		min_scores.addAll(s.visited);
            	} else if(s.score==min_score) {
            		min_scores.addAll(s.visited);
            	}
                continue;
            }
            
            for (int i = 0; i < dx.length; i++) {
            	int turn_score=1;
            	if(s.dir != i) turn_score += 1000;
            	int new_score = s.score+turn_score;
            	Place p = map[s.y+dy[i]][s.x+dx[i]];
    			if(!p.wall && !s.visited.contains(p) && p.min_score[s.dir] >= new_score) {
					p.min_score[s.dir]=new_score;
					List<Place> visited = new ArrayList<Place>(s.visited);
    				visited.add(p);
    				stack.add(new StackState(p.y, p.x, i, new_score, visited));
    			}
    		}
        }
	}
	
	private void traverseMaze2() {
	    PriorityQueue<StackState> queue = new PriorityQueue<>((a, b) -> Integer.compare(a.score + heuristic(a), b.score + heuristic(b)));
	    start.min_score[1] = 1;
	    queue.add(new StackState(start.y, start.x, 1, 0, List.of(start)));

	    while (!queue.isEmpty()) {
	        StackState current = queue.poll();

	        // If we've reached the end, update the minimum score
	        if (current.x == end.x && current.y == end.y) {
	            if (current.score < min_score) {
	                min_score = current.score;
	                min_scores.clear();
	                min_scores.addAll(current.visited);
//	                min_scores.add(map[current.y][current.x]);
	            } else if(current.score==min_score) {
            		min_scores.addAll(current.visited);
            	}
	            continue;
	        }

	        for (int i = 0; i < dx.length; i++) {
	            int newX = current.x + dx[i];
	            int newY = current.y + dy[i];
	            
	            // Ensure the new position is valid
	            if (newX < 0 || newX >= width || newY < 0 || newY >= height) continue;
	            Place next = map[newY][newX];
	            if (next.wall) continue;

	            int turnScore = (current.dir != i && current.dir != -1) ? 1000 : 0;
	            int newScore = current.score + 1 + turnScore;

	            // If this path is better, update and enqueue
	            if (newScore <= next.min_score[i]) {
	                next.min_score[i] = newScore;
	                List<Place> visited = new ArrayList<Place>(current.visited);
    				visited.add(next);
	                queue.add(new StackState(newY, newX, i, newScore, visited));
	            }
	        }
	    }
	}

	private int heuristic(StackState state) {
	    return Math.abs(end.x - state.x) + Math.abs(end.y - state.y);
	}


	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day16 day = new Day16();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	class StackState {
		int y = 0;
        int x = 0;
        int dir = 0;
        int score = 0;
        List<Place> visited;
        
        public StackState(int _y, int _x, int _dir, int _score, List<Place> _visited) {
        	this.y=_y;
        	this.x=_x;
        	this.dir=_dir;
        	this.score=_score;
        	visited=_visited;
        }
        
        @Override
        public String toString() {
        	return "("+y+","+x+") d:"+dir+" s:"+score+" pl:"+visited.toString();
        }
	}
	
	
	class Place extends Coordinate {
		boolean wall;
		int[] min_score;
		boolean shortest_path;
		
		public Place(int x, int y, boolean _wall) {
			super(x, y);
			this.wall = _wall;
			min_score = new int[]{Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE,Integer.MAX_VALUE};
			shortest_path = false;
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
	
	class MapPanel extends JPanel {
		
        private final int panelWidth;
        private final int panelHeight;
        private final int pixelSize = INPUT_REAL?5:50;
        private final List<Place> places;

        public MapPanel(int width, int height) {
            this.panelWidth = width;
            this.panelHeight = height;
            this.places = new ArrayList<>();
            setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
        }

        public void updatePlaces(List<Place> places) {
            synchronized (this.places) {
                this.places.clear();
                this.places.addAll(places);
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
            
            synchronized (places) {
                
                for (Place place : places) {
                	if(place.wall)
                		g.setColor(Color.BLACK);
                	else if(place.shortest_path)
                		g.setColor(Color.GREEN);
                	else if(!place.wall)
                		g.setColor(Color.WHITE);
                	
                    g.fillRect(place.getY() * pixelSize, place.getX() * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
}
