import re
import sys
import time

# Global variables
char_array = None
xmas = "XMAS"

def wander_array(i,j, step_i, step_j, string) -> bool:
    global char_array
    if i < 0 or j < 0 or i >= len(char_array) or j >= len(char_array[0]): return False
    check = char_array[i][j]
    if string[0] == check:
        if len(string) == 1: 
            return True
        else: 
            return wander_array(i+step_i,j+step_j,step_i,step_j,string[1:])
    
    return False

def main():
    global char_array
    
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day04_data.txt'
    
    try:
        with open(file_path, "r") as file:
            char_array = [list(line.strip()) for line in file]
    except FileNotFoundError:
        print("Error: File not found.")
        return
    
    result_part_1 = 0
    for i in range(len(char_array)):
        for j in range(len(char_array[0])):
            if wander_array(i, j, 0, 1, xmas):  result_part_1 +=1
            if wander_array(i, j, 1, 0, xmas):  result_part_1 +=1
            if wander_array(i, j, 1, 1, xmas):  result_part_1 +=1
            if wander_array(i, j, 0, -1, xmas): result_part_1 +=1
            if wander_array(i, j, -1, 0, xmas): result_part_1 +=1
            if wander_array(i, j, -1, -1, xmas):result_part_1 +=1
            if wander_array(i, j, -1, 1, xmas): result_part_1 +=1
            if wander_array(i, j, 1, -1, xmas): result_part_1 +=1
    print("result part 1:", result_part_1)  # correct: 2378
    
    
    result_part_2 = 0
    for i in range(1,len(char_array)-1):
        for j in range(1,len(char_array[0])-1):
            if char_array[i][j] == 'A' and char_array[i-1][j-1] == 'M' and char_array[i-1][j+1] == 'S' and char_array[i+1][j-1] == 'M' and char_array[i+1][j+1] == 'S':
                result_part_2 += 1
            elif char_array[i][j] == 'A' and char_array[i-1][j-1] == 'S' and char_array[i-1][j+1] == 'S' and char_array[i+1][j-1] == 'M' and char_array[i+1][j+1] == 'M':
                result_part_2 += 1
            elif char_array[i][j] == 'A' and char_array[i-1][j-1] == 'M' and char_array[i-1][j+1] == 'M' and char_array[i+1][j-1] == 'S' and char_array[i+1][j+1] == 'S':
                result_part_2 += 1
            elif char_array[i][j] == 'A' and char_array[i-1][j-1] == 'S' and char_array[i-1][j+1] == 'M' and char_array[i+1][j-1] == 'S' and char_array[i+1][j+1] == 'M':
                result_part_2 += 1
    print("result part 2:", result_part_2)  # correct: 1796
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
    
    
