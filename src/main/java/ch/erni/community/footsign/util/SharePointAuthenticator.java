package ch.erni.community.footsign.util;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * User: ban
 * Date: 23. 2. 2015
 * Time: 15:12
 */

public class SharePointAuthenticator extends Authenticator {

	private final PasswordAuthentication authentication;

	public SharePointAuthenticator(String domainName, String password) {

		if (domainName == null || password == null) {
			authentication = null;
		} else {
			authentication = new PasswordAuthentication(domainName, password.toCharArray());
		}
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		return authentication;
	}
}
