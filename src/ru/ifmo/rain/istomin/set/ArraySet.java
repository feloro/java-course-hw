package ru.ifmo.rain.istomin.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.SortedSet;

public class ArraySet<T extends Comparable> implements NavigableSet<T> {

	private Object[] arr;

	@Override
	public T lower(T t) {
		int i = 0;
		while (i < arr.length) {
			if (t.compareTo(arr[i]) < 0)
				return (T) arr[i];
			i++;
		}
		return null;
	}

	@Override
	public T floor(T t) {
		int i = 0;
		while (i < arr.length) {
			if (t.compareTo(arr[i]) <= 0)
				return (T) arr[i];
			i++;
		}
		return null;
	}

	@Override
	public T ceiling(T t) {
		int i = arr.length;
		while (i > -1) {
			if (t.compareTo(arr[i]) >= 0)
				return (T) arr[i];
			i--;
		}
		return null;
	}

	@Override
	public T higher(T t) {
		int i = arr.length;
		while (i > -1) {
			if (t.compareTo(arr[i]) > 0)
				return (T) arr[i];
			i--;
		}
		return null;
	}

	@Override
	public T pollFirst() {
		T result = (T) arr[0];
		arr = Arrays.copyOfRange(arr, 1, arr.length);
		return result;
	}

	@Override
	public T pollLast() {
		T result = (T) arr[arr.length-1];
		arr = Arrays.copyOfRange(arr, 0, arr.length-1);
		return result;
	}

	@Override
	public int size() {
		return arr.length;
	}

	@Override
	public boolean isEmpty() {
		return arr.length==0;
	}

	@Override
	public boolean contains(Object o) {
		for (Object elem: arr) {
			if (elem.equals(o))
				return true;
		}
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
		arr = new Object[0];
		return false;
	}

	@Override
	public void clear() {
		arr = new Object[0];
	}

	@Override
	public NavigableSet<T> descendingSet() {
		for (int i=0; i < arr.length/2; i++) {
			Object temp = arr[i];
			arr[i] = arr[arr.length-i];
			arr[arr.length-i] = temp;
		}
		return this;
	}

	@Override
	public Iterator<T> descendingIterator() {
		return this.iterator();
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
		return (T) arr[0];
	}

	@Override
	public T last() {
		return (T) arr[arr.length-1];
	}

	private T[] getSubset(int from, int to) {
		return (T[]) Arrays.copyOfRange(arr, from, to);
	}

	public ArraySet() {
		//arr = new T[]();
	}
}
