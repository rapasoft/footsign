package ch.erni.community.footsign;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author rap
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
public class ServletApplication extends SpringBootServletInitializer {

	private static Class<Application> applicationClass = Application.class;

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		try {
//			FileUtils.deleteRecursively(new File(DataConfiguration.NEO4J_DB_PATH));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		return application.sources(applicationClass);
	}

}
