package org.paces.Stata.Cryptography;

import org.apache.commons.io.FilenameUtils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.Arrays;
import java.util.List;

/**
 * This class is based - in part - on the example Gist provided at:
 * https://gist.github.com/dweymouth/11089238
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class CryptoIO {

	/**
	 * Member that stores the file path used to locate the file for
	 * encryption/decryption
	 */
	private String filePath;

	/**
	 * File name stub is the name of the file without the extension.
	 */
	private String fileNameStub;

	/**
	 * The file extension
	 */
	private String extension;

	/**
	 * The absolute path to the file of interest
	 */
	private String absolutePath;

	/**
	 * The File object used to construct the File Input Stream object
	 */
	private File in;

	/**
	 * Reads smaller blocks of data...When the block size was too large it
	 * was causing issues when decrypting the file.
	 */
	private static final Integer BUFFER_SIZE = 4;

	/**
	 * Class constructor used with a String value to initialize the object
	 * @param fullFilePath The fully qualified file path
	 */
	public CryptoIO(String fullFilePath) {
		this.fileNameStub = FilenameUtils.getBaseName(fullFilePath);
		this.extension = FilenameUtils.getExtension(fullFilePath);
		this.filePath = FilenameUtils.getFullPath(fullFilePath);
		this.absolutePath = fullFilePath;
	}

	/**
	 * Class constructor used with a File object
	 * @param fileObject The file object representing the input file
	 */
	public CryptoIO(File fileObject) {
		this.fileNameStub = FilenameUtils.getBaseName(fileObject.getAbsolutePath());
		this.extension = FilenameUtils.getExtension(fileObject.getAbsolutePath());
		this.filePath = fileObject.getAbsolutePath();
		this.absolutePath = fileObject.getAbsolutePath();
	}

	/**
	 * Creates the File class object used to initialize the File Input Stream
	 * object
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	public void read() throws IOException {
		this.in = makeFile(this.extension);
	}

	/**
	 * Creates the File class object used to initialize the File Input Stream
	 * object
	 * @param extension The file extension to append to the file name stub.
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	public void read(String extension) throws IOException {
		this.in = makeFile(extension);
	}

	/**
	 * Method that initializes the File Output Stream object based on the
	 * name of the file created by this application
	 * @return A FileOutputStream object used when writing decrypted data
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	private FileOutputStream write() throws IOException {
		return new FileOutputStream(new File(this.filePath + this.fileNameStub));
	}

	/**
	 * Method used to initialize a File class object with the given file
	 * extension
	 * @param ext The file extension to use when initializing the object
	 * @return A File class object used to initialize the File Input Stream
	 * object
	 */
	private File makeFile(String ext) {
		return new File(this.filePath + this.fileNameStub + "." + ext);
	}

	/**
	 * Method used to write the salt value to disk.  Used primarily for
	 * debugging
	 * @param salt The salt value from the saltShaker method of the CryptoKey
	 *                class
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	private void writeSalt(byte[] salt) throws IOException {
		OutputStream salo = new FileOutputStream(this.in + "_salt.aes");
		salo.write(salt.length);
		salo.write(salt);
		salo.flush();
		salo.close();
	}

	/**
	 * Method to write the initialization vector to disk
	 * @param iv The bytes representing the initialization vector for the
	 *              encryption algorithm
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	private void writeIV(byte[] iv) throws IOException {
		OutputStream ivo = new FileOutputStream(this.in + "_iv.aes");
		ivo.write(iv.length);
		ivo.write(iv);
		ivo.flush();
		ivo.close();
	}

	/**
	 * Method used to use password based encryption to encrypt a file to disk
	 * @param keyLength The bit length of the keys to generate
	 * @param pw The Password character array retrieved from the
	 *              application's GUI
	 * @return A string containing the message to display to the user via the
	 * GUI
	 * @throws NoSuchAlgorithmException Cryptography exception
	 * @throws NoSuchPaddingException Cryptography exception
	 * @throws InvalidKeyException Cryptography exception
	 * @throws InvalidParameterSpecException Cryptography exception
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	public String encrypt(Integer keyLength, char[] pw) throws
			NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidParameterSpecException, IOException {

		// Initializes a file output stream object and appends the extension
		// .aes to the file (this will help identify the file as encrypted)
		FileOutputStream o = new FileOutputStream(this.in + ".aes");

		// Initialize a new CryptoKey object
		CryptoKey keys = new CryptoKey();

		// Generate the salt object used for generating the secret keys
		byte[] salt = keys.saltShaker();

		// Stores the encryption and authorization keys in the object encKeys
		List<SecretKey> encKeys = keys.keyMaker(keyLength, pw, salt);

		// gets a new Cipher class object based on the key encryption parameters
		Cipher cryptKeeper = keys.getCipher();

		// Initialize the cipher for encryption with the encryption key
		cryptKeeper.init(Cipher.ENCRYPT_MODE, encKeys.get(0));

		// Gets the initialization vector bytes
		byte[] iv = cryptKeeper.getParameters().getParameterSpec(IvParameterSpec.class).getIV();

		// Writes the byte length of the key to the file
		o.write(keyLength / 8);

		// Writes the salt object to the file
		o.write(salt);

		// Gets the encrypted byte array of the authorization secret key
		byte[] auth = encKeys.get(1).getEncoded();

		// Writes the authorization key to the file
		o.write(auth);

		// Writes the initialization vector to the file
		o.write(iv);

		// Initializes the file input stream (the data to be encrypted)
		FileInputStream fis = new FileInputStream(this.in);

		// Writes the file out to disk
		this.writeOut(BUFFER_SIZE, o, fis, cryptKeeper);

		// Flushes the output stream object
		o.flush();

		// Closes the output stream object
		o.close();

		// Closes the input stream object
		fis.close();

		// Returns the message to the user
		return "File successfully encrypted";

	} // End of method declaration

	/**
	 * Method used to decrypt the data in a file encrypted by this application
	 * @param pw The character array for the password retrieved from the GUI
	 * @return A string message to display to the user via the GUI
	 */
	public String decrypt(char[] pw) {
		try {
			// Initializes a file input stream (this is the file containing the
			// encrypted data
			FileInputStream fis = new FileInputStream(new File(this.absolutePath));

			// Gets the length of the keys to generate
			Integer keyLength = fis.read() * 8;

			// For now uses a standard byte length for the salt value
			byte[] salt = new byte[CryptoKey.DEFAULT_SALT_LENGTH];

			// Reads the salt object into the byte array
			fis.read(salt);

			// Initialize a CryptoKey class object
			CryptoKey cryptKeeper = new CryptoKey();

			// Generates a list of SecretKey objects for the encryption and
			// authorization keys
			List<SecretKey> keys = cryptKeeper.keyMaker(keyLength, pw, salt);

			// Uses default byte length for authorization keys
			byte[] authread = new byte[CryptoKey.DEFAULT_AUTH_LENGTH];

			// Reads the authorization key from the file
			fis.read(authread);

			// Checks to see if the authorization SecretKey object is the same
			// value as the authorization secret key encoded in the encrypted
			// file
			if (!Arrays.equals(keys.get(1).getEncoded(), authread)) {
				throw new Exception("Incorrect Password Specified");
			}

			// The initialization vector is a fixed length
			byte[] iv = new byte[16];

			// Reads the initialization vector into the object iv
			fis.read(iv);

			// Gets a cipher object from the CryptoKey class
			Cipher decoder = cryptKeeper.getCipher();

			// Initializes the Cipher object with the encryption secret key and
			// the initialization vector in decryption mode
			decoder.init(Cipher.DECRYPT_MODE, keys.get(0), new IvParameterSpec(iv));

			// Initializes the file output stream object
			FileOutputStream fos = write();

			// Calls the method used to handle the I/O flow of the data from
			// the unencrypted to encrypted states
			this.writeOut(BUFFER_SIZE, fos, fis, decoder);

			// Closes the output stream object
			fos.close();

			// Closes the input stream object
			fis.close();

			// Returns the message to display to the user
			return "File decrypted";

		// Catch any exceptions
		} catch (Exception e) {

			// Print the stack trace to the console
			e.printStackTrace();

			// Returns this message to the user
			return "File not decrypted";

		} // End Catch block

	} // End of method declaration

	/**
	 * Method used to write encrypted/decrypted data to the disk
	 * @param size The size of the byte buffer to use
	 * @param fos The file output stream where data will be written
	 * @param fis The file input stream where data is read from
	 * @param cryptKeeper A cipher class object used to encrypt/decrypt the data
	 * @return A boolean indicating whether or not the method successfully
	 * wrote the data to disk
	 * @throws IOException Exception thrown on generic I/O operations
	 */
	private Boolean writeOut(Integer size, FileOutputStream fos, FileInputStream
			fis, Cipher cryptKeeper) throws IOException {

		// Initializes a byte array of size defined by the size parameter
		byte[] byteBuffer = new byte[size];

		// Used to identify the number of bytes read from the input stream
		Integer numBytesRead;

		// Byte array used to store the data temporarily while
		// encrypting/decrypting
		byte[] encryptedData;

		// As long as some number of bytes were read from the file
		while ((numBytesRead = fis.read(byteBuffer)) > 0) {

			// Empty the byte array and prep the values for
			// encryption/decryption
			encryptedData = cryptKeeper.update(byteBuffer, 0, numBytesRead);

			// If there are data available write it to the output stream
			if (encryptedData != null) fos.write(encryptedData);

		} // End of While Loop

		// Attempt to finalize the cipher
		try {

			// Attempt finalizing the cipher
			encryptedData = cryptKeeper.doFinal();

			// If data exists, write it to the output stream
			if (encryptedData != null) fos.write(encryptedData);

			// Return the success boolean
			return true;

		// Catch block to handle errors (should never be reached)
		} catch (IllegalBlockSizeException | BadPaddingException e) {

			// Prints the stack trace to the console
			e.printStackTrace();

			// Returns failure boolean if the data were not written
			return false;

		} // End Catch Block

	} // End Method declaration

}
