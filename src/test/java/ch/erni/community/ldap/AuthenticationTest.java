package ch.erni.community.ldap;

import ch.erni.community.ldap.data.DefaultCredentials;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;

/**
 * @author rap
 */
public class AuthenticationTest {

	private Connection connection;

	@Before
	public void init() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		connection = Connection.forCredentials(new DefaultCredentials().getCredentials());
	}

	@Test(expected = UserNotFoundException.class)
	public void testFailedAuthentication() throws UserNotFoundException {
		LdapService ldapService = new LdapServiceImpl(connection);

		ldapService.authenticate("Eminem", "I'mSorryMama");
	}

}
