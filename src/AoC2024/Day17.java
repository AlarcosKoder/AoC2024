package AoC2024;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;

import AoC2022.Day;

public class Day17 extends Day {
	BigInteger success = BigInteger.valueOf(Long.MAX_VALUE).pow(5);
	BigInteger MAX_VALUE = BigInteger.valueOf(Long.MAX_VALUE).pow(5);
	final BigInteger  THREE = BigInteger.valueOf(3L);
	public Day17() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	
	public BigInteger getComboOperand(int operand, BigInteger reg_a, BigInteger reg_b, BigInteger reg_c) {
		if(operand==0) return BigInteger.ZERO;
		else if(operand==1) return BigInteger.ONE;
		else if(operand==2) return BigInteger.TWO;
		else if(operand==3) return THREE;
		else if(operand==4) return reg_a;
		else if(operand==5) return reg_b;
		else if(operand==6) return reg_c;
		else throw new IllegalArgumentException("reserved value: " + 7);
	}
	
	public List<Integer> runProgram(BigInteger reg_a, BigInteger reg_b, BigInteger reg_c, List<Integer> program) {
		List<Integer> output = new ArrayList<Integer>();
		int opcode=0;
		int operand=0;
		int instruction_pointer=0;
		
		while(instruction_pointer < program.size()-1){
			opcode = program.get(instruction_pointer);
			operand = program.get(instruction_pointer+1);
			int pointer_jumper = 2;
			if(opcode==0){
				BigInteger comboOperand = getComboOperand(operand,reg_a,reg_b,reg_c);
				int shift = comboOperand.intValueExact();
				BigInteger divisor = BigInteger.ONE.shiftLeft(shift);
				reg_a = reg_a.divide(divisor);
				
			} else if(opcode==1){
				reg_b = reg_b.xor(BigInteger.valueOf(operand));
			} else if (opcode==2){
				reg_b = getComboOperand(operand,reg_a,reg_b,reg_c).mod(BigInteger.valueOf(8L));
			} else if (opcode==3){
				if(reg_a.compareTo(BigInteger.valueOf(0))!=0) {
					instruction_pointer = operand;
					pointer_jumper = 0;
				}
			} else if (opcode ==4){
				reg_b = reg_b.xor(reg_c);
			} else if(opcode==5){
				output.add(getComboOperand(operand,reg_a,reg_b,reg_c).mod(BigInteger.valueOf(8L)).intValue());
			} else if(opcode==6){
//				double d = reg_a / Math.pow(2, getComboOperand(operand,reg_a,reg_b,reg_c));
//				reg_b = (int)Math.floor(d);
				BigInteger comboOperand = getComboOperand(operand,reg_a,reg_b,reg_c);
				int shift = comboOperand.intValueExact();
				BigInteger divisor = BigInteger.ONE.shiftLeft(shift);
				reg_b = reg_a.divide(divisor);
			} else if (opcode==7){
//				double d = reg_a / Math.pow(2, getComboOperand(operand,reg_a,reg_b,reg_c));
//				reg_c = (int)Math.floor(d);
				BigInteger comboOperand = getComboOperand(operand,reg_a,reg_b,reg_c);
				int shift = comboOperand.intValueExact();
				BigInteger divisor = BigInteger.ONE.shiftLeft(shift);
				reg_c = reg_a.divide(divisor);
			}
			
			instruction_pointer+=pointer_jumper;
		}
		return output;//.stream().map(String::valueOf).collect(Collectors.joining(",")).toString();
	}

	public void processFile() {
		String[] lines = sb.toString().split("\n");
		BigInteger reg_a = BigInteger.valueOf(Long.parseLong(lines[0].split(":")[1].trim()));
		BigInteger reg_b = BigInteger.valueOf(Long.parseLong(lines[1].split(":")[1].trim()));
		BigInteger reg_c = BigInteger.valueOf(Long.parseLong(lines[2].split(":")[1].trim()));
		List<Integer> program = Arrays.stream(lines[4].split(":")[1].trim().split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
		
		String result_part1 = runProgram(reg_a, reg_b, reg_c, program).stream().map(String::valueOf).collect(Collectors.joining(",")).toString();
		String input = program.stream().map(String::valueOf).collect(Collectors.joining(",")).toString();
		logln("A:"+reg_a+" B:"+reg_b+" c:"+reg_c);
		logln("input  part1:  "+input);
		logln("result part1: "+result_part1); // correct: 6,5,4,7,1,6,0,3,1
		
//		for (long i = 0; i < 10000; i++) { //2147483647
//			String output=runProgram(BigInteger.valueOf(i), reg_b, reg_c, program).stream().map(String::valueOf).collect(Collectors.joining(",")).toString();;
//			if(output.equals(input)) {
//				break;
//			}
////			if(i!=0 && i%10010000==0) {
//				logln("Checking A: "+i+" input: "+input+" output: "+output+" ol: "+output.split(",").length/2);
////			}
//		}
		// checked 0-2147483647
		/*
	    List<Long> matches = Arrays.asList(0L);
	    for (int i = program.size() - 1; i >= 0; --i) {
	        long p8 = (long) Math.pow(8, i);
	        List<Long> newMatches = new ArrayList<>();
	        for (Long m : matches) {
	            for (int n = 0; n < 8; ++n) {
	                long regA = m + p8 * n;
	                List<Long> result = runProgram(regA,reg_b, reg_c, program);
	                if (result.equals(program.subList(program.size()-1, program.size()))) {
	                    // collect all the options that match partially the output
	                    // we see that the same level value can be the result 
	                    // of different regA values
	                    newMatches.add(regA);
	                }
	            }
	        }
	        matches = newMatches;
	    }*/

	    // get the lowest value
	    String result_part2 = part2(program, reg_a, reg_b, reg_c);
		
		
//		dfs(program,0,0,reg_a,reg_b,reg_c);
//		result_part2=success;
		System.out.println("result part2: "+result_part2); // correct:
		//9223372036854775807 too high
		//106086382266778
		
	}
	/*
	2,4,1,5,7,5,1,6,0,3,4,2,5,5,3,0
	2,4 B = A % 8 
	1,5 B = B ^ 5
	7,5 C = A / (1 << B)
	1,6 B = B ^ 6 
	0,3 A = A / (1 << 3)
	4,2 B = B ^ C 
	5,5 print ( B % 8 ) 
	3,0 if (A != 0) restart
	*/
	public String part2(List<Integer> program, BigInteger reg_a, BigInteger reg_b, BigInteger reg_c) {
        Queue<State> queue = new PriorityQueue<>(State.REGISTER_COMPARATOR);
        queue.add(new State(0, 0));
        BigInteger min = MAX_VALUE;
        while (!queue.isEmpty() && min.equals(MAX_VALUE)) {
            State state = queue.poll();
            List<Integer> sufix = program.subList(program.size()-state.outputSize-1, program.size());
            String _sufix = sufix.stream().map(String::valueOf).collect(Collectors.joining(",")).toString();
            BigInteger nextRegisterABase = BigInteger.valueOf(state.registerA).shiftLeft(3);
            
            //BigInteger nextNum = cur.shiftLeft(3).add(BigInteger.valueOf(i));
            
            for(long i=0L; i<8L; ++i) {
            	BigInteger nextRegisterA = nextRegisterABase.or(BigInteger.valueOf(i));
                
                List<Integer> result = runProgram(nextRegisterA, reg_b, reg_c, program);
                String _result = result.stream().map(String::valueOf).collect(Collectors.joining(",")).toString();
                if(_result.equals(_sufix)) {
                    if(program.size() == sufix.size()) {
                    	
                        min = min.min(nextRegisterA);
                    }
                    queue.add(new State(nextRegisterA.longValue(), state.outputSize+1));
                }
            }
        }
        return min.toString();
    }
	public record State(long registerA, int outputSize) {
	    // Static comparator using BigInteger for registerA comparison
	    static final Comparator<State> REGISTER_COMPARATOR =
	            Comparator.comparing(state -> BigInteger.valueOf(state.registerA()));
	}
	
//	private record State(long registerA, int outputSize) {
//        static final Comparator<State> REGISTER_COMPARATOR = Comparator.comparingLong(State::registerA);
//    }
	
	private void dfs(List<Integer> program,BigInteger cur, long pos, BigInteger reg_a, BigInteger reg_b, BigInteger reg_c) {
		for (int i = 0; i < 8; i++) {
//			BigInteger curBig = BigInteger.valueOf(cur); // Replace with your value
	        // Perform the equivalent of: nextNum = (cur << 3) + i
	        BigInteger nextNum = cur.shiftLeft(3).add(BigInteger.valueOf(i));
			
			List<Integer> execResult = runProgram(nextNum, reg_b,reg_c,program);
			if(!execResult.equals(program.subList(program.size()-1, program.size()))) {
				continue;
			}
			if(pos==program.size()-1) {
				success = nextNum.min(success);
				return;
			}
			dfs(program,nextNum,pos+1,reg_a,reg_b,reg_c);
		}
	}
	/*
	public String partTwo() throws Exception {
	    
	    // Analyzing the sequence manually and based on the machine 
	    // characteristics (3 bit logic) the resulting pattern changes
	    // following the power of 8, so I try to guess the result
	    // by approaching it using powers of 8.
	    
	    List<Integer> program = machine.program;
	    List<Long> matches = Arrays.asList(0L);
	    for (int i = program.size() - 1; i >= 0; --i) {
	        long p8 = (long) Math.pow(8, i);
	        List<Long> newMatches = new ArrayList<>();
	        for (Long m : matches) {
	            for (int n = 0; n < 8; ++n) {
	                long regA = m + p8 * n;
	                List<Integer> result = runNewMachine(regA);
	                if (matchListFromIndex(result, program, i)) {
	                    // collect all the options that match partially the output
	                    // we see that the same level value can be the result 
	                    // of different regA values
	                    newMatches.add(regA);
	                }
	            }
	        }
	        matches = newMatches;
	    }

	    // get the lowest value
	    return matches.stream().min(Long::compare).get().toString();
	}
*/
	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day17 day = new Day17();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
