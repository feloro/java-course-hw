package ru.ifmo.rain.istomin.parallel;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ParallelMapperImpl implements ParallelMapper {

	private Thread[] threads;

	@Override
	public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)args.size()/threads.length)+1);
		List<R>[] maxElems = new List[threads.length];
		for (int i = 0; i < threads.length; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = args.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, args.size()));
				List<R> result = new ArrayList<>();
				for (int j = 0; j < sublist.size() || !Thread.currentThread().isInterrupted(); j++) {
					result.add(f.apply(sublist.get(j)));
				}
				maxElems[finalI] = sublist.stream().map(f).collect(Collectors.toList());
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	@Override
	public void close() {
		Arrays.stream(threads).forEach(Thread::interrupt);
	}

	public ParallelMapperImpl(int threads) {
		this.threads = new Thread[threads];
	}
}
