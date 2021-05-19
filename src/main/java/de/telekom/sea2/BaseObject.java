package de.telekom.sea2;

public class BaseObject {
	
	private Object parent;
	private long id;
	private static long counter=1;

	public static long getCounter() {
		return counter;
	}

	public static void setCounter(long counter) {
		BaseObject.counter = counter;
	}

	public Object getParent() {
		return parent;
	}

	public void setParent(final Object parent) {
		this.parent = parent;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long i) {
		this.id = i;
	}

	public BaseObject (){
		this.id = counter++;
	}
	
	@Override
	public String toString() {
		return String.valueOf(id);
	}
	
	//check if the Objects are of the same type
	//then check if the ids are the same
	//if both are true we assume they are equal
	//in all other cases we assume they are different
	public boolean equals(final Object obj) {
		if (obj == null) {return false;}
		if (this == obj) {return true;}
		if (obj instanceof BaseObject) {
			BaseObject bo = (BaseObject) obj;
			if (bo.getId()==this.getId()) {
				return true;
			}else {
				return false;
			}
		} else {
			return false;
		}
	}
	
}
