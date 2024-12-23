package AoC2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import AoC2022.Day;

public class Day09 extends Day {

	public Day09() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	
	private int getLastIndexOfNonDot(List<Integer> blocks, int lastFound) {
		for (int i = lastFound; i > 0 ; i--) {
			if(blocks.get(i).intValue()!=-1) return i;
		}
		return -1;
	}
	
	private int getLastIndexOfID(List<Integer> blocks, Integer id) {
		for (int i = blocks.size()-1; i > 0 ; i--) {
			if(blocks.get(i).intValue()==id.intValue()) return i;
		}
		return -1;
	}
	
	private int getFirstIndexOfDot(List<Integer> blocks, int firstFound) {
		for (int i = firstFound; i < blocks.size() ; i++) {
			if(blocks.get(i).intValue()==-1) return i;
		}
		return -1;
	}
	
	private int getFirstIndexOfNDot(List<Integer> blocks,int len, int end) {
		for (int i = 0; i < end-len ; i++) {
			if(blocks.get(i).intValue()==-1 && i+len < blocks.size()) {
				boolean enoughDots = true;
				for (int j = 1; j < len; j++) {
					if(blocks.get(i+j).intValue()!=-1) {
						enoughDots = false;
						break;
					}
				}
				if(enoughDots) return i;
			}
		}
		return -1;
	}

	public void processFile() {
	    // Parse the input from the sb variable
	    String diskMap = sb.toString().trim();

	    // Validate the input length
	    if (diskMap.length() % 2 != 0) {
	        //System.err.println("Invalid input: Length of disk map is not even. Adding 0 to the end.");
	        diskMap=diskMap+"0";
	    }
	    
	    List<Integer> ids = new ArrayList<Integer>();
	    HashMap<Integer,Integer> ids_map = new HashMap<Integer,Integer>();

	    // Step 1: Decode the disk map into individual blocks
	    List<Integer> blocks_list1 = new ArrayList<Integer>();
	    List<Integer> blocks_list2 = new ArrayList<Integer>();
	    for (int i = 0; i < diskMap.length(); i += 2) {
	        int fileLength = Character.getNumericValue(diskMap.charAt(i));
	        int freeLength = Character.getNumericValue(diskMap.charAt(i + 1));
	        
	        // Append file blocks
	        for (int j = 0; j < fileLength; j++) {
	        	Integer id = (i / 2);
	        	blocks_list1.add(id);
	        	if(!ids.contains(id)) ids.add(id);
	        	if(!ids_map.containsKey(id)) ids_map.put(id,fileLength);
	        	
	        }
	        // Append free space
	        for (int j = 0; j < freeLength; j++) {
	        	blocks_list1.add(-1);
	        }
	    }
	    blocks_list2.addAll(blocks_list1);
	    
	    // Step 2: Simulate the compaction process
	    int writeIndex = getFirstIndexOfDot(blocks_list1,0);
	    int readIndex = getLastIndexOfNonDot(blocks_list1,blocks_list1.size()-1);
	    
	    while(writeIndex<readIndex) {
	    	blocks_list1.remove(writeIndex);
	    	blocks_list1.add(writeIndex, blocks_list1.get(readIndex-1));
	    	blocks_list1.remove(readIndex);
	    	blocks_list1.add(readIndex, -1);
	    	
	    	writeIndex = getFirstIndexOfDot(blocks_list1,writeIndex);
	    	readIndex = getLastIndexOfNonDot(blocks_list1,readIndex);
	    }

	    // Step 3: Calculate the checksum
	    long result_part1 = 0;
	    for (int i = 0; i < blocks_list1.size(); i++) {
	        if (blocks_list1.get(i).intValue()!=-1) {
	        	result_part1 += i * blocks_list1.get(i).intValue();
	        }
	    }

	    // Output the result for part 1
	    System.out.println("result part1: " + result_part1); // Correct: 6258319840548 1928
	    
	    Collections.reverse(ids);
	    for (Integer currentID : ids) {
	    	int n = ids_map.get(currentID).intValue();
	    	
	    	int end = getLastIndexOfID(blocks_list2,currentID);
		    
		    writeIndex = getFirstIndexOfNDot(blocks_list2,n,end);
		    
		    if(writeIndex >= 0) {
		    	
		    	blocks_list2.removeAll(Collections.singletonList(currentID));
		    	for (int i = writeIndex; i < writeIndex+n; i++) {
		    		Integer o = blocks_list2.remove(writeIndex);
		    		blocks_list2.add(end-n,o);
				}
//		    	for (int i = 0; i < n; i++) {
//		    		blocks_list2.add(writeIndex,currentID);
//				}
		    	
		    	blocks_list2.addAll(writeIndex,Stream.generate(() -> currentID).limit(n).collect(Collectors.toCollection(ArrayList<Integer>::new)));
		    } 
	    }
	    
	    // Step 3: Calculate the checksum
	    long result_part2 = 0;
	    for (int i = 0; i < blocks_list2.size(); i++) {
	        if (blocks_list2.get(i).intValue()!=-1) {
	        	result_part2 += i * blocks_list2.get(i).intValue();
	        }
	    }
	    
	    // Placeholder for part 2
	    System.out.println("result part2: " + result_part2); // Correct: 6286182965311 2858
	}



	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day09 day = new Day09();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}

}
