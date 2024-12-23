import math
from collections import defaultdict

class Coordinate:
    def __init__(self, x, y):
        self.x = x
        self.y = y

    def __eq__(self, other):
        return self.x == other.x and self.y == other.y

    def __hash__(self):
        return hash((self.x, self.y))

    def distance(self, other):
        return math.sqrt((self.x - other.x) ** 2 + (self.y - other.y) ** 2)


class Day08:
    def __init__(self):
        self.map = []
        self.rows = 0
        self.cols = 0
        self.LOG_LEVEL = 1
        self.INPUT_REAL = True

    def is_real_coordinate(self, coord):
        return 0 <= coord.x < self.rows and 0 <= coord.y < self.cols

    def process_file(self, input_text):
        values = defaultdict(list)
        antinodes = set()
        antinodes_part2 = set()

        lines = input_text.strip().split("\n")
        self.map = [list(line) for line in lines]
        self.rows = len(self.map)
        self.cols = len(self.map[0])

        for i, row in enumerate(self.map):
            for j, ch in enumerate(row):
                if ch == '.':
                    continue
                coord = Coordinate(i, j)
                values[ch].append(coord)

        for c_list in values.values():
            for c1 in c_list:
                for c2 in c_list:
                    if c1 == c2:
                        continue
                    diff_x = c1.x - c2.x
                    diff_y = c1.y - c2.y

                    m1 = self.rows // abs(diff_x) if diff_x != 0 else 0
                    m2 = self.cols // abs(diff_y) if diff_y != 0 else 0
                    m = max(m1, m2)

                    for i in range(1, m):
                        a1 = Coordinate(c1.x + i * diff_x, c1.y + i * diff_y)
                        a2 = Coordinate(c2.x - i * diff_x, c2.y - i * diff_y)
                        if self.is_real_coordinate(a1):
                            if i == 1:
                                antinodes.add(a1)
                            antinodes_part2.add(a1)
                        if self.is_real_coordinate(a2):
                            if i == 1:
                                antinodes.add(a2)
                            antinodes_part2.add(a2)

                antinodes_part2.add(c1)

        result_part1 = len(antinodes)
        result_part2 = len(antinodes_part2)

        print("result part1:", result_part1)  # correct: 423
        print("result part2:", result_part2)  # correct: 1287

    def visualize(self, antinodes):
        for i in range(self.rows):
            for j in range(self.cols):
                c = Coordinate(i, j)
                print("#" if c in antinodes else self.map[i][j], end="")
            print()

if __name__ == "__main__":
    import time
    time_start = int(time.time() * 1000)
    
    day = Day08()
    with open(r'..\input_2024\day08_data.txt') as file:
        input_data = file.read()

    day.process_file(input_data)
    
    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec") 
