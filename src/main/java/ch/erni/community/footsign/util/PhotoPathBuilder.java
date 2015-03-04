package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.Normalizer;

/**
 * Created by cepe on 04.03.2015.
 */
@Component
public class PhotoPathBuilder {

    private static final String PHOTO_URL = "https://sps2010-secure.erninet.ch/people/portraits/";
    private static final String PHOTO_SUFFIX = "_197x245px_RGB.jpg";
    private static final String DEFAULT_PHOTO_NAME = "default_profile_photo.png";

    @Autowired
    PropertyLoader propertyLoader;

    public String buildURLPhotoPath(UserDetails userDetails) {
        return PHOTO_URL + buildPhotoName(userDetails);
    }

    public String buildPhotoName(UserDetails userDetails) {
        if (userDetails == null) {
            return DEFAULT_PHOTO_NAME;
        }
        return normalize(userDetails.getSecondName()) + "_" + normalize(userDetails.getFirstName()) + PHOTO_SUFFIX;
    }

    public String buildAvatarsPath() throws PropertyFileNotFound {
        return propertyLoader.getProperty(PropertyConstatns.MAVEN_PROJECT_DIR_PROPERTY) +
                propertyLoader.getProperty(PropertyConstatns.MAVEN_PHOTO_DIR_PROPERTY);
    }

    public String buildRelativePath(UserDetails userDetails) throws PropertyFileNotFound {
        return propertyLoader.getProperty(PropertyConstatns.MAVEN_PHOTO_DIR_PROPERTY) + buildPhotoName(userDetails);
    }

    private String normalize(String input) {
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        return input.replaceAll("[^\\x00-\\x7F]", "");
    }
}
