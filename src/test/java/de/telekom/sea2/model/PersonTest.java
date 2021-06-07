package de.telekom.sea2.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PersonTest {

	private Person cut;
	
	@BeforeEach
	void setup() {
		cut = new Person();
	}
	
	@Test
	void setFirstname_test() {
		
		HashMap<String, String> testcases = new HashMap<String, String>();
		testcases.put("FirstNameTest","FirstNameTest" );
		//testcases.put("", "");
		//testcases.put(null, "");
		//testcases.put("FirstNameTest1", "");
		//testcases.put("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", ""); //more than 40 characters
		
		for (String testcase : testcases.keySet()) {
			//Arrange
			cut = new Person();
			cut.setFirstName(testcase);
		
			//Act
			var result = cut.getFirstname();

			//Assert
			System.out.println("Testcase: " + testcase + " - " + testcases.get(testcase));
			assertEquals(testcases.get(testcase), result);
			
			//reset
			cut=null;
		}
	}
	
	@AfterEach
	void teardown() {
		cut = null;
	}
}
