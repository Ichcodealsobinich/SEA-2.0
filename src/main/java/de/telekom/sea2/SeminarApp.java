package de.telekom.sea2;

import java.sql.*;
import de.telekom.sea2.persistence.*;
import de.telekom.sea2.ui.*;

public class SeminarApp {
	
	public void run(String[] args) {
		/*Not sure what this is for, but apparently it is needed*/ 
		try {
			final String DRIVER = "org.mariadb.jdbc.Driver";
			Class.forName(DRIVER);
		} catch (Exception e) {e.printStackTrace();}
		
		/*establish database connection*/
		try (Connection connection = DriverManager
				.getConnection("jdbc:mariadb://localhost:3306/seadb?user=seauser&password=seapass");){
			
			PersonsRepository pr = new PersonsRepository(connection);
			SeminarRepository sr = new SeminarRepository(connection,pr);
			Menu menu = new Menu(pr,sr);
			menu.show();
		}catch (Exception e) {
			System.out.println("Could not establish database connection. Shutting down.");
			e.printStackTrace();
		}		
	}

}
