package ch.erni.community.footsign.util;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.ldap.data.UserDetails;

import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * User: ban
 * Date: 23. 2. 2015
 * Time: 15:18
 */

public class FileDownloader {

	private static final String DEFAULT_PHOTO_PATH = "https://sps2010-secure.erninet.ch/people/portraits/";
	private static final String PHOTO_SUFFIX = "_197x245px_RGB.jpg";
	private static final String IMG_DIRECTORY = "resources/img/profile_photos/";


	public static Path downloadPhoto(UserDetails userDetails, String password) {

		try {
			URL server = new URL(buildPhotoPath(userDetails));
			Path target = new File(IMG_DIRECTORY + buildPhotoName(userDetails)).toPath();

			Authenticator.setDefault(new SharePointAuthenticator(userDetails.getDomainUserName(), password));
			Files.copy(server.openStream(), target, StandardCopyOption.REPLACE_EXISTING);

			return target;
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private static String buildPhotoPath(UserDetails userDetails) {
		return DEFAULT_PHOTO_PATH + buildPhotoName(userDetails);
	}

	private static String buildPhotoName(UserDetails userDetails) {
		return userDetails.getSecondName() + "_" + userDetails.getFirstName() + PHOTO_SUFFIX;
	}
}
