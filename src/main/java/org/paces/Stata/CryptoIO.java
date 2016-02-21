package org.paces.Stata.Cryptography;

import org.apache.commons.io.FilenameUtils;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidParameterSpecException;
import java.util.List;

public class CryptoIO {

	private String filePath;
	private String fileNameStub;
	private String extension;
	private File in;
	private File out;

	public CryptoIO(String filenameStub) {
		this.fileNameStub = FilenameUtils.getBaseName(filenameStub);
		this.extension = FilenameUtils.getExtension(filenameStub);
		this.filePath = FilenameUtils.getFullPath(filenameStub);
	}

	public CryptoIO(File fileObject) {
		this.fileNameStub = FilenameUtils.getBaseName(fileObject.getAbsolutePath());
		this.extension = FilenameUtils.getExtension(fileObject.getAbsolutePath());
		this.filePath = fileObject.getAbsolutePath();
	}

	public void read() throws IOException {
		this.in = makeFile(this.extension);
	}

	public void read(String extension) throws IOException {
		this.in = makeFile(this.extension);
	}

	public void write(String extension) throws IOException {
		this.out = new File(this.filePath + this.fileNameStub + "." + extension);
	}

	private File makeFile(String extension) {
		return new File(this.filePath + this.fileNameStub + "." + extension);
	}

	public String encrypt(Integer keyLength, char[] pw) throws
			NoSuchAlgorithmException,
			NoSuchPaddingException, InvalidKeyException,
			InvalidParameterSpecException, IOException {

		OutputStream o = new FileOutputStream(this.filePath + this.fileNameStub + ".aes");

		CryptoKey keys = new CryptoKey();
		byte[] salt = keys.saltShaker();
		List<SecretKey> encKeys = keys.keyMaker(keyLength, pw, salt);
		Cipher cryptKeeper = keys.getCipher();
		cryptKeeper.init(Cipher.ENCRYPT_MODE, encKeys.get(0));
		byte[] iv = cryptKeeper.getParameters().getParameterSpec(IvParameterSpec.class).getIV();
		o.write(keyLength / 8);
		o.write(salt);
		byte[] auth = encKeys.get(1).getEncoded();
		o.write(auth);
		o.write(iv);

		try {
			FileInputStream fis = new FileInputStream(this.in);
			FileChannel fc = fis.getChannel();
			MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
			byte[] contents = new byte[mbb.capacity()];
			mbb.get(contents);
			cryptKeeper.update(contents);
			byte[] encData = cryptKeeper.doFinal();
			o.write(encData);
			o.flush();
			o.close();
			return "File successfully encrypted";
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			return "File was not encrypted";
		}
	}

}
