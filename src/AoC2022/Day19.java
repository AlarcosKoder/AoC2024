package AoC2022;

import java.util.ArrayList;
import java.util.List;

public class Day19 extends Day {

	public List<BluePrint> blueprints;
	public static long COUNTER = 0;
	
	
	
//	int ore;
//	int clay;
//	int obsidian;
//	int geodes;
//	int oreRobot;
//	int clayRobot;
//	int obsidianRobot;
//	int geodeRobot;
	
	public Day19() {
		blueprints = new ArrayList<>();
	}
	
	class BluePrint {
		int number;
		int oreRobot_ore;
		int clayRobot_ore;
		int obsidianRobot_ore;
		int obsidianRobot_clay;
		int geodeRobot_ore;
		int geodeRobot_obsidian;
		int maxGeodes;
		int max_ore_robot;
		
		String log;
		
		public BluePrint(String line){
			String[] parts = line.split(" ");
			number = Integer.parseInt(parts[1].replaceAll(":", ""));
			oreRobot_ore = Integer.parseInt(parts[6]);
			clayRobot_ore = Integer.parseInt(parts[12]);
			obsidianRobot_ore = Integer.parseInt(parts[18]);
			obsidianRobot_clay = Integer.parseInt(parts[21]);
			geodeRobot_ore = Integer.parseInt(parts[27]);
			geodeRobot_obsidian = Integer.parseInt(parts[30]);
			maxGeodes = 0;
			max_ore_robot = Math.max(Math.max(Math.max(oreRobot_ore, clayRobot_ore),obsidianRobot_ore),geodeRobot_ore);
		}
		@Override
		public String toString() {
			StringBuffer sb = new StringBuffer();
			sb.append("Blueprint ").append(number).append(":\n");
			sb.append("\tEach ore robot costs ").append(oreRobot_ore).append(" ore.\n");
			sb.append("\tEach clay robot costs ").append(clayRobot_ore).append(" ore.\n");
			sb.append("\tEach obsidian robot costs ").append(obsidianRobot_ore).append(" ore and ").append(obsidianRobot_clay).append(" clay.\n");
			sb.append("\tEach geode robot costs ").append(geodeRobot_ore).append(" ore and ").append(geodeRobot_obsidian).append(" obsidian.\n");
			return sb.toString();
		}	
	}
	
	public void fillStructure() {
		String[] lines = sb.toString().split("\n");
		for(String line:lines) {
			blueprints.add(new BluePrint(line));
		}
		
		for(BluePrint b:blueprints) {
			System.out.println(b);
		}
	}
	
	public void resetValues() {
//		ore = 0;
//		clay = 0;
//		obsidian = 0;
//		geodes = 0;
//		oreRobot = 1;
//		clayRobot = 0;
//		obsidianRobot = 0;
//		geodeRobot = 0;
	}
	
	public void DSF(BluePrint bp, int timer,
		int ore,
		int clay,
		int obsidian,
		int geodes,
		int oreRobot,
		int clayRobot,
		int obsidianRobot,
		int geodeRobot,
		int expiration
			) {
		
		if(timer > expiration) {
			if(geodes>bp.maxGeodes) {
				bp.maxGeodes = geodes;
			}
			return;
		}
		// itt már hiába építünk, abból nem lesz cucc
		if(timer == expiration) {
			DSF(bp,timer+1,ore+oreRobot,clay+clayRobot,obsidian+obsidianRobot,geodes+geodeRobot,oreRobot,clayRobot,obsidianRobot,geodeRobot,expiration);
			return;
		}
		//ha mostantól mindig építünk és az kevesebb mint az eddigi max, akkor dobjuk az ágat
		if(timer<expiration) {
			int newGeodeRobot = geodeRobot;
			int sum=geodes+geodeRobot;
			for(int i=timer;i<expiration;i++) {
				newGeodeRobot++;
				sum+=newGeodeRobot;
			}
			if(sum<=bp.maxGeodes) {
				return;
			}
		}
		if(ore>=bp.geodeRobot_ore && obsidian>=bp.geodeRobot_obsidian) {
			DSF(bp,timer+1,ore-bp.geodeRobot_ore+oreRobot,clay+clayRobot,obsidian-bp.geodeRobot_obsidian+obsidianRobot,geodes+geodeRobot,oreRobot,clayRobot,obsidianRobot,geodeRobot+1,expiration);
		} else {
			if(ore>=bp.obsidianRobot_ore && clay >= bp.obsidianRobot_clay) {
				DSF(bp,timer+1,ore-bp.obsidianRobot_ore+oreRobot,clay-bp.obsidianRobot_clay+clayRobot,obsidian+obsidianRobot,geodes+geodeRobot,oreRobot,clayRobot,obsidianRobot+1,geodeRobot,expiration);
			} else if(ore>=bp.clayRobot_ore) {
				DSF(bp,timer+1,ore-bp.clayRobot_ore+oreRobot,clay+clayRobot,obsidian+obsidianRobot,geodes+geodeRobot,oreRobot,clayRobot+1,obsidianRobot,geodeRobot,expiration);
			} 
			if(oreRobot<=bp.max_ore_robot && ore >=bp.oreRobot_ore) {
				DSF(bp,timer+1,ore-bp.oreRobot_ore+oreRobot,clay+clayRobot,obsidian+obsidianRobot,geodes+geodeRobot,oreRobot+1,clayRobot,obsidianRobot,geodeRobot,expiration);
			} 
			DSF(bp,timer+1,ore+oreRobot,clay+clayRobot,obsidian+obsidianRobot,geodes+geodeRobot,oreRobot,clayRobot,obsidianRobot,geodeRobot,expiration);
		}
	}
	
	public void part1() {
		long t1 = System.currentTimeMillis();
		int result = 0;
		for(BluePrint bp:blueprints) {
			bp.maxGeodes=0;
			DSF(bp,1,0,0,0,0,1,0,0,0,24);
			logln("bp:"+bp.number+" max geodes: "+bp.maxGeodes);
			result+=(bp.number*bp.maxGeodes);
		}
		
		long t2 = System.currentTimeMillis();
		logln("Solution part1: "+result+" ["+(t2-t1)/1000+"s]"); // sol: 988 4s
	}
	
	public void part2() {
		long t1 = System.currentTimeMillis();
		int result = 1;
		for(int i=0;i<(INPUT_REAL?3:2);i++) {
			BluePrint bp = blueprints.get(i);
			bp.maxGeodes=0;
			DSF(bp,1,0,0,0,0,1,0,0,0,32);
			
			logln("bp:"+bp.number+" max geodes:"+bp.maxGeodes + " counter:"+COUNTER);
			result*=bp.maxGeodes;
		}
		long t2 = System.currentTimeMillis();
		logln("Solution part2: "+result+" ["+(t2-t1)/1000+"s]"); //sol: 8580 8s
	}

	public static void main(String[] args) {
		Day19 d19 = new Day19();
		
		d19.readFile();
		d19.fillStructure();
		d19.part1();
		d19.part2();
	}
}
