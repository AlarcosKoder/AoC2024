package AoC2022;

public class Day14 extends Day {

	private int maxX;
	private int maxY;
	private int minX;
	private int minY;
	private int xWidener;
	private int yWidener;
	private Element[][] matrix;
	private Source source;
	
	public Day14() {
		super();
		source = new Source(500, 0);
		maxX=Integer.MIN_VALUE;
		maxY=Integer.MIN_VALUE;
		minX=Integer.MAX_VALUE;
		minY=Integer.MAX_VALUE;
		xWidener=150;
		yWidener=3;
	}
	
	public void fillStructure() {
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			String[] wallParts = line.split(" -> ");
			for (String wp : wallParts) {
				int x = Integer.parseInt(wp.split(",")[0]);
				int y = Integer.parseInt(wp.split(",")[1]);
				if(x>maxX) maxX = x;
				if(y>maxY) maxY = y;
				if(x<minX) minX = x;
				if(y<minY) minY = y;
			}
		}
		minY=0;
		
		log("MaxX:"+maxX+" MaxY:"+maxY);
		log("MinX:"+minX+" MinY:"+minY);
		
		matrix=new Element[maxX+xWidener+1][maxY+yWidener+1];
		
		lines = sb.toString().split("\n");
		for (int i=0;i<lines.length;i++) {
			String[] wallParts = lines[i].split(" -> ");
			for (int j=0;j<wallParts.length-1;j++) {
				int x1 = Integer.parseInt(wallParts[j].split(",")[0]);
				int y1 = Integer.parseInt(wallParts[j].split(",")[1]);
				int x2 = Integer.parseInt(wallParts[j+1].split(",")[0]);
				int y2 = Integer.parseInt(wallParts[j+1].split(",")[1]);
				drawLine(x1, y1, x2, y2);
			}
		}
		//for part2
		drawLine(minX-xWidener, maxY+2, maxX+xWidener, maxY+2);
		
		matrix[500][0]=source;
	}
	
	public void drawLine(int x1, int y1, int x2, int y2) {
		if(x1==x2) {
			int stepper = y1-y2>0?-1:1;
			int dist = Math.abs(y1-y2);
			for (int i = 0; i <= dist; i++) {
				int x=x1;
				int y=y1+(i*stepper);
				Wall wall= new Wall(x,y);
				matrix[x][y]=wall;
			}
		}
		if(y1==y2) {
			int stepper = x1-x2>0?-1:1;
			int dist = Math.abs(x1-x2);
			for (int i = 0; i <= dist; i++) {
				int x = x1+(i*stepper);
				int y = y1;
				Wall wall= new Wall(x,y);
				matrix[x][y]=wall;
			}
		}
	}
	
	class Element {
		int x;
		int y;
		
		public Element(int x, int y) {
			this.x=x;
			this.y=y;
		}
		public boolean samePosition(Element e) {
			return x==e.x&&y==e.y;
		}
	}
	class Wall extends Element {
		public Wall(int x, int y) {
			super(x,y);
		}
		@Override
		public String toString() {
			return "#";
		}
	}
	class Source extends Element {
		public Source(int x, int y) {
			super(x,y);
		}
		@Override
		public String toString() {
			return "+";
		}
	}
	class Sand extends Element {
		boolean infiniteFall;
		boolean falling;
		public Sand(int x, int y) {
			super(x,y);
			falling=true;
			infiniteFall=false;
		}
		@Override
		public String toString() {
			return "o";
		}
	}
//	public Sand getSandAt(int x, int y) {
//		Element e = matrix[x][y];
//		if(e instanceof Sand) return (Sand)e;
//		return null;
//	}
	
	public boolean stepSand(Sand fallingSand) {
		Element underFalling = null;
		if(maxY+yWidener-1<fallingSand.y+1) {
			fallingSand.infiniteFall=true;
			return false;
		}
		try {
			underFalling = matrix[fallingSand.x][fallingSand.y+1];
		} catch (Exception e) {
		}
		if(underFalling!=null) {
			Element leftUnder = null;
			try {
				leftUnder = matrix[fallingSand.x-1][fallingSand.y+1];
			} catch (Exception e) {
			}
			if(leftUnder==null) {
				matrix[fallingSand.x][fallingSand.y]=null;
				matrix[fallingSand.x-1][fallingSand.y+1]=fallingSand;
				fallingSand.x=fallingSand.x-1;
				fallingSand.y=fallingSand.y+1;
				return false;
			}
			
			Element rightUnder = null;
			try {
				rightUnder = matrix[fallingSand.x+1][fallingSand.y+1];
			} catch (Exception e) {
			}
			if(rightUnder==null) {
				matrix[fallingSand.x][fallingSand.y]=null;
				matrix[fallingSand.x+1][fallingSand.y+1]=fallingSand;
				fallingSand.x=fallingSand.x+1;
				fallingSand.y=fallingSand.y+1;
				return false;
			}
			
			fallingSand.falling=false;
			return true;
		} else {
			matrix[fallingSand.x][fallingSand.y]=null;
			matrix[fallingSand.x][fallingSand.y+1]=fallingSand;
			fallingSand.y=fallingSand.y+1;
			return false;
		}
	}
	
	public void sandFall() {
		int sandNumber=0;
		Sand fallingSand=new Sand(source.x,source.y);
		matrix[source.x][source.y]=fallingSand;
		while (true) {
			boolean stopped = stepSand(fallingSand);
			if(fallingSand.infiniteFall) {
				trace();
				log("Solution:"+sandNumber);
				break;
			} else if(!fallingSand.infiniteFall && stopped) {
				if(fallingSand.samePosition(source)) {
					trace();
					sandNumber++;
					log("Solution:"+sandNumber);
					break;
				}
				fallingSand=new Sand(source.x,source.y);
				matrix[source.x][source.y]=fallingSand;
				sandNumber++;
			}
		}
	}
	
	public static void main(String[] args) {
		Day14 d14 = new Day14();
		d14.readFile();
		d14.fillStructure();
		d14.trace();
		d14.sandFall();
	}
	
	public void trace() {
		for (int y = minY; y < maxY+yWidener; y++) {
			
			if(y>99)log(""+y);
			else if(y>9)log(" "+y);
			else log("  "+y);
			
			for(int x=minX-xWidener;x<maxX+xWidener;x++) {
				if(x==source.x&&y==source.y) 
					log("+");
				else if(matrix[x][y]!=null)
					log(""+matrix[x][y]); 
				else
					log(" ");
			}
			log("\n");
		}
		log("--");
		for(int x=minX;x<maxX+xWidener;x++) {
			log("-");
		}
		log("\n");
	}
}
