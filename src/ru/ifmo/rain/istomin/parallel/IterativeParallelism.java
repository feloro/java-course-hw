package ru.ifmo.rain.istomin.parallel;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelism implements ListIP {

	@Override
	public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		T[] maxElems = (T[]) new Object[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().max(comparator).get();
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).max(comparator).get();
	}

	@Override
	public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		T[] maxElems = (T[]) new Object[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().min(comparator).get();
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).min(comparator).get();
	}

	@Override
	public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		Boolean[] maxElems = new Boolean[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().allMatch(predicate);
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce(true, (aBoolean, aBoolean2) -> aBoolean&&aBoolean2);
	}

	@Override
	public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		Boolean[] maxElems = new Boolean[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().anyMatch(predicate);
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce(false, (aBoolean, aBoolean2) -> aBoolean||aBoolean2);
	}

	@Override
	public String join(int threads, List<?> values) throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		String[] maxElems = new String[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().map(Object::toString).reduce("", (o, o2) -> o.toString().concat(o2.toString())).toString();
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce(String::concat).get();
	}

	@Override
	public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		List<T>[] maxElems = new List[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().filter(predicate).collect(Collectors.toList());
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	@Override
	public <T, U> List<U> map(int threads, List<? extends T> values,
	                          Function<? super T, ? extends U> f) throws InterruptedException {
		int batchSize = (int) (Math.ceil((double)values.size()/threads)+1);
		List<U>[] maxElems = new List[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			Thread thread = new Thread(()->{
				List<? extends T> sublist = values.subList(finalI * batchSize, Math.min((finalI + 1) * batchSize, values.size()));
				maxElems[finalI] = sublist.stream().map(f).collect(Collectors.toList());
			});
			thread.run();
			thread.join();
		}
		return Arrays.stream(maxElems).reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}
}
