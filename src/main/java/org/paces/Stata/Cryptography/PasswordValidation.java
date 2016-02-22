package org.paces.Stata.Cryptography;

import java.util.*;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class PasswordValidation {

	private static final String L = "Contains Lower Case Character : \t";
	private static final String U = "\nContains Upper Case Character : \t";
	private static final String N = "\nContains Numeric Character : \t";
	private static final String S = "\nContains Special Character : \t";

	public static Boolean validPassword(char[] entry) {
		Set<Boolean> checked = new HashSet<>();
		checked.addAll(check(entry));
		if (checked.size() > 1) return false;
		else return true;
	}

	public static String invalidMessage(char[] entry) {
		List<Boolean> chkval = check(entry);
		StringBuilder msg = new StringBuilder();
		msg.append(PasswordValidation.L).append(chkval.get(0))
				.append(PasswordValidation.U).append(chkval.get(1))
				.append(PasswordValidation.N).append(chkval.get(2))
				.append(PasswordValidation.S).append(chkval.get(3));
		return msg.toString();
	}

	private static List<Boolean> check(char[] entry) {
		Boolean[] returnArray = {false, false, false, false};
		for(int i = 0; i < entry.length; i++) {
			if (Character.isLowerCase(entry[i])) returnArray[0] = true;
			if (Character.isUpperCase(entry[i])) returnArray[1] = true;
			if (Character.isDigit(entry[i])) returnArray[2] = true;
			if (!Character.isDigit(entry[i]) && !Character
					.isAlphabetic(entry[i])) returnArray[3] = true;
		}

		return Arrays.asList(returnArray);
	}

}
