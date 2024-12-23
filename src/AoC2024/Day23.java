package AoC2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import AoC2022.Day;

public class Day23 extends Day {
	private HashMap<String, List<String>> connections;
	public Day23() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		connections = new HashMap<String, List<String>>();
	}
	
	public boolean is_connected(String n1, String n2) {
		return connections.get(n1).contains(n2);
	}

	public void processFile() {
		List<String> nodes_all = new ArrayList<String>();
		
		String[] lines = sb.toString().split("\n");
		for (String line : lines) {
			String[] nodes_parts = line.split("-");
			if(!connections.containsKey(nodes_parts[0])) {
				connections.put(nodes_parts[0], new ArrayList<String>(List.of(nodes_parts[1])));
			} else {
				connections.get(nodes_parts[0]).add(nodes_parts[1]);
			}
				
			if(!connections.containsKey(nodes_parts[1])) {
				connections.put(nodes_parts[1], new ArrayList<String>(List.of(nodes_parts[0])));
			} else {
				connections.get(nodes_parts[1]).add(nodes_parts[0]);
			}
			nodes_all.add(nodes_parts[0]);
			nodes_all.add(nodes_parts[1]);
		}
		
		/*
		 * Start by looking for sets of three computers where each computer in the set 
		 * is connected to the other two computers.
		 */
		Set<Set<String>> biggest_group = new HashSet<Set<String>>();
		List<String> nodes_tstart = nodes_all.stream().filter(s -> s.startsWith("t")).toList();
		for (String node_t : nodes_tstart) {
			List<String> node_t_conns = new ArrayList<String>(connections.get(node_t));
			for (String node_t_conn : node_t_conns) {
				List<String> node_t_conns_conns = new ArrayList<String>(connections.get(node_t_conn));
				for (String node_t_conn_conn : node_t_conns_conns) {
					
					if(!node_t_conn_conn.equals(node_t) && is_connected(node_t,node_t_conn_conn)) {
						Set<String> threesum = new HashSet<String>();
						threesum.addAll(List.of(node_t,node_t_conn,node_t_conn_conn));
						biggest_group.add(threesum);
					}
				}
				
			}
		}
		long result_part1 = biggest_group.size();
		logln("result part1: "+result_part1); // correct: 1323 [sample: 7]
		
		boolean still_growing = true;
		while (still_growing) {
			Set<Set<String>> new_biggest_group = new HashSet<Set<String>>();
			boolean was_growth = false;
			
			for (Set<String> nodes_big : biggest_group) {
				List<String> nodes_to_be_checked = new ArrayList<String>(nodes_all);
				
				//to remove already connected nodes
				nodes_to_be_checked.removeAll(nodes_big);
				
				for (String node : nodes_to_be_checked) {
					boolean connected = true;
					//check all connections to the group members
					for (String node_big : nodes_big) {
						if(!is_connected(node_big, node)) {
							connected = false;
							break;
						}
					}
					//if all connected, create new bigger group, store in the new_bigger group, sign that we found at least one bigger group
					if(connected) {
						Set<String> new_group = new HashSet<String>();
						new_group.addAll(nodes_big);
						new_group.add(node);
						new_biggest_group.add(new_group);
						was_growth = true;
					}
				}
			}
			
			still_growing = was_growth;
			//let's keep latest groups
			if(still_growing) {
				biggest_group = new_biggest_group;
			}
		}
		
		Set<String> last_set=null;
		for (Set<String> set : biggest_group) {
			last_set = set;
		}

		if(last_set == null){
			logln("baj van more !!!444negy, ez nem lehetséges");
			System.exit(-1);
		}
		String result_part2 = last_set.stream().sorted().collect(Collectors.joining(",")).toString();
		logln("result part2: "+result_part2+" size:"+biggest_group.size()); // correct: er,fh,fi,ir,kk,lo,lp,qi,ti,vb,xf,ys,yu [sample: co,de,ka,ta]
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day23 day = new Day23();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
