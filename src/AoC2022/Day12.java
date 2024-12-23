package AoC2022;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Day12 extends Day {
	
	class Graph {

        private Set<Node> nodes = new HashSet<>();
        
        public void addNode(Node nodeA) {
            nodes.add(nodeA);
        }

    }
	
	class Node {
		String stringValue;
		int value;
		List<Node> connections;
		int x;
		int y;
		boolean startNode;
		boolean endNode;
		
		private List<Node> shortestPath;
	    private Integer distance;
	    
	    public void resetNode() {
	    	shortestPath.clear();
			distance = Integer.MAX_VALUE;
	    }
		
		public Node(int ch,int i,int j) {
			value=ch;
			stringValue = Character.toString(value);
			connections=new ArrayList<>();
			
			if(stringValue.equals("S")){
				startNode=true;
				value="a".charAt(0)-1;
			} else if(stringValue.equals("a")){
				startNode=true;
			} else
				startNode=false;
			
			endNode=stringValue.equals("E");
			if(endNode) {
				value="z".charAt(0)+1;
			}
			x=i;
			y=j;
			shortestPath= new LinkedList<>();
			distance = Integer.MAX_VALUE;
		}
	}
	
	private int width;
	private int heigth;
	private List<Node> sources;
	private Node endNode;
//	private int startX;
//	private int startY;
//	private int endX;
//	private int endY;
	private Node[][] matrix;

	public Day12() {
		super();
		sources=new ArrayList<>();
	}
	
	public void calculateShortestPathFromSource() {
		
		int shortestPath=Integer.MAX_VALUE;
		for (Node source : sources) {
			
			for(int x=0;x<heigth;x++) {
				for(int y=0;y<width;y++) {
					matrix[x][y].resetNode();
				}
			}
			
			source.distance=0;

		    Set<Node> settledNodes = new HashSet<>();
		    Set<Node> unsettledNodes = new HashSet<>();

		    unsettledNodes.add(source);

		    while (unsettledNodes.size() != 0) {
		        Node currentNode = getLowestDistanceNode(unsettledNodes);
		        unsettledNodes.remove(currentNode);
		        for (Node adjacentNode: currentNode.connections) {
		            Integer edgeWeight = 1;
		            if (!settledNodes.contains(adjacentNode)) {
		                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
		                unsettledNodes.add(adjacentNode);
		            }
		        }
		        settledNodes.add(currentNode);
		    }
		    System.out.println("shortest path:"+endNode.shortestPath.size());
		    for (Node node : endNode.shortestPath) {
				System.out.print(node.stringValue+"->");
			}
		    if(endNode.shortestPath.size()>0 && endNode.shortestPath.size()<shortestPath) {
		    	shortestPath = endNode.shortestPath.size();
		    }
		}
		System.out.println("The ultimate shortest path: "+shortestPath);
	    
	}
	
	private Node getLowestDistanceNode(Set < Node > unsettledNodes) {
	    Node lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Node node: unsettledNodes) {
	        int nodeDistance = node.distance;
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}
	
	private void calculateMinimumDistance(Node evaluationNode,
	  Integer edgeWeigh, Node sourceNode) {
	    Integer sourceDistance = sourceNode.distance;
	    if (sourceDistance + edgeWeigh < evaluationNode.distance) {
	        evaluationNode.distance= (sourceDistance + edgeWeigh);
	        LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.shortestPath);
	        shortestPath.add(sourceNode);
	        evaluationNode.shortestPath = shortestPath;
	    }
	}
	
	public void loadConnections(Node n, int x, int y) {
		
		Node mate=null;
		try { mate = matrix[x][y];} catch (Exception e) {}

		
		if(mate!=null&&!mate.stringValue.equals("S")&&!n.stringValue.equals("E")) {
			
			if(n.value==mate.value || n.value+1==mate.value || n.value>mate.value) {
				n.connections.add(mate);
			}
		}
	}
	
	public void fillMatrix() {
		String[] lines = sb.toString().split("\n");
		width=lines[0].length();
		heigth=lines.length;
		matrix = new Node[heigth][width];
		
		for(int x=0;x<heigth;x++) {
			String line = lines[x];
			for(int y=0;y<width;y++) {
				matrix[x][y]=new Node(line.charAt(y),x,y);
				if(matrix[x][y].startNode) sources.add(matrix[x][y]);
				if(matrix[x][y].endNode) endNode = matrix[x][y];
			}
		}
		for(int x=0;x<heigth;x++) {
			log(x+(x>9?" ":"  "));
			for(int y=0;y<width;y++) {
				Node n = matrix[x][y];
				
				loadConnections(n, x-1,y);
				loadConnections(n, x+1,y);
				loadConnections(n, x,y-1);
				loadConnections(n, x,y+1);
				
				log("["+n.stringValue+":"+n.connections.size()+"]");
			}
			log("\n");
		}
	}
	
	public void readFile() {
		String strCurrentLine;
		
		try {
			while ((strCurrentLine = fileReader.readLine()) != null) {
				sb.append(strCurrentLine).append("\n");
//				log(strCurrentLine+"\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
		Day12 d12 = new Day12();
		d12.readFile();
		d12.fillMatrix();
		d12.calculateShortestPathFromSource();
	}
}
