package org.paces.Stata;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class Password {

	private static final Integer iterations = 65536;
	private static final Integer saltLength = 512;
	private byte[] salt;
	private static final Integer keyLength = 512;
	private SecretKeyFactory skf;
	private byte[] hash;
	private Base64.Encoder enc = Base64.getEncoder();
	private byte[] encryptedPassword;
	private SecretKey key;
	private SecretKey secret;
	private String saltFilePath;

	public Password(String saltFile) throws InvalidKeySpecException, NoSuchAlgorithmException {
		this.saltFilePath = saltFile;
	}

	protected void setPassword(char[] pw) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		salt = SecureRandom.getInstanceStrong().generateSeed(saltLength);
		skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
		KeySpec keySpec = new PBEKeySpec(pw, salt, iterations, keyLength);
		hash = skf.generateSecret(keySpec).getEncoded();
		key = skf.generateSecret(keySpec);
		secret = new SecretKeySpec(key.getEncoded(), "AES");

	}

	protected String getEncodedSalt() {
		return this.enc.encodeToString(this.salt);
	}

	protected String getEncodedHash() {
		return this.enc.encodeToString(this.hash);
	}

	protected void writeSalt(String path) throws IOException {
		File saltFile = new File(path + "_salt.enc");
		FileOutputStream saltOutFile = new FileOutputStream(saltFile);
		saltOutFile.write(this.salt);
		saltOutFile.close();
	}

	protected void writeSalt() throws IOException {
		File saltFile = new File(this.saltFilePath);
		FileOutputStream saltOutFile = new FileOutputStream(saltFile);
		saltOutFile.write(this.salt);
		saltOutFile.close();
	}

	protected SecretKey getSecret() {
		return this.secret;
	}

}
