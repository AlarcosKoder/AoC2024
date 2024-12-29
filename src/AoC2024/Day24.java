package AoC2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import AoC2022.Day;

public class Day24 extends Day {
	
	final private Map<String,Gate> wires;
	final private List<Gate> gates;
	final private Map<String,Integer> index;
	
	private long gate_x_result;
	private long gate_y_result;
	private long gate_z_result;
	
	private long xy_len;
	private long z_len;
	
	public Day24() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
		
		wires = new HashMap<String,Gate>();
		gates = new ArrayList<Gate>();
		index = new HashMap<String, Integer>();
		
		gate_x_result = 0;
		gate_y_result = 0;
		gate_z_result = 0;
		xy_len=0;
		z_len=0;
		
	}

	public void processFile() {
		String[] lines = sb.toString().split("\n");
		Pattern i_pattern = Pattern.compile("^(\\w+)\\:\\s+(\\w+)$");
		Pattern g_pattern = Pattern.compile("^(\\w+)\\s+(AND|XOR|OR)\\s+(\\w+)\\s+->\\s+(\\w+)$");
		
		for (String line : lines) {
			Matcher i_m = i_pattern.matcher(line);
			Matcher g_m = g_pattern.matcher(line);
			if(i_m.find()) {
				Gate wire = new Gate(i_m.group(1), Long.parseLong(i_m.group(2)));
				wires.put(wire.out,wire);
				
				if(wire.out.startsWith("x"))
					xy_len++;
			}
			if(g_m.find()) {
				Gate gate = new Gate(g_m.group(1), g_m.group(2), g_m.group(3), g_m.group(4));
				gates.add(gate);
				index.put(gate.out, index.size());
				
				if(gate.out.startsWith("z"))
					z_len++;
			}
		}
		
		evaluate_gates();
		evaluate_results();
		
		long result_part1 = gate_z_result;
		long supposed_result = (INPUT_REAL?(gate_x_result+gate_y_result):(gate_x_result&gate_y_result));
		logln("result part1: "+result_part1); // correct: 51410244478064 [sample: 2024]
		
		Set<List<String>> result_set = new HashSet<List<String>>();
		
		List<String> anomalies_keys = find_bad_z_gates();
		List<String> remaining_gates = new ArrayList<String>(gates.stream().map(s -> s.out).toList());
		remaining_gates.removeAll(anomalies_keys);
		
		Set<Set<String>> combis = Sets.combinations(new HashSet<String>(remaining_gates), 2);
        for (Set<String> combination : combis) {
        	List <String> list = combination.stream().toList();
        	
        	if(list.get(0).equals(list.get(1))) continue;
        	
			swap_gates(index.get(anomalies_keys.get(0)), index.get(anomalies_keys.get(1)));
        	swap_gates(index.get(anomalies_keys.get(2)), index.get(anomalies_keys.get(3)));
        	swap_gates(index.get(anomalies_keys.get(4)), index.get(anomalies_keys.get(5)));
        	swap_gates(index.get(list.get(0)), index.get(list.get(1)));
        	
        	evaluate_gates();
    		
    		swap_gates(index.get(list.get(1)), index.get(list.get(0)));
    		swap_gates(index.get(anomalies_keys.get(5)), index.get(anomalies_keys.get(4)));
    		swap_gates(index.get(anomalies_keys.get(3)), index.get(anomalies_keys.get(2)));
    		swap_gates(index.get(anomalies_keys.get(1)), index.get(anomalies_keys.get(0)));
    		
    		if(gate_z_result == supposed_result) {
    			List<String> found_set = new ArrayList<String>();
    			found_set.addAll(anomalies_keys);  
    			found_set.add(list.get(0));
    			found_set.add(list.get(1));
    			result_set.add(found_set);
    		}
    		
        }
        String result_part2 = "";
        for (List<String> set: result_set) {
        	if (test_set(set)) {
        		result_part2=set.stream().sorted().collect(Collectors.joining(",")).toString();
        	}
		}
        
		logln("result part2: "+result_part2); // correct:gst,khg,nhn,tvb,vdc,z12,z21,z33
		// gst,nhn,tvb,vdc,vjg,z12,z21,z33 NOK
		// gst,jhd,nhn,qss,vdc,z12,z21,z33 NOK
	}
	
	public void swap_list(List<String> list) {
		swap_gates(index.get(list.get(0)), index.get(list.get(1)));
		swap_gates(index.get(list.get(2)), index.get(list.get(3)));
		swap_gates(index.get(list.get(4)), index.get(list.get(5)));
		swap_gates(index.get(list.get(6)), index.get(list.get(7)));
		
		evaluate_gates();
		evaluate_results();
		
		swap_gates(index.get(list.get(0)), index.get(list.get(1)));
		swap_gates(index.get(list.get(2)), index.get(list.get(3)));
		swap_gates(index.get(list.get(4)), index.get(list.get(5)));
		swap_gates(index.get(list.get(6)), index.get(list.get(7)));
	}
	
	public boolean test_set(List<String> list) {
		boolean all_good = true;
		for (int i = 0; i < xy_len; i++) {
			for (int j = 0; j < xy_len; j++) {
				long number = 0L;
				if(i==j){
					number=1L;
				}
				String xKey = String.format("x%02d", j);
				String yKey = String.format("y%02d", j);
				wires.get(xKey).value=number;
				wires.get(yKey).value=number;
			}
			
			swap_list(list);
			
			if(gate_z_result != gate_x_result+gate_y_result) {
				all_good = false;
				break;
			}
			
			for (int j = 0; j < xy_len; j++) {
				long number = 0L;
				if(i==j){
					number=1L;
				}
				String xKey = String.format("x%02d", j);
				String yKey = String.format("y%02d", j>0?j-1:j);
				wires.get(xKey).value=number;
				wires.get(yKey).value=number;
			}
			
			swap_list(list);
			
			if(gate_z_result != gate_x_result+gate_y_result) {
				all_good = false;
				break;
			}
			
			for (int j = 0; j < xy_len; j++) {
				long number = 0L;
				if(i==j){
					number=1L;
				}
				String xKey = String.format("x%02d", j>0?j-1:j);
				String yKey = String.format("y%02d", j);
				wires.get(xKey).value=number;
				wires.get(yKey).value=number;
			}
			
			swap_list(list);
			
			if(gate_z_result != gate_x_result+gate_y_result) {
				all_good = false;
				break;
			}
		}
		return all_good;
	}
	
	public List<String> find_bad_z_gates() {
		/*
		 * z[n] = xor(
				    xor(x[n], y[n]),
				    or(
				        and(x[n-1], y[n-1]),
				        and(
				            xor(x[n-1], y[n-1]),
				            ...
				        )
				    )
				)
		 * 
		 */
        List<String> swapped_zs = new ArrayList<>();

        for (int z = 0; z < z_len - 1; z++) {
            String zKey = String.format("z%02d", z);
            Integer gate_id = index.get(zKey);

            if (gate_id == null) {
                continue;
            }

            Gate gate = gates.get(gate_id);
            if (gate == null) {
                continue;
            }

            if (gate.op == Operation.XOR) {
                continue;
            }

            int expected_dependents = (int) (z * 6);
            SwapResult swapResult = find_replacement(expected_dependents);

            if (swapResult == null || swapResult.swapGate == null) {
                System.err.println("No suitable swap gate found for expected dependents: " + expected_dependents);
                continue;
            }

            Gate swapGate = swapResult.swapGate;

            swapped_zs.add(gate.out);
            swapped_zs.add(swapGate.out);

            swap_gates(gate_id, swapResult.gateId);
        }
        //swapback
        for (int i = 0; i < swapped_zs.size(); i+=2) {
        	swap_gates(index.get(swapped_zs.get(i)), index.get(swapped_zs.get(i+1)));
		}
        return swapped_zs;
    }

    public SwapResult find_replacement(int nbr_dependents) {
        for (int g = 0; g < gates.size(); g++) {
            Gate gate = gates.get(g);
            if (gate.op != Operation.XOR) {
                continue;
            }
            Set<String> dependents = dependents_of_wire(gate.out);
            if (dependents.size() == nbr_dependents) {
                return new SwapResult(g, gate);
            }
        }
        return null;
    }
    
    public class SwapResult {
        int gateId;
        Gate swapGate;

        public SwapResult(int gateId, Gate swapGate) {
            this.gateId = gateId;
            this.swapGate = swapGate;
        }
    }

    public Set<String> dependents_of_wire(String output) {
        Set<String> dependents = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(output);

        while (!queue.isEmpty()) {
            String current = queue.pollLast();
            if (current.startsWith("x") || current.startsWith("y")) {
                continue;
            }

            Integer gateId = index.get(current);
            if (gateId == null) {
                continue;
            }

            Gate gate = gates.get(gateId);
            if (gate == null) {
                continue;
            }

            List<String> inputs = Arrays.asList(gate.pin_1, gate.pin_2);
            for (String wire : inputs) {
                if (!dependents.contains(wire)) {
                    queue.add(wire);
                    dependents.add(wire);
                }
            }
        }

        return dependents;
    }
	
	public void swap_gates(int g, int s) {
		String _g = gates.get(g).out;
		String _s = gates.get(s).out;
		gates.get(g).out=_s;
		gates.get(s).out=_g;
		index.put(_s, g);
		index.put(_g, s);
	}
	
	public void reset_gates_value() {
		for (Gate gate : gates) {
			gate.value=-1L;
		}
	}

	public void evaluate_gates() {
		
		reset_gates_value();
		
		boolean found_null = true;
		while(found_null) {
			found_null = false;
			for (Gate gate : gates) {
				Gate g1 = wires.get(gate.pin_1);
				if(g1==null) g1 = gates.get(index.get(gate.pin_1));
				Gate g2 = wires.get(gate.pin_2);
				if(g2==null) g2 = gates.get(index.get(gate.pin_2));
				
				if(gate.value==-1 && g1.value != -1L && g2.value != -1L) {
					if(gate.op==Operation.AND) {
						gate.value = g1.value & g2.value;
					} else if(gate.op==Operation.OR) {
						gate.value = g1.value | g2.value;
					} else if(gate.op==Operation.XOR) {
						gate.value = g1.value ^ g2.value;
					}
					found_null=true;
				}
			}
		}
		
		gate_z_result = 0;
		for (int i=0; i < z_len; i++) {
			Gate gate = gates.get(index.get(String.format("z%02d", i)));
			gate_z_result |= (gate.value & 1L) << i;

		}
	}
	
	public void evaluate_results() {
		gate_x_result = 0;
		gate_y_result = 0;
		gate_z_result = 0;
		int i=0;
		for (; i < xy_len; i++) {
			Gate gate = wires.get(String.format("x%02d", i));
			gate_x_result |= (gate.value & 1L)<<i;
			gate = wires.get(String.format("y%02d", i));
			gate_y_result |= (gate.value & 1L)<<i;
			gate = gates.get(index.get(String.format("z%02d", i)));
			gate_z_result |= (gate.value & 1L)<<i;
			
		}
		for (; i < z_len; i++) {
			Gate gate = gates.get(index.get(String.format("z%02d", i)));
			gate_z_result |= (gate.value & 1L)<<i;
		}
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
	
	public class Gate implements Comparable<Gate> {
		String out;
		long value;
		Operation op;
		String pin_1;
		String pin_2;
		
		public Gate(String _out,long _value) {
			this.out=_out;
			this.value=_value;
		}
		
		public Gate(String _pin_1,String _op, String _pin_2,String _out) {
			this.out=_out;
			this.pin_1=_pin_1;
			this.pin_2=_pin_2;
			this.op = _op.equals("AND")?Operation.AND:(_op.equals("OR")?Operation.OR:Operation.XOR);
			this.value=-1;
		}
		
			
		@Override
		public String toString() {
			return "\t"+out+"="+value+" p1:"+pin_1+" p2:"+pin_2;
		}
		
		@Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Gate that = (Gate) o;
	        return out.equals(that.out);
	    }

		@Override
		public int compareTo(Gate o) {
			return out.compareTo(((Gate)o).out);
		}
	}
}
