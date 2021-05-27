package de.telekom.sea2.validation;

/* Unicode is weird */ 
public class NamePredicates {
	
	@Deprecated
	public static boolean isLetter(char c) {
		int cCode = Character.getNumericValue(c);
		System.out.println(cCode);
		
		return false;
	}
	
	@Deprecated
	public static boolean isLetter(int cCode) {
		if (cCode > 64 && cCode < 91) {return true;}
		if (cCode > 96 && cCode < 123) {return true;}
		
		return false;
	}
	
	@Deprecated
	public static boolean isLetterOrWhitespace(int cCode) {
		if (cCode > 64 && cCode < 91) {return true;} //uppercase letters
		if (cCode > 96 && cCode < 123) {return true;}//lowercase letters
		if (cCode == 32) {return true;} //whitespace
		return false;
	}
	
	public static boolean isAllowedInNames(int cCode) {
		if (Character.isLetter(cCode)) {return true;}
		if (Character.isWhitespace(cCode)) {return true;}
		return false;
	}

}
