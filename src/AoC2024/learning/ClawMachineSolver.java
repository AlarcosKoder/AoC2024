package AoC2024.learning;

import org.apache.commons.math3.optim.linear.*;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.PointValuePair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClawMachineSolver {

    public static void main(String[] args) throws IOException {
        String filePath = "input_2024\\day13_data.txt";
        List<MachineData> machines = parseClawMachineFile(filePath);

        int totalPrizesWon = 0;
        int totalCost = 0;

        for (MachineData machine : machines) {
            int cost = solveClawMachine(machine);
            if (cost != -1) {
                totalPrizesWon++;
                totalCost += cost;
            }
        }

        System.out.println("Prizes Won: " + totalPrizesWon);
        System.out.println("Total Cost: " + totalCost);
    }

    private static int solveClawMachine(MachineData machine) {
        LinearObjectiveFunction objective = new LinearObjectiveFunction(new double[]{3, 1}, 0);

        // Constraints
        List<LinearConstraint> constraints = new ArrayList<>();
        constraints.add(new LinearConstraint(
                new double[]{machine.ax, machine.bx},
                Relationship.EQ,
                machine.px
        ));
        constraints.add(new LinearConstraint(
                new double[]{machine.ay, machine.by},
                Relationship.EQ,
                machine.py
        ));

        // Solver
        SimplexSolver solver = new SimplexSolver();
        try {
            PointValuePair solution = solver.optimize(
                    new LinearConstraintSet(constraints),
                    objective,
                    GoalType.MINIMIZE
            );

            double[] vars = solution.getPoint();
            int x = (int) Math.round(vars[0]);
            int y = (int) Math.round(vars[1]);

            if (x < 0 || y < 0) {
                return -1;
            }

            return 3 * x + y;
        } catch (Exception e) {
            return -1; // No feasible solution
        }
    }

    private static List<MachineData> parseClawMachineFile(String filePath) throws IOException {
        List<MachineData> machines = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int ax = 0, ay = 0, bx = 0, by = 0, px = 0, py = 0;

        while ((line = reader.readLine()) != null) {
            if (line.startsWith("Button A")) {
                String[] parts = line.split("[X+, Y+]");
                ax = Integer.parseInt(parts[1].trim());
                ay = Integer.parseInt(parts[2].trim());
            } else if (line.startsWith("Button B")) {
                String[] parts = line.split("[X+, Y+]");
                bx = Integer.parseInt(parts[1].trim());
                by = Integer.parseInt(parts[2].trim());
            } else if (line.startsWith("Prize")) {
                String[] parts = line.split("[X=, Y=]");
                px = Integer.parseInt(parts[1].trim());
                py = Integer.parseInt(parts[2].trim());

                machines.add(new MachineData(ax, ay, bx, by, px, py));
            }
        }
        reader.close();
        return machines;
    }

    private static class MachineData {
        int ax, ay, bx, by, px, py;

        public MachineData(int ax, int ay, int bx, int by, int px, int py) {
            this.ax = ax;
            this.ay = ay;
            this.bx = bx;
            this.by = by;
            this.px = px;
            this.py = py;
        }
    }
}
