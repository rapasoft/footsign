package ch.erni.community.footsign.security;

import ch.erni.community.TestDataConfiguration;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.util.FileDownloader;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

/**
 * @author rap
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@Transactional
@TransactionConfiguration(defaultRollback = true)
public class SuccesfulLoginIntegrationTest {

	private UserAfterLoginHandler userAfterLoginHandler;

	@Autowired
	private UserRepository userRepository;

	private Authentication authentication;

	private UserDetails userDetails;

	@AfterClass
	public static void afterClass() throws IOException {
		FileUtils.deleteRecursively(new File("target/test-neo4j.db"));
	}

	@Before
	public void before() {
		userAfterLoginHandler = spy(new UserAfterLoginHandler());
		userAfterLoginHandler.userRepository = userRepository;

		FileDownloader fileDownloader = mock(FileDownloader.class);
		when(fileDownloader.downloadPhoto(any(), anyString())).thenReturn(new File("/").toPath());

		userAfterLoginHandler.fileDownloader = fileDownloader;

		authentication = mock(ErniAuthentication.class);
		userDetails = mock(UserDetails.class);

		when(userDetails.getDomainUserName()).thenReturn("test");
		when(userDetails.getFirstName()).thenReturn("Pavol");
		when(userDetails.getSecondName()).thenReturn("Rajzak");

		when(authentication.getPrincipal()).thenReturn(userDetails);
		when(authentication.getCredentials()).thenReturn("password");
	}

	@Test
	public void testCreateNewUserAfterLogin() throws IOException, ServletException {
		User user = userRepository.findByDomainShortName("test");

		Assert.assertNull(user);

		// First login
		userAfterLoginHandler.onAuthenticationSuccess(mock(HttpServletRequest.class), mock(HttpServletResponse.class), authentication);

		// Creates new user
		user = userRepository.findByDomainShortName("test");
		Long id = user.getId();

		Assert.assertNotNull(user);
		Assert.assertEquals("test", user.getDomainShortName());
		Assert.assertEquals("Pavol Rajzak", user.getFullName());

		// Any other successful login (when the user's info changes)
		when(userDetails.getSecondName()).thenReturn("NewSecond");
		userAfterLoginHandler.onAuthenticationSuccess(mock(HttpServletRequest.class), mock(HttpServletResponse.class), authentication);

		user = userRepository.findByDomainShortName("test");
		Long newId = user.getId();

		Assert.assertNotNull(user);
		Assert.assertEquals("test", user.getDomainShortName());
		Assert.assertEquals("Pavol NewSecond", user.getFullName());

		Assert.assertEquals(id, newId);
	}

}
