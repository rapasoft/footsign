package ch.erni.community.ldap;

import ch.erni.community.footsign.util.PhotoPathBuilder;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class LdapServiceSearchTest {

	private LdapServiceImpl ldapService;

	@Before
	public void before() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		PhotoPathBuilder photoPathBuilder = mock(PhotoPathBuilder.class);
		ldapService = spy(new LdapServiceImpl(photoPathBuilder));
	}

	@Test
	public void testFindEskEmployees() throws Exception {
		List<UserDetails> userDetailsList = ldapService.fetchEskEmployees();

		assertNotNull(userDetailsList);
		assertTrue(userDetailsList.size() > 0);

		userDetailsList.forEach(Assert::assertNotNull);
	}

	@Test
	public void testSuccessfulSearchByDomainUserName() throws UserNotFoundException {
		UserDetails userDetails = ldapService.findByDomainUserName("rap");

		check(userDetails);
	}

	@Test(expected = UserNotFoundException.class)
	public void testUnsuccessfulSearchByDomainUserName() throws UserNotFoundException {
		ldapService.findByDomainUserName("jfk");
	}

	@Test
	public void testSearchByEmail() throws UserNotFoundException {
		UserDetails userDetails = ldapService.findByEmail("pavol.rajzak@erni.sk");

		check(userDetails);
	}

	@Test
	public void testSearchByName() throws UserNotFoundException {
		UserDetails userDetails = ldapService.findByName("Pavol", "Rajzák");

		check(userDetails);
	}

	private void check(UserDetails userDetails) {
		assertNotNull(userDetails);
		assertEquals("Pavol", userDetails.getFirstName());
		assertEquals("Rajzák", userDetails.getSecondName());
		assertEquals("pavol.rajzak@erni.sk", userDetails.getEmail().toLowerCase());
		assertEquals("rap", userDetails.getDomainUserName());
		assertEquals("Professional", userDetails.getTitle());
		assertEquals("Competence Team Java Vanek", userDetails.getDepartment());
	}

}