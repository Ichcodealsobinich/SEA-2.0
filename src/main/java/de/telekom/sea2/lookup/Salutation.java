package de.telekom.sea2.lookup;

/**
 * This enum is a representation of allowed salutations
 * and offers some conversion methods for convenience
 * @author sea4
 *
 */
public enum Salutation {
	MR,
	MRS,
	OTHER;
	
	/** 
	 * Convert an arbitrary string to a salutation.
	 * Defaults to OTHER
	 */
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
	
	/**
	 * Returns "Herr", "Frau", or "Person"
	 * in case of MR, MRS or OTHER
	 */
	@Override
	public String toString() {
		switch (this) {
		case MR: 		return "Herr";
		case MRS: 		return "Frau";
		case OTHER: 	
		default:		return "Person";
		}
	}
	
	/**
	 * Convert to Byte for more efficient storing
	 * in a database
	 * @return 1,2 or 3
	 */
	public byte toByte() {
		switch (this) {
			case MR: 		return 1;
			case MRS: 		return 2;
			case OTHER: 	
			default:		return 3;
		}
	}
	
	/**
	 * If the salutation was saved in a database as byte
	 * this is a convenient method to convert back to salutation
	 * @param b Should be 1,2, or 3. Otherwise OTHER will be returned
	 * @return Returns the corresponding salutation
	 */
	public static Salutation fromByte(Byte b) {
		switch (b) {
			case 1: 		return MR;
			case 2: 		return MRS;
			case 3: 	
			default:		return OTHER;
		}
	}
}
