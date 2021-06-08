package de.telekom.sea2.together;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PairTest {
	
	private Pair<String> cut;
	
	@BeforeEach
	void setup() {
		cut = new Pair<String>("Paul","Paul");
	}
	
	@Test
	void iterTest() {
		//Arrange
		
		//Act

		//Assert
		for (String elem : cut) {
			assertEquals(elem,"Paul");
		}
	}
	
	@AfterEach
	void teardown() {
		cut = null;
	}
}
