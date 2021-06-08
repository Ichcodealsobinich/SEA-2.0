package de.telekom.sea2.together;

import java.util.Iterator;

public class Pair<T> extends Together<T,T> implements Iterable<T>{

	public Iterator<T> iterator() {
		return new PairIterator<T>(this);
	}
	
	public Pair(T t, T u) {
		super.setFirst(t);
		super.setSecond(u);
	}
}
