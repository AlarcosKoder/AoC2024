package AoC2022;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Day16 extends Day {

	private TreeMap<String,Valve> allValves;
	private List<Valve> valves;
	private String maxSizeTraverseMe;
	private String maxSizeTraverseEli;
	private int maxValue;
	private Valve startValve;
	private static long counter;
	
	public Day16() {
		super();
		allValves=new TreeMap<>();
		valves = new LinkedList<>();
		maxValue=Integer.MIN_VALUE;
		maxSizeTraverseMe="";
		maxSizeTraverseEli="";
		startValve=null;
		counter=0;
	}
	
	class Valve implements Comparable<Valve>{
		String name;
		int rate;
		boolean open;
		int timer;
		List<String> connectionString;
		Map<Valve, Integer> adjacentNodes;
		private List<Valve> shortestPath;
		private Integer distance;
		public Valve(Valve v) {
			this.name=v.name;
			this.rate=v.rate;
			open=false;
			timer=0;
			connectionString=new ArrayList<>();
			shortestPath= new LinkedList<>();
			distance = Integer.MAX_VALUE;
			adjacentNodes = new TreeMap<>();
		}
		public boolean expired() {
			return --timer<=0?true:false;
		}
		public Valve(String name,int rate) {
			this.name=name;
			this.rate=rate;
			open=false;
			timer=0;
			connectionString=new ArrayList<>();
			shortestPath= new LinkedList<>();
			distance = Integer.MAX_VALUE;
			adjacentNodes = new TreeMap<>();
		}
		public void resetValve() {
	    	shortestPath.clear();
			distance = Integer.MAX_VALUE;
	    }
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Valve "+name+" flow rate="+rate+"; tunnels lead to valves ");
			int index=1;
			for (Valve v : adjacentNodes.keySet()) {
				sb.append(v.name).append("["+adjacentNodes.get(v)+"]");
				if(index++  <adjacentNodes.size())
					sb.append(", ");
				else
					sb.append("\n");
			}
			return sb.toString();
		}
		@Override
		public int compareTo(Valve o) {
			return name.compareTo(o.name);
//			if(rate==o.rate)return 0;
//			return rate>o.rate?-1:1;
		}
	}
	
	public int evalOpenValves(List<Valve> openedValves) {
		int sum=0;
		for(Valve v:openedValves) {
			sum+=v.rate;
		}
		return sum;
	}
	

	
	public void part1() {
		List<Valve> tempValves = new ArrayList<>();
		for(String key:allValves.keySet()) {
			Valve v = allValves.get(key);
			if(v.rate>0 || v.name.equals("AA"))
				tempValves.add(v);
			
		}
		
		logln("valves:");
		logln(""+tempValves);
		logln("-------");

		HashMap<String,Valve> valvesMap = new HashMap<>();
		for(Valve source:tempValves) {
			Valve valve = new Valve(source);
			valvesMap.put(valve.name,valve);
		}
		
		for(Valve source:tempValves) {
			for(Valve to:tempValves) {
				int dist = calculateShortestPathFromNode(source, to);
				if(source != to && !to.name.equals("AA"))
					valvesMap.get(source.name).adjacentNodes.put(valvesMap.get(to.name), Integer.valueOf(dist+1));
			}
		}
		
		valves.addAll(valvesMap.values());
		Collections.sort(valves);
		for(Valve v:valves) {
			logln("Valve: "+v);
		}
		logln("-------");
		
		List<Valve> visited = new ArrayList<>();
		
		for(Valve v:valves) {
			if(v.name.equals("AA"))
				startValve=v;
		}
		long t1=System.currentTimeMillis();
		visited.add(startValve);
		DSF(startValve,visited,30,0,startValve.name);
		long t2=System.currentTimeMillis();
		logln("Part 1 solution:"+maxValue+" order:"+maxSizeTraverseMe+" ["+(t2-t1)/100+"s]");
		
		//1586 answer is too low
		//1638 correct answer
		//1685 answer is too high
	}
	
	public void DSF(Valve v, List<Valve> visited, int timer, int value, String traversingMe) {
		if(valves.size()==visited.size()) {
			if(value>maxValue) {
				maxValue=value;
				maxSizeTraverseMe=traversingMe;
//				System.out.println("found bigger: "+maxValue+" o:"+maxSizeTraverseMe);
			}
			return;
		}
		
		for(Valve adjacent : v.adjacentNodes.keySet()) {
			if(visited.contains(adjacent))
				continue;
			
			int distance = v.adjacentNodes.get(adjacent);
			// ha idejovok es megnyitom akkor ahátralevő időre hozzáadom + DSF a hátralévőre
			if(timer-distance<0) {
				if(value>maxValue) {
					maxValue=value;
					maxSizeTraverseMe=traversingMe;
//					System.out.println("found bigger: "+maxValue+" o:"+maxSizeTraverseMe);
				}
				continue;
			}
			visited.add(adjacent);
			DSF(adjacent,visited,timer-distance,value+adjacent.rate*(timer-distance),traversingMe+"->"+adjacent.name);
			visited.remove(adjacent);
		}
	}
	
	public void part2() {
		//2400 solution
		maxValue=Integer.MIN_VALUE;
		
		List<Valve> visited = new ArrayList<>();
		List<Valve> nonvisited = new ArrayList<>();
		nonvisited.addAll(valves);
		
		StringBuffer output = new StringBuffer();
		
		visited.add(startValve);
		long t1=System.currentTimeMillis();
		DSF2(startValve,startValve,visited,nonvisited,0,26,0,startValve.name,startValve.name,output);
		long t2=System.currentTimeMillis();
		logln("Part 2 solution:"+maxValue +"["+(t1-t1)/100+"s]");
		logln(" order4Me:"+maxSizeTraverseMe);
		logln(" order4Eli:"+maxSizeTraverseEli);
		
	}
	
	public Valve getNextNonVisitedNotCurrent(Valve forValve, Valve me, Valve elephant, List<Valve> visited) {
		Valve returnValve = forValve;
		for (Valve valve : forValve.adjacentNodes.keySet()) {
			if(visited.contains(valve) || valve.equals(me) || valve.equals(elephant) || valve.equals(forValve))
					continue;
		}
		return returnValve;
	}
	
	public void DSF2(Valve me, Valve elephant, List<Valve> visited, List<Valve> nonvisited, int timer, int endTime, int value, String traversingMe, String traversingEli, StringBuffer output) {
		counter++;
		if(counter%10000000==0){
			logln("counter:"+counter);
//			System.out.println(" Max result:"+maxValue);
//			System.out.println(" order4Me:"+maxSizeTraverseMe);
//			System.out.println(" order4Eli:"+maxSizeTraverseEli);
			logln("Currently checking");
			logln(" order4Me:"+traversingMe);
			logln(" order4Eli:"+traversingEli);
		}
		/*
Part 2 solution:1790
 order4Me:AA->JJ->CC->BB
 order4Eli:AA->DD->HH->EE -> ezt kell megdebuggolni
 
 == Minute 3 == [1912]
Valve DD, open, releasing 20 pressure value:20
You open valve JJ
Elephant moves to valve HH left:4

== Minute 4 == [1945]
Valve DD, JJ, open, releasing 41 pressure value:61
You move to valve CC left:4
Elephant opens valve HH

 traversingMe.equals("AA->JJ->BB->CC") && traversingEli.equals("AA->DD->HH->EE")
		*/
		if(timer>endTime || valves.size()==visited.size()) {
			
			if(timer<endTime)
				value+=(endTime-timer+1)*getOpenValues(visited);
			
			if(value>maxValue) {
//				System.out.println(output.toString());
				logln(" BIGGEST SO FAR");
				logln(" Current result:"+value);
				logln(" order4Me:"+traversingMe);
				logln(" order4Eli:"+traversingEli);
				logln("------------------------");
				maxValue=value;
				maxSizeTraverseMe = traversingMe;
				maxSizeTraverseEli = traversingEli;
			}
			
			return;
		}
		
		int newValue=0;
		boolean meExpired = me.expired();
		boolean eliExpired = elephant.expired();
		
		newValue=value+getOpenValues(visited);
		
		StringBuffer newBuffer = new StringBuffer(output);
		newBuffer.append(trace(me,elephant,visited,timer,newValue));
		
		if(meExpired && eliExpired) {
			
			if(!me.equals(startValve))
				visited.add(me);
			nonvisited.remove(me);
			if(!me.equals(startValve))
				visited.add(elephant);
			nonvisited.remove(elephant);
			
			boolean foundMe=false;
			for(Valve newMe:nonvisited) {
				foundMe=true;
				List<Valve> otherNonVisiteds = new ArrayList<>(nonvisited);
				otherNonVisiteds.remove(newMe);
				boolean foundEli=false;
				for(Valve newEli:otherNonVisiteds) {
					foundEli=true;
					List<Valve> passingNonVisiteds = new ArrayList<>(otherNonVisiteds);
					passingNonVisiteds.remove(newEli);
					newMe.timer = me.adjacentNodes.get(newMe).intValue();
					newEli.timer = elephant.adjacentNodes.get(newEli).intValue();
					DSF2(newMe,newEli,visited,passingNonVisiteds,timer+1,endTime,newValue,traversingMe+"->"+newMe.name,traversingEli+"->"+newEli.name,newBuffer);
				}
				if(!foundEli) {
					newMe.timer = me.adjacentNodes.get(newMe).intValue();
					elephant.timer=Integer.MAX_VALUE;
					int oldEliTimer=elephant.timer;
					DSF2(newMe,elephant,visited,otherNonVisiteds,timer+1,endTime,newValue,traversingMe+"->"+newMe.name,traversingEli,newBuffer);
					elephant.timer=oldEliTimer;
					
					newMe.timer = elephant.adjacentNodes.get(newMe).intValue();
					me.timer=Integer.MAX_VALUE;
					int oldMeTimer=me.timer;
					DSF2(me,newMe,visited,otherNonVisiteds,timer+1,endTime,newValue,traversingMe,traversingEli+"->"+newMe.name,newBuffer);
					me.timer=oldMeTimer;
				}
			}
			
			if(!foundMe) {
				me.timer=Integer.MAX_VALUE;
				elephant.timer=Integer.MAX_VALUE;
				DSF2(me,elephant,visited,nonvisited,timer+1,endTime,newValue,traversingMe,traversingEli,newBuffer);
			}
			visited.remove(me);
			visited.remove(elephant);
		} else if(meExpired) {
			visited.add(me);
			boolean foundMe=false;
			for(Valve newMe:nonvisited) {
				foundMe=true;
				List<Valve> otherNonVisiteds = new ArrayList<>(nonvisited);
				otherNonVisiteds.remove(newMe);
				
				newMe.timer = me.adjacentNodes.get(newMe).intValue();
				int oldEliTimer=elephant.timer;
				DSF2(newMe,elephant,visited,otherNonVisiteds,timer+1,endTime,newValue,traversingMe+"->"+newMe.name,traversingEli,newBuffer);
				elephant.timer=oldEliTimer;
			}
			if(!foundMe) {
				me.timer=Integer.MAX_VALUE;
				int oldEliTimer=elephant.timer;
				DSF2(me,elephant,visited,nonvisited,timer+1,endTime,newValue,traversingMe,traversingEli,newBuffer);
				elephant.timer=oldEliTimer;
			}
			visited.remove(me);
		} else if(eliExpired) {
			visited.add(elephant);
			boolean foundEli=false;
			for(Valve newEli:nonvisited) {
				foundEli=true;
				List<Valve> otherNonVisiteds = new ArrayList<>(nonvisited);
				otherNonVisiteds.remove(newEli);
				
				newEli.timer = elephant.adjacentNodes.get(newEli).intValue();
				int oldMeTimer=me.timer;
				DSF2(me,newEli,visited,otherNonVisiteds,timer+1,endTime,newValue,traversingMe,traversingEli+"->"+newEli.name,newBuffer);
				me.timer=oldMeTimer;
			}
			if(!foundEli) {
				elephant.timer=Integer.MAX_VALUE;
				int oldMeTimer=me.timer;
				DSF2(me,elephant,visited,nonvisited,timer+1,endTime,newValue,traversingMe,traversingEli,newBuffer);
				me.timer=oldMeTimer;
			}
			visited.remove(elephant);
		} else {
			DSF2(me,elephant,visited,nonvisited,timer+1,endTime,newValue,traversingMe,traversingEli,newBuffer);
		}
		
	}
	
	public StringBuffer trace(Valve me, Valve elephante, List<Valve> visited, int timer, int value) {
		StringBuffer newBuffer = new StringBuffer();
		if(me.name.equals("AA") || elephante.name.equals("AA")) return newBuffer;
		
		newBuffer.append("== Minute "+timer+" ==\n");
//		newBuffer.append("== Minute "+timer+" == ["+counter+"]\n");
		if(visited.size()>1) {
			newBuffer.append("Valve ");
			for(Valve v:visited) {
				if(!v.name.equals("AA"))newBuffer.append(v.name+", ");
			}
			newBuffer.append("open, releasing "+getOpenValues(visited,null)+" pressure value:"+value+"\n");
		} else {
			newBuffer.append("No valves are open.\n");
		}
		if(me.timer>0) {
			newBuffer.append("You move to valve " + me.name +" left:"+me.timer+"\n");
		} else {
			newBuffer.append("You open valve " + me.name+"\n");
		}
		
		if(elephante.timer>0) {
			newBuffer.append("Elephant moves to valve " + elephante.name +" left:"+elephante.timer+"\n");
		} else {
			newBuffer.append("Elephant opens valve " + elephante.name+"\n");
		}
		newBuffer.append("\n");
		return newBuffer;
	}
	
	public String getValveNames(List<Valve> visited) {
		StringBuffer names = new StringBuffer();
		for(Valve v:visited)
			names.append(v.name).append("->");
		return names.toString();
	}
	
	public int getOpenValues(List<Valve> visited) {
		return getOpenValues(visited,null);
	}
	
	public int getOpenValues(List<Valve> visited, Valve except) {
		int allRate = 0;
		for(Valve v:visited) {
			if(except!=null && v.equals(except))
				continue;
			allRate+=v.rate;
		}
		return allRate;
	}
	
	public int remainingValue(List<Valve> visited, int timer) {
		int allRate = 0;
		for(Valve v:visited)
			allRate+=v.rate;
		return timer*allRate;
	}
	
	public int calculateShortestPathFromNode(Valve source, Valve to) {
		for(String key:allValves.keySet()) {
			allValves.get(key).resetValve();
		}
		
		source.distance=0;

	    Set<Valve> settledNodes = new HashSet<>();
	    Set<Valve> unsettledNodes = new HashSet<>();

	    unsettledNodes.add(source);

	    while (unsettledNodes.size() != 0) {
	    	Valve currentNode = getLowestDistanceNode(unsettledNodes);
	        unsettledNodes.remove(currentNode);
	        for (Valve adjacentNode: currentNode.adjacentNodes.keySet()) {
	            Integer edgeWeight = 1;//currentNode.rate;
	            if (!settledNodes.contains(adjacentNode)) {
	                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
	                unsettledNodes.add(adjacentNode);
	            }
	        }
	        settledNodes.add(currentNode);
	    }
	    return to.shortestPath.size();
	}
	
	public void calculateShortestPathFromSource() {
		
		//https://www.baeldung.com/cs/shortest-path-visiting-all-nodes
		
//		int shortestPath=Integer.MAX_VALUE;
		Valve source = allValves.get("JJ");
//		for (Valve source : valves) {
			
			for(String key:allValves.keySet()) {
				allValves.get(key).resetValve();
			}
			
			source.distance=0;

		    Set<Valve> settledNodes = new HashSet<>();
		    Set<Valve> unsettledNodes = new HashSet<>();

		    unsettledNodes.add(source);

		    while (unsettledNodes.size() != 0) {
		    	Valve currentNode = getLowestDistanceNode(unsettledNodes);
		        unsettledNodes.remove(currentNode);
		        for (Valve adjacentNode: currentNode.adjacentNodes.keySet()) {
		            Integer edgeWeight = 1;//currentNode.rate;
		            if (!settledNodes.contains(adjacentNode)) {
		                calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
		                unsettledNodes.add(adjacentNode);
		            }
		        }
		        settledNodes.add(currentNode);
		    }
//		    System.out.println("shortest path:"+endNode.shortestPath.size());
//		    for (Valve node : endNode.shortestPath) {
//				System.out.print(node.stringValue+"->");
//			}
//		    if(endNode.shortestPath.size()>0 && endNode.shortestPath.size()<shortestPath) {
//		    	shortestPath = endNode.shortestPath.size();
//		    }
//		}
		System.out.println("The ultimate shortest path: "+allValves.get("HH").shortestPath.size());
		System.out.println("Shortest path: "+allValves.get("HH").shortestPath);
	    
	}
	
	private Valve getLowestDistanceNode(Set < Valve > unsettledNodes) {
	    Valve lowestDistanceNode = null;
	    int lowestDistance = Integer.MAX_VALUE;
	    for (Valve node: unsettledNodes) {
	        int nodeDistance = node.distance;
	        if (nodeDistance < lowestDistance) {
	            lowestDistance = nodeDistance;
	            lowestDistanceNode = node;
	        }
	    }
	    return lowestDistanceNode;
	}
	
	private void calculateMinimumDistance(Valve evaluationNode,
	  Integer edgeWeigh, Valve sourceNode) {
	    Integer sourceDistance = sourceNode.distance;
	    if (sourceDistance + edgeWeigh < evaluationNode.distance) {
	        evaluationNode.distance= (sourceDistance + edgeWeigh);
	        LinkedList<Valve> shortestPath = new LinkedList<>(sourceNode.shortestPath);
	        shortestPath.add(sourceNode);
	        evaluationNode.shortestPath = shortestPath;
	    }
	}
	
	public void fillStructure() {

		//adding startnode
//		Valve startValve = new Valve("000", 0);
//		startValve.connectionString.add("AA");
//		allValves.put("000", startValve);
		
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			String[] parts = line.split(" ");
			Valve valve = new Valve(parts[1], Integer.parseInt(parts[4].replaceAll("rate=", "").replaceAll(";", "")));
			allValves.put(parts[1],valve);
			for(int i=9;i<parts.length;i++) {
				valve.connectionString.add(parts[i].replaceAll(",", ""));
			}
		}
		
		//adding endnode
//		valves.get(valves.lastKey()).connectionString.add("ZZZ");
//		Valve endValve = new Valve("ZZZ", 0);
//		valves.put("ZZZ", endValve);
		
		for(String key : allValves.keySet()) {
			Valve v = allValves.get(key);
			for(String vString:v.connectionString) {
				Valve connection = allValves.get(vString);
				v.adjacentNodes.put(connection, Integer.valueOf(1));
			}
		}
		System.out.println(allValves);
	}
	
	public static void main(String[] args) {
		
		Day16 d16 = new Day16();
		
		d16.readFile();
		d16.fillStructure();
//		d16.calculateShortestPathFromSource();
		d16.part1();
		d16.part2();
	}

}

/*

(traversingMe.equals("AA->JJ->BB->CC") && traversingEli.equals("AA->DD->HH->EE"))
||
(traversingEli.equals("AA->JJ->BB->CC") && traversingMe.equals("AA->DD->HH->EE"))


 */
