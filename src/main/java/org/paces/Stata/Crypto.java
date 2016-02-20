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
public class Crypto {

	public static int encryptFile(String[] args) throws NoSuchPaddingException, InvalidKeySpecException, NoSuchAlgorithmException, IllegalBlockSizeException, IOException, BadPaddingException, InvalidKeyException, InvalidParameterSpecException {
		StEncrypt.encryptFile(args);
		return 0;
	}

}
