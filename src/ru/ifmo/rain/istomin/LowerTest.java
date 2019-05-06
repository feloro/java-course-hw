package ru.ifmo.rain.istomin;

import java.util.Arrays;

import java.util.Comparator;
import ru.ifmo.rain.istomin.set.ArraySet;

public class LowerTest {

	public static void main(String... args) {
		Integer elem = -1895379872;
		Comparator comparator = Comparator.comparingInt((Integer i) -> i / 100);
		// Comparator comparator = Integer::compare;
//				new NamedComparator("Reverse order", Comparator.comparingInt(Integer::intValue).reversed()),
//				new NamedComparator("Div 100", Comparator.comparingInt(i -> i / 100)),
//				new NamedComparator("Even first", Comparator.<Integer>comparingInt(i -> i % 2).thenComparing(Integer::intValue)),
//				new NamedComparator("All equal", Comparator.comparingInt(i -> 0)),
		Integer[] arr = new Integer[] {-925187023, 1788918170, 1706261644, 1144111252, 456235711, -556064536, 1360860074, -1118057655, 1810331610};
		ArraySet<Integer> integers = new ArraySet<>(Arrays.asList(arr), comparator);
//		for (Integer inte : integers) {
//			System.out.println(inte);
//		}

		integers.headSet(1788918170).forEach(System.out::println);

	}
}
