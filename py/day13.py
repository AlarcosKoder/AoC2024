from ortools.linear_solver import pywraplp
import re

def solve_claw_machine(button_a, button_b, prize):
    """
    Solves the claw machine problem for one machine using Google OR-Tools.
    
    Args:
        button_a (tuple): (ax, ay) movements for Button A.
        button_b (tuple): (bx, by) movements for Button B.
        prize (tuple): (px, py) coordinates of the prize.
    
    Returns:
        tuple: (prize_won, cost) where prize_won is a boolean indicating 
               if the prize is winnable, and cost is the total cost.
    """
    ax, ay = button_a
    bx, by = button_b
    px, py = prize
    
    # Create solver instance
    solver = pywraplp.Solver.CreateSolver('SCIP')
    if not solver:
        raise RuntimeError("Google OR-Tools solver initialization failed.")

    # Variables: Number of presses for Button A (x) and Button B (y)
    x = solver.IntVar(0, solver.infinity(), 'x')  # Button A presses
    y = solver.IntVar(0, solver.infinity(), 'y')  # Button B presses

    # Constraints: Align X and Y coordinates with the prize
    solver.Add(ax * x + bx * y == px)
    solver.Add(ay * x + by * y == py)

    # Objective: Minimize the cost (3 tokens per Button A press, 1 token per Button B press)
    solver.Minimize(3 * x + y)

    # Solve the problem
    status = solver.Solve()

    if status == pywraplp.Solver.OPTIMAL:
        return True, solver.Objective().Value()
    else:
        return False, float('inf')

def solve_all_machines(claw_machines):
    """
    Solves the claw machine problem for all machines and calculates total cost.

    Args:
        claw_machines (list): List of tuples, each representing a claw machine
                              in the form (button_a, button_b, prize).
    
    Returns:
        tuple: (prizes_won, total_cost) where prizes_won is the number of prizes won,
               and total_cost is the combined cost of winning them.
    """
    total_cost = 0
    prizes_won = 0

    for machine in claw_machines:
        button_a, button_b, prize = machine
        won, cost = solve_claw_machine(button_a, button_b, prize)
        if won:
            prizes_won += 1
            total_cost += cost

    return prizes_won, total_cost

def parse_claw_machine_file(file_path):
    """
    Parses the claw machine data from the given file.

    Args:
        file_path (str): Path to the input file.

    Returns:
        list: A list of tuples, where each tuple represents a claw machine in the format:
              ((ax, ay), (bx, by), (px, py))
    """
    claw_machines = []

    # Regular expression patterns for parsing
    button_a_pattern = r"Button A: X\+(\d+), Y\+(\d+)"
    button_b_pattern = r"Button B: X\+(\d+), Y\+(\d+)"
    prize_pattern = r"Prize: X=(\d+), Y=(\d+)"

    with open(file_path, 'r') as file:
        content = file.read()

    # Split data into sections, one per claw machine
    machines_data = content.strip().split("\n\n")
    for machine_data in machines_data:
        # Extract values using regex
        button_a_match = re.search(button_a_pattern, machine_data)
        button_b_match = re.search(button_b_pattern, machine_data)
        prize_match = re.search(prize_pattern, machine_data)

        if button_a_match and button_b_match and prize_match:
            ax, ay = map(int, button_a_match.groups())
            bx, by = map(int, button_b_match.groups())
            px, py = map(int, prize_match.groups())
            px += 10000000000000
            py += 10000000000000
            # Append the parsed data as a tuple
            claw_machines.append(((ax, ay), (bx, by), (px, py)))

    return claw_machines

# File path to your input
file_path = r'..\\input_2024\\day13_data.txt'

# Parse the file
claw_machines = parse_claw_machine_file(file_path)

# Solve the problem
prizes_won, total_cost = solve_all_machines(claw_machines)
print(f"Prizes Won: {prizes_won}")
print(f"Total Cost: {total_cost}")
