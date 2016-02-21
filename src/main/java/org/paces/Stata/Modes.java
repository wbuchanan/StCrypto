package org.paces.Stata.Cryptography;

/**
 * Created by billy on 2/20/16.
 */
public enum Modes {
	none("NONE"),
	cbc("CBC"),
	ccm("CCM"),
	cfb("CFB"),
	cfbx("CFBX"),
	ctr("CTR"),
	cts("CTS"),
	ecb("ECB"),
	gcm("GCM"),
	ofb("OFB"),
	ofbx("OFBX"),
	pcbc("PCBC");

	private final String mode;

	private Modes(final String mode) {
		this.mode = mode;
	}

	@Override
	public String toString() {
		return this.mode;
	}

}
