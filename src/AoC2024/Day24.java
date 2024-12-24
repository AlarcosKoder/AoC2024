package AoC2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		INPUT_REAL = false;
		
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
				//gates_defined.put(gate.id,gate);
//				logln("Found initial: "+gate);
				
			}
			if(g_m.find()) {
				gate = new Gate(g_m.group(1), g_m.group(2), g_m.group(3), g_m.group(4));
				gates.add(gate);
				gates_complex.put(gate.id,gate);
				all_gates.put(gate.id,gate);
				//gates_undefined.put(gate.id,gate);
//				logln("Found gate: "+gate);
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
		
//		Map<String,Gate> gates_defined = new HashMap<String,Gate>(gates_initial);
//		Map<String,Gate> gates_undefined = new HashMap<String,Gate>(gates_complex);
		
//		while(gates_undefined.size()>0) {
//			List<Gate> found_gates = new ArrayList<Gate>();
//			for (Entry<String, Gate> entry : gates_undefined.entrySet()) {
//				Gate gate = entry.getValue();
//				
//				if(gates_defined.containsKey(gate.pin_1) && gates_defined.containsKey(gate.pin_2)) {
//					Gate g1 = gates_defined.get(gate.pin_1);
//					Gate g2 = gates_defined.get(gate.pin_2);
//					gate.evalGate(g1, g2);
//					found_gates.add(gate);
//				}
//			}
//			for (Gate gate : found_gates) {
//				gates_undefined.remove(gate.id);
//				gates_defined.put(gate.id,gate);
//			}
//		}
//		keys_x.addAll(gates_x.keySet());
//		keys_y.addAll(gates_y.keySet());
//		keys_z.addAll(gates_z.keySet());
//		
//		long result_part1 = 0;
//		long gates_x = evaluate_x_gates();
//		long gates_y = evaluate_y_gates();
//		long gates_z = result_part1=evaluate_z_gates();
//		
//		logln("x-gates: "+gates_x+" gates:"+this.gates_x);
//		logln("y-gates: "+gates_y+" gates:"+this.gates_y);
//		logln("z-gates: "+gates_z+" gates:"+this.gates_z);
//		logln("Evaluation state: "+(gates_x+gates_y==gates_z) + " x+y="+(gates_x+gates_y)+" diff="+(gates_x+gates_y-gates_z));		
		
		long result_part1 = evaluate_gates(gates_initial,gates_complex);
		logln("result part1: "+result_part1); // correct: 51410244478064 [sample: 2024]
		
		swap_gates(all_gates.get("z05"), all_gates.get("z00"));
		swap_gates(all_gates.get("z02"), all_gates.get("z01"));
		
		for (String key : gates_complex.keySet()) {
			gates_complex.get(key).value=null;
		}
		
		result_part1 = evaluate_gates(gates_initial,gates_complex);
		
		swap_gates(all_gates.get("z05"), all_gates.get("z00"));
		swap_gates(all_gates.get("z02"), all_gates.get("z01"));
		
		for (String key : gates_complex.keySet()) {
			gates_complex.get(key).value=null;
		}
		
		result_part1 = evaluate_gates(gates_initial,gates_complex);
		
		long result_part2 = 0;
		
		// sample result
		// x00 AND y00 -> z05 and x05 AND y05 -> z00;
		// x01 AND y01 -> z02 and x02 AND y02 -> z01
		
		logln("result part2: "+result_part2); // correct: 
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
		
		logln("x-gates: "+gates_x+" gates:"+this.gates_x);
		logln("y-gates: "+gates_y+" gates:"+this.gates_y);
		logln("z-gates: "+gates_z+" gates:"+this.gates_z);
		
		if(INPUT_REAL)
			logln("Evaluation state: "+((gates_x+gates_y)==gates_z) + " x+y="+(gates_x+gates_y)+" diff="+(gates_x+gates_y-gates_z));
		else
			logln("Evaluation state: "+((gates_x&gates_y)==gates_z) + " x+y="+(gates_x&gates_y)+" diff="+(gates_x&gates_y-gates_z));
		
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
