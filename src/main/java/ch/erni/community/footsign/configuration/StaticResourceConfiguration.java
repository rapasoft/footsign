package ch.erni.community.footsign.configuration;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.footsign.util.PhotoPathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;
import org.springframework.web.servlet.resource.VersionResourceResolver;

/**
 * User: ban
 * Date: 26. 2. 2015
 * Time: 14:30
 */
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	@Autowired
	private Environment env;

	@Autowired
	private PhotoPathBuilder photoPathBuilder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		addAvatarsResourceHandler(registry);
		addGzippedStaticResourceHandler(registry);
	}

	private void addGzippedStaticResourceHandler(ResourceHandlerRegistry registry) {
		boolean isCacheEnabled = Boolean.valueOf(env.getProperty("spring.thymeleaf.cache"));

		registry.addResourceHandler("/public/css/**", "/public/js/**")
				.addResourceLocations("/public/css/", "classpath:/public/css")
				.addResourceLocations("/public/js", "classpath:/public/js")
				.setCachePeriod(Integer.parseInt(env.getProperty("spring.resources.cache-period")))
				.resourceChain(isCacheEnabled)
				.addResolver(new GzipResourceResolver())
				.addResolver(new VersionResourceResolver().addContentVersionStrategy("/**"))
				.addTransformer(new AppCacheManifestTransformer());
	}

	private void addAvatarsResourceHandler(ResourceHandlerRegistry registry) {
		try {
			registry.addResourceHandler("/avatars/**")
					.addResourceLocations("file:" + photoPathBuilder.buildAvatarsPath());
		} catch (PropertyFileNotFound propertyFileNotFound) {
			throw new RuntimeException(propertyFileNotFound);
		}
	}
}
