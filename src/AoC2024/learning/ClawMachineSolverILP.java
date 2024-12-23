package AoC2024.learning;

import com.google.ortools.linearsolver.*;

public class ClawMachineSolverILP {

    static {
//        System.loadLibrary("jniortools");
    }

    public static long solveMachine(int ax, int bx, long px, int ay, int by, long py) {
        // Create solver instance
        MPSolver solver = MPSolver.createSolver("GLOP");
        if (solver == null) {
            throw new RuntimeException("Solver not available!");
        }

        // Decision variables x and y
        MPVariable x = solver.makeIntVar(0, Integer.MAX_VALUE, "x");
        MPVariable y = solver.makeIntVar(0, Integer.MAX_VALUE, "y");

        // Add constraints
        solver.makeConstraint("ax * x + bx * y == px");
        solver.makeConstraint("ay * x + by * y == py");

        // Objective: Minimize cost = 3x + y
        MPObjective objective = solver.objective();
        objective.setCoefficient(x, 3);
        objective.setCoefficient(y, 1);
        objective.setMinimization();

        // Solve
        MPSolver.ResultStatus resultStatus = solver.solve();
        if (resultStatus != MPSolver.ResultStatus.OPTIMAL) {
            return -1; // No solution found
        }

        return (long) (3 * x.solutionValue() + y.solutionValue());
    }

    public static void main(String[] args) {
        int[][] machines = {
            {94, 22, 8400, 34, 67, 5400},
            {26, 67, 12748, 66, 21, 12176},
            {17, 84, 7870, 86, 37, 6450},
            {69, 27, 18641, 23, 71, 10279}
        };

        long totalCost = 0;
        int prizesWon = 0;

        for (int[] machine : machines) {
            int ax = machine[0], bx = machine[1], ay = machine[3], by = machine[4];
            long px = machine[2], py = machine[5];

            long cost = solveMachine(ax, bx, px, ay, by, py);
            if (cost != -1) {
                prizesWon++;
                totalCost += cost;
            }
        }

        System.out.println("Prizes Won: " + prizesWon);
        System.out.println("Total Cost: " + totalCost);
    }
}
