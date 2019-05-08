package ru.ifmo.rain.istomin.parallel;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IterativeParallelismV2 implements ListIP {

	ParallelMapper mapper;

	@Override
	public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, T> f = (List<? extends T> sublist) -> ((List<? extends T>) sublist).stream().max(comparator).orElse(null);

		List<? extends T> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().filter(Objects::nonNull).max(comparator).orElse(null);
	}

	@Override
	public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator)
			throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, T> f = (List<? extends T> sublist) -> ((List<? extends T>) sublist).stream().min(comparator).orElse(null);

		List<? extends T> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().filter(Objects::nonNull).min(comparator).orElse(null);
	}

	@Override
	public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, Boolean> f = (List<? extends T> sublist) -> ((List<? extends T>) sublist).stream().allMatch(predicate);

		List<Boolean> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().reduce(true, (aBoolean, aBoolean2) -> aBoolean&&aBoolean2);
	}

	@Override
	public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, Boolean> f = (List<? extends T> sublist) -> ((List<? extends T>) sublist).stream().anyMatch(predicate);
		List<Boolean> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().reduce(false, (aBoolean, aBoolean2) -> aBoolean||aBoolean2);
	}

	@Override
	public String join(int threads, List<?> values) throws InterruptedException {
		int endPoint = values.size();
		List<List<?>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}


		Function<List<?>, String> f = (List<?> sublist) -> ((List<?>) sublist).stream().map(Object::toString).collect(Collectors.joining());
		List<String> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);

		return String.join("", maxElemsList);
	}

	@Override
	public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate)
			throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, List<T>> f = (List<? extends T> sublist) -> sublist.stream().filter(predicate).collect(Collectors.toList());
		List<List<T>> maxElemsList = mapper.map(f, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	@Override
	public <T, U> List<U> map(int threads, List<? extends T> values,
	                          Function<? super T, ? extends U> f) throws InterruptedException {
		int endPoint = values.size();
		List<List<? extends T>> args = new ArrayList<>();
		for (int i = 0; i < threads; i++) {
			args.add(values.subList(endPoint - (endPoint/(threads-i)), endPoint));
			endPoint -= endPoint/(threads-i);
		}

		Function<List<? extends T>, List<U>> localf = (List<? extends T> sublist) -> sublist.stream().map(f).collect(Collectors.toList());
		List<List<U>> maxElemsList = mapper.map(localf, args);
		Collections.reverse(maxElemsList);
		return maxElemsList.stream().reduce((ts, ts2) -> {ts.addAll(ts2); return ts;}).get();
	}

	public IterativeParallelismV2() {
	}

	public IterativeParallelismV2(ParallelMapper mapper) {
		this.mapper = mapper;
    }
}
