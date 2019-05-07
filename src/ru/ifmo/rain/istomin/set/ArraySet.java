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

	private int lowerIndex(T e) {
		int index = Collections.binarySearch(this.arr, e, this.comparator);
		if (index > 0) {
			return index - 1;
		}

		if (index < -1) {
			return -(index + 1) - 1;
		}

		return -1;
	}

	@Override
	public T lower(T e) {
		int index = this.lowerIndex(e);
		if (index >= 0) {
			return this.arr.get(index);
		}
		return null;
	}

	private int floorIndex(T e) {
		int index = Collections.binarySearch(this.arr, e, this.comparator);
		if (index >= 0) {
			return index;
		}

		if (index < -1) {
			return -(index + 1) - 1;
		}

		return -1;
	}

	@Override
	public T floor(T e) {
		int index = floorIndex(e);

		if (index >= 0) {
			return this.arr.get(index);
		}

		return null;
	}

	private int ceilingIndex(T e) {
		int index = Collections.binarySearch(this.arr, e, this.comparator);
		if (index >= 0) {
			return index;
		}

		if (-this.arr.size() - 1 < index && index < 0) {
			return -(index + 1);
		}

		return -1;
	}

	@Override
	public T ceiling(T e) {
		int index = this.ceilingIndex(e);

		if (index >= 0) {
			return this.arr.get(index);
		}
		return null;
	}

	private int higherIndex(T e) {
		int index = Collections.binarySearch(this.arr, e, this.comparator);
		if (index >= 0 && index < this.arr.size() - 1) {
			return index + 1;
		}

		if (-this.arr.size() - 1 < index && index < 0) {
			return -(index + 1);
		}

		return -1;
	}

	@Override
	public T higher(T e) {
		int index = this.higherIndex(e);
		if (index >= 0) {
			return this.arr.get(index);
		}
		return null;
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
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!this.contains(o)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
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
		int indexFrom = fromElement==null?0:fromInclusive?ceilingIndex(fromElement):higherIndex(fromElement);

		if (indexFrom < 0) {
			return new ArraySet<>(new ArrayList<>(), this.originComparator);
		}

		int indexTo = toElement==null?arr.size()-1:toInclusive?floorIndex(toElement):lowerIndex(toElement);

		if (indexTo < 0 || indexFrom > indexTo) {
			return new ArraySet<>(new ArrayList<>(), this.comparator);
		}

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
