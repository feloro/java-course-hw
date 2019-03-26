package ru.ifmo.rain.istomin.set;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableSet;
import java.util.SortedSet;

public class ArraySet<T extends Comparable> implements NavigableSet<T> {

	T[] arr;

	@Override
	public T lower(T t) {
		int i = 0;
		while (i < arr.length) {
			if (t.compareTo(arr[i]) < 0)
				return arr[i];
			i++;
		}
		return null;
	}

	@Override
	public T floor(T t) {
		int i = 0;
		while (i < arr.length) {
			if (t.compareTo(arr[i]) <= 0)
				return arr[i];
			i++;
		}
		return null;
	}

	@Override
	public T ceiling(T t) {
		return null;
	}

	@Override
	public T higher(T t) {
		return null;
	}

	@Override
	public T pollFirst() {
		return null;
	}

	@Override
	public T pollLast() {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean contains(Object o) {
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		return null;
	}

	@Override
	public Object[] toArray() {
		return new Object[0];
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		return null;
	}

	@Override
	public boolean add(T t) {
		return false;
	}

	@Override
	public boolean remove(Object o) {
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return false;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return false;
	}

	@Override
	public void clear() {

	}

	@Override
	public NavigableSet<T> descendingSet() {
		return null;
	}

	@Override
	public Iterator<T> descendingIterator() {
		return null;
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement,
	                              boolean toInclusive) {
		return null;
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return null;
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return null;
	}

	@Override
	public Comparator<? super T> comparator() {
		return null;
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return null;
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return null;
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return new ArraySet<>(subSet());
	}

	@Override
	public T first() {
		return arr[0];
	}

	@Override
	public T last() {
		return arr[arr.length-1];
	}

	private T[] getSubset(int from, int to) {
		return Arrays.copyOfRange(arr, from, to);
	}
}
