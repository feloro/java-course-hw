package ru.ifmo.rain.istomin;

import java.util.Arrays;

import java.util.Comparator;
import ru.ifmo.rain.istomin.set.ArraySet;

public class LowerTest {

	public static void main(String... args) {
		Integer elem = -773537190;
		Comparator comparator = Comparator.comparingInt((Integer i) -> i / 100);
		// Comparator comparator = Integer::compare;
//				new NamedComparator("Reverse order", Comparator.comparingInt(Integer::intValue).reversed()),
//				new NamedComparator("Div 100", Comparator.comparingInt((Integer i) -> i / 100)),
//				new NamedComparator("Even first", Comparator.<Integer>comparingInt(i -> i % 2).thenComparing(Integer::intValue)),
//				new NamedComparator("All equal", Comparator.comparingInt(i -> 0)),
		//Integer[] arr = new Integer[] {-753667119, -299090360, 1666794384, 2105241616, -396122440, -309354932, -568729826};
		Integer[] arr = new Integer[] {-2042552561, -1452988359, -1827253010, 2139131438};

		ArraySet<Integer> integers = new ArraySet<>(Arrays.asList(arr), comparator);
//		for (Integer inte : integers) {
//			System.out.println(inte);
//		}

		integers.tailSet(-2042552561).forEach(System.out::println);

	}
}
