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

/**
 * User: ban
 * Date: 23. 2. 2015
 * Time: 15:18
 */
@Component
public class FileDownloader {

	@Autowired
	PhotoPathBuilder photoPathBuilder;
	
	public Path downloadPhoto(UserDetails userDetails, String password) {

		try {
			File avatarsDir = new File(photoPathBuilder.buildAvatarsPath());
			if (!avatarsDir.exists()) {
				return new File(photoPathBuilder.buildRelativePath(null)).toPath();
			}

			File avatarFile = new File(photoPathBuilder.buildAvatarsPath() + photoPathBuilder.buildPhotoName(userDetails));
			Path target = avatarFile.toPath();

			if (avatarFile.exists()) {
				return new File(photoPathBuilder.buildRelativePath(userDetails)).toPath();
			}

			URL server = new URL(photoPathBuilder.buildURLPhotoPath(userDetails));
			Authenticator.setDefault(new SharePointAuthenticator(userDetails.getDomainUserName(), password));
			Files.copy(server.openStream(), target, StandardCopyOption.REPLACE_EXISTING);

			return new File(photoPathBuilder.buildRelativePath(userDetails)).toPath();
		} catch (PropertyFileNotFound | IOException e) {
			throw new RuntimeException(e);
		}

	}



}
