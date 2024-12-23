import re
import sys
import time


def read_file_to_arrays(file_path):
    array_p1_a = []
    array_p1_b = []
    array_p2_a = []
    array_p2_b = []

    # Open the file and process it line by line
    with open(file_path, 'r') as file:
        for line in file:
            a, b = map(int, line.split())
            array_p1_a.append(a)
            array_p1_b.append(b)

    array_p2_a = array_p1_a.copy()
    array_p2_b = array_p1_b.copy()

    return array_p1_a, array_p1_b, array_p2_a, array_p2_b


def main():
    time_start = int(time.time() * 1000)
    file_path = r'..\\input_2024\day01_data.txt'
    array_p1_a, array_p1_b, array_p2_a, array_p2_b = read_file_to_arrays(file_path)

    result_part_1 = 0
    
    for i in range(len(array_p1_a)):
        
        result_part_1 += abs(min(array_p1_a)-min(array_p1_b))
        
        array_p1_a.remove(min(array_p1_a))
        array_p1_b.remove(min(array_p1_b))
    
    print("result part 1:", result_part_1)
    
    
    result_part_2 = 0
    
    for i, value in enumerate(array_p2_a):
        count = array_p2_b.count(value)
        result_part_2 += value * count
        
    print("result part 2:", result_part_2)
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")
    

if __name__ == "__main__":
    main()
    
    
