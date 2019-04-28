package ru.ifmo.rain.istomin.set;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NavigableSet;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class ArraySet<T extends Comparable> implements NavigableSet<T> {

	private Object[] arr;
	private Comparator comparator = null;

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
		return Arrays.asList((T[]) arr).iterator();
	}

	@Override
	public Object[] toArray() {
		return Arrays.copyOf(arr, arr.length);
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		return (T1[]) Arrays.copyOf(arr, arr.length, a.getClass());
	}

	@Override
	public boolean add(T t) {
		Object[] newArr = new Object[arr.length+1];
		int i = arr.length-1;
		while (i > -1) {
			if (comparator().compare(t, (T)arr[i]) > 0)
				newArr[i+1] = arr[i];
			i--;
		}
		if (i==-1 || arr[i].equals(t))
			return false;
		arr[i] = t;
		System.arraycopy(arr, 0, newArr, 0, i);
		arr = newArr;
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Object[] newArr = new Object[arr.length-1];
		int i = arr.length;
		while (i > -1) {
			if (!arr[i].equals(o))
				newArr[i-1] = arr[i];
			i--;
		}
		if (i==-1 || !arr[i].equals(o))
			return false;
		System.arraycopy(arr, 0, newArr, 0, i);
		arr = newArr;
		return true;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return c.stream().filter(this::contains).collect(Collectors.toList()).isEmpty();
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return c.stream().map(this::add).reduce(true, (acc, it)->acc && it);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		arr = Arrays.stream(arr).filter(c::contains).toArray();
		return false;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return c.stream().map(this::remove).reduce(true, (acc, it)->acc && it);
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
		List arrList = Arrays.asList(arr);
		arrList.sort(comparator().reversed());
		return arrList.iterator();
	}

	@Override
	public NavigableSet<T> subSet(T fromElement, boolean fromInclusive, T toElement,
	                              boolean toInclusive) {
		int i = 0, j = arr.length-1;
		boolean from = false, to = false;
		Comparator comparator = comparator();
		while (!from && !to && i <= j) {
			if (toElement!= null && comparator.compare(arr[i], fromElement) <= 0  && (!fromInclusive || !fromElement.equals(arr[i])))
				i++;
			else from = true;
			if (toElement!= null && comparator.compare(arr[i], toElement) >= 0  && (!toInclusive || !toElement.equals(arr[i])))
				j--;
			else to = true;
		}
		if (i>j) return null;
		T[] newArr = (T[]) new Object[j-i+1];
		System.arraycopy(arr, i, newArr, 0, j-i);
		return new ArraySet<>(newArr);
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
		if (comparator == null) {
			comparator = (k1, k2) ->  ((Comparable<? super T>)k1).compareTo((T)k2);
		}
		return comparator;
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
		return tailSet(fromElement, false);
	}

	@Override
	public T first() {
		return (T) arr[0];
	}

	@Override
	public T last() {
		return (T) arr[arr.length-1];
	}

	private ArraySet(T[] arr) {
		this.arr = arr;
	}

	public ArraySet() {}
}
