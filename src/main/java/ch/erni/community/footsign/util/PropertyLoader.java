package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: ban
 * Date: 26. 2. 2015
 * Time: 10:35
 */
@Component
public class PropertyLoader {

	public static final String APPLICATION_PROPERTY_FILE = "application.properties";

	public String getProperty(String key) throws PropertyFileNotFound {
		return getProperty(key, APPLICATION_PROPERTY_FILE);
	}

	public String getProperty(String key, String fileName) throws PropertyFileNotFound {
		InputStream input = getPropertyFileAsStream(fileName);
		Properties prop = new Properties();
		try {
			prop.load(input);
		} catch (IOException | NullPointerException e) {
			throw new PropertyFileNotFound("Property file can not be loaded. Please check whether proper file exist. " + e);
		}
		return prop.getProperty(key);
	}

	private InputStream getPropertyFileAsStream(String fileName) {
		return this.getClass().getClassLoader().getResourceAsStream(fileName);
	}


}
