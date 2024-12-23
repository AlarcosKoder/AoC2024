import time
from collections import defaultdict

def wander_trail(map_data, value, x, y, scores, trailhead, width, height):
    next_value = value + 1

    if map_data[x][y] == 9 and next_value == 10:
        scores[trailhead].add((x, y))
        return 1

    total = 0

    # Up
    if x > 0 and map_data[x - 1][y] == next_value:
        total += wander_trail(map_data, next_value, x - 1, y, scores, trailhead, width, height)

    # Down
    if x < height - 1 and map_data[x + 1][y] == next_value:
        total += wander_trail(map_data, next_value, x + 1, y, scores, trailhead, width, height)

    # Left
    if y > 0 and map_data[x][y - 1] == next_value:
        total += wander_trail(map_data, next_value, x, y - 1, scores, trailhead, width, height)

    # Right
    if y < width - 1 and map_data[x][y + 1] == next_value:
        total += wander_trail(map_data, next_value, x, y + 1, scores, trailhead, width, height)

    return total

def process_file(file_content):
    lines = file_content.strip().split("\n")
    height = len(lines)
    width = len(lines[0])

    # Parse the map
    map_data = [[int(ch) for ch in line] for line in lines]

    # Initialize trailhead scores
    scores = defaultdict(set)

    for x in range(height):
        for y in range(width):
            if map_data[x][y] == 0:
                scores[(x, y)] = set()

    result_part_1 = 0
    result_part_2 = 0

    for trailhead in scores.keys():
        x, y = trailhead
        result_part_2 += wander_trail(map_data, 0, x, y, scores, trailhead, width, height)

    for trailhead in scores.keys():
        result_part_1 += len(scores[trailhead])

    return result_part_1, result_part_2

def main():
    time_start = int(time.time() * 1000)

    file_path = r'..\\input_2024\\day10_data.txt'

    try:
        with open(file_path, "r") as file:
            file_content = file.read()

    except FileNotFoundError:
        print("Error: File not found.")
        return

    result_part_1, result_part_2 = process_file(file_content)

    print("result part 1:", result_part_1)  # Should match 617
    print("result part 2:", result_part_2)  # Should match 1477

    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
