package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.Normalizer;

/**
 * User: ban
 * Date: 23. 2. 2015
 * Time: 15:18
 */
@Component
public class FileDownloader {

	private static final String PHOTO_URL = "https://sps2010-secure.erninet.ch/people/portraits/";
	private static final String PHOTO_SUFFIX = "_197x245px_RGB.jpg";
	private static final String MAVEN_DIR_PROPERTY = "photo.dir";

	@Autowired
	PropertyLoader propertyLoader;

	public Path downloadPhoto(UserDetails userDetails, String password) {

		try {

			URL server = new URL(buildURLPhotoPath(userDetails));
			Path target = new File(propertyLoader.getProperty(MAVEN_DIR_PROPERTY) + buildPhotoName(userDetails)).toPath();

			Authenticator.setDefault(new SharePointAuthenticator(userDetails.getDomainUserName(), password));
			Files.copy(server.openStream(), target, StandardCopyOption.REPLACE_EXISTING);

			return target;
		} catch (PropertyFileNotFound | IOException e) {
			throw new RuntimeException(e);
		}

	}

	private String buildURLPhotoPath(UserDetails userDetails) {
		return PHOTO_URL + buildPhotoName(userDetails);
	}

	private String buildPhotoName(UserDetails userDetails) {
		return normalize(userDetails.getSecondName()) + "_" + normalize(userDetails.getFirstName()) + PHOTO_SUFFIX;
	}

	private String normalize(String input) {
		input = Normalizer.normalize(input, Normalizer.Form.NFD);
		return input.replaceAll("[^\\x00-\\x7F]", "");
	}

}
