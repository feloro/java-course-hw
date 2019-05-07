package ru.ifmo.rain.istomin.parallel;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelMapperImpl implements ParallelMapper {

	private ExecutorService executor;

	@Override
	public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args)
			throws InterruptedException {
		List<Future<? extends R>> futures = args.stream().map(it->executor.submit(new CallableTask<>(f, it))).collect(Collectors.toList());
		return futures.stream().map(it -> {
			try {
				return it.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			return null;
		}).collect(Collectors.toList());
	}

	@Override
	public void close() {
		executor.shutdown();
	}

	public ParallelMapperImpl(int threads) {
		executor = Executors.newFixedThreadPool(threads);
	}
}
