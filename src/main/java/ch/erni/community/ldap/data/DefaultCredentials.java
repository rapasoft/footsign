package ch.erni.community.ldap.data;

import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;

import java.util.Properties;

/**
 * @author rap
 */
public class DefaultCredentials {

	public static final String LDAP_USER = "ldap.user";

	public static final String LDAP_PASSWORD = "ldap.password";

	public Credentials getCredentials() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		Properties properties = new Properties();

		try {
			properties.load(this.getClass().getResourceAsStream("/security.properties"));
			String user = (String) properties.get(LDAP_USER);
			String password = (String) properties.get(LDAP_PASSWORD);

			if (user == null || password == null) {
				throw new CredentialsNotFoundException("Properties `" + LDAP_USER + "` and `" + LDAP_PASSWORD + "` must be set in order to connect to ActiveDirectory. Please create security.properties file.");
			}

			return new Credentials(user, password);
		} catch (Exception e) {
			throw new CredentialsFileNotFoundException("In order to connect you must create a `security.properties` file with `" + LDAP_USER + "` and `" + LDAP_PASSWORD + "` properties defined.");
		}
	}

}
