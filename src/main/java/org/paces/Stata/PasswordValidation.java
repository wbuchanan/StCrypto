package org.paces.Stata;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class PasswordValidation {

	private static final String L1 = "Contains at least 1 Lower Case Character : true";
	private static final String L0 = "Contains at least 1 Lower Case Character : false";
	private static final String U1 = "Contains at least 1 Upper Case Character : true";
	private static final String U0 = "Contains at least 1 Upper Case Character : false";
	private static final String N1 = "Contains at least 1 Numeric Character : true";
	private static final String N0 = "Contains at least 1 Numeric Character : false";
	private static final String S1 = "Contains at least 1 Non-Word Character : true";
	private static final String S0 = "Contains at least 1 Non-Word Character : false";
	private static final Pattern LOWER_CASE = Pattern.compile("\\p{Lu}");
	private static final Pattern UPPER_CASE = Pattern.compile("\\p{Ll}");
	private static final Pattern DIGIT = Pattern.compile("\\d");
	private static final Pattern SPECIAL = Pattern.compile("\\W");

	public static Boolean samePassword(char[] entry1, char[] entry2) {
		return entry1.equals(entry2);
	}

	public static Boolean validPassword(char[] entry) {
		String estr = Arrays.toString(entry);
		return 	LOWER_CASE.matcher(estr).find() && UPPER_CASE.matcher(estr).find() &&
				DIGIT.matcher(estr).find() && SPECIAL.matcher(estr).find();
	}

	public static String validPW1(char[] entry) {
		String estr = Arrays.toString(entry);
		StringJoiner msg = new StringJoiner("\n");
		if (hasLower(estr)) msg.add(L1);
		else msg.add(L0);
		if (hasUpper(estr)) msg.add(U1);
		else msg.add(U0);
		if (hasNumber(estr)) msg.add(N1);
		else msg.add(N0);
		if (hasSpecial(estr)) msg.add(S1);
		else msg.add(S0);
		return msg.toString();
	}

	private static Boolean hasLower(String entry) {
		return LOWER_CASE.matcher(entry).find();
	}

	private static Boolean hasUpper(String entry) {
		return UPPER_CASE.matcher(entry).find();
	}

	private static Boolean hasNumber(String entry) {
		return DIGIT.matcher(entry).find();
	}

	private static Boolean hasSpecial(String entry) {
		return SPECIAL.matcher(entry).find();
	}

}
