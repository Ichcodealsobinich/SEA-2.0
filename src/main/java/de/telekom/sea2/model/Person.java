package de.telekom.sea2.model;

import de.telekom.sea2.lookup.Salutation;
import de.telekom.sea2.BaseObject;

public class Person extends BaseObject{
	private String firstname;
	private String lastname;
	private Salutation salutation;
	
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
	
	@Override
	public String toString() {
		return "Id: " + getId() + " " + salutation + " " + firstname + " " + lastname;
	}
	
	public boolean isValidName(final String name) {
		return name != null && name.chars().allMatch(Character::isLetter);
	}
}
