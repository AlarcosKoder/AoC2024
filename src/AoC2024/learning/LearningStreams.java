package AoC2024.learning;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LearningStreams {

	public LearningStreams() {
	}

	public static void main(String[] args) {

		//Task-1 Write a method that takes a list of integers and returns a list of only even numbers.
		int[] test = {1,2,3,4,5,6,7,8,9,10};
		Arrays.stream(test)
	      .filter(s -> s % 2 == 0)
	      .forEach(System.out::println);
		
		//Task-2 Write a method that takes a list of strings and returns a new list where all strings are converted to uppercase.
		String[] test_s = {"alma","x","körte","barack","banán","mandarin","rohadt a narancs","y2"};
		List<String> upperCaseStrings = Arrays.stream(test_s)
                .map(String::toUpperCase)
                .collect(Collectors.toList());
		System.out.println(upperCaseStrings);
		
		//Task-3 Write a method that takes a list of integers, squares each integer, and returns the sum of all squares.
		int sum = Arrays.stream(test)
	      .map(s -> s * s)
	      .sum();
		List<Integer> testList = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		int sum2 = testList.stream()
		                  .map(s -> s * s)
		                  .reduce(0, Integer::sum);
		System.out.println("sum:"+sum + " sum2:"+sum2);
		
		// Task 4: Find Longest String - Write a method that takes a list of strings and returns the longest string.
		String[] test_4 = {"alma","x","körte","barack","banán","mandarin","rohadt a narancs","y2"};
		Optional<String> max = Arrays.stream(test_4)
                .max(Comparator.comparing(String::length));
		if (max.isPresent()) {
		    System.out.println("Longest string: " + max.get());
		} else {
		    System.out.println("No strings found.");
		}
		
		// Task 5: Group Strings by First Character 
		// Write a method that takes a list of strings and groups them into a map where the key is the first character of the string, and the value is a list of all strings starting with that character.
		
		String[] test_5 = {"Apple", "banana", "apricot", "blueberry", "cherry"};
		Map<Character, List<String>> groupByAlphabet = Arrays.stream(test_5)
			.collect(
					Collectors.groupingBy(e -> Character.toLowerCase(e.charAt(0)))
			);
		System.out.println(groupByAlphabet);
		
		//Task 6: Count Words of Specific Length
		//Write a method that takes a list of strings and an integer n and returns the count of strings whose length is n.
		String[] test_6 = {"alma","x1","körte","barack","banán","mandarin","rohadt a narancs","y2"};
		long count = Arrays.stream(test_6)
				.filter(s -> s.length() == 2)
                .count();
		System.out.println("count: "+count);
		
		//Task 7: Find Duplicate Elements
		//Write a method that takes a list of integers and returns a list of duplicate elements (each duplicate should appear only once).
		List<Integer> test_7 = Arrays.asList(1, 5, 2, 3, 2, 4, 5, 6, 5);

		// Find duplicates
		Set<Integer> seen = new HashSet<>();
		Set<Integer> duplicates = test_7.stream()
		    .filter(n -> !seen.add(n)) // If `add` returns false, the number is a duplicate
		    .collect(Collectors.toSet());

		//Task 8: Join Strings with Comma
		//Write a method that takes a list of strings and joins them into a single string separated by commas (,).
		System.out.println("duplicates: " + duplicates);
		String[] test_8 = {"alma","x1","körte","barack","banán","mandarin","rohadt a narancs","y2"};
		String joined = Arrays.stream(test_8)
			.collect(Collectors.joining(", "))
			.toString();
		System.out.println("joined: "+joined);
		
		//Task 9: Calculate Average of Numbers
		//Write a method that takes a list of integers and returns the average as a double.
		List<Integer> test_9 = Arrays.asList(1, 5, 2, 3, 2, 4, 5, 6, 5);
		OptionalDouble avg = test_9.stream().mapToDouble(Double::new).average();
		if (avg.isPresent()) {
		    System.out.println("Avg: " + avg.getAsDouble());
		} else {
		    System.out.println("No avg");
		}
		//Task 10: Find First Non-Repeated Character
		// Write a method that takes a string and finds the first character that does not repeat in the string.
		String test_10 = "aabbccadabc";
		Character c1 = test_10.chars()
			.mapToObj(c -> (char) c)
			.collect(Collectors.groupingBy(c -> c, LinkedHashMap::new, Collectors.counting()))
			.entrySet()
			.stream()
			.filter(entry -> entry.getValue()==1)
			.map(Map.Entry::getKey)
			.findFirst()
			.orElse(null);
		System.out.println("c1: "+c1);
	}

}
