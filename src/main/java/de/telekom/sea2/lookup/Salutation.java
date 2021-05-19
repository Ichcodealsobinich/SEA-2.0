package de.telekom.sea2.lookup;

public enum Salutation {
	HERR,
	FRAU,
	DIVERS;
	
	public static Salutation fromString(String s) {
		switch (s.toUpperCase()) {
			case "M":
			case "HERR": 
			case "MISTER":
				return HERR;
			case "F":
			case "FRAU": 	
			case "MISS":
					return FRAU;
			default: 		
					return DIVERS;
		}
	}
	
	@Override
	public String toString() {
		switch (this) {
		case HERR: 		return "Herr";
		case FRAU: 		return "Frau";
		case DIVERS: 	
		default:		return "keine Anrede";
		}
	}
	
	public byte toByte() {
		switch (this) {
		case HERR: 		return 1;
		case FRAU: 		return 2;
		case DIVERS: 	
		default:		return 3;
		}
	}
}
