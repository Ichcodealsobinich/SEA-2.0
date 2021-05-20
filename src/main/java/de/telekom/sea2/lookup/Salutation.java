package de.telekom.sea2.lookup;

public enum Salutation {
	MR,
	MRS,
	OTHER;
	
	public static Salutation fromString(String s) {
		switch (s.toUpperCase()) {
			case "M":
			case "MR":
			case "HERR": 
			case "MISTER":
				return MR;
			case "F":
			case "FRAU": 	
			case "MISS":
			case "MRS":
					return MRS;
			default: 		
					return OTHER;
		}
	}
	
	@Override
	public String toString() {
		switch (this) {
		case MR: 		return "Herr";
		case MRS: 		return "Frau";
		case OTHER: 	
		default:		return "Person";
		}
	}
	
	public byte toByte() {
		switch (this) {
			case MR: 		return 1;
			case MRS: 		return 2;
			case OTHER: 	
			default:		return 3;
		}
	}
	
	public static Salutation fromByte(Byte b) {
		switch (b) {
			case 1: 		return MR;
			case 2: 		return MRS;
			case 3: 	
			default:		return OTHER;
		}
	}
}
