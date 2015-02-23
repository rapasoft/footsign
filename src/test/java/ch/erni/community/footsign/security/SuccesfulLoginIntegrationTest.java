package ch.erni.community.footsign.security;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author rap
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/neo4j.xml")
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SuccesfulLoginIntegrationTest {

	private UserAfterLoginHandler userAfterLoginHandler;

	@Autowired
	private UserRepository userRepository;

	private Authentication authentication;

	private UserDetails userDetails;

	@Before
	public void before() {
		userAfterLoginHandler = spy(new UserAfterLoginHandler());
		userAfterLoginHandler.userRepository = userRepository;

		authentication = mock(ErniAuthentication.class);
		userDetails = mock(UserDetails.class);

		when(userDetails.getDomainUserName()).thenReturn("test");
		when(userDetails.getFirstName()).thenReturn("First");
		when(userDetails.getSecondName()).thenReturn("Second");

		when(authentication.getPrincipal()).thenReturn(userDetails);
	}

	@Test
	public void testCreateNewUserAfterLogin() throws IOException, ServletException {
		User user = userRepository.findByDomainShortName("test");

		Assert.assertNull(user);

		// First login
		userAfterLoginHandler.onAuthenticationSuccess(mock(HttpServletRequest.class), mock(HttpServletResponse.class), authentication);

		// Creates new user
		user = userRepository.findByDomainShortName("test");

		Assert.assertNotNull(user);
		Assert.assertEquals("test", user.getDomainShortName());
		Assert.assertEquals("First Second", user.getFullName());

		// Any other successful login (when the user's info changes)
		when(userDetails.getSecondName()).thenReturn("NewSecond");
		userAfterLoginHandler.onAuthenticationSuccess(mock(HttpServletRequest.class), mock(HttpServletResponse.class), authentication);

		user = userRepository.findByDomainShortName("test");

		Assert.assertNotNull(user);
		Assert.assertEquals("test", user.getDomainShortName());
		Assert.assertEquals("First NewSecond", user.getFullName());
	}

}
