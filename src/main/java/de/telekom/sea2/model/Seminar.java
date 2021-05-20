package de.telekom.sea2.model;

import java.util.ArrayList;

public class Seminar {
	private long id;


	private static long counter=1;
	private String name;
	private Person lecturer;
	private ArrayList<Person> participants;
	
	public Seminar(String name){
		this.id = counter++;
		this.name = name;
		participants = new ArrayList<Person>();
	}
	
	public Seminar () {		
	}
	
	public void setId(long id) {
		this.id = id;
	}

	public long getId() {
		return id;
	}
	
	public boolean add(Person p) {
		participants.add(p);
		return true;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Person getLecturer() {
		return lecturer;
	}

	public void setLecturer(Person lecturer) {
		this.lecturer = lecturer;
	}
	
	public ArrayList<Person> getParticipants() {
		return participants;
	}
	
}
