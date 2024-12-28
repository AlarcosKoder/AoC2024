package AoC2024;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import AoC2022.Day;

/**
 * This class implements a Genetic Algorithm with enhancements to handle 4 gate swaps
 * and retrieve pin1 and pin2 of the swapped gates.
 */
public class Day24_genetic extends Day {
    final private Map<String, Gate> wires;
    final private List<Gate> gates;
    final private Map<String, Integer> index;

    private long gate_x_result;
    private long gate_y_result;
    private long gate_z_result;

    private long xy_len;
    private long z_len;

    // Genetic Algorithm Parameters
    private static final int INITIAL_POPULATION_SIZE = 1000;
    private static final int MAX_GENERATIONS = 100000;
    private static final double INITIAL_MUTATION_RATE = 0.05;
    private static final double INITIAL_CROSSOVER_RATE = 0.7;
    private static final int CHROMOSOME_LENGTH = 4; // 4 swaps = 8 gate indices

    // Elitism Parameters
    private static final int ELITE_COUNT = 5;

    // Dynamic Parameters
    private static final double MUTATION_RATE_ADJUSTMENT = 0.05;
    private static final double CROSSOVER_RATE_ADJUSTMENT = 0.05;

    private Random random = new Random();

    public Day24_genetic() {
        super();
        LOG_LEVEL = 1;
        INPUT_REAL = true;

        wires = new HashMap<>();
        gates = new ArrayList<>();
        index = new HashMap<>();

        gate_x_result = 0;
        gate_y_result = 0;
        gate_z_result = 0;
        xy_len = 0;
        z_len = 0;
    }

    /**
     * Represents a gate in the circuit.
     */
    public class Gate implements Comparable<Gate> {
        String out;
        long value;
        Operation op;
        String pin_1;
        String pin_2;

        public Gate(String _out, long _value) {
            this.out = _out;
            this.value = _value;
        }

        public Gate(String _pin_1, String _op, String _pin_2, String _out) {
            this.out = _out;
            this.pin_1 = _pin_1;
            this.pin_2 = _pin_2;
            this.op = _op.equals("AND") ? Operation.AND
                    : (_op.equals("OR") ? Operation.OR : Operation.XOR);
            this.value = -1;
        }

        @Override
        public String toString() {
            return "\t" + out + "=" + value + " p1:" + pin_1 + " p2:" + pin_2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Gate that = (Gate) o;
            return out.equals(that.out);
        }

        @Override
        public int compareTo(Gate o) {
            return out.compareTo(o.out);
        }
    }

    /**
     * Enum for gate operations.
     */
    public enum Operation {
        AND, OR, XOR
    }

    /**
     * Represents an individual in the population.
     */
    public class Individual {
        List<Integer> swapIndices; // List of gate indices to swap (8 indices for 4 swaps)
        double fitness;

        public Individual(List<Integer> swapIndices) {
            this.swapIndices = swapIndices;
            this.fitness = 0;
        }

        // Copy constructor
        public Individual(Individual other) {
            this.swapIndices = new ArrayList<>(other.swapIndices);
            this.fitness = other.fitness;
        }
    }

    /**
     * Implements the Genetic Algorithm with enhancements.
     */
    public class GeneticAlgorithm {
        int populationSize;
        int generations;
        double mutationRate;
        double crossoverRate;
        int chromosomeLength;
        long expected;

        List<Individual> population;
        Day24_genetic day;

        public GeneticAlgorithm(int populationSize, int generations, double mutationRate, double crossoverRate,
                int chromosomeLength, long expected, Day24_genetic day) {
            this.populationSize = populationSize;
            this.generations = generations;
            this.mutationRate = mutationRate;
            this.crossoverRate = crossoverRate;
            this.chromosomeLength = chromosomeLength;
            this.expected = expected;
            this.day = day;
            this.population = new ArrayList<>();
        }

        /**
         * Initializes the population with random individuals.
         */
        public void initializePopulation() {
            for (int i = 0; i < populationSize; i++) {
                List<Integer> swapIndices = new ArrayList<>();
                while (swapIndices.size() < chromosomeLength) {
                    int index = random.nextInt(gates.size());
                    if (!swapIndices.contains(index)) {
                        swapIndices.add(index);
                    }
                }
                population.add(new Individual(swapIndices));
            }
        }

        /**
         * Evaluates the fitness of each individual in the population.
         */
        public void evaluateFitness() {
            for (Individual individual : population) {
                day.performSwaps(individual.swapIndices);
                day.evaluate_gates();
                long current = day.gate_z_result;
                if (current == expected) {
                    individual.fitness = Double.MAX_VALUE; // Maximum fitness for exact match
                } else {
                    individual.fitness = 1.0 / (1.0 + Math.abs(current - expected));
                }
                day.revertSwaps(individual.swapIndices);
            }
        }

        /**
         * Selects a parent using tournament selection.
         */
        public Individual selectParent() {
            int tournamentSize = 3;
            Individual best = null;
            for (int i = 0; i < tournamentSize; i++) {
                int randIndex = random.nextInt(population.size());
                Individual ind = population.get(randIndex);
                if (best == null || ind.fitness > best.fitness) {
                    best = ind;
                }
            }
            return best;
        }

        /**
         * Performs single-point crossover between two parents.
         */
        public List<Individual> crossover(Individual parent1, Individual parent2) {
            List<Integer> child1Swap = new ArrayList<>();
            List<Integer> child2Swap = new ArrayList<>();
            if (random.nextDouble() < crossoverRate) {
                int crossoverPoint = random.nextInt(chromosomeLength);
                for (int i = 0; i < chromosomeLength; i++) {
                    if (i < crossoverPoint) {
                        child1Swap.add(parent1.swapIndices.get(i));
                        child2Swap.add(parent2.swapIndices.get(i));
                    } else {
                        child1Swap.add(parent2.swapIndices.get(i));
                        child2Swap.add(parent1.swapIndices.get(i));
                    }
                }
            } else {
                child1Swap.addAll(parent1.swapIndices);
                child2Swap.addAll(parent2.swapIndices);
            }
            // Ensure uniqueness
            child1Swap = ensureUnique(child1Swap);
            child2Swap = ensureUnique(child2Swap);
            Individual child1 = new Individual(child1Swap);
            Individual child2 = new Individual(child2Swap);
            List<Individual> children = new ArrayList<>();
            children.add(child1);
            children.add(child2);
            return children;
        }

        /**
         * Ensures that all swap indices in the chromosome are unique.
         */
        private List<Integer> ensureUnique(List<Integer> swapIndices) {
            List<Integer> unique = new ArrayList<>();
            for (Integer index : swapIndices) {
                if (!unique.contains(index)) {
                    unique.add(index);
                }
            }
            // If duplicates were removed, add random unique indices
            while (unique.size() < chromosomeLength) {
                int newIndex = random.nextInt(gates.size());
                if (!unique.contains(newIndex)) {
                    unique.add(newIndex);
                }
            }
            return unique;
        }

        /**
         * Mutates an individual by randomly changing some of its swap indices.
         */
        public void mutate(Individual individual) {
            for (int i = 0; i < individual.swapIndices.size(); i++) {
                if (random.nextDouble() < mutationRate) {
                    int newSwap;
                    do {
                        newSwap = random.nextInt(gates.size());
                    } while (individual.swapIndices.contains(newSwap));
                    individual.swapIndices.set(i, newSwap);
                }
            }
        }

        /**
         * Calculates the diversity of the population based on unique swap indices.
         */
        public double calculateDiversity() {
            Set<Set<Integer>> uniqueSwapSets = new HashSet<>();
            for (Individual ind : population) {
                Set<Integer> swapSet = new HashSet<>(ind.swapIndices);
                uniqueSwapSets.add(swapSet);
            }
            return (double) uniqueSwapSets.size() / population.size();
        }

        /**
         * Runs the Genetic Algorithm and returns the best individual found.
         */
        public Individual run() {
            initializePopulation();
            evaluateFitness();

            for (int gen = 0; gen < generations; gen++) {
                List<Individual> newPopulation = new ArrayList<>();

                // Elitism: Preserve top ELITE_COUNT individuals
                List<Individual> elites = population.stream()
                        .sorted(Comparator.comparingDouble((Individual ind) -> ind.fitness).reversed())
                        .limit(ELITE_COUNT)
                        .map(ind -> new Individual(ind)) // Deep copy
                        .collect(Collectors.toList());
                newPopulation.addAll(elites);

                // Generate the rest of the population
                while (newPopulation.size() < populationSize) {
                    // Selection
                    Individual parent1 = selectParent();
                    Individual parent2 = selectParent();

                    // Crossover
                    List<Individual> children = crossover(parent1, parent2);

                    // Mutation
                    mutate(children.get(0));
                    mutate(children.get(1));

                    // Add to new population
                    newPopulation.add(children.get(0));
                    if (newPopulation.size() < populationSize) {
                        newPopulation.add(children.get(1));
                    }
                }

                population = newPopulation;
                evaluateFitness();

                // Find the best individual in the current generation
                Individual best = population.stream()
                        .max(Comparator.comparingDouble(ind -> ind.fitness))
                        .orElse(null);

                // Logging
                if (gen % 100 == 0 || (best != null && best.fitness == Double.MAX_VALUE)) {
                    logln("Generation " + gen + " Best fitness: " + best.fitness);
                }

                // Dynamic Parameters Adjustment based on diversity
                double diversity = calculateDiversity();
                if (diversity < 0.2) { // Threshold for low diversity
                    mutationRate = Math.min(1.0, mutationRate + MUTATION_RATE_ADJUSTMENT);
                    crossoverRate = Math.max(0.0, crossoverRate - CROSSOVER_RATE_ADJUSTMENT);
                    logln("Low diversity detected. Increasing mutation rate to " + mutationRate
                            + " and decreasing crossover rate to " + crossoverRate);
                } else if (diversity > 0.8) { // Threshold for high diversity
                    mutationRate = Math.max(0.0, mutationRate - MUTATION_RATE_ADJUSTMENT);
                    crossoverRate = Math.min(1.0, crossoverRate + CROSSOVER_RATE_ADJUSTMENT);
                    logln("High diversity detected. Decreasing mutation rate to " + mutationRate
                            + " and increasing crossover rate to " + crossoverRate);
                }

                // Check if an exact solution is found
                if (best != null && best.fitness == Double.MAX_VALUE) {
                    logln("Solution found in generation " + gen);
                    return best;
                }
            }

            // Return the best individual after all generations
            Individual best = population.stream()
                    .max(Comparator.comparingDouble(ind -> ind.fitness))
                    .orElse(null);
            return best;
        }
    }

    /**
     * Swaps the 'out' fields of two gates and updates the index map.
     */
    public void swap_gates(int g, int s) {
        String gateOut1 = gates.get(g).out;
        String gateOut2 = gates.get(s).out;

        // Swap the 'out' fields
        gates.get(g).out = gateOut2;
        gates.get(s).out = gateOut1;

        // Update the index map accordingly
        index.put(gates.get(g).out, g);
        index.put(gates.get(s).out, s);
    }

    /**
     * Performs swaps as defined by a list of gate indices.
     */
    public void performSwaps(List<Integer> swapIndices) {
        // Assuming swapIndices contains pairs to swap: [g1, g2, g3, g4, g5, g6, g7, g8]
        // Swaps: g1 <-> g2, g3 <-> g4, g5 <-> g6, g7 <-> g8
        for (int i = 0; i < swapIndices.size(); i += 2) {
            if (i + 1 < swapIndices.size()) {
                swap_gates(swapIndices.get(i), swapIndices.get(i + 1));
            }
        }
    }

    /**
     * Reverts swaps by performing the same swaps again.
     */
    public void revertSwaps(List<Integer> swapIndices) {
        performSwaps(swapIndices);
    }

    /**
     * Evaluates all gates based on their operations and inputs.
     */
    public void evaluate_gates() {
        // Reset gate values
        for (Gate gate : gates) {
            gate.value = -1L;
        }

        boolean progress = true;
        while (progress) {
            progress = false;
            for (Gate gate : gates) {
                if (gate.value == -1L) {
                    Gate g1 = wires.get(gate.pin_1);
                    if (g1 == null && index.containsKey(gate.pin_1)) {
                        g1 = gates.get(index.get(gate.pin_1));
                    }
                    Gate g2 = wires.get(gate.pin_2);
                    if (g2 == null && index.containsKey(gate.pin_2)) {
                        g2 = gates.get(index.get(gate.pin_2));
                    }

                    if (g1 != null && g1.value != -1L && g2 != null && g2.value != -1L) {
                        switch (gate.op) {
                            case AND:
                                gate.value = g1.value & g2.value;
                                break;
                            case OR:
                                gate.value = g1.value | g2.value;
                                break;
                            case XOR:
                                gate.value = g1.value ^ g2.value;
                                break;
                        }
                        progress = true;
                    }
                }
            }
        }

        // Calculate gate_z_result
        gate_z_result = 0;
        for (int i = 0; i < z_len; i++) {
            String number = (i < 10 ? ("0" + i) : String.valueOf(i));
            Gate gate = gates.get(index.get("z" + number));
            gate_z_result |= (gate.value << i);
        }
    }

    /**
     * Evaluates gate_x_result and gate_y_result based on wire values.
     */
    public void evaluate_results() {
        gate_x_result = 0;
        gate_y_result = 0;
        gate_z_result = 0;
        int i = 0;
        for (; i < xy_len; i++) {
            String number = (i < 10 ? ("0" + i) : String.valueOf(i));
            Gate gate = wires.get("x" + number);
            if (gate != null) {
                gate_x_result |= (gate.value << i);
            }
            gate = wires.get("y" + number);
            if (gate != null) {
                gate_y_result |= (gate.value << i);
            }
            gate = gates.get(index.get("z" + number));
            if (gate != null) {
                gate_z_result |= (gate.value << i);
            }
        }
        for (; i < z_len; i++) {
            String number = (i < 10 ? ("0" + i) : String.valueOf(i));
            Gate gate = gates.get(index.get("z" + number));
            if (gate != null) {
                gate_z_result |= (gate.value << i);
            }
        }
    }

    /**
     * Processes the input file, initializes gates and wires, and runs the Genetic Algorithm.
     */
    public void processFile() {
        String[] lines = sb.toString().split("\n");
        Pattern i_pattern = Pattern.compile("^(\\w+)\\:\\s+(\\w+)$");
        Pattern g_pattern = Pattern.compile("^(\\w+)\\s+(AND|XOR|OR)\\s+(\\w+)\\s+->\\s+(\\w+)$");

        for (String line : lines) {
            Matcher i_m = i_pattern.matcher(line);
            Matcher g_m = g_pattern.matcher(line);
            if (i_m.find()) {
                Gate wire = new Gate(i_m.group(1), Long.parseLong(i_m.group(2)));
                wires.put(wire.out, wire);

                if (wire.out.startsWith("x"))
                    xy_len++;
            }
            if (g_m.find()) {
                Gate gate = new Gate(g_m.group(1), g_m.group(2), g_m.group(3), g_m.group(4));
                gates.add(gate);
                index.put(gate.out, index.size());

                if (gate.out.startsWith("z"))
                    z_len++;
            }
        }

        evaluate_gates();
        evaluate_results();

        long result_part1 = gate_z_result;
        logln("result part1: " + result_part1); // correct: 51410244478064 [sample: 2024]
        logln("\tinit\tx: " + gate_x_result + " y:" + gate_y_result + " sum:"
                + (INPUT_REAL ? (gate_x_result + gate_y_result) : (gate_x_result & gate_y_result)));

        // Solution for part2 using Genetic Algorithm
        long expected_result = INPUT_REAL ? (gate_x_result + gate_y_result) : (gate_x_result & gate_y_result);
        logln("Expected result for part2: " + expected_result);

        // Initialize and run the Genetic Algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(INITIAL_POPULATION_SIZE, MAX_GENERATIONS, INITIAL_MUTATION_RATE,
                INITIAL_CROSSOVER_RATE, CHROMOSOME_LENGTH, expected_result, this);
        Individual bestIndividual = ga.run();

        // Apply the best individual's swaps
        if (bestIndividual != null && bestIndividual.fitness == Double.MAX_VALUE) {
            performSwaps(bestIndividual.swapIndices);
            evaluate_gates();
            evaluate_results();

            // Collect the swapped gate outputs along with their pin1 and pin2
            StringBuilder result_part2 = new StringBuilder();
            for (int i = 0; i < bestIndividual.swapIndices.size(); i += 2) {
                if (i + 1 < bestIndividual.swapIndices.size()) {
                    int idx1 = bestIndividual.swapIndices.get(i);
                    int idx2 = bestIndividual.swapIndices.get(i + 1);
                    Gate gate1 = gates.get(idx1);
                    Gate gate2 = gates.get(idx2);
                    result_part2.append(String.format("Swap %s (pin1: %s, pin2: %s) <-> %s (pin1: %s, pin2: %s)\n",
                            gate1.out, gate1.pin_1, gate1.pin_2,
                            gate2.out, gate2.pin_1, gate2.pin_2));
                }
            }

            logln("result part2 (Swaps Applied):\n" + result_part2.toString());
            logln("\tx: " + gate_x_result + " y:" + gate_y_result + " sum:"
                    + (INPUT_REAL ? (gate_x_result + gate_y_result) : (gate_x_result & gate_y_result)));
        } else if (bestIndividual != null) {
            logln("No exact solution found. Best attempt:");

            performSwaps(bestIndividual.swapIndices);
            evaluate_gates();
            evaluate_results();

            // Collect the swapped gate outputs along with their pin1 and pin2
            StringBuilder result_part2 = new StringBuilder();
            for (int i = 0; i < bestIndividual.swapIndices.size(); i += 2) {
                if (i + 1 < bestIndividual.swapIndices.size()) {
                    int idx1 = bestIndividual.swapIndices.get(i);
                    int idx2 = bestIndividual.swapIndices.get(i + 1);
                    Gate gate1 = gates.get(idx1);
                    Gate gate2 = gates.get(idx2);
                    result_part2.append(String.format("Swap %s (pin1: %s, pin2: %s) <-> %s (pin1: %s, pin2: %s)\n",
                            gate1.out, gate1.pin_1, gate1.pin_2,
                            gate2.out, gate2.pin_1, gate2.pin_2));
                }
            }
            long diff = result_part1-gate_z_result;
            result_part2.append("Diff:"+diff+" digits:"+String.valueOf(diff).length());

            logln("result part2 (Best Attempt Swaps Applied):\n" + result_part2.toString());
            
//            swap_gates(bestIndividual.swapIndices.get(0), bestIndividual.swapIndices.get(1));
//            swap_gates(bestIndividual.swapIndices.get(2), bestIndividual.swapIndices.get(3));
//        	evaluate_gates();
//            logln("check: "+gate_z_result+" diff:"+(result_part1-gate_z_result));
            
        } else {
            logln("No individuals in population.");
        }
    }

    /**
     * Main method to execute the program.
     */
    public static void main(String[] args) {
        long time_start = System.currentTimeMillis();
        Day24_genetic day = new Day24_genetic();
        day.readFile();
        day.processFile();
        long time_end = System.currentTimeMillis();
        day.logln("Execution time: " + (time_end - time_start) + " msec");
        day.flush();
    }
}
