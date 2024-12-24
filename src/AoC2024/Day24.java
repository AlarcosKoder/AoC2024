package AoC2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import AoC2022.Day;

public class Day24 extends Day {
	
	final private Map<String,Gate> gates_x;
	final private Map<String,Gate> gates_y;
	final private Map<String,Gate> gates_z;
	final private List<String> keys_x;
	final private List<String> keys_y;
	final private List<String> keys_z;
	final private Map<String,Gate> gates_initial;
	final private Map<String,Gate> gates_complex;
	final private Map<String,Gate> all_gates;
	
	public Day24() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		
		gates_x = new TreeMap<String,Gate>();
		gates_y = new TreeMap<String,Gate>();
		gates_z = new TreeMap<String,Gate>();
		
		keys_x = new LinkedList<String>();
		keys_y = new LinkedList<String>();
		keys_z = new LinkedList<String>();
		
		gates_initial = new HashMap<String,Gate>();
		gates_complex = new HashMap<String,Gate>();
		
		all_gates = new HashMap<String,Gate>();
	}

	public void processFile() {
		String[] lines = sb.toString().split("\n");
		Pattern i_pattern = Pattern.compile("^(\\w+)\\:\\s+(\\w+)$");
		Pattern g_pattern = Pattern.compile("^(\\w+)\\s+(AND|XOR|OR)\\s+(\\w+)\\s+->\\s+(\\w+)$");
		
		List<Gate> gates = new ArrayList<Gate>();
		
		for (String line : lines) {
			Matcher i_m = i_pattern.matcher(line);
			Matcher g_m = g_pattern.matcher(line);
			Gate gate = null;
			if(i_m.find()) {
				gate = new Gate(i_m.group(1), Long.parseLong(i_m.group(2)));
				gates.add(gate);
				gates_initial.put(gate.id,gate);
				all_gates.put(gate.id,gate);
				
			}
			if(g_m.find()) {
				gate = new Gate(g_m.group(1), g_m.group(2), g_m.group(3), g_m.group(4));
				gates.add(gate);
				gates_complex.put(gate.id,gate);
				all_gates.put(gate.id,gate);
			}
		}
		
		for (Gate gate : gates) {
			if(gate!=null && gate.id.startsWith("x")) {
				gates_x.put(gate.id, gate);
			} else if(gate!=null && gate.id.startsWith("y")) {
				gates_y.put(gate.id, gate);
			} else if(gate!=null && gate.id.startsWith("z")) {
				gates_z.put(gate.id, gate);
			}
		}
		
		keys_x.addAll(gates_x.keySet());
		keys_y.addAll(gates_y.keySet());
		keys_z.addAll(gates_z.keySet());
		
		long result_part1 = evaluate_gates(new HashMap<String,Gate>(gates_initial)  , new HashMap<String,Gate>(gates_complex) );
		logln("result part1: "+result_part1); // correct: 51410244478064 [sample: 2024]
		
		
		//solution for part2 starts here
		long gates_x_origi = evaluate_x_gates();
		long gates_y_origi = evaluate_y_gates();
		long gates_z_origi = evaluate_z_gates();
		
		
		List<Set<Map.Entry<String, Gate>>> allCombinations = generateCombinations(gates_complex,4);
		List<Set<Map.Entry<String, Gate>>> combi_list = allCombinations.stream().toList();
		
		Set<String> result = new TreeSet<String>();
		
        for (Set<Map.Entry<String, Gate>> combination : combi_list) {
        	long gates_x_combi=0;
    		long gates_y_combi=0;
    		long gates_z_combi=0;
        	
        	List <Entry <String, Gate>> list = combination.stream().toList();
        	logln("checking: "+list);
        	
        	Gate g1 = list.get(0).getValue();
        	Gate g2 = list.get(1).getValue();
        	Gate g3 = list.get(2).getValue();
        	Gate g4 = list.get(3).getValue();
        	
        	swap_gates(g1, g2);
    		swap_gates(g3, g4);
        	
        	gates_x_combi = evaluate_x_gates();
    		gates_y_combi = evaluate_y_gates();
    		gates_z_combi = evaluate_z_gates();
    		
    		swap_gates(g1, g2);
    		swap_gates(g3, g4);
        	
    		if(gates_z_origi==(INPUT_REAL?(gates_x_combi+gates_y_combi):(gates_x_combi&gates_y_combi))) {
    			result.add(g1.id); result.add(g2.id); result.add(g3.id); result.add(g4.id);    
    			break;
    		}
        	
    		swap_gates(g1, g3);
    		swap_gates(g2, g4);
        	
        	gates_x_combi = evaluate_x_gates();
    		gates_y_combi = evaluate_y_gates();
    		gates_z_combi = evaluate_z_gates();
    		
    		swap_gates(g1, g3);
    		swap_gates(g2, g4);
        	
    		if(gates_z_origi==(INPUT_REAL?(gates_x_combi+gates_y_combi):(gates_x_combi&gates_y_combi))) {
    			result.add(g1.id); result.add(g2.id); result.add(g3.id); result.add(g4.id);
    			break;
    		}
        	
    		swap_gates(g1, g4);
    		swap_gates(g2, g3);
        	
        	gates_x_combi = evaluate_x_gates();
    		gates_y_combi = evaluate_y_gates();
    		gates_z_combi = evaluate_z_gates();
    		
    		swap_gates(g1, g4);
    		swap_gates(g2, g3);
        	
    		if(gates_z_origi==(INPUT_REAL?(gates_x_combi+gates_y_combi):(gates_x_combi&gates_y_combi))) {
    			result.add(g1.id); result.add(g2.id); result.add(g3.id); result.add(g4.id);
    			break;
    		}
    		
        }
        
		String result_part2 = result.stream().collect(Collectors.joining(", ")).toString();
		
		logln("result part2: "+result_part2); // correct: 
	}
	
	public List<List<Map.Entry<String, Gate>>> generatePermutations(Map<String, Gate> gates_complex, int k) {
		logln("permutations generating");
        List<Map.Entry<String, Gate>> gateEntries = new ArrayList<>(gates_complex.entrySet());
        
        // Check if the map has enough elements
        if (gateEntries.size() < k) {
            throw new IllegalArgumentException("Not enough elements in the map to generate permutations.");
        }

        List<List<Map.Entry<String, Gate>>> permutations = new ArrayList<>();
        generatePermutationsRecursive(gateEntries, k, 0, new LinkedList<>(), permutations);
        logln("permutations generated"+permutations.size());
        return permutations;
    }

    private void generatePermutationsRecursive(List<Map.Entry<String, Gate>> gateEntries, int k, int start,
                                                      LinkedList<Map.Entry<String, Gate>> currentPermutation,
                                                      List<List<Map.Entry<String, Gate>>> permutations) {
        // If current permutation size reaches k, add it to the result list
        if (currentPermutation.size() == k) {
            permutations.add(new ArrayList<>(currentPermutation)); // Use ArrayList for ordered result
            return;
        }

        // Recursively build permutations
        for (int i = start; i < gateEntries.size(); i++) {
            currentPermutation.add(gateEntries.get(i));
            generatePermutationsRecursive(gateEntries, k, i + 1, currentPermutation, permutations);
            currentPermutation.removeLast();
        }

        // Generate all permutations of remaining elements (from current position)
        for (int i = 0; i < gateEntries.size(); i++) {
            if (!currentPermutation.contains(gateEntries.get(i))) {
                currentPermutation.add(gateEntries.get(i));
                generatePermutationsRecursive(gateEntries, k, i + 1, currentPermutation, permutations);
                currentPermutation.removeLast();
            }
        }
    }
	
	public List<Set<Map.Entry<String, Gate>>> generateCombinations(Map<String, Gate> gates_complex, int k) {
		logln("combination generating");
        List<Map.Entry<String, Gate>> gateEntries = new ArrayList<>(gates_complex.entrySet());
        
        if (gateEntries.size() < k) {
            throw new IllegalArgumentException("Not enough elements in the map to generate combinations.");
        }

        List<Set<Map.Entry<String, Gate>>> combinations = new ArrayList<>();
        generateCombinationsRecursive(gateEntries, k, 0, new LinkedList<>(), combinations);
        logln("combination generated"+combinations.size());
        return combinations;
    }

    private void generateCombinationsRecursive(List<Map.Entry<String, Gate>> gateEntries, int k, int start,LinkedList<Map.Entry<String, Gate>> currentCombination,List<Set<Map.Entry<String, Gate>>> combinations) {
        if (currentCombination.size() == k) {
            combinations.add(new HashSet<>(currentCombination)); // Use HashSet to avoid duplicates
            return;
        }

        for (int i = start; i < gateEntries.size(); i++) {
            currentCombination.add(gateEntries.get(i));
            generateCombinationsRecursive(gateEntries, k, i + 1, currentCombination, combinations);
            currentCombination.removeLast();
        }
    }
	
	public void swap_gates(Gate g1, Gate g2) {
		Gate g1_p1=all_gates.get(g1.pin_1);
		Gate g1_p2=all_gates.get(g1.pin_2);
		Gate g2_p1=all_gates.get(g2.pin_1);
		Gate g2_p2=all_gates.get(g2.pin_2);
		
		long c = g1_p1.value;
		g1_p1.value = g2_p1.value;
		g2_p1.value = c;
		
		c = g1_p2.value;
		g1_p2.value = g2_p2.value;
		g2_p2.value = c;
		
		String swap_id = g1.id;
		g1.id = g2.id;
		g2.id = swap_id;
	}
	
	public long evaluate_gates(Map<String,Gate> gates_defined, Map<String,Gate> gates_undefined) {
		// reset everything
		for (String key : gates_complex.keySet()) {
			gates_complex.get(key).value=null;
		}
		
		while(gates_undefined.size()>0) {
			List<Gate> found_gates = new ArrayList<Gate>();
			for (Entry<String, Gate> entry : gates_undefined.entrySet()) {
				Gate gate = entry.getValue();
				
				if(gates_defined.containsKey(gate.pin_1) && gates_defined.containsKey(gate.pin_2)) {
					Gate g1 = gates_defined.get(gate.pin_1);
					Gate g2 = gates_defined.get(gate.pin_2);
					gate.evalGate(g1, g2);
					found_gates.add(gate);
				}
			}
			for (Gate gate : found_gates) {
				gates_undefined.remove(gate.id);
				gates_defined.put(gate.id,gate);
			}
		}
		
		long result_part1 = 0;
		long gates_x = evaluate_x_gates();
		long gates_y = evaluate_y_gates();
		long gates_z = result_part1=evaluate_z_gates();
		
		return result_part1;
	}
	
		
	private long evaluate_x_gates() {
		return evaluate_gates(keys_x,gates_x);
	}
	private long evaluate_y_gates() {
		return evaluate_gates(keys_y,gates_y);
	}
	private long evaluate_z_gates() {
		return evaluate_gates(keys_z,gates_z);
	}
	
	private long evaluate_gates(List<String> keys,Map<String,Gate> gates) {
		long result = 0;
		for (int i = 0; i < keys.size(); i++) {
//		for (int i = keys.size()-1; i >= 0; i--) {
			Gate g = gates.get(keys.get(i));
			result = result | g.value<<i;
		}
		return result;
	}

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day24 day = new Day24();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
	
	public enum Operation {
	    AND,
	    OR,
	    XOR
	}
	
	public class Gate {
		Operation op;
		String id;
		Long value;
		
		String pin_1;
		String pin_2;
		
		
		public Gate(String _id,long _value) {
			this.id = _id;
			this.value = _value;
		}
		
		public Gate(String _pin_1,String _op, String _pin_2,String _id) {
			this.pin_1 = _pin_1;
			this.pin_2 = _pin_2;
			this.op = _op.equals("AND")?Operation.AND:(_op.equals("OR")?Operation.OR:Operation.XOR);
			this.id = _id;
		}
		
		public void evalGate(Gate g1, Gate g2) {
			switch (op) {
			case AND : {
				value = g1.value & g2.value;
				break;
			}
			case OR : {
				value = g1.value | g2.value;
				break;
			}
			case XOR : {
				value = g1.value ^ g2.value;
				break;
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + op);
			}
		}
		
		@Override
		public String toString() {
			return value!=null?("id: "+id+" v:"+value):(pin_1+" "+op+" "+pin_2+" -> "+id);
		}
	}
}
