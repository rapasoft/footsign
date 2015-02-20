package ch.erni.community.ldap;

import ch.erni.community.ldap.data.DefaultCredentials;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class LdapTest {

	private static Connection connection;

	@BeforeClass
	public static void init() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		connection = Connection.forCredentials(new DefaultCredentials().getCredentials());
	}

	@AfterClass
	public static void destroy() {
		connection.close();
	}

	@Test
	public void testConnectionCreated() throws Exception {
		assertNotNull(connection);
	}

	@Test
	public void testFindEskEmployees() throws Exception {
		LdapService ldapService = new LdapServiceImpl(connection);
		List<UserDetails> userDetailsList = ldapService.fetchEskEmployees();

		assertNotNull(userDetailsList);
		assertTrue(userDetailsList.size() > 0);

		userDetailsList.forEach(Assert::assertNotNull);
	}

	@Test
	public void testSuccessfulSearchByDomainUserName() throws UserNotFoundException {
		LdapService ldapService = new LdapServiceImpl(connection);
		UserDetails userDetails = ldapService.findByDomainUserName("rap");

		check(userDetails);
	}

	@Test(expected = UserNotFoundException.class)
	public void testUnsuccessfulSearchByDomainUserName() throws UserNotFoundException {
		LdapService ldapService = new LdapServiceImpl(connection);
		ldapService.findByDomainUserName("jfk");
	}

	@Test
	public void testSearchByEmail() throws UserNotFoundException {
		LdapService ldapService = new LdapServiceImpl(connection);
		UserDetails userDetails = ldapService.findByEmail("pavol.rajzak@erni.sk");

		check(userDetails);
	}

	@Test
	public void testSearchByName() throws UserNotFoundException {
		LdapService ldapService = new LdapServiceImpl(connection);
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