package ru.ifmo.rain.istomin.set;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ArraySet<T extends Comparable> implements NavigableSet<T> {

	private List<T> arr;
	private Comparator comparator = null;
	private Comparator originComparator = null;

	@Override
	public T lower(T t) {
		int i = 0;
		T lastOne = null;
		while (i < arr.size()) {
			if (comparator.compare(arr.get(i), t) < 0)
				lastOne = arr.get(i);
			if (comparator.compare(arr.get(i), t) >= 0 && lastOne!=null)
				return lastOne;
			i++;
		}
		return lastOne;
	}

	@Override
	public T floor(T t) {
		int i = 0;
		T lastOne = null;
		while (i < arr.size()) {
			if (comparator.compare(arr.get(i), t) <= 0)
				lastOne = arr.get(i);
			if (comparator.compare(arr.get(i), t) > 0 && lastOne!=null)
				return lastOne;
			i++;
		}
		return lastOne;
	}

	@Override
	public T ceiling(T t) {
		int i = 0;
		T lastOne = null;
		while (i < arr.size()) {
			if (comparator.compare(arr.get(i), t) >= 0 && lastOne==null)
				lastOne = arr.get(i);
			if (comparator.compare(arr.get(i), t) > 0 && lastOne!=null)
				return lastOne;
			i++;
		}
		return lastOne;
	}

	@Override
	public T higher(T t) {
		int i = 0;
		T lastOne = null;
		while (i < arr.size()) {
			if (comparator.compare(arr.get(i), t) > 0)
				lastOne = arr.get(i);
			if (comparator.compare(arr.get(i), t) >= 0 && lastOne!=null)
				return lastOne;
			i++;
		}
		return lastOne;
	}

	@Override
	public T pollFirst() {
		T result = arr.get(0);
		arr = arr.subList(1, arr.size());
		return result;
	}

	@Override
	public T pollLast() {
		T result = arr.get(arr.size()-1);
		arr = arr.subList(0, arr.size()-1);
		return result;
	}

	@Override
	public int size() {
		return arr == null?0:arr.size();
	}

	@Override
	public boolean isEmpty() {
		return arr == null || arr.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return Collections.binarySearch(arr, o, this.comparator)!=-1;
	}

	@Override
	public Iterator<T> iterator() {
		return Collections.unmodifiableList(arr).iterator();
	}

	@Override
	public Object[] toArray() {
		return arr==null?new Object[]{}:arr.toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		return arr.toArray(a);
	}

	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
//		Object[] newArr = new Object[arr.length-1];
//		int i = arr.length;
//		while (i > -1) {
//			if (!arr.get(i).equals(o))
//				newArr[i-1] = arr.get(i);
//			i--;
//		}
//		if (i==-1 || !arr.get(i).equals(o))
//			return false;
//		System.arraycopy(arr, 0, newArr, 0, i);
//		arr = newArr;
//		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
//		for (Object o: c)
//			if (!arr.contains(o)) return false;
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
//		return c.stream().map(this::add).reduce(true, (acc, it)->acc && it);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
//		return c.stream().map(this::remove).reduce(true, (acc, it)->acc && it);
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public NavigableSet<T> descendingSet() {
		arr.sort(this.comparator.reversed());
		return this;
	}

	@Override
	public Iterator<T> descendingIterator() {
		return descendingSet().iterator();
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement,
	                              boolean toInclusive) {
		int indexFrom = fromElement==null?0:arr.indexOf(fromInclusive?ceiling(fromElement):higher(fromElement));
		int indexTo = toElement==null?arr.size()-1:arr.indexOf(toInclusive?floor(toElement):lower(toElement));
		//arr = arr.subList(indexFrom, indexTo+1);
		if (indexFrom==-1)
			indexFrom = 0;
		return new ArraySet<>(arr.subList(indexFrom, indexTo+1), originComparator);
	}

	@Override
	public NavigableSet<T> headSet(T toElement, boolean inclusive) {
		return subSet(null, true, toElement, inclusive);
	}

	@Override
	public NavigableSet<T> tailSet(T fromElement, boolean inclusive) {
		return subSet(fromElement, inclusive, null , true);
	}

	@Override
	public Comparator<? super T> comparator() {
		return originComparator;
	}

	@Override
	public SortedSet<T> subSet(T fromElement, T toElement) {
		return subSet(fromElement, false, toElement, false);
	}

	@Override
	public SortedSet<T> headSet(T toElement) {
		return headSet(toElement, false);
	}

	@Override
	public SortedSet<T> tailSet(T fromElement) {
		return tailSet(fromElement, true);
	}

	@Override
	public T first() {
		return arr.get(0);
	}

	@Override
	public T last() {
		return arr.get(arr.size()-1);
	}

	private ArraySet(T[] arr) {
		this.arr = Arrays.asList(arr);
	}

	public ArraySet() {}

	public ArraySet(Collection collection, Comparator comparator) {
		this.comparator = comparator==null? (k1, k2) -> {
			if (k1 != null)
				return ((Comparable<? super T>) k1).compareTo((T) k2);
			if (k2 != null)
				return ((Comparable<? super T>) k2).compareTo((T) k1);
			return 0;
		}:comparator;

		Set temp = (new TreeSet(this.comparator));
		temp.addAll(collection);
		arr = new ArrayList<T>(temp);
		this.originComparator = comparator;
	}

	public ArraySet(Collection collection) {
		this.comparator = (k1, k2) -> {
			if (k1 != null && k2 != null)
				return ((Comparable<? super T>) k1).compareTo((T) k2);
			if (k1 != null)
				return 1;
			if (k2 != null)
				return -1;
			return 0;
		};

		Set temp = (new TreeSet(this.comparator));
		temp.addAll(collection);
		this.arr = new ArrayList<T>(temp);
	}
}
