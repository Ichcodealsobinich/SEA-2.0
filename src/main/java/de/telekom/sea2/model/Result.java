package de.telekom.sea2.model;

import java.util.function.Consumer;

public class Result <T> {
	private T data;

	public static final Result none = new Result();

	public T unwrap() {
		return data;
	}
	
	public Result(T data) {
		this.data = data;
	}
	
	public boolean isEmpty() {
		return (data==null);
	}
	
	private Result(){
		this.data=null;
	}
	
	public void ifNotEmpty(Consumer action) {
		if (!isEmpty()) {
			action.accept(data);
		}
	}
	
	
	
	
}
