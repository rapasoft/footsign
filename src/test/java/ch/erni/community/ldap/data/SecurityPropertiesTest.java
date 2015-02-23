package ch.erni.community.ldap.data;

import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * @author rap
 */
public class SecurityPropertiesTest {

	private DefaultCredentials defaultCredentials;

	@Before
	public void before() {
		defaultCredentials = spy(new DefaultCredentials());
	}

	@Test(expected = CredentialsFileNotFoundException.class)
	public void testFailOnMissingPropertiesFile() throws Exception {
		when(defaultCredentials.getSecurityProperties()).thenReturn("/missing.properties");

		defaultCredentials.getCredentials();
	}

	@Test(expected = CredentialsNotFoundException.class)
	public void testFailOnEmptyPropertiesFile() throws Exception {
		when(defaultCredentials.getSecurityProperties()).thenReturn("/empty.properties");

		defaultCredentials.getCredentials();
	}

}
