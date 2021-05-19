package de.telekom.sea2.lookup;

public enum Salutation {
	MR,
	MISS,
	OTHER;
	
	public static Salutation fromString(String s) {
		switch (s.toUpperCase()) {
			case "M":
			case "HERR": 
			case "MISTER":
				return MR;
			case "F":
			case "FRAU": 	
			case "MISS":
					return MISS;
			default: 		
					return OTHER;
		}
	}
	
	@Override
	public String toString() {
		switch (this) {
		case MR: 		return "Herr";
		case MISS: 		return "Frau";
		case OTHER: 	
		default:		return "Person";
		}
	}
	
	public byte toByte() {
		switch (this) {
			case MR: 		return 1;
			case MISS: 		return 2;
			case OTHER: 	
			default:		return 3;
		}
	}
	
	public static Salutation fromByte(Byte b) {
		switch (b) {
			case 1: 		return MR;
			case 2: 		return MISS;
			case 3: 	
			default:		return OTHER;
		}
	}
}
