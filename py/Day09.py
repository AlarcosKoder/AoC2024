from collections import defaultdict
import time



# Helper functions
def get_last_index_of_non_dot(blocks, last_found):
    for i in range(last_found, -1, -1):
        if blocks[i] != -1:
            return i
    return -1

def get_last_index_of_id(blocks, block_id):
    for i in range(len(blocks) - 1, -1, -1):
        if blocks[i] == block_id:
            return i
    return -1

def get_first_index_of_dot(blocks, first_found):
    for i in range(first_found, len(blocks)):
        if blocks[i] == -1:
            return i
    return -1

def get_first_index_of_n_dot(blocks, length, end):
    for i in range(0, end - length + 1):
        if blocks[i] == -1 and i + length <= len(blocks):
            if all(blocks[i + j] == -1 for j in range(length)):
                return i
    return -1

def process_file(file_content):
    # Parse the input
    disk_map = file_content.strip()
    
    # Validate input length
    if len(disk_map) % 2 != 0:
        disk_map = disk_map+"0"

    # Decode the disk map into individual blocks
    ids = []
    ids_map = {}
    blocks_list1 = []
    blocks_list2 = []

    for i in range(0, len(disk_map), 2):
        file_length = int(disk_map[i])
        free_length = int(disk_map[i + 1])
        block_id = i // 2

        blocks_list1.extend([block_id] * file_length)
        blocks_list1.extend([-1] * free_length)

        if block_id not in ids:
            ids.append(block_id)
        if block_id not in ids_map:
            ids_map[block_id] = file_length

    blocks_list2 = blocks_list1[:]

    # Simulate the compaction process
    write_index = get_first_index_of_dot(blocks_list1, 0)
    read_index = get_last_index_of_non_dot(blocks_list1, len(blocks_list1) - 1)

    while write_index < read_index:
        blocks_list1[write_index], blocks_list1[read_index] = blocks_list1[read_index], -1
        write_index = get_first_index_of_dot(blocks_list1, write_index)
        read_index = get_last_index_of_non_dot(blocks_list1, read_index)

    # Calculate the checksum for part 1
    result_part1 = sum(i * val for i, val in enumerate(blocks_list1) if val != -1)

    # Reverse the order of IDs for part 2
    ids.reverse()
    for current_id in ids:
        n = ids_map[current_id]
        end = get_last_index_of_id(blocks_list2, current_id)
        write_index = get_first_index_of_n_dot(blocks_list2, n, end)

        if write_index >= 0:
            for i in range (n):
                blocks_list2.remove(current_id)
            for i in range(write_index, write_index+n):
                o = blocks_list2.pop(write_index)
                blocks_list2.insert(end-n, o) 
            
            for i in range (n):
                blocks_list2.insert(write_index,current_id)
            
    # Calculate the checksum for part 2
    result_part2 = sum(i * val for i, val in enumerate(blocks_list2) if val != -1)

    return result_part1, result_part2

def main():
    time_start = int(time.time() * 1000)
    file_path = r'..\input_2024\day09_data.txt'

    try:
        with open(file_path, "r") as file:
            file_content = file.read()

    except FileNotFoundError:
        print("Error: File not found.")
        return
    
    result_part_1, result_part_2 = process_file(file_content)

    print("result part 1:", result_part_1)
    print("result part 2:", result_part_2)

    time_end = int(time.time() * 1000)
    print(f"Execution time: {time_end - time_start} msec")

if __name__ == "__main__":
    main()
