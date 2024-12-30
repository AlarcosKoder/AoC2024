package AoC2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.ortools.*;

import AoC2022.Day;

public class Day13 extends Day {
	public Day13() {
		super();
		LOG_LEVEL = 1;
		INPUT_REAL = true;
	}
	
	private static class MachineData {
        long ax, ay, bx, by, px, py;

        public MachineData(long ax, long ay, long bx, long by, long px, long py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }
    }

	public void processFile() {
		String [] lines = sb.toString().split("\n");
		List<MachineData> machines_part1 = new ArrayList<>();
		long result_part1 = 0;
		for (int i = 0; i < lines.length; i+=4) {
			Pattern p1 = Pattern.compile("\\+(\\d+).*\\+(\\d+)");
			Matcher m1 = p1.matcher(lines[i]);
			Matcher m2 = p1.matcher(lines[i+1]);
			Pattern p3 = Pattern.compile("\\=(\\d+).*\\=(\\d+)");
			Matcher m3 = p3.matcher(lines[i+2]);
			long ax=0,ay=0,bx=0,by=0,px=0,py=0;
			if(m1.find()) {
				ax = Long.parseLong(m1.group(1));
				ay = Long.parseLong(m1.group(2));
			}
			
			if(m2.find()) {
				bx = Long.parseLong(m2.group(1));
				by = Long.parseLong(m2.group(2));
			}
			
			if(m3.find()) {
				px = Long.parseLong(m3.group(1));
				py = Long.parseLong(m3.group(2));	
			}
			System.out.println(ax+" "+ay+" "+bx+" "+by+" "+px+" "+py);
			machines_part1.add(new MachineData(ax, ay, bx, by, px, py));
			
			
			long cost = Long.MAX_VALUE;
			for (long a = 0; a <= 100; a++) {
				long xRemaining = px - ax * a;
		        long yRemaining = py - ay * a;

		        if (xRemaining % bx == 0L && yRemaining % by == 0L) {
		            long b = xRemaining / bx;
		            if (b >= 0L && yRemaining / by == b) {
		            	cost = Math.min(cost, 3L * a + b);
		            }
		        }
		    }
			cost = cost==Long.MAX_VALUE ? -1L : cost;
			if(cost!=-1)result_part1+=cost;
			
		}
		int totalPrizesWon = 0;
		
//        for (MachineData machine : machines_part1) {
//            long cost = solveClawMachine(machine);
//            if (cost != -1) {
//                totalPrizesWon++;
//                result_part1 += cost;
//            }
//        }
        System.out.println("Prizes Won: " + totalPrizesWon);
        
		System.out.println("result part1: "+result_part1); // correct: 40369
		
		List<MachineData> machines_part2 = new ArrayList<>();
		long result_part2 = 0;
		for (int i = 0; i < lines.length; i+=4) {
			Pattern p1 = Pattern.compile("\\+(\\d+).*\\+(\\d+)");
			Matcher m1 = p1.matcher(lines[i]);
			Matcher m2 = p1.matcher(lines[i+1]);
			Pattern p3 = Pattern.compile("\\=(\\d+).*\\=(\\d+)");
			Matcher m3 = p3.matcher(lines[i+2]);
			long ax=0,ay=0,bx=0,by=0,px=0,py=0;
			long shift = 10000000000000L;
			if(m1.find()) {
				ax = Long.parseLong(m1.group(1));
				ay = Long.parseLong(m1.group(2));
			}
			
			if(m2.find()) {
				bx = Long.parseLong(m2.group(1));
				by = Long.parseLong(m2.group(2));
			}
			
			if(m3.find()) {
				px = Long.parseLong(m3.group(1))+shift;
				py = Long.parseLong(m3.group(2))+shift;	
			}
			
			machines_part2.add(new MachineData(ax, ay, bx, by, px, py));
		}
		
		totalPrizesWon = 0;

        for (MachineData machine : machines_part2) {
            long cost = solveClawMachine(machine);
            if (cost != -1) {
                totalPrizesWon++;
                result_part2 += cost;
            }
        }

        System.out.println("Prizes Won: " + totalPrizesWon);
		//TODO: implement this, it only works in python, this solver sucks
		System.out.println("result part2: "+result_part2); // correct: 72587986598368
	}
	
	private static long solveClawMachine(MachineData machine) {
//        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{3, 1}, 0);
//
//        // Constraints
//        List<LinearConstraint> constraints = new ArrayList<>();
//        constraints.add(new LinearConstraint(
//                new double[]{machine.ax, machine.bx},
//                Relationship.EQ,
//                machine.px
//        ));
//        constraints.add(new LinearConstraint(
//                new double[]{machine.ay, machine.by},
//                Relationship.EQ,
//                machine.py
//        ));
//
//        // Solver
//        SimplexSolver solver = new SimplexSolver();
//        try {
//            PointValuePair solution = solver.optimize(
//                    new LinearConstraintSet(constraints),
//                    objective,
//                    GoalType.MINIMIZE
//            );
//
//            double[] vars = solution.getPoint();
//            long x = Math.round(vars[0]);
//            long y = Math.round(vars[1]);
//
//            if (x < 0 || y < 0) {
//                return -1;
//            }
//
//            return 3L * x + y;
//        } catch (Exception e) {
//            return -1; // No feasible solution
//        }
		return -1;
    }

	public static void main(String[] args) {
		long time_start = System.currentTimeMillis();
		Day13 day = new Day13();
		day.readFile();
		day.processFile();
		long time_end = System.currentTimeMillis();
		day.logln("Execution time: "+(time_end-time_start)+" msec");
		day.flush();
	}
}
