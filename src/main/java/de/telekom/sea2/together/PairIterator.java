package de.telekom.sea2.together;

public class PairIterator<T> implements java.util.Iterator<T>{

	private Pair<T> pair;
	private boolean first = true;
	private boolean hasNext = true;
	
	
	public boolean hasNext() {
		return hasNext;
	}
	
	public T next() {
		if(first) {
			first=false;
			return pair.getFirst();
		} else {
			hasNext = false;
			return pair.getSecond();
		}		
	}
	
	PairIterator(Pair<T> pair) {
		this.pair = pair;
	}
}
