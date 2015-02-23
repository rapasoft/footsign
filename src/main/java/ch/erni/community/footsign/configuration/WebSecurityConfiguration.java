package ch.erni.community.footsign.configuration;

import ch.erni.community.footsign.security.ErniLdapAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author rap
 */
@Configuration
@EnableWebMvcSecurity
@ComponentScan(basePackages = "ch.erni.community.footsign.security")
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	private ErniLdapAuthenticationProvider erniLdapAuthenticationProvider;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.authorizeRequests()
				.antMatchers("/").permitAll()
				.antMatchers("/secured").fullyAuthenticated()

				.and()
				.formLogin()
				.loginPage("/login")

				.and()
				.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/")
				.permitAll()

				.and()
				.rememberMe();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(erniLdapAuthenticationProvider);
	}
}
