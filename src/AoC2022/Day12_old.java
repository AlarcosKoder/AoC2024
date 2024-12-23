package AoC2022;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;


// 432 too high
public class Day12_old extends Day {
	
	class Place {
		Character c;
		
		TreeMap<Double, Place> connections;
		
		Place dirR;
		double valR;
		Place dirL;
		double valL;
		Place dirU;
		double valU;
		Place dirD;
		double valD;
		boolean visited=false;
		int i;
		int j;
		
		Place(Character c1){
			connections = new TreeMap<>();
			c=Character.valueOf(c1);
			dirR=null;
			dirL=null;
			dirU=null;
			dirD=null;
			valR=0d;
			valL=0d;
			valU=0d;
			valD=0d;
		}
		@Override
		public boolean equals(Object obj) {
			Place other = (Place)obj;
			return other.i==i&&other.j==j;
		}
	}

	private int width;
	private int heigth;
	private Place[][] matrix;
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private List<Place> routes;
	private Map<Long,List<Place>> solutions;
	
	public Day12_old() {
		super();
		routes = new ArrayList<>();
		solutions=new HashMap<>();
	}
	
	public double distance(Place a, Place b) {
		return Math.sqrt((a.i-b.i)*(a.i-b.i)+(a.j-b.j)*(a.j-b.j));
	}
	
	public void fillMatrix() {
		String[] lines = sb.toString().split("\n");
		width=lines[0].length();
		heigth=lines.length;
		matrix = new Place[heigth][width];
		
		for(int x=0;x<heigth;x++) {
			String line = lines[x];
			for(int y=0;y<width;y++) {
				matrix[x][y]=new Place(line.charAt(y));
				matrix[x][y].i=x;
				matrix[x][y].j=y;
				if(matrix[x][y].c.equals("S".charAt(0))) {
					startX=x;
					startY=y;
				}
				if(matrix[x][y].c.equals("E".charAt(0))) {
					endX=x;
					endY=y;
				}
			}
		}
		
		log("startPos ["+startX+","+startY+"]\n");
		log("endPos ["+endX+","+endY+"]\n");
		//fill linkes
		for(int x=0;x<heigth;x++) {
			for(int y=0;y<width;y++) {
				Place p = matrix[x][y];
//				if(matrix[x][y].c.equals("S".charAt(0))) {
//					System.out.println("start");
//				}
				if((x==17 && y==82)||(matrix[x][y].c.equals("m".charAt(0)))) {
					System.out.println("mn");
				}
				
				//up
				if(x>0) {
					if(matrix[x][y].c==matrix[x-1][y].c || matrix[x][y].c+1==matrix[x-1][y].c || matrix[x][y].c>matrix[x-1][y].c) {
						matrix[x][y].dirU=matrix[x-1][y];
						double val = distance(matrix[endX][endY], matrix[x-1][y])+0.1;
						if(matrix[x][y].c+1==matrix[x-1][y].c) {
							val*=10;
						}
						matrix[x][y].connections.put(Double.valueOf(val),matrix[x-1][y]);
					}
						
					
					
//					matrix[x][y].dirU=matrix[x][y].c<=matrix[x-1][y].c;
					if(matrix[x][y].c.equals("S".charAt(0)) && matrix[x-1][y].c.equals("a".charAt(0)) ) {
						matrix[x][y].dirU=matrix[x-1][y];
						matrix[x][y].connections.put(Double.valueOf(200d),matrix[x-1][y]);
					}
					if(matrix[x-1][y].c.equals("S".charAt(0))) matrix[x][y].dirU=null;
					if(matrix[x-1][y].c.equals("E".charAt(0)) && matrix[x][y].c.equals("z".charAt(0))) {
						matrix[x][y].dirU=matrix[x-1][y];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x-1][y]);
					}
				}
				
				//down
				if(x<heigth-1) {
					if(matrix[x][y].c==matrix[x+1][y].c || matrix[x][y].c+1==matrix[x+1][y].c || matrix[x][y].c > matrix[x+1][y].c) {
						matrix[x][y].dirD= matrix[x+1][y];
						
						double val = distance(matrix[endX][endY], matrix[x+1][y])+0.05;
						if(matrix[x][y].c+1==matrix[x+1][y].c) {
							val*=10;
						}
						matrix[x][y].connections.put(Double.valueOf(val),matrix[x+1][y]);
					}
						
					
//					matrix[x][y].dirD=matrix[x][y].c<=matrix[x+1][y].c;
					if( matrix[x][y].c.equals("a".charAt(0)) && matrix[x+1][y].c.equals("a".charAt(0)) ) {
						matrix[x][y].dirD=matrix[x+1][y];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x+1][y]);
					}
					if(matrix[x+1][y].c.equals("S".charAt(0))) matrix[x][y].dirD=null;
					if(matrix[x+1][y].c.equals("E".charAt(0)) && matrix[x][y].c.equals("z".charAt(0)) ) {
						matrix[x][y].dirD=matrix[x+1][y];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x+1][y]);
					}
				}
				
				//right
				if(y<width-1) {
					if (matrix[x][y].c==matrix[x][y+1].c || matrix[x][y].c+1==matrix[x][y+1].c || matrix[x][y].c > matrix[x][y+1].c) {
						matrix[x][y].dirR=matrix[x][y+1];
						double val=distance(matrix[endX][endY], matrix[x][y+1])+0.2;
						if(matrix[x][y].c+1==matrix[x][y+1].c) {
							val*=10;
						}
						matrix[x][y].connections.put(Double.valueOf(val),matrix[x][y+1]);
					}
					
//					matrix[x][y].dirR=matrix[x][y].c<=matrix[x][y-1].c;
					if( matrix[x][y].c.equals("S".charAt(0)) && matrix[x][y+1].c.equals("a".charAt(0)) ) {
						matrix[x][y].dirR=matrix[x][y+1];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x][y+1]);
					}
					if(matrix[x][y+1].c.equals("S".charAt(0))) matrix[x][y].dirR=null;
					if(matrix[x][y+1].c.equals("E".charAt(0)) && matrix[x][y].c.equals("z".charAt(0))) {
						matrix[x][y].dirR=matrix[x][y+1];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x][y+1]);
					}
				}
				
				//left
				if(y>0) {
					if(matrix[x][y].c==matrix[x][y-1].c || matrix[x][y].c+1==matrix[x][y-1].c || matrix[x][y].c > matrix[x][y-1].c) {
						matrix[x][y].dirL=matrix[x][y-1];
						double val=distance(matrix[endX][endY], matrix[x][y-1])+0.0025;
						if(matrix[x][y].c+1==matrix[x][y-1].c) {
							val*=10;
						}
						matrix[x][y].connections.put(Double.valueOf(val),matrix[x][y-1]);
					}
					
//					matrix[x][y].dirL=matrix[x][y].c<=matrix[x][y+1].c;
					if( matrix[x][y].c.equals("S".charAt(0)) && matrix[x][y-1].c.equals("a".charAt(0)) ) {
						matrix[x][y].dirL=matrix[x][y-1];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x][y-1]);
					}
					if(matrix[x][y-1].c.equals("S".charAt(0))) matrix[x][y].dirL=null;
					if(matrix[x][y-1].c.equals("E".charAt(0)) && matrix[x][y].c.equals("z".charAt(0))) {
						matrix[x][y].dirL=matrix[x][y-1];
						matrix[x][y].connections.put(Double.valueOf(100d),matrix[x][y-1]);
					}
				}
					
//				log(x+","+y+" "+matrix[x][y].c+" u:"+(matrix[x][y].dirU!=null?true:false)+" d:"+(matrix[x][y].dirD!=null?true:false)+" r:"+(matrix[x][y].dirR!=null?true:false)+" l:"+(matrix[x][y].dirL!=null?true:false)+"\n");
			}
			log("\n");
		}
	}
	
	public boolean contains(List<Place> r, Place p) {
		
		return false;
	}
	
	public void findRoute(Place start, List<Place> r) {
		if(r.size()>432) return;
		start.visited=true;
		if(start.c.equals("d".charAt(0))) { 
			solutions.put(Long.valueOf(r.size()), new ArrayList<>(r));
			System.out.println(start.i+","+start.j+" "+"Solution found. Length: "+r.size());
		}
		
		for (Double value : start.connections.keySet()) {
			if(start.connections.get(value).visited) continue;
			r.add(start.connections.get(value));
			findRoute(start.connections.get(value), r);
			r.remove(start.connections.get(value));
			start.connections.get(value).visited=false;
		}
		
//		if(start.dirR!=null && !start.dirR.visited && !r.contains(start.dirR)) {
//			r.add(start.dirR);
//			findRoute(start.dirR, r);
//			r.remove(start.dirR);
//			start.dirR.visited=false;
//		}
//		
//		if(start.dirL!=null && !start.dirL.visited && !r.contains(start.dirL)) {
//			r.add(start.dirL);
//			findRoute(start.dirL, r);
//			r.remove(start.dirL);
//			start.dirL.visited=false;
//		}
//		
//		
//		if(start.dirU!=null&& !start.dirU.visited && !r.contains(start.dirU)) {
//			r.add(start.dirU);
//			findRoute(start.dirU, r);
//			r.remove(start.dirU);
//			start.dirU.visited=false;
//		}
//		
//		if(start.dirD!=null&& !start.dirD.visited && !r.contains(start.dirD)) {
//			r.add(start.dirD);
//			findRoute(start.dirD, r);
//			r.remove(start.dirD);
//			start.dirD.visited=false;
//		}
		
	}
	
	public void listVisited() {
		for(int x=0;x<heigth;x++) {
			for(int y=0;y<width;y++) {
//				log(x+","+y+" "+matrix[x][y].c+" visited:"+matrix[x][y].visited+"\n");
			}
		}
	}
	

	public static void main(String[] args) {
		Day12_old d12 = new Day12_old();
		d12.readFile();
		d12.fillMatrix();
		d12.findRoute(d12.matrix[d12.startX][d12.startY], d12.routes);
		d12.listVisited();
	}

}
