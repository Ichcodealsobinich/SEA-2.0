package de.telekom.sea2.model;

import de.telekom.sea2.lookup.Salutation;
import de.telekom.sea2.BaseObject;

public class Person extends BaseObject{
	private String vorname;
	private String nachname;
	private Salutation anrede;
	
	public Salutation getAnrede() {
		return anrede;
	}
	public void setAnrede(Salutation anrede) {
		this.anrede = anrede;
	}
	public void setAnrede(byte b) {
		switch (b) {
		case 1: 		this.anrede=Salutation.HERR;
						break;
		case 2: 		this.anrede=Salutation.FRAU;
						break;
		case 3: 	
		default:		this.anrede=Salutation.DIVERS;
		}
	}
	public void setVorname(final String vn) {
		if (this.isValidName(vn)) {this.vorname = vn;}
	}
	public void setNachname(final String nn) {
		if (this.isValidName(nn)) {this.nachname=nn;}
	}

	public String getVorname() {
		return this.vorname;
	}
	public String getNachname() {
		return this.nachname;
	}

	//constructor
	public Person() {
		this.setVorname("");
		this.setNachname("");
		this.setAnrede(Salutation.DIVERS);
	}
	//constructor with parameters
	public Person(final String vn, final String nn, final String salutation) {
		if (this.isValidName(vn)) {this.setVorname(vn);}
		else {this.setVorname("");}
		if (this.isValidName(nn)) {this.setNachname(nn);}
		else {this.setNachname("");}
		this.anrede = Salutation.fromString(salutation);
	}

	public boolean equals(final Person p) {
		return  	(super.equals(p)) && 
					(this.getAnrede()==p.getAnrede()) &&	
					(this.getVorname()==p.getVorname()) && 
					(this.getNachname()==p.getNachname());
	}
	
	public Person clone() {
		Person clone = new Person();
		clone.setVorname(this.getVorname());
		clone.setNachname(this.getNachname());
		clone.setAnrede(this.getAnrede());
		return clone;
	}
	
	@Override
	public String toString() {
		return "Id: " + getId() + " " + anrede + " " + vorname + " " + nachname;
	}
	
	public boolean isValidName(final String name) {

		if (name.contains("\\"))return false;
		if (name.contains("/")) return false;
		if (name.contains("\""))return false;
		if (name.contains("§")) return false;
		if (name.contains("$")) return false;
		if (name.contains("!")) return false;
		if (name.contains("&")) return false;
		if (name.contains("(")) return false;
		if (name.contains(")")) return false;
		if (name.contains("[")) return false;
		if (name.contains("]")) return false;
		if (name.contains("{")) return false;
		if (name.contains("}")) return false;
		if (name.contains("+")) return false;
		if (name.contains("#")) return false;
		if (name.contains(",")) return false;
		if (name.contains(";")) return false;
		if (name.contains(".")) return false;
		if (name.contains(":")) return false;
		if (name.contains("<")) return false;
		if (name.contains(">")) return false;	
		return true;
	}
}
