package org.paces.Stata;

import org.apache.commons.io.FilenameUtils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class EncryptFile {

	private Cipher cipher;
	private AlgorithmParameters params;
	private String outfileName;
	private String infileName;
	private static final String outfileExt = ".des";
	private String ivFilePath;


	public EncryptFile(String inputFile, SecretKey secret) throws
			NoSuchPaddingException, NoSuchAlgorithmException, IOException,
			BadPaddingException, IllegalBlockSizeException, InvalidKeyException,
			InvalidParameterSpecException {
		String ivFile = FilenameUtils.getBaseName(inputFile) + "_iv.enc";
		new EncryptFile(inputFile, ivFile, secret);
	}

	public EncryptFile(String inputFile, String ivFilePath, SecretKey secret)
			throws
			InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException,
			IOException, BadPaddingException, IllegalBlockSizeException,
			InvalidParameterSpecException {

		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, secret);
		params = cipher.getParameters();

		this.infileName = FilenameUtils.getBaseName(inputFile);
		this.outfileName = this.infileName + outfileExt;
		this.ivFilePath = ivFilePath;

		File nfile = new File(inputFile);
		writeIV(this.ivFilePath);

		FileInputStream inFile = new FileInputStream(nfile);

		byte[] input = new byte[512];
		int bytesRead;

		File outputFile = new File(this.outfileName);

		FileOutputStream outFile = new FileOutputStream(outputFile);

		while ((bytesRead = inFile.read(input)) != -1) {
			byte[] output = cipher.update(input, 0, bytesRead);
			if (output != null)
				outFile.write(output);
		}

		byte[] output = cipher.doFinal();
		if (output != null) outFile.write(output);
		inFile.close();
		outFile.flush();
		outFile.close();

	}

	private void writeIV(String path) throws IOException, InvalidParameterSpecException {
		File ivFile = new File(path + "_iv.enc");
		FileOutputStream ivOutFile = new FileOutputStream(ivFile);
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		ivOutFile.write(iv);
		ivOutFile.close();
	}

	private void writeIV() throws IOException, InvalidParameterSpecException {
		File ivFile = new File(this.ivFilePath);
		FileOutputStream ivOutFile = new FileOutputStream(ivFile);
		byte[] iv = params.getParameterSpec(IvParameterSpec.class).getIV();
		ivOutFile.write(iv);
		ivOutFile.close();
	}



}
