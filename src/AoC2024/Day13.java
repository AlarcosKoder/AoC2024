package AoC2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.LUDecomposition;

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
//			System.out.println(ax+" "+ay+" "+bx+" "+by+" "+px+" "+py);
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
		
		System.out.println("result part1: "+result_part1); // correct: 40369
		
		String result_part2 = runSolution(sb.toString(), 10000000000000L, Long.MAX_VALUE, 1e-4d);

		//TODO: implement this in java, google or-tool only works in python for me. java implementation copied from https://topaz.github.io/paste/#XQAAAQDvBwAAAAAAAAAgFMpWhFDWrqNP1EhXUpOetTZ/XtXy2GuQ/I6DAbJ0NZKE6lRjkInUXvNuccwyb1EL/HWPzH6Rc+9Jmws7N9dg4g0xuuFhI4Go3jdANVfm7A7p5tJuwRnj8zhj7JHeY/a91J/CcC9+QiIeLXw5NNFnYGg3X61eTYNlaQtCMVPf9/JyHCfyR7XGMr9wcrRVSLWe5fAFvAcAa+KuPprpisbNbX2pI0mT+E12Nq42HS+O5AE0vuVOo9LFWGG4u0SV9LFS/qpw5d2xzHTXmDUcfGCiXcJESM0e5cdmiYnFkBEVmNqOjhej2I/q/98ujX97j/wB8a8GPICYtoQklx6tYRdnwHkz+k6KvS086uf8xJNeP6vqcuGNhpbLosvrbMdHN7/ufzWEs2n4AEbNs9aVnmjgdsPR7TBBEITHgmzhIc19/vCTE9MpJlakgliS7oH4h125yVumF9nhd9yDX8gDPRyHmMw+VK9e0L1qAy2p5R+fCQjIrBfGJV/q9dymvawsaxs1lMvHfGWcQFPIWs11lM/x793qlJxkKztTT7IPaI83qy4Wp5B5fCZMOAS6bsm74eDuWOsCSBPsrbcFyWKldIi3BrQv7SDksMDdf/RxaARHGLijs2IsY4Xdy7SOIm4MKorTOcUronli9Pab+Q1CkpSyQaOOndMKLjZjkBfbpgDd8UNUYdjl8WQiGP9ppFeoIEPIYg0GvMfQMsY4ErlJ7VcEWZ1MhAhMDmF+ATNNPH3RtM3OsbosxUtgn9bqLckOczICR2Ge+lD3gLN8io7aksjxPE/uxJO9LDHo5hwn+ut5qwI4TKFSFBOmd2RIhR4BUTq+ICjqGdHk+gWhXoMPr6N+Nc17RQLY+23F1tkF9K5ruksLsNzzvINdE6ZTyU9j0ksEE4IkMozNIXYsKvHYGOoorJJoUe5YXNPk9S3iV3EnKFVMRBhLGgYgKCRxYs7kal7B8UPgVERzeZ6FS1x8UPz7td1OB+/wrIAZJP8O0SgNly6ref5bU1I=
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

    protected String runSolution(String input, long prizeOffset, long maxPresses, double eps) {
        var tokens = 0L;

        for (var eq : input.split("\n\n")) {
            var reButton = Pattern.compile("Button [AB]: X\\+(?<x>\\d+), Y\\+(?<y>\\d+)");
            var rePrize = Pattern.compile("Prize: X=(?<x>\\d+), Y=(?<y>\\d+)");

            var lines = eq.lines().toArray(String[]::new);
            var buttonA = reButton.matcher(lines[0]);
            var buttonB = reButton.matcher(lines[1]);
            var prize = rePrize.matcher(lines[2]);

            buttonA.matches();
            buttonB.matches();
            prize.matches();

            var a = new Array2DRowRealMatrix(new double[][]{
                    {Double.parseDouble(buttonA.group("x")), Double.parseDouble(buttonB.group("x"))},
                    {Double.parseDouble(buttonA.group("y")), Double.parseDouble(buttonB.group("y"))}
            });
            var b = new ArrayRealVector(new double[]{
                    Double.parseDouble(prize.group("x")) + prizeOffset,
                    Double.parseDouble(prize.group("y")) + prizeOffset
            });

            var solver = new LUDecomposition(a).getSolver();
            var solution = solver.solve(b);

            var n = solution.getEntry(0);
            var m = solution.getEntry(1);

            // </3 floating point numbers :(
            if (Math.abs(n - Math.round(n)) < eps && Math.abs(m - Math.round(m)) < eps  // integer solution!
                    && Math.round(n) <= maxPresses && Math.round(m) <= maxPresses) {
                tokens += Math.round(n) * 3 + Math.round(m);
            }
        }

        return "%d".formatted(tokens);
    }
}
