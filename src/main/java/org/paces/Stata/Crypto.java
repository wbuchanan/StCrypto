package org.paces.Stata;

import com.stata.sfi.SFIToolkit;

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

	public static int encryptFile(String[] args) {
		try {
			StEncrypt.encryptFile(args);
			return 0;
		} catch (NoSuchPaddingException | NoSuchAlgorithmException |
				IllegalBlockSizeException | BadPaddingException |
				InvalidKeyException | InvalidParameterSpecException |
				IOException | InvalidKeySpecException e) {
			SFIToolkit.stackTraceToString(e);
			return 1;
		}
	}

}
