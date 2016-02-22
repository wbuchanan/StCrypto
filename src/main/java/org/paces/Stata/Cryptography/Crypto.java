package org.paces.Stata.Cryptography;

import com.stata.sfi.SFIToolkit;

/**
 * @author Billy Buchanan
 * @version 0.0.0
 */
public class Crypto {

	public static int stcrypto(String[] args) {
		try {
			Boolean complete = PasswordWindow.launch(args);
			return 0;
		} catch (Exception e) {
			SFIToolkit.errorln(SFIToolkit.stackTraceToString(e));
			return 1;
		}
	}

}
