package org.paces.Stata.Cryptography;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public enum Algorithms {
	aes("AES"),
	aeswrap("AESWrap"),
	arcfour("ARCFOUR"),
	blowfish("Blowfish"),
	ccm("CCM"),
	des("DES"),
	desede("DESede"),
	desedewrap("DESedeWrap"),
	ecies("ECIES"),
	rc2("RC2"),
	rc4("RC4"),
	rc5("RC5"),
	rsa("RSA");

	private final String algorithm;

	private Algorithms(final String algorithm) {
		this.algorithm = algorithm;
	}

	@Override
	public String toString() {
		return this.algorithm;
	}

}
