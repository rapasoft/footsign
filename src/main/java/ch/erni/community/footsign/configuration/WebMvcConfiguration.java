package ch.erni.community.footsign.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.resource.AppCacheManifestTransformer;
import org.springframework.web.servlet.resource.GzipResourceResolver;

/**
 * @author rap
 */
@Configuration
@AutoConfigureAfter(DispatcherServletAutoConfiguration.class)
public class WebMvcConfiguration extends WebMvcAutoConfiguration.WebMvcAutoConfigurationAdapter {

	private final int CACHE_PERIOD = 86400;
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/rules").setViewName("rules");
		registry.addViewController("/login").setViewName("login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		super.addResourceHandlers(registry);

		// TODO: not working :(
		registry
				.addResourceHandler("/resources/**")
				.addResourceLocations("/resources/", "classpath:/resources/")
				.setCachePeriod(CACHE_PERIOD)
				.resourceChain(true)
				.addResolver(new GzipResourceResolver())
				.addTransformer(new AppCacheManifestTransformer());
		
	}
}
