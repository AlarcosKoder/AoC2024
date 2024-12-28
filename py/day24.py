#!/usr/bin/env python3
# 2024 Day 24: Crossed Wires

"""
Basic strategy:
  1. Find anomalous z output gates. For each one found, find the swap partner, and
     swap the outputs. Note that the logic will not work unless you swap as you go.
  2. Use brute force to find the last pair to swap.
"""

from itertools import combinations
import sys

def process_input(filename):
    """Acquire input data"""
    with open(filename) as file:
        input = file.read().splitlines()

    """
    Part 1 used named tuples for the gates, which was cool. But named tuples aren't
    mutable, which makes it more work to swap the output wires; you have to create a
    completely new tuple to replace the old one.
    
    Part 2 changes the gates to dictionaries: they're mutable, but you can
    still have named values such as gate['op'].
    """

    wires = {}
    gates = []  # list of gates, each is a dictionary
    index = {}  # reverse index of output wires to gates that set them
    xy_len = 0
    z_len  = 0
    
    for line in input:
        if ':' in line:
            token = line.replace(':','').split()
            wires[token[0]] = int(token[1])
            if token[0][0] == 'x': xy_len += 1
        elif '>' in line:
            in_1, op, in_2, out = line.replace('-','').replace('>','').split()
            gate = {'in':(in_1, in_2), 'op':op, 'out':out}
            gates.append(gate)
            index[gate['out']] = len(gates) -1
            if gate['out'][:1] == 'z': z_len += 1

    return wires, gates, index, xy_len, z_len

def find_z_anomalies():
    """
    Find z wire output gates that don't look right.
    Expected number of dependents (increases by 6 for each z wire) came
    from observation of listing # of dependents of each z wire.
    """
    swapped_outputs = []
    print()
    print('Analyzing gates for z outputs...')
    for z in range(z_len-1):
        g = index[f'z{z:02}']
        gate = gates[g]
        if gate['op'] == 'XOR': continue
        print()
        print('Bad gate: ',format_gate(gate))
        expected_nbr_dependents = z * 6
        s, swap_gate = find_swap_gate(expected_nbr_dependents)
        print('Swap with:',format_gate(swap_gate))
        swapped_outputs.extend((gate['out'],swap_gate['out']))
        swap_gates(g, s)
    return swapped_outputs

def find_swap_gate(nbr_dependents):
    """ Find a XOR gate with this number of dependents """
    for g, gate in enumerate(gates):
        if gate['op'] != 'XOR': continue
        dependents = dependents_of_wire(gate['out'])
        if len(dependents) == nbr_dependents:
            return (g, gate)
    return

def dependents_of_wire(output):
    """ Dependents are all the inputs that lead to this output wire """
    dependents = set()
    queue = [output]
    while len(queue) > 0:
        output = queue.pop()
        if output[:1] in 'xy':
            continue    # wire is not an output
        gate = gates[index[output]]
        for wire in gate['in']:
            if wire not in dependents:
                queue.append(wire)
                dependents.add(wire)
    return dependents

def format_gate(gate):
    return gate['in'][0] + ' ' + gate['op'] + ' ' + gate['in'][1] + ' -> ' + gate['out']


def find_last_pair(test_data):
    """ Test all combinations of gates, skipping those that output to z """
    print()
    print('Testing other combinations of gates...')

    # Create list of outputs to swap, without z wires, not including already swapped
    # Note: Not using a set because then you don't get repeatable results
    outputs = [output for output in index if output[:1] != 'z' and output not in swapped_outputs]

    tested = 0

    for out1, out2 in combinations(outputs,2):
        tested += 1
        g1 = index[out1]
        g1_gate = gates[g1]

        g2 = index[out2]
        g2_gate = gates[g2]

        if g1_gate['out'] in g2_gate['in']: continue    # would cause cycle
        if g2_gate['out'] in g1_gate['in']: continue
                   
        swap_gates(g1, g2)

        for (x, y) in test_data:
            worked = run_test(x,y)
            if not worked: break

        if worked:
            print()
            print('Tested', tested, 'combinations')
            print('Found a pair that works!')
            print('  ',format_gate(g1_gate))
            print('  ',format_gate(g2_gate))
            swapped_outputs.extend((g1_gate['out'],g2_gate['out']))
            return True

        swap_gates(g1, g2)

    print('No pair found')
    return False

def swap_gates(g, s):
    """ Swap the outputs of two gates, given the indexes of the gates """
    gates[g]['out'], gates[s]['out'] = gates[s]['out'], gates[g]['out']
    index[gates[g]['out']] = g
    index[gates[s]['out']] = s
    return

def run_test(x,y):
    correct = x + y
    wires.clear()       # very important!
    set_wires(wires, 'x', x)
    set_wires(wires, 'y', y)
    expected = {}
    set_wires(expected, 'z', correct)   # correct list of z wires
    output = simulate_gates(expected)
    result = correct == output
    return result

def set_wires(wires, w, value):
    if w == 'z':
        w_len = z_len
    else:
        w_len = xy_len
    for n in range(w_len):
        wire = f'{w}{n:02}'
        bit = value % 2
        wires[wire] = bit
        value //= 2
    return

def simulate_gates(expected = []):
    outputs = set(index)    # starts as all outputs
    while len(outputs) > 0:     # outputs not yet determined
        output_removed = False
        iter_outputs = set(outputs)
        for output in iter_outputs:
            gate = gates[index[output]]     # gate that produces this output
            if all(wire in wires for wire in gate['in']):
                wires[output] = gate_value(gate)
                if len(expected) > 0 and output[:1] == 'z':
                    if wires[output] != expected[output]:
                        return -1   # wrong z value, no point in continuing
                outputs.remove(output)
                output_removed = True
        if not output_removed:
            return -1       # swaps created an unsolvable set of outputs
    z_bits = output_bits()
    return int(z_bits,2)

def gate_value(gate):
    op = {'AND':'and', 'OR':'or', 'XOR':'^'}
    in_1 = wires[gate['in'][0]]
    in_2 = wires[gate['in'][1]]
    output = eval("in_1 " + op[gate['op']] + " in_2")
    return output

def output_bits():
    z_bits = ''
    for z in range(z_len):
        wire = f'z{z:02}'
        z_bits = str(wires[wire]) + z_bits
    return z_bits

#-----------------------------------------------------------------------------------------

filename = r'..\\input_2024\\day24_data.txt'

wires, gates, index, xy_len, z_len = process_input(filename)

swapped_outputs = find_z_anomalies()

# Note: If doesn't work for input, increase number of randomly generated x,y pairs
test_data = [(28872341726885, 28414614475596)]   # this pair finds answer in one test

found = find_last_pair(test_data)
if not found: sys(exit)

answer = ','.join(sorted(swapped_outputs))

print()
print('Swapped outputs =', answer)