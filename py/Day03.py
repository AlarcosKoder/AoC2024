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
    file_path = r'..\input_2024\day03_data.txt'
    
    with open(file_path, "r") as file:
        str = file.read()
    
    result_part_1 = 0
    #mul(X,Y)
    pattern = r"mul\((\d{1,3}),(\d{1,3})\)"
    for match in re.finditer(pattern, str):
        num1 = int(match.group(1))
        num2 = int(match.group(2))
        
        result_part_1 += num1 * num2
    
    print("result part 1:", result_part_1)
    
    
    result_part_2 = 0
    
    pattern = r"(?s)don't\(\).*?do\(\)"
    str = re.sub(pattern, "", str)
    
    pattern = r"mul\((\d{1,3}),(\d{1,3})\)"
    for match in re.finditer(pattern, str):
        num1 = int(match.group(1))
        num2 = int(match.group(2))
        
        result_part_2 += num1 * num2
   
    
    print("result part 2:", result_part_2)
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")
    

if __name__ == "__main__":
    main()
    
    
