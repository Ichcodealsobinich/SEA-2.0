package de.telekom.sea2.model;

import de.telekom.sea2.validation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;

import de.telekom.sea2.lookup.Salutation;

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
	private LocalDate birthdate;
	


	public LocalDate getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(LocalDate birthdate) {
		if (birthdate == null) {
			this.birthdate=LocalDate.parse("1900-01-01");
		} else {
			this.birthdate = birthdate;
		}
	}

	public void setBirthdate(Date birthdate) {
		if (birthdate == null) {
			this.birthdate=LocalDate.parse("1900-01-01");
		} else {
			this.birthdate = birthdate.toLocalDate();
		} 
	}
	
	public void setBirthdate(String birthdate) {
		try {
			this.birthdate=LocalDate.parse(birthdate);
		} catch (Exception e) {
			this.birthdate = LocalDate.parse("1900-01-01");
		} 
	}

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
		if (Person.isValidName(vn)) {this.firstname = vn;}
	}
	public void setLastName(final String nn) {
		if (Person.isValidName(nn)) {this.lastname=nn;}
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
		this.setBirthdate(LocalDate.parse("1900-01-01"));
	}
	//constructor with parameters
	public Person(final String vn, final String nn, final String salutation) {
		if (Person.isValidName(vn)) {this.setFirstName(vn);}
		else {this.setFirstName("");}
		if (Person.isValidName(nn)) {this.setLastName(nn);}
		else {this.setLastName("");}
		this.salutation = Salutation.fromString(salutation);
	}

	public boolean equals(final Person p) {
		return  	(super.equals(p)) && 
					(this.getSalutation()==p.getSalutation()) &&	
					(this.getFirstname().equals(p.getFirstname())) && 
					(this.getLastname().equals(p.getLastname())) &&
					(this.getBirthdate().equals(p.getBirthdate()));
	}
	
	public Person clone() {
		Person clone = new Person();
		clone.setFirstName(this.getFirstname());
		clone.setLastName(this.getLastname());
		clone.setSalutation(this.getSalutation());
		clone.setBirthdate(this.getBirthdate());
		return clone;
	}
	
	/**
	 * Returns id, salutation, firstname, lastname and age in a neatly formatted string
	 */
	@Override
	public String toString() {
		String ret = String.format("Id: %s %s %s %s, %s", 
				id, 
				salutation, 
				firstname, 
				lastname, 
				Period.between(birthdate,  LocalDate.now()).getYears());
		return ret;
	}
	
	/** 
	 * Validate a name. Returns true, if the name consists only of letters.
	 * @param name to be validated. Can be firstname or lastame.
	 * @return True, if all characters in the name are letters. Otherwise false.
	 */
	public static boolean isValidName(final String name) {
		if (name.length()<1 || name.length()>40) {return false;}
		//return name != null && name.chars().allMatch(Character::isLetter);
		return name != null && name.chars().allMatch(NamePredicates::isAllowedInNames);		
	}
}
