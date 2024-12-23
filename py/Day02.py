import re
import sys
import time


def check_line(line: str) -> bool:
    splits = line.split(" ")
    increasing = None
    
    for i in range(1, len(splits)):
    
        current = int(splits[i - 1])
        next_val = int(splits[i])
        
        if increasing is None and next_val != current:
            increasing = next_val > current
        
        if current == next_val:
            break
        
        if increasing:
            if next_val - current <= 0:
                break
            if next_val - current > 3:
                break
        else:
            if current - next_val <= 0:
                break
            if current - next_val > 3:
                break
            
        if i+1 == len(splits):
            i += 1
    
    return i == len(splits)


def main():
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day02_data.txt'
    
    result_part_1 = 0
    
    with open(file_path, 'r') as file:
        for line in file:
            if check_line(line):
                result_part_1 += 1
    
    print("result part 1:", result_part_1)
    
    
    result_part_2 = 0    
    with open(file_path, 'r') as file:
        for line in file:
            if check_line(line):
                result_part_2 += 1
            else:
                splits = line.split(" ")
                
                for j in range(0, len(splits)):
                    new_line="";
                    for k in range(0, len(splits)):
                        if j!=k:
                            new_line += splits[k]
                            new_line += " "
                    
                    if check_line(new_line.strip()):
                        result_part_2 += 1
                        break;
    
    
    print("result part 2:", result_part_2)
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")
    

if __name__ == "__main__":
    main()
    
    
