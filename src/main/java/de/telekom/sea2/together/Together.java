package de.telekom.sea2.together;

public class Together <T, U>{
	private T first;
	private U second;
	
	public Together(T first, U second) {
		this.first=first;
		this.second=second;
	}
	
	public Together() {
		
	}
	
	public boolean join (T first, U second) {		
		if (first != null && second != null) {
			this.first=first;
			this.second=second;
			return true;
		}
		return false;
	}
	
	public void split() {
		this.first=null;
		this.second=null;
	}
	
	public boolean equals(Together<T, U> t) {
		//this.first.getClass()==t.getFirst().getClass();
		return (this.first==t.getFirst() && this.second==t.getSecond());
	}
	
	public int hashCode() {
		return this.first.hashCode() + this.second.hashCode();
	}
	
	public String toString() {
		return String.format("%s + %s",this.first.toString(), this.second.toString()); 
	}
	
	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public U getSecond() {
		return second;
	}

	public void setSecond(U second) {
		this.second = second;
	}
	
	public boolean isHomogeneous() {
		return (first.getClass()==second.getClass());
	}
	
	
}
