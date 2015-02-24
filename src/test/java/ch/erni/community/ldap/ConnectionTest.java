package ch.erni.community.ldap;

import ch.erni.community.ldap.data.DefaultCredentials;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import com.unboundid.ldap.sdk.LDAPException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

/**
 * @author rap
 */
public class ConnectionTest {

	private Connection connection;

	@Test
	public void testConnectionCreated() throws Exception {
		assertNotNull(connection);
		assertNotNull(connection.getLdapConnection());
		assertNotNull(connection.toString());

		connection.close();

		assertFalse(connection.getLdapConnection().isConnected());
	}

	@Before
	public void init() throws CredentialsNotFoundException, CredentialsFileNotFoundException, LDAPException {
		connection = Connection.forCredentials(new DefaultCredentials().getCredentials());
	}

	@After
	public void destroy() {
		connection.close();
	}

}
