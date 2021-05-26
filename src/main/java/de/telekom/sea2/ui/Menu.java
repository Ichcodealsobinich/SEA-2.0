package de.telekom.sea2.ui;

import de.telekom.sea2.persistence.*;
import de.telekom.sea2.validation.NamePredicates;
import de.telekom.sea2.model.*;
import de.telekom.sea2.lookup.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.NoSuchElementException;

import de.telekom.sea2.*;


public class Menu extends BaseObject implements AutoCloseable{
	
	private PersonsRepository pr;
	private SeminarRepository sr;
	private String result;
	private java.util.Scanner scanner = new java.util.Scanner(System.in);
	
	//without a list, menu will not work
	//so we enforce a MyList at construction
	public Menu(PersonsRepository pr, SeminarRepository sr) {
		this.pr = pr;
		this.sr = sr;
	}
	
	//Loop for continuous user interaction
	public void show() { 
		do {
			showMenu();
			result = inputMenu();
			checkMenu(result);
		}while (!result.equals("q")); 
		System.out.println("Goodbye");
	}

	// Show options
	private void showMenu() { 
		System.out.println("******** Menu *********");
		System.out.println("* 1: Person anlegen   *");
		System.out.println("* 2: Liste anzeigen   *");
		System.out.println("* 3: Person löschen   *");
		System.out.println("* 4. Person ausgeben  *");		
		System.out.println("* 5. Liste löschen    *");
		System.out.println("* 6. Einträge zählen  *");
		System.out.println("* 7. Person suchen    *");
		System.out.println("* 8. Person updaten   *");
		System.out.println("* q: Zurück           *");
		System.out.println("***********************");
	}
	
	//read user input
	private String inputMenu() { 				
		return scanner.nextLine();
	}
	
	//evaluate user input and act accordingly
	public void checkMenu(String eingabe) { 	
		switch(eingabe) {
			case "1": 	System.out.println("Eine neue Person anlegen");			
						inputPerson();
						break;
			case "2": 	System.out.println("Liste der Personen");
			  			listAllPersons();
			  			break;
			case "3": 	System.out.println("Person löschen");
						removePerson();
						break;
			case "4":   System.out.println("Person ausgeben");
						getPerson();
						break;
			case "5":	System.out.println("Liste löschen");
						deleteAll();
						break;
			case "6":	System.out.println("Anzahl der Einträge");
						count();
						break;
			case "7":	System.out.println("Person suchen");
						search();
						break;
			case "8":	System.out.println("Person updaten");
						update();
						break;
			case "9":	System.out.println("Seminar anlegen");		
						inputSeminar();
						break;
			case "q":   break;
			default: 	System.out.println("Falsche Eingabe.");		
		}
	}
	
	private void inputSeminar() {
		Seminar s = new Seminar("Java for Dummies");
		s.add(pr.get(15));
		s.setLecturer(pr.get(16));
		sr.create(s);
	}

	//Enter new Person
	private void inputPerson() {				
		Person p = new Person();
		String firstName;
		String lastName;
		String birthdate;
		Salutation salut;
		
		System.out.println("Bitte eine Anrede eingeben");
		System.out.println("Herr, Frau, Divers / Default ist Divers");
		salut = Salutation.fromString(scanner.nextLine().trim());
		
		System.out.println("Bitte einen Vornamen eingeben");
		firstName = scanner.nextLine();
		if (!Person.isValidName(firstName)) {
			System.out.println("Der Name ist ungültig - breche ab");
			return;
		}
		
		System.out.println("Bitte einen Nachnamen eingeben");
		lastName = scanner.nextLine();
		if (!Person.isValidName(lastName)) {
			System.out.println("Der Name ist ungültig - breche ab");
			return;			
		}
		System.out.println("Bitte ein  Geburtsdatum eingeben (Format: yyyy-mm-dd)");
		birthdate = scanner.nextLine();
		try {
			LocalDate.parse(birthdate);
		} catch (DateTimeParseException e) {
			System.out.println("Ungültiges Datum. Breche ab.");
			return;	
		}
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setSalutation(salut);
		p.setBirthdate(birthdate);
		Long sId = pr.create(p);
		sr.addPersonToSeminar(pr.get(17), sId);
		
	}
	
	private void listAllPersons() {					
		try {
			ArrayList<Person> list = pr.getAll();
			System.out.println("---Die aktuelle Liste sieht so aus:---");
			if (list.size()==0) {
				System.out.println("Die Liste ist leer");
			} else {
				for (Person p : list) {
					System.out.println(p.toString());
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Das hat leider nicht geklappt");
		}
	}
	
	private void removePerson() {
		System.out.print("Bitte id eingeben");
		int id = scanner.nextInt();
		Boolean result = pr.delete(id);
		if (!result){
			System.out.println("Person konnte nicht gelöscht werden");
		}
		scanner.nextLine();
	}
	
	private void deleteAll() {
		String confirmation = "";
		Boolean result =false;
		System.out.println("Sind Sie sicher? Bitte \"yes\" eingeben");
		confirmation = scanner.nextLine().trim();
		if (confirmation.toUpperCase().equals("YES")) {
			result = pr.deleteAll();
			if (result) {
				System.out.println("Alle Einträge erfolgreich gelöscht");
			} else {
				System.out.println("Leider ist etwas schiefgegangen");
			}
		}
	}
	
	private void getPerson() {
		System.out.println("Bitte Id eingeben");
		int index = scanner.nextInt();
		try {
			Person p = pr.get(index);
			System.out.println("Index: "+ index + ", " +p.toString());
		} catch (NoSuchElementException e) {
			System.out.println("Person nicht gefunden");
		}
		scanner.nextLine();
	}
	
	private void count() {
		int i = pr.getCount();
		if (i<0) {
			System.out.println("Anzahl konnte nicht ermittelt werden");
		}else {
			System.out.println("Es gibt " + i + " Einträge.");
		}
	}
	
	private void search() {
		String firstname ="";
		String lastname = "";
		ArrayList<Person> list = new ArrayList<Person>();
		System.out.println("Bitte Vornamen eingeben oder Enter zum überspringen");
		firstname = scanner.nextLine().trim();
		System.out.println("Bitte Nachnamen eingeben");
		lastname = scanner.nextLine().trim();
		if (firstname.equals("")) {
			list = pr.find(lastname);
		}else {
			list = pr.find(firstname, lastname);
		}
		if (list.size()>0) {
			System.out.println("Es wurden " + list.size() + " Einträge gefunden:");
			for (Person person : list) {
				System.out.println(person.toString());
			}
		}else {
			System.out.println("Es wurden keine Einträge gefunden:");
		}
	}
	
	private void update() {
		int id = 0;
		System.out.println("Bitte Id eingeben");
		id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Bitte Anrede eingeben");
		Salutation salutation = Salutation.fromString(scanner.nextLine().trim());
		System.out.println("Bitte Vorname eingeben");
		String firstname = scanner.nextLine().trim();
		if (!validateName(firstname)) {
			System.out.println("Kein gültiger Vorname - breche ab");
			return;
		}
		System.out.println("Bitte Vorname eingeben");
		String lastname = scanner.nextLine().trim();
		if (!validateName(lastname)) {
			System.out.println("Kein gültiger Nachname - breche ab");
			return;
		}
		Person p = new Person();
		p.setId((long) id);
		p.setSalutation(salutation);
		p.setFirstName(firstname);
		p.setLastName(lastname);
		if (!pr.update(p)) {
			System.out.println("Das hat leider nicht geklappt");
		}else {
			System.out.println("Person mit Id " + id + " erfolgreich geändert");
		}
	}
	
	private boolean validateName(String name) {
		if (name.length()<1 || name.length()>40) {return false;}
		return name != null && name.chars().allMatch(Character::isLetter);
	}
	
	public void close() {
		this.scanner.close();
	}
}
