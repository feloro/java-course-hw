package ru.ifmo.rain.istomin;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import ru.ifmo.rain.istomin.set.ArraySet;

public class LowerTest {

	public static void main(String... args) {
		Integer elem = -987289629;
		Comparator comparator = Comparator.comparingInt(Integer::intValue).reversed();
		Integer[] arr = new Integer[]{2048379824, -987289629, -1866245492, -919443364, -679901667, 1959457375};
		//lower() (comparator = Reverse order, elements = []) expected:<-919443364> but was:<2048379824>
				//java.lang.AssertionError: in lower(-987289629) (comparator = Reverse order, elements = [2048379824, -987289629, -1866245492, -919443364, -679901667, 1959457375]) expected:<-919443364> but was:<2048379824>
		ArraySet<Integer> integers = new ArraySet<>(Arrays.asList(arr), comparator);
		for (Integer inte: integers) {
			System.out.println(inte);
		}
		System.out.println(integers.lower(elem));
	}
}
