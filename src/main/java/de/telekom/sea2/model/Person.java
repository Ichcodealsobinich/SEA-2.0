package de.telekom.sea2.model;

import de.telekom.sea2.lookup.Salutation;
import de.telekom.sea2.BaseObject;

/**
 * Represents basic information about a Person.
 * With id, firstname, lastname and salutation.
 * @author sea4
 *
 */
public class Person {
	
	private Long id;
	private String firstname;
	private String lastname;
	private Salutation salutation;
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public Salutation getSalutation() {
		return salutation;
	}
	
	public void setSalutation(Salutation anrede) {
		this.salutation = anrede;
	}
	public void setSalutation(byte b) {
		this.salutation = Salutation.fromByte(b);
	}
	public void setFirstName(final String vn) {
		if (this.isValidName(vn)) {this.firstname = vn;}
	}
	public void setLastName(final String nn) {
		if (this.isValidName(nn)) {this.lastname=nn;}
	}

	public String getFirstname() {
		return this.firstname;
	}
	public String getLastname() {
		return this.lastname;
	}

	//constructor
	public Person() {
		this.setFirstName("");
		this.setLastName("");
		this.setSalutation(Salutation.OTHER);
	}
	//constructor with parameters
	public Person(final String vn, final String nn, final String salutation) {
		if (this.isValidName(vn)) {this.setFirstName(vn);}
		else {this.setFirstName("");}
		if (this.isValidName(nn)) {this.setLastName(nn);}
		else {this.setLastName("");}
		this.salutation = Salutation.fromString(salutation);
	}

	public boolean equals(final Person p) {
		return  	(super.equals(p)) && 
					(this.getSalutation()==p.getSalutation()) &&	
					(this.getFirstname()==p.getFirstname()) && 
					(this.getLastname()==p.getLastname());
	}
	
	public Person clone() {
		Person clone = new Person();
		clone.setFirstName(this.getFirstname());
		clone.setLastName(this.getLastname());
		clone.setSalutation(this.getSalutation());
		return clone;
	}
	
	/**
	 * Returns id, salutation, firstname and lastname in a neatly formatted string
	 */
	@Override
	public String toString() {
		return "Id: " + id + " " + salutation + " " + firstname + " " + lastname;
	}
	
	/** 
	 * Validate a name. Returns true, if the name consists only of letters.
	 * @param name to be validated. Can be firstname or lastame.
	 * @return True, if all characters in the name are letters. Otherwise false.
	 */
	public boolean isValidName(final String name) {
		if (name.length()<1 || name.length()>40) {return false;}
		return name != null && name.chars().allMatch(Character::isLetter);
	}
}
