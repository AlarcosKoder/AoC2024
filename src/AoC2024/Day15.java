package AoC2024;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day15 extends Day {
	
	/*
	int[][] grid = new int[4][5]; // 4 rows (height), 5 columns (width)
	
	map[0][0] map[0][1] map[0][2] map[0][3] map[0][4]  // Row 0
	map[1][0] map[1][1] map[1][2] map[1][3] map[1][4]  // Row 1
	map[2][0] map[2][1] map[2][2] map[2][3] map[2][4]  // Row 2
	map[3][0] map[3][1] map[3][2] map[3][3] map[3][4]  // Row 3

	 */
	
	private int width;
	private int height;
	private Place[][] map;
	private MapPanel mapPanel;
	private Place ego;
	private boolean PART2;
	
	int[] dx = {-1, 0, 1, 0};
    int[] dy = {0, 1, 0, -1};
	
	public Day15() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		PART2 = true;
	}

	public void processFile() {
		
		String[] lines = sb.toString().split("\n");
		List<String> str_map = new ArrayList<String>();
		List<Character> ops = new ArrayList<Character>();
		
		for (String string : lines) {
			if(string.startsWith("#")) {
				str_map.add(string);
			} else if(!string.equals("")) {
				ops.addAll(string.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
			}
		}
		width = str_map.get(0).length();
		height = str_map.size();
		char wall_char = "#".charAt(0);
		char box_char = "O".charAt(0);
		char ego_char = "@".charAt(0);
		
		if(PART2)
			map = new Place[2*width][height];
		else
			map = new Place[width][height];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				char c = str_map.get(i).charAt(j);
				
				if(PART2) {
					if(c==wall_char) {
						map[2*j][i]=new Place(i, 2*j,true,false,false);
						map[2*j+1][i]=new Place(i, 2*j+1,true,false,false);
					} else if(c==box_char) {
						map[2*j][i]=new Place(i, 2*j,false,true,false);
						map[2*j][i].box_left=true;
						map[2*j+1][i]=new Place(i, 2*j+1,false,true,false);
						map[2*j+1][i].box_right=true;
						map[2*j][i].connected_box=map[2*j+1][i];
						map[2*j+1][i].connected_box=map[2*j][i];
					} else if(c==ego_char) {
						map[2*j][i]=new Place(i, 2*j,false,false,true);
						map[2*j+1][i]=new Place(i, 2*j+1,false,false,false);
						ego = map[2*j][i];
					} else {
						map[2*j][i]=new Place(i, 2*j,false,false,false);
						map[2*j+1][i]=new Place(i, 2*j+1,false,false,false);
					}
				} else {
					map[j][i]=new Place(i, j,wall_char==c,box_char==c,ego_char==c);
					if(ego_char==c) {
						ego = map[j][i];
					}
				}
				
			}
		}
		
		// Setup the JFrame and MapPanel
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Day15 Map Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mapPanel = new MapPanel();
            frame.add(mapPanel);
            frame.pack();
            frame.setVisible(true);

            // Start the simulation after initializing the GUI
            new Thread(() -> runSimulation(ops)).start();
        });
		
//		long result_part2 = 0;
		
//		System.out.println("result part2: "+result_part2); // correct: 
	}
	/*
	private Place findNextSpace(int x, int y, int dir) {
		while (true) {
			int newX = map[y][x].x+dx[dir];
			int newY = map[y][x].y+dy[dir];
			Place p = map[newY][newX];
			if(p.wall) {
				break;
			} else if(!p.box) {
				return p;
			}
			x=newX;
			y=newY;
		}
		return null;
	}
	*/
	private List<Place> findNextSpace(Place p, int dir) {
		ArrayList<Place> boxes = new ArrayList<Place>();
		boxes.add(p);
		
		while (true) {
			int newX = p.x+dx[dir];
			int newY = p.y+dy[dir];
			Place new_p = map[newY][newX];
			if(new_p.wall) {
				return null;
			} else if(!new_p.box) {
				boxes.add(new_p);
				return boxes;
			}
			boxes.add(new_p);
			p=new_p;
		}
	}
	
	private boolean expandUnchecked(Set<Place> unchecked, int dir) {
		for (Place place : unchecked) {
			Place d1 = map[place.y + dy[dir]][place.x + dx[dir]];
			Place d2 = d1.connected_box;
			if(d1!=null && d1.wall) return false;
			if(d2!=null && d2.wall) return false;
			Set<Place> newset = new HashSet<Place>();
			if(d1!=null && d1.box) {
				newset.add(d1);
			}
			if(d2!=null && d2.box) {
				newset.add(d2);
			}
			if(newset.size()>0) {
				boolean check= expandUnchecked(newset,dir);
				if(!check) return false;
			}
		}
		return true;
	}
	
	private LinkedHashSet<Place> moveUnchecked(LinkedHashSet<Place> unchecked, int dir) {
		LinkedHashSet<Place> checked = new LinkedHashSet<Day15.Place>();
		for (Place place : unchecked) {
			Place d1 = map[place.y + dy[dir]][place.x + dx[dir]];
			Place d2 = d1.connected_box;
			if(d1!=null && d1.wall) return null;
			if(d2!=null && d2.wall) return null;
			LinkedHashSet<Place> newset = new LinkedHashSet<Place>();
			if(d1!=null && d1.box) {
				newset.add(d1);
			}
			if(d2!=null && d2.box) {
				newset.add(d2);
			}
			if(newset.size()>0) {
				LinkedHashSet<Place> checkNew= moveUnchecked(newset,dir);
				if(checkNew==null) return null;
				else checked.addAll(checkNew);
			}
		}
		checked.addAll(unchecked);
		return checked;
	}
	
	private void runSimulation(List<Character> ops) {
		
	    char char_l = '<';
	    char char_r = '>';
	    char char_u = '^';
	    char char_d = 'v';
//	    int counter = 0;
	    for (Character op : ops) {
//	    	System.out.println("current ops:"+op+" current ego:"+ego+" op#:"+counter++);
	        // Determine movement direction
	        int dir = -1;
	        if (op == char_u) dir = 0; // Up
	        else if (op == char_r) dir = 1; // Right
	        else if (op == char_d) dir = 2; // Down
	        else if (op == char_l) dir = 3; // Left

	        if (dir != -1) {
	            int newX = ego.x + dx[dir];
	            int newY = ego.y + dy[dir];

	            // Check bounds and wall collision
	            if (newY >= 0 && newY < (PART2?2*width:width) && newX >= 0 && newX < height && !map[newY][newX].wall) {
	                if (map[newY][newX].box) {
	                	if(dir==1 || dir ==3 || ((dir==0 || dir ==2) &&!PART2) ) {
	                		List<Place> boxes = findNextSpace(map[newY][newX], dir);
	                		if(boxes==null) continue;
	                		for (int i = boxes.size()-1; i > 0; i--) {
	                			
	                			int x2 = map[newY][newX].x+i*dx[dir];
	                			int y2 = map[newY][newX].y+i*dy[dir];
	                			int x1 = map[newY][newX].x+(i-1)*dx[dir];
	                			int y1 = map[newY][newX].y+(i-1)*dy[dir];
	                			Place p2 = map[y2][x2];
	                			Place p1 = map[y1][x1];
	                			p2.swapPlace(p1,map);
							}
	                		if(boxes.size()>0) {
	                			map[newY][newX].box=false;
	                			map[newY][newX].ego=true;
	                			map[newY][newX].connected_box=null;
	                			map[ego.y][ego.x].ego=false;
	                			ego=map[newY][newX];
	                		}
	                	} else if(dir==0 || dir ==2) {
	                		LinkedHashSet<Place> unchecked = new LinkedHashSet<Place>();
	                		Place p = map[newY][newX];
	                		unchecked.add(p);
	                		unchecked.add(p.connected_box);
	                		LinkedHashSet<Place> checked=moveUnchecked(unchecked, dir);
	                		if(checked!=null&&checked.size()>0) {
		                		for (Place m : checked) {
									int nmX = m.x+dx[dir];
									int nmY = m.y+dy[dir];
									map[nmY][nmX].swapPlace(m, map);
								}
		                		map[newY][newX].ego=true;
	                			map[ego.y][ego.x].ego=false;
	                			ego=map[newY][newX];
	                		}
	                		
	                	}
	                } else {
	                    // Clear the old ego position
	                	map[ego.y][ego.x].ego = false;

	                    // Move ego to the new position
	                    map[newY][newX].ego = true;
	                    ego = map[newY][newX];
	                }
	            }
	        }

	        mapPanel.repaint();

	        try {
	            Thread.sleep(1);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
	    }

	    long result_part1 = 0;
	    int result_counter=0;
	    for (int i = 0; i < height; i++) {
			for (int j = 0; j < (PART2?2*width:width); j++) {
				if(map[j][i].box_left) {
				//if(map[j][i].box_left&&map[j][i].box) {
					result_counter++;
					result_part1 += map[j][i].x*100+map[j][i].y;
				}
			}
	    }
		System.out.println("result part1: "+result_part1 +" cumulated in boxes: "+result_counter); // correct: 1476771
		//part2: 1468005
	}



	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day15 day = new Day15();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	/**
     * JPanel for rendering the map
     */
    class MapPanel extends JPanel {
//        private int cellSize = 20; // Size of each cell in the grid
        private int cellWidth = PART2?INPUT_REAL?10:20:40;
        private int cellHeight = INPUT_REAL?20:40;

        public MapPanel() {
            setPreferredSize(new Dimension(PART2?(2*width * cellWidth):(width * cellWidth), height * cellHeight));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            for (int row = 0; row < height; row++) {
                for (int col = 0; col < (PART2?2*width:width); col++) {
                    Place place = map[col][row];

                    // Draw cell background
                    g2d.setColor(Color.LIGHT_GRAY);
                    g2d.fillRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);
                    g2d.setColor(Color.BLACK);
                    g2d.drawRect(col * cellWidth, row * cellHeight, cellWidth, cellHeight);

                    // Draw symbol based on Place properties
                    
                    String symbol ="";
                    if(place.wall) symbol="#";
                    if(place.box && PART2) symbol=place.box_left?"[":"]";
                    if(place.box && !PART2) symbol="O";
                    if(place.ego) symbol="@";
                    
                    g2d.setColor(Color.BLACK);
                    g2d.drawString(symbol, col * cellWidth + cellWidth / 3, row * cellHeight + 2 * cellHeight / 3);
                }
            }
        }
    }
	
	class Place extends Coordinate {
		boolean wall;
		boolean box;
		boolean ego;
		boolean box_left;
		boolean box_right;
		Place connected_box;
		
		public Place(int x, int y, boolean _wall, boolean _box, boolean _ego) {
			super(x, y);
			this.wall = _wall;
			this.box = _box;
			this.ego= _ego;
			this.box_left = false;
			this.box_right = false;
			this.connected_box=null;
		}
		@Override
		public String toString() {
			if(PART2) return ego?"@":""+(wall?"#":"")+(box?box_left?"[":"]":".")+super.toString();
			else return ego?"@":""+(wall?"#":"")+(box?"O":".")+super.toString();
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
		
		public void updateWithPlace(Place p,Place map[][]) {
			this.wall = p.wall;
			this.box = p.box;
			this.ego= p.ego;
			this.box_left = p.box_left;
			this.box_right = p.box_right;
			if(box_left)connected_box=map[y+1][x];
			if(box_right)connected_box=map[y-1][x];
		}
		
		public void swapPlace(Place p,Place map[][]) {
			boolean _wall = this.wall;
			boolean _box = this.box;
			boolean _ego= this.ego;
			boolean _box_left = this.box_left;
			boolean _box_right = this.box_right;
			Place _connected = this.connected_box;
			
			this.wall = p.wall;
			this.box = p.box;
			this.ego= p.ego;
			this.box_left = p.box_left;
			this.box_right = p.box_right;
			if(box_left)connected_box=map[y+1][x];
			if(box_right)connected_box=map[y-1][x];
			
			p.wall=_wall;
			p.box=_box;
			p.ego=_ego;
			p.box_left=_box_left;
			p.box_right=_box_right;
			p.connected_box=_connected;
		}
	}
}
