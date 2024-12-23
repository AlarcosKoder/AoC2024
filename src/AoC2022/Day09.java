package AoC2022;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Day09 extends Day {

	private List<AoC2022.Day.Point> visitedPoints;
//	private File inputFile;
//	private BufferedReader fileReader;
//	private Point head;
//	private Point tail;
	private AoC2022.Day.Point[] points;
//	private BufferedWriter bw;
	
	private double distance;
	
	private static final DecimalFormat df = new DecimalFormat("0.00");
	
	public static void main(String[] args) {
		
		Day09 d09 = null;
		try {
			d09 = new Day09();
			d09.readFile();
			d09.execute();
			
//			d09.bw.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public Day09() {
		super();
		visitedPoints = new ArrayList<>();

		distance=0.0f;
		points = new Point[10];
		
		points[0] = new Point(0,0); //9
		points[1] = new Point(0,0); //8
		points[2] = new Point(0,0); //7
		points[3] = new Point(0,0); //6
		points[4] = new Point(0,0); //5
		points[5] = new Point(0,0); //4
		points[6] = new Point(0,0); //3
		points[7] = new Point(0,0); //2
		points[8] = new Point(0,0); //1
		points[9] = new Point(0,0); //H
		visitedPoints.add(points[0]);
		trace();
	}
	
//	public void log(String log) {
//		try {
//			System.out.print(log);
//			bw.append(log);
//			bw.flush();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
//	-15 - 15 x
//	-5 -> 15 y
	public void trace() {
		for (int y = 15; y >= -5; y--) {
			if(y<10 && y>=0)log(" "+y); else log (""+y);
			for (int x = -15; x <= 15; x++) {
			
			
				Point current = new Point(x,y);
				
				int found = -1;
				for (int i = points.length-1; i >=0; i--) {
					if (points[i].equals(current)) {
						found = i;
						break;
					}
				}
//				log("x:"+x+" y:"+y+" ");
				if (found == points.length-1) {
					log("H");
				} else if (found>=0) {
					log(String.valueOf(points.length-found-1));
				} else {
					log(".");
				}
			}
			log("\n");
		}
	}
	
	public boolean existingPoint(Point candidate) {
		for (Point point : points) {
			if (point.equals(candidate)) return true;
		}
		return false;
	}
	
	public double distance(int i) {
		int x=points[i].x-points[i-1].x;
		int y=points[i].y-points[i-1].y;
		return Math.sqrt(x*x+y*y);
	}
	
	public void stepCloser(int i) {
		Point stepTo = points[i];
		Point point2Step = points[i-1];
		
		if (stepTo.x-2==point2Step.x)
			points[i-1]=new Point(points[i-1].x+1,points[i-1].y);
		else if(stepTo.x+2==point2Step.x)
			points[i-1]=new Point(points[i-1].x-1,points[i-1].y);
		else if(stepTo.y-2==point2Step.y)
			points[i-1]=new Point(points[i-1].x,points[i-1].y+1);
		else
			points[i-1]=new Point(points[i-1].x,points[i-1].y-1);
			
	}
	
	public void diagonalJump(int i) {
		Point jumpTo = points[i];
		Point point2Jump = points[i-1];
		
		if(jumpTo.x>point2Jump.x && jumpTo.y > point2Jump.y)
			points[i-1]=new Point(points[i-1].x+1,points[i-1].y+1);
		else if(jumpTo.x<point2Jump.x && jumpTo.y > point2Jump.y)
			points[i-1]=new Point(points[i-1].x-1,points[i-1].y+1);
		else if(jumpTo.x<point2Jump.x && jumpTo.y < point2Jump.y)
			points[i-1]=new Point(points[i-1].x-1,points[i-1].y-1);
		else
			points[i-1]=new Point(points[i-1].x+1,points[i-1].y-1);
		
	}
	
	public void evaluate(String direction) {
		if(direction.equals("R")) {
			//csak akkor szabad jobbra létrehozni, ha ott még nincs másik
			points[points.length-1] = new Point(points[points.length-1].x+1,points[points.length-1].y);
			
			for (int i = points.length-1; i > 0; i--) {
				distance = distance(i);
				if(distance == 2) {
					stepCloser(i);
				} else if (distance > 2) {
					diagonalJump(i);
				}
				
			}
			
		} else if(direction.equals("L")) {
			
			points[points.length-1] = new Point(points[points.length-1].x-1,points[points.length-1].y);
			
			for (int i = points.length-1; i > 0; i--) {
				distance = distance(i);
				if(distance == 2) {
					stepCloser(i);
				} else if (distance > 2) {
					diagonalJump(i);
				}
				
			}
		} else if(direction.equals("U")) {
			
			points[points.length-1] = new Point(points[points.length-1].x,points[points.length-1].y+1);
			
			for (int i = points.length-1; i > 0; i--) {
				distance = distance(i);
				if(distance == 2) {
					stepCloser(i);
				} else if (distance > 2) {
					diagonalJump(i);
				}
				
			}
		} else if(direction.equals("D")) {
			points[points.length-1] = new Point(points[points.length-1].x,points[points.length-1].y-1);
			if(!visitedPoints.contains(points[0]))
				visitedPoints.add(points[0]);
			
			for (int i = points.length-1; i > 0; i--) {
				distance = distance(i);
				if(distance == 2) {
					stepCloser(i);
				} else if (distance > 2) {
					diagonalJump(i);
				}
				
			}
		}
		
		Point tail = new Point(points[0].x,points[0].y);
		if(!visitedPoints.contains(tail))visitedPoints.add(tail);
		
	}
	
	public void execute() {
		String[] lines =sb.toString().split("\n");
		for (String strCurrentLine : lines) {
			String direction = strCurrentLine.split(" ")[0];
			int length = Integer.parseInt(strCurrentLine.split(" ")[1]);
			
			execute(direction,length);
			
			trace();
		}
		log("tail places:"+visitedPoints.size());
	}
	
	public void execute(String direction, int length) {
		evaluate(direction);
		if(length>1)execute(direction,length-1);
	}
}
