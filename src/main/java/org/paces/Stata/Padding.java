package org.paces.Stata.Cryptography;

/**
 * Created by billy on 2/20/16.
 */
public enum Padding {
	no("NoPadding"),
	iso("ISO10126Padding"),
	oaep("OAEPPadding"),
	pkcs1("PKCS1Padding"),
	pkcs5("PKCS5Padding"),
	ssl("SSL3Padding");

	private final String padding;

	private Padding(final String padding) {
		this.padding = padding;
	}

	@Override
	public String toString() {
		return this.padding;
	}

}
