package de.telekom.sea2.ui;

import de.telekom.sea2.persistence.*;
import de.telekom.sea2.model.*;
import de.telekom.sea2.lookup.*;

import java.util.NoSuchElementException;

import de.telekom.sea2.*;


public class Menu extends BaseObject{
	
	private PersonsRepository pr;
	private String result;
	private java.util.Scanner scanner = new java.util.Scanner(System.in);
	
	//without a list, menu will not work
	//so we enforce a MyList at construction
	public Menu(PersonsRepository pr) {
		this.pr = pr;
	}
	
	//Loop for continuous user interaction
	public void show() { 
		/* get the highest id from db so that id remains unique*/
		BaseObject.setCounter(pr.getHighestId()+1);
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
			case "q":   break;
			default: 	System.out.println("Falsche Eingabe.");		
		}
	}
	
	//Enter new Person
	private void inputPerson() {				
		Person p = new Person();
		String firstName;
		String lastName;
		Salutation salut;
		
		System.out.println("Bitte eine Anrede eingeben");
		System.out.println("Herr, Frau, Divers / Default ist Divers");
		salut = Salutation.fromString(scanner.nextLine().trim());
		
		System.out.println("Bitte einen Vornamen eingeben");
		firstName = scanner.nextLine();
		if (!validateName(firstName)) {
			System.out.println("Der Name ist ungültig - breche ab");
			return;
		}
		
		System.out.println("Bitte einen Nachnamen eingeben");
		lastName = scanner.nextLine();
		if (!validateName(lastName)) {
			System.out.println("Der Name ist ungültig - breche ab");
			return;
			
		}		
		p.setFirstName(firstName);
		p.setLastName(lastName);
		p.setSalutation(salut);
		pr.create(p);
	}
	
	private void listAllPersons() {					
		try {
			Person[] list = pr.getAll();
			System.out.println("---Die aktuelle Liste sieht so aus:---");
			if (list.length==0) {
				System.out.println("Die Liste ist leer");
			} else {
				for (int i=0;i<list.length;i++) {
					if (list[i] != null) {
						System.out.println(list[i].toString());
					}
				}
			}
		}catch (Exception e) {System.out.println("Das hat leider nicht geklappt");}
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
		System.out.println("Bitte Index eingeben");
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
	private boolean validateName(String name) {
		return name != null && name.chars().allMatch(Character::isLetter);
	}
	
	public void close() {
		this.scanner.close();
	}
}
