package ru.ifmo.rain.istomin;

import java.util.*;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;
import ru.ifmo.rain.istomin.parallel.IterativeParallelismV2;
import ru.ifmo.rain.istomin.parallel.ParallelMapperImpl;
import ru.ifmo.rain.istomin.set.ArraySet;

public class LowerTest {

	public static void main(String... args) throws InterruptedException {
		ParallelMapper mapper = new ParallelMapperImpl(4);
		IterativeParallelismV2 parallelism = new IterativeParallelismV2(mapper);
		System.out.println(parallelism.maximum(4, Arrays.asList(9,1,2,3,4,5,6,7,8),Comparator.naturalOrder()));
		mapper.close();
	}
}
