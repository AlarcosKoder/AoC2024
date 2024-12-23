import time
from collections import defaultdict
from bisect import insort

def process_file(file_path):
    # Parse input data
    rules = defaultdict(set)
    page_numbers = []
    incorrect_page_numbers = []

    try:
        with open(file_path, "r") as file:
            lines = file.readlines()
    except FileNotFoundError:
        print("Error: File not found.")
        return

    for line in lines:
        line = line.strip()
        if "|" in line:
            a, b = map(int, line.split("|"))
            rules[a].add(b)
        elif "," in line:
            row = list(map(int, map(str.strip, line.split(","))))
            page_numbers.append(row)

    # Part 1: Calculate result_part1
    result_part1 = 0
    for page_number in page_numbers:
        correct_order = True
        for i in range(len(page_number) - 1):
            a, b = page_number[i], page_number[i + 1]
            if b not in rules.get(a, set()):
                correct_order = False
                break
        if correct_order:
            result_part1 += page_number[len(page_number) // 2]
        else:
            incorrect_page_numbers.append(page_number)

    print("result part 1:", result_part1) # correct: 4281

    # Part 2: Calculate result_part2
    result_part2 = 0
    for icpn in incorrect_page_numbers:
        new_list = []
        for number in icpn:
            inserted = False
            for i, a in enumerate(new_list):
                if number in rules and a in rules[number]:
                    new_list.insert(i, number)
                    inserted = True
                    break
            if not inserted:
                new_list.append(number)
        result_part2 += new_list[len(new_list) // 2]

    print("result part 2:", result_part2) # correct: 5466

def main():
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day05_data.txt'

    process_file(file_path)

    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
