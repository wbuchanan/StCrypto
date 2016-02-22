package org.paces.Stata.Cryptography;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

/**
 * This class is based - in part - on the example Gist provided at:
 * https://gist.github.com/dweymouth/11089238
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class CryptoKey {

	private static final String DEFAULT_ALGORITHM_TYPE = "AES";
	private static final String DEFAULT_MODE_TYPE = "CBC";
	private static final String DEFAULT_PADDING_TYPE = "PKCS5Padding";
	private static final String DEFAULT_KEYGEN = "PBKDF2WithHmacSHA1";
	private static String ALGORITHM_TYPE;
	private static String MODE_TYPE;
	private static String PADDING_TYPE;
	private static String CIPHER_TYPE;
	private static String KEYGEN;
	private static final Integer DEFAULT_KEY_LENGTH = 256;
	protected static final Integer DEFAULT_SALT_LENGTH = 16;
	protected static final Integer DEFAULT_AUTH_LENGTH = 8;
	private static final Integer DEFAULT_ITERATIONS = 1000;
	private static Cipher encrypt;

	public CryptoKey() throws NoSuchPaddingException, NoSuchAlgorithmException {
		ALGORITHM_TYPE = DEFAULT_ALGORITHM_TYPE;
		MODE_TYPE = DEFAULT_MODE_TYPE;
		PADDING_TYPE = DEFAULT_PADDING_TYPE;
		StringJoiner x = new StringJoiner("/");
		CIPHER_TYPE = x.add(ALGORITHM_TYPE).add(MODE_TYPE).add(PADDING_TYPE).toString();
		KEYGEN = DEFAULT_KEYGEN;
		encrypt = Cipher.getInstance(CIPHER_TYPE);
	}

	public CryptoKey(String keyType, Algorithms algo, Modes mode, Padding pad) throws NoSuchPaddingException, NoSuchAlgorithmException {
		ALGORITHM_TYPE = algo.toString();
		MODE_TYPE = mode.toString();
		PADDING_TYPE = pad.toString();
		StringJoiner x = new StringJoiner("/");
		CIPHER_TYPE =x.add(ALGORITHM_TYPE).add(MODE_TYPE).add(PADDING_TYPE).toString();
		KEYGEN = keyType;
		encrypt = Cipher.getInstance(CIPHER_TYPE);
	}

	public CryptoKey(String keyType, String cipherType) throws NoSuchPaddingException, NoSuchAlgorithmException {
		CIPHER_TYPE = cipherType;
		KEYGEN = keyType;
		encrypt = Cipher.getInstance(CIPHER_TYPE);
	}

	public Cipher getCipher() {
		return encrypt;
	}

	protected byte[] saltShaker() throws NoSuchAlgorithmException {
		return saltShaker(CryptoKey.DEFAULT_SALT_LENGTH);
	}

	/**
	 * Makes the salt value
	 * @param length Parameter used to specify the length of the salt element
	 *                  to generate
	 * @return an array of byte values
	 * @throws NoSuchAlgorithmException
	 */
	protected byte[] saltShaker(Integer length) throws
			NoSuchAlgorithmException {
		return SecureRandom.getInstanceStrong().generateSeed(length);
	}

	protected List<SecretKey> keyMaker(char[] pw) throws NoSuchAlgorithmException {
		return keyMaker(DEFAULT_KEY_LENGTH, pw, saltShaker(), DEFAULT_ITERATIONS);
	}

	protected List<SecretKey> keyMaker(Integer length, char[] pw) throws NoSuchAlgorithmException {
		return keyMaker(length, pw, saltShaker(), DEFAULT_ITERATIONS);
	}

	protected List<SecretKey> keyMaker(Integer length, char[] pw,
											  byte[] salt) {
		return keyMaker(length, pw, salt, DEFAULT_ITERATIONS);
	}

	protected List<SecretKey> keyMaker(
		Integer length, char[] pw, byte[] salt, Integer iterations) {
		List<SecretKey> ret = new ArrayList<>();
		try {
			SecretKeyFactory f = SecretKeyFactory.getInstance(KEYGEN);
			KeySpec specification = new PBEKeySpec(pw, salt, iterations,
					length + DEFAULT_AUTH_LENGTH * 8);
			SecretKey tmp;
			try {
				tmp = f.generateSecret(specification);
				ret.add(new SecretKeySpec(Arrays.copyOfRange(tmp.getEncoded(),
						DEFAULT_AUTH_LENGTH, tmp.getEncoded().length), ALGORITHM_TYPE));
				ret.add(new SecretKeySpec(Arrays.copyOfRange(tmp
						.getEncoded(), 0, DEFAULT_AUTH_LENGTH), ALGORITHM_TYPE));
			} catch (InvalidKeySpecException e) {
				e.printStackTrace();
			}
		} catch (NoSuchAlgorithmException e) {
			System.out.println(e.toString());
		}
		return ret;
	}



}
