package AoC2024;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import AoC2022.Day;
import AoC2024.utils.Coordinate;

public class Day14 extends Day {
	private int width;
	private int height;
	private VisualizationPanel visualizationPanel;
	
	public Day14() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		
		if(INPUT_REAL) {
			width = 101;
			height = 103;
		} else {
			width = 11;
			height = 7;
		}
	}

	public void processFile() {
		String [] lines = sb.toString().split("\n");
		List<Robot> robots = new ArrayList<Day14.Robot>();
		List<Robot> robots2 = new ArrayList<Day14.Robot>();
		long result_part1 = 0;
		for (int i = 0; i < lines.length; i++) {
			Pattern p = Pattern.compile("p=(\\-?\\d+)\\,(\\-?\\d+).v=(\\-?\\d+)\\,(\\-?\\d+)");
			Matcher m = p.matcher(lines[i]);
			if(m.find()) {
				robots.add(new Robot(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(3))));
				robots2.add(new Robot(Integer.parseInt(m.group(2)), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(4)), Integer.parseInt(m.group(3))));
			}
		}
		logln("Robots parsed: "+robots.size());
		StringBuffer sb = new StringBuffer();
		
		if(!INPUT_REAL) visualize(robots);
		for (int i = 0; i < 100; i++) {
			for (Robot robot : robots) {
				robot.step(width, height);
			}
			if(!INPUT_REAL) visualize(robots,false,false,sb);
		}
		visualize(robots,true,false,sb);
		logln(sb.toString());
		
		result_part1=calculate_result(robots);
		logln("result part1: "+result_part1); // correct: 228410028
		// 216572928 too low
		// 228410028
		// 221343102
		
		// Setup the JFrame and MapPanel
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Day15 Map Display");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            visualizationPanel = new VisualizationPanel(width, height);
            frame.add(visualizationPanel);
            frame.setSize(width * 10, height * 10);
            frame.pack();
            frame.setVisible(true);

            // Start the simulation after initializing the GUI
            new Thread(() -> runSimulation(robots2)).start();
        });
		
	}
	
	private void runSimulation(List<Robot> robots2) {

		long result_part2 = 0;
		for (int i = 0; i < 10000; i++) {
			for (Robot robot : robots2) {
				robot.step(width, height);
			}
			visualize(robots2,false,true,sb);
			if(sb.indexOf("*******************************")>0) {
//				log(sb.toString());
				if(result_part2==0) {
					result_part2=i+1;
					break;
				}
				try {
					bw.flush();
				} catch (Exception e) {
					
				}
			}
			sb = new StringBuffer();
			
			try {
	            Thread.sleep(1);
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }
		}
		
		try {
			bw.flush();
		} catch (Exception e) {
			
		}
		
		logln("result part2: "+result_part2); // correct: 8258
		// 8157 too low 
		// 8158: too low
        
    }
	
	public long calculate_result(List<Robot> robots) {
		long q1=robots.stream().filter(s->s.q==0).count();
		long q2=robots.stream().filter(s->s.q==1).count();
		long q3=robots.stream().filter(s->s.q==2).count();
		long q4=robots.stream().filter(s->s.q==3).count();
		return q1*q2*q3*q4;
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day14 day = new Day14();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	public void visualize(List<Robot> robots) {
		visualize(robots,false,false,new StringBuffer());
	}
	
	public void visualize(List<Robot> robots, boolean finale, boolean part2, StringBuffer sb) {
		if (visualizationPanel != null) {
            visualizationPanel.updateRobots(robots);
        }
		HashMap<Coordinate, Integer> display = new HashMap<Coordinate, Integer>();
		for (Robot robot : robots) {
			Coordinate key = new Coordinate(robot.getX(), robot.getY());
			Integer count = display.get(key);
			if(count != null) {
				display.put(key, count+1);
			} else {
				display.put(key, 1);
			}
		}
		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				Integer count = display.get(new Coordinate(i, j));
				if(finale && (i==height/2||j==width/2)) {
					sb.append(" ");
					continue;
				}
			
				if(count==null) {
					sb.append(part2?" ":".");
				} else {
					sb.append(part2?"*":count+"");
				}
			}
			sb.append("\n");
		}
		sb.append("\n");
	}
	
	class Robot extends Coordinate {
		private Coordinate dir;
		int q;
		
		public Robot(int x, int y, int dx, int dy) {
			super(x, y);
			dir = new Coordinate(dx, dy);
		}
		@Override
		public String toString() {
			return super.toString()+"/"+dir.toString();
		}
		public void step(int width,int height) {
			x=(x+dir.getX())%height;
			y=(y+dir.getY())%width;
			if(x<0)x=x+height;
			if(y<0)y=y+width;
			
				 if(x < height/2     && y < width/2) q=0;
			else if(x < height/2     && y > (width/2)) q=1;
			else if(x > (height/2) && y < width/2) q=2;
			else if(x > (height/2) && y > (width/2)) q=3;
			else q = -1;
		}
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Robot that = (Robot) o;
	        return x == that.x && y == that.y && dir.getX() == that.dir.getX() && dir.getY() == that.dir.getY();
	    }
	}
	
	class VisualizationPanel extends JPanel {
        private final int panelWidth;
        private final int panelHeight;
        private final int pixelSize = 10;
        private final List<Robot> robots;

        public VisualizationPanel(int width, int height) {
            this.panelWidth = width;
            this.panelHeight = height;
            this.robots = new ArrayList<>();
            setPreferredSize(new Dimension(width * pixelSize, height * pixelSize));
        }

        public void updateRobots(List<Robot> robots) {
            synchronized (this.robots) {
                this.robots.clear();
                this.robots.addAll(robots);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panelWidth * pixelSize, panelHeight * pixelSize);

            synchronized (robots) {
                g.setColor(Color.YELLOW);
                for (Robot robot : robots) {
                    g.fillRect(robot.getY() * pixelSize, robot.getX() * pixelSize, pixelSize, pixelSize);
                }
            }
        }
    }
}