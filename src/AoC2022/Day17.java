package AoC2022;

public class Day17 extends Day {

	public static final int WIDTH = 7;
	
	private boolean[][] scene;
	private String[] ops;
	private int height;
	private long cumulatedHeight;
	private Rock rock1;
	private Rock rock2;
	private Rock rock3;
	private Rock rock4;
	private Rock rock5;
	private Rock[] rocks;
	private Rock fallingRock;
	long counter;
	int opnumber=0;
	int x;
//	int y;
	
	class Rock{
		boolean[][] rock;
		int spaceToRight;
		int actualHeight;
		int height;
		String name;
		
		
		public Rock(boolean[][] rock, int spaceToRight, int height, String name) {
			this.rock = rock;
			this.spaceToRight = spaceToRight;
			this.height=height;
			this.name=name;
		}
		
	}
	
	public Day17() {
	}
	
	public void fillStructure() {
		ops = sb.toString().replaceAll("\n","").split("");
		logln("Number of ops:"+ops.length);
	}
	
	public boolean checkAndCopy() {
		boolean found=false;
		for(int i=x;i<x+4;i++) {
			if(i>=WIDTH) continue;
			if(fallingRock.actualHeight == 0 
				|| (scene[fallingRock.actualHeight-1][i] && fallingRock.rock[0][i-x])
				|| (i-1>=0 && i-x-1 >=0 && scene[fallingRock.actualHeight][i-1] && fallingRock.rock[1][i-x-1])
				|| (i+1<WIDTH && i-x+1<4 && scene[fallingRock.actualHeight][i+1] && fallingRock.rock[1][i-x+1])
				) {
				found = true;
				break;
			}
		}
		if(found) {
			//copy to scene
			for(int i=height+3+3;i>=0;i--) {
				for(int j=0;j<WIDTH;j++) {
					//ha beleesik a rock sorába
					if(i<=fallingRock.actualHeight+3 && i >= fallingRock.actualHeight) {
						if(j>=x && j<x+4) {
							if(fallingRock.rock[i-fallingRock.actualHeight][j-x]) {
								scene[i][j]=true;
							}
						} 
					}
				}
			}
			
			return true;
		}
		return false;
	}
	
	public boolean stepRight(int x) {
		boolean blocked = false;
		for(int i=0;i<fallingRock.rock.length;i++) {
			
			int j=fallingRock.rock[i].length-1;
			boolean foundRightmostRockForRow=false;
			for(;j>=0;j--) {
				if(fallingRock.rock[i][j]) {
					foundRightmostRockForRow=true;
					break;
				}
			}
			
			if(foundRightmostRockForRow && (x+j >=  scene[fallingRock.actualHeight+i].length-1 || scene[fallingRock.actualHeight+i][x+j+1])) {
				blocked = true;
				break;
				
			}
		}
		return !blocked;
	}
	
	public boolean stepLeft(int x) {
		boolean blocked = false;
		for(int i=0;i<fallingRock.rock.length;i++) {
			
			int j=0;
			boolean foundLeftmostRockForRow=false;
			for(;j<fallingRock.rock[i].length;j++) {
				if(fallingRock.rock[i][j]) {
					foundLeftmostRockForRow=true;
					break;
				}
			}
			
			if(foundLeftmostRockForRow && (x+j-1 <  0 || scene[fallingRock.actualHeight+i][x+j-1])) {
				blocked = true;
				break;
				
			}
		}
		return !blocked;
	}
	
	public void resetStructure() {
		opnumber=0;
		height=0;
		cumulatedHeight=0;
		counter=0L;
		
		rock1 = new Rock(new boolean[][] 
				{{true ,true ,true ,true },
				 {false,false,false,false},
				 {false,false,false,false},
				 {false,false,false,false}},0,1,"1");
		rock2 = new Rock(new boolean[][] 
				{{false,true ,false,false},
				 {true ,true ,true ,false},
				 {false,true ,false,false},
				 {false,false,false,false}},1,3,"2");
		rock3 = new Rock(new boolean[][] 
				{{true ,true ,true ,false},
				 {false,false,true ,false},
				 {false,false,true ,false},
				 {false,false,false,false}},1,3,"3");
		rock4 = new Rock(new boolean[][] 
				{{true ,false,false,false},
				 {true ,false,false,false},
				 {true ,false,false,false},
				 {true ,false,false,false}},3,4,"4");
		rock5 = new Rock(new boolean[][] 
				{{true ,true ,false,false},
				 {true ,true ,false,false},
				 {false,false,false,false},
				 {false,false,false,false}},2,2,"5");
		rocks = new Rock[] {rock1,rock2,rock3,rock4,rock5};
		rock1.actualHeight=0;
		rock2.actualHeight=0;
		rock3.actualHeight=0;
		rock4.actualHeight=0;
		rock5.actualHeight=0;
		
		scene= new boolean[ops.length*rocks.length*4*4][WIDTH];
		fallingRock = null;
	}
	
	public void part1() {
		
		resetStructure();
		
		while(true) {
			for(Rock rock:rocks) {
				x = 2;
				fallingRock = rock;
				counter++;
				rock.actualHeight=height+3;
//				trace("new");
				
				while(true) {
					
					String op = ops[opnumber++%ops.length];
					if(op.equals(">")) {
						x=stepRight(x)?++x:x;
					} else if(op.equals("<")) {
						x=stepLeft(x)?--x:x;
					}
//					trace(op);
					//megnézni hogy lefelé van e ütközés, ha igen, akkor 
					if(checkAndCopy()) {
						
						if(fallingRock.actualHeight+fallingRock.height>height) {
							height=fallingRock.actualHeight+fallingRock.height; //csak akkor megyünk magasabba ha felfelé jobban kilógunk mint eddig
							cumulatedHeight=height;
						}
//						trace("v");
						break;
					} else {
						fallingRock.actualHeight--;
//						trace("\\/");
					}
				}
				
				if(counter>=2022) {
//					trace("end");
					logln("Solution part1:"+height);
					flush();
					return;
				}
			}
		}
	}
	
	public void part2() {
		
		resetStructure();
		
		boolean foundSecond = false;
		boolean foundThird = false;
		long firstRockCount = 0;
		long secondRockCount = 0;
		long thirdRockCount = 0;
		long firstHeight = 0;
		int secondHeight = 0;
		int thirdHeight = 0;
		String secondString=null;
		String thirdString=null;
		long multiplierRock=0;
		long residualRock=0;
		
		while(true) {
			for(Rock rock:rocks) {
				x = 2;
				fallingRock = rock;
				counter++;
				rock.actualHeight=height+3;
//				trace("new");
				
				while(true) {
					
					String op = ops[opnumber++%ops.length];
					if(op.equals(">")) {
						x=stepRight(x)?++x:x;
					} else if(op.equals("<")) {
						x=stepLeft(x)?--x:x;
					}
					
					if(opnumber/(ops.length*rocks.length)==2 && opnumber%(ops.length*rocks.length)==0) {
						foundSecond = true;
//						trace("second");
					}
					
					if(opnumber/(ops.length*rocks.length)==3 && opnumber%(ops.length*rocks.length)==0) {
						foundThird = true;
//						trace("third");
					}
					
//					if(counter==(thirdRockCount+residualRock)) {
//						long heigthDiff = height-thirdHeight;
//						long finalHeigth =  (thirdHeight-secondHeight)*multiplierRock+heigthDiff;
//						logln("Solution part2:"+finalHeigth);
//						return;
//					}
					
//					trace(op);
					//megnézni hogy lefelé van e ütközés, ha igen, akkor 
					if(checkAndCopy()) {
						
						if(fallingRock.actualHeight+fallingRock.height>height) {
							height=fallingRock.actualHeight+fallingRock.height; //csak akkor megyünk magasabba ha felfelé jobban kilógunk mint eddig
							cumulatedHeight=height;
						}
						if(foundSecond) {
//							secondString=trace("opnumber");
							secondRockCount = counter;
							secondHeight=height;
							foundSecond=false;
						}
						if(foundThird) {
//							thirdString=trace("opnumber");
							thirdRockCount = counter;
							thirdHeight=height;
							
							firstRockCount = thirdRockCount-2*(thirdRockCount-secondRockCount);
							firstHeight = thirdHeight - 2*(thirdHeight-secondHeight);
							
							multiplierRock = (1000000000000L - firstRockCount)/(thirdRockCount-secondRockCount); //28571428571
							residualRock = 1000000000000L-firstRockCount-multiplierRock*(thirdRockCount-secondRockCount);
							
							foundThird=false;
						}
						

						if(counter==(thirdRockCount+residualRock)) {
//							trace("residual");
							long heigthDiff = height-thirdHeight;
							long finalHeigth =  firstHeight+(thirdHeight-secondHeight)*multiplierRock+heigthDiff;
							logln("Solution part2:"+finalHeigth);
							return;
							// 1564705882325 too low
							// 1564705882327 correct
							
						}
						break;
					} else {
						fallingRock.actualHeight--;
//						trace("\\/");
					}
				}
				
//				if(counter>=2022) {
////					trace("end");
//					logln("Solution part1:"+height);
//					flush();
//					return;
//				}
			}
		}
	}
	
	public String trace(String op) {
		StringBuffer opLog = new StringBuffer();
		opLog.append("rock ["+counter+"] op:"+op+" ["+opnumber+"] height:"+height+" cumh:"+cumulatedHeight+"\n");
		int meddig = Math.max(height-10,0);
		for(int i=height+3+3;i>=0;i--) {
//		for(int i=height+3+3;i>=meddig;i--) {
			int pretty=i;
//			int pretty=i+1;
			if(pretty>999)
				opLog.append(""+pretty);
			else if(pretty>99)
				opLog.append(" "+pretty);
			else if(pretty>9)
				opLog.append("  "+pretty);
			else
				opLog.append("   "+pretty);
				
			opLog.append("|");
			for(int j=0;j<WIDTH;j++) {
				//ha beleesik a rock sorába
				if(i<=fallingRock.actualHeight+3 && i >= fallingRock.actualHeight) {
					if(j>=x && j<x+4) {
						if(scene[i][j]) {
							opLog.append("#");
						} else {
//							log(fallingRock.rock[i-y-3][j-x]?"@":".");
							opLog.append(fallingRock.rock[i-fallingRock.actualHeight][j-x]?"@":".");
						}
					} else {
						opLog.append(scene[i][j]?"#":".");
					}
				} else {
					opLog.append(scene[i][j]?"#":".");
				}
			}
			opLog.append("|\n");
		}
		
		opLog.append("    +");
		for(int j=0;j<WIDTH;j++) {
			opLog.append("-");
		}
		opLog.append("+");
		opLog.append("\n");
		opLog.append("\n");
		log(opLog.toString());
		flush();
		return opLog.toString();
	}
	

	public static void main(String[] args) {
		
		Day17 d17 = new Day17();
		
		d17.readFile();
		d17.fillStructure();
		d17.part1();
		d17.part2();
	}

}
