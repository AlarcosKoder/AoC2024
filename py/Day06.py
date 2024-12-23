import re
import sys
import time

# Global variables
map = None

def main():
    global map
    
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day06_data.txt'
    
    try:
        with open(file_path, "r") as file:
            map = [list(line.strip()) for line in file]
    except FileNotFoundError:
        print("Error: File not found.")
        return

    rows, cols = len(map), len(map[0])

    dx = [-1, 0, 1, 0]
    dy = [0, 1, 0, -1]

    guardX = guardY = direction = -1
    
    for i in range(rows):
        for j in range(cols):
            if map[i][j] == '^':
                o_guardX, guardX, o_guardY, guardY, direction = i, i, j, j, 0
                break
        if direction != -1: break
        
    visited = set()
    visited.add((guardX,guardY))
    
    while True:
        nextX, nextY = guardX + dx[direction], guardY + dy[direction]

        if nextX < 0 or nextX >= rows or nextY < 0 or nextY >= cols:
            break

        if map[nextX][nextY] == '#':
            direction = (direction + 1) % 4
        else:
            guardX, guardY = nextX, nextY
            if (guardX,guardY) not in visited:
                visited.add((guardX,guardY))
    
    result_part_1 = len(visited)
    print("result part 1:", result_part_1) # correct: 5453
    
    result_part_2 = 0
    
    for i, j in visited:
            
        guardX, guardY, direction = o_guardX, o_guardY, 0
        
        if (i,j) == (guardX,guardY): 
            continue
        
        original = map[i][j]
        map[i][j]='#'
        
        loop_detector = set()
        loop_detector.add((guardX,guardY,direction))

        while True: 
            nextX, nextY = guardX + dx[direction], guardY + dy[direction]

            if nextX < 0 or nextX >= rows or nextY < 0 or nextY >= cols: 
               break

            if map[nextX][nextY] == '#': 
                direction = (direction + 1) % 4
                
            else:
                guardX, guardY = nextX, nextY
                state = (guardX, guardY, direction)
                
                if state in loop_detector:
                    result_part_2+=1
                    break
                loop_detector.add(state)
                
        map[i][j]=original
    
    
    print("result part 2:", result_part_2) # correct: 2188
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
    
    
