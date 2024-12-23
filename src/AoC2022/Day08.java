package AoC2022;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 */

public class Day08 {
	
	class Tree {
		long view;
		int height;
		boolean visible;
		boolean up;
		boolean down;
		boolean right;
		boolean left;
		Tree(int h){
			this(h,false);
		}
		Tree(int h, boolean v){
			height=h;
			visible=v;
			view=1;
		}
	}
	
	private File inputFile;
	private BufferedReader fileReader;
	private List<String> forrest;
	private Tree[][] trees;
	private int width;
	private int heigth;
	
	//nem 1469 az eredmény, ennél több van
	//1559-nel is tobb van
	//1703
	public Day08() throws Exception {
		inputFile = new File(".\\src\\input_08.txt");
//		inputFile = new File(".\\src\\input_08_ctrl.txt");
//		inputFile = new File(".\\src\\input_08_ctrl_spec.txt");
		fileReader = new BufferedReader(new FileReader(inputFile));
		forrest = new ArrayList<>();
		width=0;
		heigth=0;
	}
	
	public int countVisible() {
		int count=0;
		
		for(int x=0;x<forrest.size();x++) {
			for(int y=0;y<forrest.get(x).length();y++) {
				if(trees[y][x].visible) count++; 
			}
		}
		System.out.println("countv:"+count);
		return count;
	}
	
	//felfele megnezzuk h mi latszik
	public long goUp(int x, int y) {
		int dist=0;
		Tree checking = trees[x][y];
		Tree up=null;
		boolean latszik = true;
		for (int i=y-1;i>=0;i--) {
			dist++;
			up=trees[x][i];
			if(up.height >= checking.height) {
				latszik = false;
				break;
			}
		}
		if(latszik && !checking.visible) {
			checking.visible=true;
			checking.up=true;
		}
		return dist==0?1:dist;
	}
	
	public long goDown(int x, int y) {
		int dist=0;
		Tree checking = trees[x][y];
		Tree down=null;
		boolean latszik = true;
		for (int i=y+1;i<heigth;i++) {
			dist++;
			down=trees[x][i];
			if(down.height >= checking.height) {
				latszik = false;
				break;
			}
		}
		if(latszik && !checking.visible) {
			checking.visible=true;
			checking.down=true;
		}
		return dist==0?1:dist;
	}
	
	public long goRight(int x, int y) {
		int dist=0;
		Tree checking = trees[x][y];
		Tree right=null;
		boolean latszik = true;
		for (int i=x+1;i<width;i++) {
			dist++;
			right=trees[i][y];
			if(right.height >= checking.height) {
				latszik = false;
				break;
			}
		}
		if(latszik && !checking.visible) {
			checking.visible=true;
			checking.right=true;
		}
		return dist==0?1:dist;
	}
	
	public long goLeft(int x, int y) {
		int dist=0;
		Tree checking = trees[x][y];
		Tree left=null;
		boolean latszik = true;
		for (int i=x-1;i>=0;i--) {
			dist++;
			left=trees[i][y];
			if(left.height >= checking.height) {
				latszik = false;
				break;
			}
		}
		if(latszik && !checking.visible) {
			checking.visible=true;
			checking.left=true;
		}
		return dist==0?1:dist;
	}
	
	public void setVisibility(){
		//fentrol nezve x parameteren iteralok és y fix 0,0 az első 0,1 utana 0,2 azutan
//		System.out.println(trees[0][0].height);
//		System.out.println(trees[0][1].height);
//		System.out.println(trees[0][2].height);
//		System.out.println(trees[0][3].height);
//		System.out.println(trees[0][4].height);
		
		for(int i=0;i<width;i++) { //oszlop
			for(int j=0;j<heigth;j++) { //sor
				Tree tree = trees[i][j];
				

				if(i>0 && j >0) {
					long left=goLeft(i, j);
					long up=goUp(i,j);
					long down=goDown(i,j);
					long right=goRight(i,j);
					trees[i][j].view=left*up*down*right;
//					System.out.println("tree["+i+"]["+j+"] u:"+up+" l:"+left+" r:"+right+" d:"+down+" v:"+trees[i][j].view + " val:"+trees[i][j].height);
					
				}
				
				//bealitjuk a széleket lathatora
				if(j==0) {
					tree.visible=true;
					trees[i][j].view=0;
				}
				if(i==0) {
					tree.visible=true;
					trees[i][j].view=0;
				}
				if(j==heigth-1) {
					tree.visible=true;
					trees[i][j].view=0;
				}
				if(i==width-1) {
					tree.visible=true;
					trees[i][j].view=0;
				}
				
			}
		}
		System.out.println("\n");
		long maxValue=0;
		Tree maxTree=null;
		for(int x=0;x<forrest.size();x++) {
			for(int y=0;y<forrest.get(x).length();y++) {
				if(trees[y][x].up) System.out.print("U");
				else if(trees[y][x].down) System.out.print("D");
				else if(trees[y][x].right) System.out.print("R");
				else if(trees[y][x].left) System.out.print("L");
				else if(trees[y][x].visible) System.out.print("Y");
				else System.out.print("-");
				if(maxTree==null || trees[y][x].view > maxTree.view) {
					maxTree=trees[y][x];
				}
			}
			System.out.println();
		}
		System.out.println("Max tree view: "+maxTree.view);
	}
	
	public void fillTrees() {
		width=forrest.get(0).length();
		heigth=forrest.size();
		trees = new Tree [forrest.get(0).length()][forrest.size()];
//		System.out.println("-----");
		
		for(int x=0;x<forrest.size();x++) {
			for(int y=0;y<forrest.get(x).length();y++) {
//				System.out.print(forrest.get(x).charAt(y));
				trees[y][x]=new Tree(Integer.parseInt(Character.toString(forrest.get(x).charAt(y))));
			}
//			System.out.println();
		}
		
//		System.out.println("-----");
		for(int x=0;x<forrest.size();x++) {
			for(int y=0;y<forrest.get(x).length();y++) {
				System.out.print(trees[y][x].height);
			}
			System.out.println();
		}
	}
	
	public void readFile()throws Exception  {
		String strCurrentLine;
		while ((strCurrentLine = fileReader.readLine()) != null) {
			forrest.add(strCurrentLine);
//			System.out.println(strCurrentLine);
		}
		
	}

	public static void main(String[] args) {
		Day08 d08 = null;
		try {
			d08 = new Day08();
			d08.readFile();
			d08.fillTrees();
			d08.setVisibility();
			d08.countVisible();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
