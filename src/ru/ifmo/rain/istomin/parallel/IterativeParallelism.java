package ru.ifmo.rain.istomin.parallel;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelism implements ListIP {

	@Override
	public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int endPoint = values.size();
		T[] maxElems = (T[]) new Object[threads];
		Thread[] threadsArr = new Thread[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().max(comparator).orElse(null);
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).filter(Objects::nonNull).max(comparator).orElse(null);
	}

	@Override
	public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		T[] maxElems = (T[]) new Object[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().min(comparator).orElse(null);
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).filter(Objects::nonNull).min(comparator).orElse(null);
	}

	@Override
	public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		Boolean[] maxElems = new Boolean[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().allMatch(predicate);
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).reduce(true, (aBoolean, aBoolean2) -> aBoolean&&aBoolean2);
	}

	@Override
	public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		Boolean[] maxElems = new Boolean[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().anyMatch(predicate);
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).reduce(false, (aBoolean, aBoolean2) -> aBoolean||aBoolean2);
	}

	@Override
	public String join(int threads, List<?> values) throws InterruptedException {
		String[] maxElems = new String[threads];
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<?> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads-finalI-1] = sublist.stream().map(Object::toString).collect(Collectors.joining());
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return String.join("", maxElems);
	}

	@Override
	public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		List<T>[] maxElems = new List[threads];
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().filter(predicate).collect(Collectors.toList());
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	@Override
	public <T, U> List<U> map(int threads, List<? extends T> values,
	                          Function<? super T, ? extends U> f) throws InterruptedException {
		List<U>[] maxElems = new List[threads];
		int endPoint = values.size();
		Thread[] threadsArr = new Thread[threads];
		for (int i = 0; i < threads; i++) {
			int finalI = i;
			int finalEndPoint = endPoint;
			threadsArr[i] = new Thread(()->{
				List<? extends T> sublist = values.subList(finalEndPoint - (finalEndPoint/(threads-finalI)), finalEndPoint);
				maxElems[threads - finalI - 1] = sublist.stream().map(f).collect(Collectors.toList());
			});
			endPoint -= endPoint/(threads-finalI);
			threadsArr[i].start();
		}
		for (Thread thread : threadsArr) {
			thread.join();
		}
		return Arrays.stream(maxElems).reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	public IterativeParallelism(ParallelMapper mapper) {

    }
}
