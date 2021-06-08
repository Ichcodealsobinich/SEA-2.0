package de.telekom.sea2.together;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class Together_Test {
	private Together<String, String> cut;
	
	@BeforeEach
	void setup() {
		cut = new Together<String, String>("Paul", "Paula");
	}
	
	@Test
	void equalsTest() {
		//Arrange
		Together<String, String> comparator = new Together<String, String>("Paul", "Paula");
		
		//Act
		
		//Assert
		assertTrue(cut.equals(comparator));
	}
	
	@Test
	void toStringTest() {
		//Arrange
		
		//Act
		
		//Assert
		assertEquals(cut.toString(), "Paul + Paula");
	}
	
	@Test
	void hashCodeTest() {
		//Arrange
		Together<String, String> comparator = new Together<String, String>("Paula", "Paula");
		
		//Act
		
		//Assert
		assertNotEquals(cut.hashCode(),comparator.hashCode());
	}
	
	@Test
	void HomogeneousTest() {
		//Arrange
		
		//Act
		
		//Assert
		assertTrue(cut.isHomogeneous());
	}
	
	@Test
	void joinTest() {
		//Arrange
		Together<String, String> t = new Together<String,String> ();
		//Act
		t.setFirst("Paul");
		t.setSecond("Paula");
		
		//Assert
		assertEquals("Paul", t.getFirst());
		assertEquals("Paula", t.getSecond());
	}
	
	@Test
	void splitTest() {
		//Arrange

		//Act
		cut.split();
		
		//Assert
		assertNull(cut.getFirst());
		assertNull(cut.getSecond());
	}
	
	@Test
	void setTest() {
		//Arrange
		Object obj = new Object();
		Object obj2 = new Object();
		Together<Object, Object> t = new Together<Object, Object>();
		//Act
		t.setFirst(obj);
		t.setSecond(obj2);
		
		//Assert
		assertEquals(obj, t.getFirst());
		assertEquals(obj2, t.getSecond());
	}
	
	@AfterEach
	void teardown() {
		cut = null;
	}
}
