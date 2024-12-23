package AoC2022;

import java.util.ArrayList;
import java.util.List;


public class Day15 extends Day {

//	double maxRadius;
	int maxDistance;
	List<Sensor> sensors;
	List<Point> beacons;
	int minX;
	int maxX;
	
	class Interval{
		int x1;
		int x2;
		public Interval(int x1, int x2) {
			this.x1=x1;
			this.x2=x2;
		}
		public boolean contains(int x) {
			return x1<=x && x <= x2;
		}
		@Override
		public String toString() {
			return "Interval [" + x1 + ", " + x2 + "]";
		}
		@Override
		public boolean equals(Object obj) {
			if(obj!=null && obj instanceof Interval) {
				Interval i = (Interval)obj;
				return i.x1==x1&&i.x2==x2;
			}
			return false;
		}
	}
	
	class Sensor extends Point {
		Point beacon;
		double radius;
		int mDistance;
		public Sensor(int x, int y,Point beacon) {
			super(x,y);
			this.beacon=beacon;
			this.mDistance=mDistance(beacon);
			this.radius=distance(beacon); 
		}
	}
	
	public Day15() {
		super();
		sensors=new ArrayList<>();
		beacons=new ArrayList<>();
//		maxRadius=Double.MIN_VALUE;
		maxDistance=Integer.MIN_VALUE;
		minX=Integer.MAX_VALUE;
		maxX=Integer.MIN_VALUE;
	}
	
	public void fillStructure() {
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			int sX = Integer.parseInt(line.substring(line.indexOf("x=")+2,line.indexOf(",")));
			int sY = Integer.parseInt(line.substring(line.indexOf("y=")+2,line.indexOf(":")));
			line=line.substring(line.indexOf(":"));
			int bX = Integer.parseInt(line.substring(line.indexOf("x=")+2,line.indexOf(",")));
			int bY = Integer.parseInt(line.substring(line.indexOf("y=")+2));
			
			if(minX>sX)minX=sX;
			if(maxX<sX)maxX=sX;
			
			Point beacon = new Point(bX,bY);
			beacons.add(beacon);
			Sensor sensor=new Sensor(sX,sY,beacon);
			sensors.add(sensor);
			if(sensor.mDistance >= maxDistance)maxDistance=sensor.mDistance;
		}
		
		minX = minX-maxDistance;
		maxX = maxX+maxDistance;
		
//		log("MaxX:"+maxX+" MaxY:"+maxY);
//		log("MinX:"+minX+" MinY:"+minY);
		log("\n");
	}
	
	public Interval getInterval(Sensor sensor,int Y) {
		int dx = sensor.mDistance-Math.abs(sensor.y-Y);
		if(dx<0)
			return null;
		
		return new Interval(sensor.x-dx, sensor.x+dx);
	}
	
	public void part1() {
		int Y = INPUT_REAL?2000000:10;
		List<Interval> intervals= new ArrayList<>();
		for (Sensor sensor : sensors) {
			
			Interval interval = getInterval(sensor, Y);
			if(interval==null) continue;
			
			intervals.add(interval);
		}
		
		List<Integer> points =new ArrayList<>();
		for (int x=minX;x<=maxX;x++) {
			boolean found=false;
			for (Interval interval : intervals) {
				if(interval.contains(x)) {
					found=true;
					break;
				}
			}
			if(found && !beacons.contains(new Point(x,Y))) points.add(Integer.valueOf(x));
		}
		System.out.println("found intervals: "+(INPUT_REAL?"":intervals));
		System.out.println("solution-part1: "+points.size()+" "+(INPUT_REAL?"":points));
	}
	
	public void part2() {
		int areaMax = INPUT_REAL?4000000:20;
		Interval foundInterval=null;
		for(int y=areaMax;y>=0;y--) {
			
			List<Interval> intervals= new ArrayList<>();
			for (Sensor sensor : sensors) {
				
				Interval interval = getInterval(sensor, y);
				if(interval==null) continue;
				
				intervals.add(interval);
			}
			int maxOfMaxIntervalValue = 0;
			
			List<Integer> points =new ArrayList<>();
			for (int x=0;x<=areaMax;x++) { //Point [x=14, y=11] a test seten
				
				for (Interval interval : intervals) {
					if(interval.contains(x)) {
						int maxIntervalValue = Math.max(interval.x1, interval.x2);
						
						if(maxIntervalValue>maxOfMaxIntervalValue) {
							maxOfMaxIntervalValue = Math.max(interval.x1, interval.x2);
							foundInterval = interval;
						}
					}
				}
				if(maxOfMaxIntervalValue<x) {
					long xL=x;
					long yL=y;
					long sol = xL*4000000+yL;
					System.out.println("Solution-part2: "+new Point(x,y)+" val:"+sol);
					break;
				}
				x=maxOfMaxIntervalValue;
				
			}
			
			if(foundInterval==null)
				break;
			
		}
	}
	
	public static void main(String[] args) {
		
		Day15 d15 = new Day15();
		
		d15.readFile();
		d15.fillStructure();
		d15.part1();
		d15.part2();
	}
	
}
