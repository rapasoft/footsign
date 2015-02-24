package ch.erni.community.ldap;

import ch.erni.community.ldap.data.AuthenticationResult;
import ch.erni.community.ldap.data.DefaultCredentials;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

/**
 * @author rap
 */
public class LdapServiceAuthenticationTest {

	@Test(expected = UserNotFoundException.class)
	public void testFailedAuthenticationWithErniActiveDirectory() throws Exception {
		Connection connection = spy(Connection.forCredentials(new DefaultCredentials().getCredentials()));

		LdapServiceImpl ldapService = spy(new LdapServiceImpl());
		when(ldapService.createConnection(new DefaultCredentials().getCredentials())).thenReturn(connection);

		ldapService.authenticate("Eminem", "I'mSorryMama");

		verify(connection, calls(1)).close();

	}

	@Test
	public void testSuccessfulAuthenticationWithMockActiveDirectory() throws Exception {
		LdapServiceImpl ldapService = mock(LdapServiceImpl.class);

		UserDetails userDetailsMock = userDetails();
		when(ldapService.authenticate("user", "password")).thenReturn(new AuthenticationResult(userDetailsMock, true));

		AuthenticationResult authenticationResult = ldapService.authenticate("user", "password");

		assertTrue(authenticationResult.isAuthenticated());

		UserDetails principal = authenticationResult.getUserDetails();

		assertEquals(userDetailsMock.getTitle(), principal.getTitle());
		assertEquals(userDetailsMock.getEmail(), principal.getEmail());
		assertEquals(userDetailsMock.getFirstName(), principal.getFirstName());
		assertEquals(userDetailsMock.getSecondName(), principal.getSecondName());
		assertEquals(userDetailsMock.getDepartment(), principal.getDepartment());
		assertEquals(userDetailsMock.getDN(), principal.getDN());
		assertEquals(userDetailsMock.getDomainUserName(), principal.getDomainUserName());
		assertEquals(userDetailsMock.toString(), principal.toString());
	}

	private UserDetails userDetails() {
		return new UserDetails(
				Optional.of("firstName"), Optional.of("secondName"), Optional.of("dn"), Optional.of("firstName.secondName@erni.sk"),
				Optional.of("Test"), Optional.of("Test"));
	}


}
