import re
import sys
import time

# Global variables

def main():
    
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day00_sample.txt'
    
    try:
        with open(file_path, "r") as file:
            file_content = file.read()

    except FileNotFoundError:
        print("Error: File not found.")
        return
    
    result_part_1 = 0
    
    print("result part 1:", result_part_1)
    
    
    result_part_2 = 0
    print("result part 2:", result_part_2)
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
    
    
