package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.footsign.test.config.TestDataConfiguration;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: ban
 * Date: 3. 3. 2015
 * Time: 15:35
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
public class FileDownloaderTest {

	private FileDownloader fileDownloader;
	private UserDetails userDetailsMock;

	@Before
	public void setUp() {
		fileDownloader = new FileDownloader();
		userDetailsMock = new UserDetails(
				Optional.of("firstName"), Optional.of("secondName"), Optional.of("dn"), Optional.of("firstName.secondName@erni.sk"),
				Optional.of("Test"), Optional.of("Test"));
	}

	@Test
	public void targetDirDoesNotExist() throws PropertyFileNotFound {
		fileDownloader.propertyLoader = mock(PropertyLoader.class);
		when(fileDownloader.buildAvatarsPath()).thenReturn("testDir/");

		Path path = fileDownloader.downloadPhoto(userDetailsMock, "password");

		String fileName = path.getFileName().toString();

		assertEquals(fileName, "default_profile_photo.png");
	}

}
