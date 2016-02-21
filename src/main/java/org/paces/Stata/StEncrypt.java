package org.paces.Stata;

import javax.crypto.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.InvalidParameterSpecException;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class StEncrypt {

	public static void encryptFile(String[] args) throws NoSuchPaddingException,
		NoSuchAlgorithmException, IllegalBlockSizeException,
		BadPaddingException, InvalidKeyException,
		InvalidParameterSpecException, IOException,
		InvalidKeySpecException {
		new PasswordWindow();
	}

}
