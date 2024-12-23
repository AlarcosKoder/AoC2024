package AoC2022;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Day05 extends Day {
	
	class Crate {
		long number;
		List<String> stacks;
		Crate(){
			stacks = new ArrayList<>();
		}
	}
	
	private List<Crate> crates;
	private String[] moveOrders;
	
	public Day05() {
		super();
		crates = new ArrayList<>();
	}
	
	public void readFile() {
		String strCurrentLine;
		
		try {
			while ((strCurrentLine = fileReader.readLine()) != null) {
				sb.append(strCurrentLine).append("\n");
			}
			log(sb.toString(),0);
			int indexOfFirst = sb.indexOf("1");
			int indexOfLast = sb.indexOf("\n", indexOfFirst);
			
			String[] cS = sb.substring(indexOfFirst, indexOfLast).split("   ");
			
			for (int i = 0; i < cS.length; i++) {
				Crate c = new Crate();
				c.number=i+1;
				crates.add(c);
			}
			
			String[] contents = sb.substring(0, indexOfFirst).split("\n");
			for (int i = 0; i < contents.length; i++) {
				String content = contents[i];
				for (int j = 0; j < (content.length()+1)/4; j++) {
					String letter=content.substring(1+j*4, 2+j*4);
					if(!letter.isBlank())
						crates.get(j).stacks.add(letter);
				}
			}
			
			for (Crate crate : crates) {
				Collections.reverse(crate.stacks);
			}
			
			logCrates();
			
			moveOrders = sb.substring(sb.indexOf("move"),sb.length()).split("\n");
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void processMoveOrders() {
		for (String moveOrder : moveOrders) {
			//move 1 from 2 to 1
			int howMany = Integer.parseInt(moveOrder.substring(5,moveOrder.indexOf("from")).trim());
			int fromWhere = Integer.parseInt(moveOrder.substring(moveOrder.indexOf("from")+4,moveOrder.indexOf("to")).trim());
			int toWhere = Integer.parseInt(moveOrder.substring(moveOrder.indexOf("to")+2).trim());
			log("moving "+howMany+" from "+fromWhere+" to "+toWhere+"\n");
			
			// Part1
//			for (int i = 0; i < howMany; i++) {
//				String moving = crates.get(fromWhere-1).stacks.get(crates.get(fromWhere-1).stacks.size()-1);
//				crates.get(toWhere-1).stacks.add(moving);
//				crates.get(fromWhere-1).stacks.remove(crates.get(fromWhere-1).stacks.size()-1);
//			}
			
			// Part2
			int length=crates.get(fromWhere-1).stacks.size();
			List<String> moving = crates.get(fromWhere-1).stacks.subList(length-howMany, length);
			crates.get(toWhere-1).stacks.addAll(moving);
			
			int size = moving.size();
			System.out.println("deleting: "+size);
			System.out.println("current size: "+crates.get(fromWhere-1).stacks.size());
			for (int i = 0; i < size; i++) {
				crates.get(fromWhere-1).stacks.remove(crates.get(fromWhere-1).stacks.size()-1);
			}
			System.out.println("afterdelete: "+crates.get(fromWhere-1).stacks.size());
			
//			System.out.println("deleting: "+size);
//			System.out.println("current size: "+crates.get(fromWhere-1).stacks.size());
//			crates.get(fromWhere-1).stacks.removeAll(moving);
//			System.out.println("afterdelete: "+crates.get(fromWhere-1).stacks.size());
			logCrates();
		}
		
		logCrates();
		
		log("SOLUTION: ",1);
		for (Crate crate : crates) {
			log(crate.stacks.get(crate.stacks.size()-1));
		}
	}
	
	private void logCrates() {
		for (Crate crate : crates) {
			log("crate "+crate.number+":");
			for (String stack : crate.stacks) {
				log("["+stack+"],");
			}
			log("\n");
		}
	}
	
	public static void main(String[] args) {
		Day05 d05= new Day05();
		d05.readFile();
		d05.processMoveOrders();
	}
}
