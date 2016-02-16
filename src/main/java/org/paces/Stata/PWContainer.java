package org.paces.Stata;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class PWContainer {

	private char[] initPassword;
	private char[] confirmPassword;
	private char[] validPassword;

	public PWContainer() {
	}

	public void setInitPassword(char[] initial) {
		this.initPassword = initial;
	}

	public void setConfPassword(char[] confirmation) {
		this.confirmPassword = confirmation;
	}

	public void setValidPassword(char[] pwstring) {
		this.validPassword = pwstring;
	}

	public char[] getValidPassword() {
		return this.validPassword;
	}

}
