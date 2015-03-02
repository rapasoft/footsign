package ch.erni.community.footsign.util;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * User: ban
 * Date: 26. 2. 2015
 * Time: 14:30
 */
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	PropertyLoader propertyLoader;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		try {
			registry.addResourceHandler("/avatars/**").addResourceLocations("file:" + propertyLoader.getProperty("photo.dir"));
		} catch (PropertyFileNotFound propertyFileNotFound) {
			throw new RuntimeException(propertyFileNotFound);
		}
	}
}
