import re
import sys
import time

# Global variables

def generate_result(result, current, remaining_list, part2) -> bool:
    
    if len(remaining_list) > 1:
        a = remaining_list.pop(0)
        b = remaining_list.pop(0)
            
        add = a+b;
        remaining_list.insert(0, add)
        if generate_result(result,add,remaining_list,part2): 
            return True
        
        remaining_list.pop(0)
        
        mul = a*b
        remaining_list.insert(0, mul)
        
        if generate_result(result,mul,remaining_list,part2): 
            return True
        
        remaining_list.pop(0)
        
        if part2: 
            concat = int(f'{a}{b}')
            remaining_list.insert(0, concat)
            if generate_result(result,concat,remaining_list,part2): 
                return True
            
            remaining_list.pop(0)
        
        remaining_list.insert(0,b)
        remaining_list.insert(0,a)
        
    elif len(remaining_list) == 1:
        if result==current: return True
        else: return False
    
    return False

def main():
    global char_array
    
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day07_data.txt'
    
    result_part_1 = 0
    try:
        with open(file_path, "r") as file:
            for line in file:
                result = int(line.split(":")[0])
                numbers = [int(n) for n in line.split(":")[1].split()] 
                if generate_result(result, 0, numbers, False):
                    result_part_1 += result
    except FileNotFoundError:
        print("Error: File not found.")
        return
    print("result part 1:", result_part_1) # correct: 4122618559853
    
    
    result_part_2 = 0
    try:
        with open(file_path, "r") as file:
            for line in file:
                result = int(line.split(":")[0])
                numbers = [int(n) for n in line.split(":")[1].split()] 
                if generate_result(result, 0, numbers, True):
                    result_part_2 += result
    except FileNotFoundError:
        print("Error: File not found.")
        return
    print("result part 2:", result_part_2) # correct: 227615740238334
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec") 

if __name__ == "__main__":
    main()
    
    
