package ru.ifmo.rain.istomin;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.TreeMap;
import java.util.TreeSet;
import ru.ifmo.rain.istomin.set.ArraySet;

public class LowerTest {

	public static void main(String... args) {
		Integer elem = 1903439937;
		Comparator comparator = Comparator.<Integer>comparingInt(i -> i % 2).thenComparing(Integer::intValue);
		// Comparator comparator = Integer::compare;
//				new NamedComparator("Reverse order", Comparator.comparingInt(Integer::intValue).reversed()),
//				new NamedComparator("Div 100", Comparator.comparingInt((Integer i) -> i / 100)),
//				new NamedComparator("Even first", Comparator.<Integer>comparingInt(i -> i % 2).thenComparing(Integer::intValue)),
//				new NamedComparator("All equal", Comparator.comparingInt((Integer i) -> 0)),
		//Integer[] arr = new Integer[] {-753667119, -299090360, 1666794384, 2105241616, -396122440, -309354932, -568729826};
		Integer[] arr = new Integer[] {30, 20, 10};

//		ArraySet<Integer> integers = new ArraySet<Integer>(List.of(10, 20, 30), null);
//		NavigableSet<Integer> set = integers.descendingSet();
//		set.descendingIterator().next().intValue();
//
//		System.out.println(set.floor(5));

		TreeSet<Integer> integers = new TreeSet(List.of(10, 20, 30));
		System.out.println(integers.floor(5));
		System.out.println(integers.descendingSet().floor(5));
	}
}
