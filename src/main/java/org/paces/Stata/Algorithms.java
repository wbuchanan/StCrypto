package org.paces.Stata;

import java.security.Security;
import java.util.Set;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class Algorithms {

	public static Set<String> algos;

	public Algorithms() {
		algos.addAll(Security.getAlgorithms("Signature"));
		algos.addAll(Security.getAlgorithms("MessageDigest"));
		algos.addAll(Security.getAlgorithms("Cipher"));
		algos.addAll(Security.getAlgorithms("Mac"));
		algos.addAll(Security.getAlgorithms("KeyStore"));
	}

	public static Boolean checkAlgo(String algorithm) {
		return algos.contains(algorithm);
	}

}
