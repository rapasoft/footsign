package ch.erni.community.footsign.configuration;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.footsign.util.PhotoPathBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;

/**
 * User: ban
 * Date: 26. 2. 2015
 * Time: 14:30
 */
@Configuration
public class StaticResourceConfiguration extends WebMvcConfigurerAdapter {

	private final int CACHE_PERIOD = 86400;

	@Autowired
	private PhotoPathBuilder photoPathBuilder;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		try {
			registry.addResourceHandler("/avatars/**")
					.addResourceLocations("file:" + photoPathBuilder.buildAvatarsPath());
		} catch (PropertyFileNotFound propertyFileNotFound) {
			throw new RuntimeException(propertyFileNotFound);
		}
		
		// TODO: not working :(
		/*registry
				.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/", "classpath:/resources/")
				.setCachePeriod(CACHE_PERIOD)
				.resourceChain(true)
				.addResolver(new GzipResourceResolver())
				.addTransformer(new AppCacheManifestTransformer());*/
	}
}
