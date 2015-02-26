package ch.erni.community.footsign.security;

import ch.erni.community.ldap.LdapService;
import ch.erni.community.ldap.data.AuthenticationResult;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ErniLdapAuthenticationProviderTest {

	private ErniLdapAuthenticationProvider erniLdapAuthenticationProvider;

	private UserDetails userDetailsMock;

	@Before
	public void before() throws CredentialsNotFoundException, CredentialsFileNotFoundException, UserNotFoundException {
		erniLdapAuthenticationProvider = spy(new ErniLdapAuthenticationProvider());

		doAnswer(invocation -> null).when(erniLdapAuthenticationProvider).createLdapConnection();

		LdapService ldapServiceMock = mock(LdapService.class);

		userDetailsMock = new UserDetails(
				Optional.of("firstName"), Optional.of("secondName"), Optional.of("dn"), Optional.of("firstName.secondName@erni.sk"),
				Optional.of("Test"), Optional.of("Test"));

		when(ldapServiceMock.authenticate("username", "password")).thenReturn(new AuthenticationResult(userDetailsMock, true));
		when(ldapServiceMock.authenticate("malicious", "blablabla")).thenThrow(new UserNotFoundException("UnF"));

		erniLdapAuthenticationProvider.ldap = ldapServiceMock;
	}

	@Test
	public void testAuthenticate() throws Exception {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("username");
		when(authentication.getCredentials()).thenReturn("password");

		ErniAuthentication successfulAuthentication = (ErniAuthentication) erniLdapAuthenticationProvider.authenticate(authentication);

		assertTrue(successfulAuthentication.isAuthenticated());

		assertEquals("firstName secondName", successfulAuthentication.getName());

		assertEquals("password", successfulAuthentication.getCredentials());
		assertEquals(null, successfulAuthentication.getDetails());
		assertEquals(null, successfulAuthentication.getAuthorities());

		UserDetails principal = (UserDetails) successfulAuthentication.getPrincipal();

		assertEquals(userDetailsMock.getTitle(), principal.getTitle());
		assertEquals(userDetailsMock.getEmail(), principal.getEmail());
		assertEquals(userDetailsMock.getFirstName(), principal.getFirstName());
		assertEquals(userDetailsMock.getSecondName(), principal.getSecondName());
		assertEquals(userDetailsMock.getDepartment(), principal.getDepartment());
		assertEquals(userDetailsMock.getDN(), principal.getDN());
		assertEquals(userDetailsMock.getDomainUserName(), principal.getDomainUserName());
	}

	@Test(expected = BadCredentialsException.class)
	public void testAuthenticationFailed() throws Exception {
		Authentication authentication = mock(Authentication.class);
		when(authentication.getName()).thenReturn("malicious");
		when(authentication.getCredentials()).thenReturn("blablabla");

		erniLdapAuthenticationProvider.authenticate(authentication);
	}

	@Test
	public void testConnectionCreation() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		ErniLdapAuthenticationProvider erniLdapAuthenticationProvider = new ErniLdapAuthenticationProvider();
		erniLdapAuthenticationProvider.createLdapConnection();

		assertNotNull(erniLdapAuthenticationProvider.ldap);
	}

	@Test
	public void testSupportsAll() {
		assertTrue(erniLdapAuthenticationProvider.supports(any()));
	}

}