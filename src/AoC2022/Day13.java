package AoC2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;

public class Day13 extends Day {
	
	class Node implements Comparable<Node>{
		public List<Node> nodes = null;
		Integer value = null;
		
		public boolean isListNode() {
			return value==null;
		}
		
		public Node(String s){
			nodes = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(s,"[],",true);
			String token = "";
			String delimiter = "";
			while(st.hasMoreTokens()) {
				token = st.nextToken();
				
				if(token.equals(",")) {
					s=s.substring(s.indexOf(token)+1);
					st = new StringTokenizer(s,"[],",true);
					continue;
				} else if(token.equals("]")) {
					delimiter=token;
				} else if(token.equals("[")) {
					// lista van a listában, levagni a lista zárójáig a stringet
					delimiter=token;
					
					int nextBracketPos = nextClosingBracketPos(s,token);
					String part = s.substring(s.indexOf(token)+1, nextBracketPos);
//					System.out.println("part:"+part);
					nodes.add(new Node(part));
					
					s=s.substring(nextBracketPos+1);
					st = new StringTokenizer(s,"[],",true);
				} else if(st.hasMoreTokens()) {
					String newToken = st.nextToken();
					if(newToken.equals(",")) {
						delimiter=newToken;
						// ha a vesszo elott nem lista volt akor azt fel kell még dolgozni
						if(!delimiter.equals("]")) {
							try {
//								System.out.println("adding int nodeINS:"+token);
								nodes.add(new Node(Integer.parseInt(token)));
							} catch (Exception e) {
								e.printStackTrace();
							}
							s=s.substring(s.indexOf(newToken)+1);
							st = new StringTokenizer(s,"[],",true);
						}
						 
					} else if(token.equals("]")) {
						System.out.println("how: TODO----------%!+!534jn53j4h5h43");
					} else if(token.equals("[")) {
						System.out.println("how: TODO----------%!+!534jn53j4h5h43");
					}
				} else {
					//vesszo utani reszt feldogloznie
					try {
//						System.out.println("adding int nodeELSE:"+token);
						int tI = Integer.parseInt(token);
						nodes.add(new Node(tI));
					} catch (Exception e) {
						e.printStackTrace();
					}
					s=s.substring(s.indexOf(token)+token.length());
					st = new StringTokenizer(s,"[],",true);
				}
			}
		}
		
		public int nextClosingBracketPos(String a, int i) {
			int open=0;
			int close=0;
			for(;i<a.length();i++) {
				if(a.substring(i, i+1).equals("[")) open++;
				if(a.substring(i, i+1).equals("]")) close++;

				if(open==close) break;
			}
			return i;
		}
		
		public int nextClosingBracketPos(String a, String token) {
			int open=0;
			int close=0;
			int i=0;
			for(i=a.indexOf(token);i<a.length();i++) {
				if(a.substring(i, i+1).equals("[")) open++;
				if(a.substring(i, i+1).equals("]")) close++;

				if(open==close) break;
			}
			return i;
		}
		
		private Node(Integer n) {
			value=n;
			nodes=null;
		}
		
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			if(nodes!=null) {
				sb.append("[");
				for (Node node : nodes) {
					sb.append(node.toString());
					if(nodes.lastIndexOf(node)!=nodes.size()-1)sb.append(",");
				}
				sb.append("]");
			} else {
				if(value!=null) sb.append(value);
			}
			return sb.toString();
		}
		
		public String processList(String a) {
			//[[9,[],[[10,10,1,4,10],[2,2,7,6,1],2,9]]]
			//[[10,10,1,4,10]
			
			int open=0;
			int close=0;
			int i=0;
			for(i=0;i<a.length();i++) {
				if(a.substring(i, i+1).equals("[")) open++;
				if(a.substring(i, i+1).equals("]")) close++;

				if(open==close) break;
			}
			return a.substring(1,i);
		}

		@Override
		public int compareTo(Node o) {
			Boolean result = evaluateNodes(this, o);
			if(result == null) return 0;
			return result?-1:1;
		}
	}
	
	public Boolean evaluateNodes(Node a, Node b) {
		int size = 0;
		try {
			size = Integer.max(a.nodes.size(), b.nodes.size());
		} catch (Exception e) {
		}
		if(size>0) {
			System.out.println("- Compare "+a.toString()+" vs "+b.toString());
			
			if(a.isListNode() && !b.isListNode() || !a.isListNode() && b.isListNode()) {
				if(a.isListNode()) {
					System.out.println("- Mixed types; convert right to ["+b.value+"] and retry comparison");
					Node newB = new Node("["+b.value+"]");
					newB=newB.nodes.get(0);
					return evaluateNodes(a, newB);
				} else if(b.isListNode()) {
					System.out.println("- Mixed types; convert right to ["+a.value+"] and retry comparison");
					Node newA = new Node("["+a.value+"]");
					newA=newA.nodes.get(0);
					return evaluateNodes(a, newA);
				}
			}
			
			for (int i=0; i<size; i++) {
				Node childA=null;
				try {
					childA = a.nodes.get(i);
				} catch (Exception e) {
				}
				Node childB=null;
				try {
					childB = b.nodes.get(i);
				} catch (Exception e) {
				}
				
				if(childA==null && childB != null) {
					System.out.println("- Left side ran out of items, so inputs are in the right order");
					return Boolean.TRUE;
				}
				if(childB==null && childA != null) {
					System.out.println("- Right side ran out of items, so inputs are not in the right order");
					return Boolean.FALSE;
				}
				Boolean res = evaluateNodes(childA, childB);
				if(res==null)
					continue;
				else
					return res;
			}
		} else if(a.isListNode() && !b.isListNode() || !a.isListNode() && b.isListNode()) {
			if(a.isListNode()) {
				System.out.println("- Mixed types; convert right to ["+b.value+"] and retry comparison");
				Node newB = new Node("["+b.value+"]");
				newB=newB.nodes.get(0);
				return evaluateNodes(a, newB);
			} else if(b.isListNode()) {
				System.out.println("- Mixed types; convert right to ["+a.value+"] and retry comparison");
				Node newA = new Node("["+a.value+"]");
				newA=newA.nodes.get(0);
				return evaluateNodes(newA, b);
			}
		} else if(a.isListNode()&&a.nodes.size()==0 && b.isListNode() && b.nodes.size()==0&&a.value==null&&b.value==null) {
			
		} else {
			System.out.println("  - Compare "+a.value+" vs "+b.value);
			if(a.value<b.value) {
				System.out.println("- Left side is smaller, so inputs are in the right order");
				return Boolean.TRUE; 
			}
			if(a.value>b.value) {
				System.out.println("- Right side is smaller, so inputs are not in the right order");
				return Boolean.FALSE;
			}
			return null;
		}
		return null;
	}
	
	
	public void execute() {
		List<Node> nodes = new ArrayList<>();
		String lines[] = sb.toString().split("\n");
		int solution=0;
		int pairNumber=1;
		for (int i = 0; i < lines.length; i+=3) {
			String a = lines[i];
			String b = lines[i+1];
			
//			if(pairNumber==6) {
//				System.out.println("coming baby");
//			}
			
			System.out.println("== Pair "+pairNumber+" ==");
			Node aNode = new Node(a);
			aNode=aNode.nodes.get(0);
			Node bNode = new Node(b);
			bNode=bNode.nodes.get(0);
			nodes.add(aNode);
			nodes.add(bNode);
			
			Boolean result = evaluateNodes(aNode, bNode);
			if(result)
				solution+=pairNumber;
			
			pairNumber++;
		}
		System.out.println("Solution1:"+solution);
		//5181 too low
		//6046 a megoldás
		
		Node newNode1 = new Node("[[2]]");
		newNode1=newNode1.nodes.get(0);
		nodes.add(newNode1);
		Node newNode2 = new Node("[[6]]");
		newNode2=newNode2.nodes.get(0);
		nodes.add(newNode2);
		
		Collections.sort(nodes);
		int solution2=(nodes.indexOf(newNode1)+1)*(nodes.indexOf(newNode2)+1);
		System.out.println("Solution2:"+solution2);
	}
	
	public Day13() {
		super();
//		anotherDay = new coa.sbaars.Day13();
//		anotherDay.part1();
	}
	
	public static void main(String[] args) {
		Day13 d13=new Day13();
		d13.readFile();
		d13.execute();
		
	}

}
