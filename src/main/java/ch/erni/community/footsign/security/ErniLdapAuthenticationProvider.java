package ch.erni.community.footsign.security;

import ch.erni.community.footsign.util.FileDownloader;
import ch.erni.community.ldap.LdapService;
import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.data.AuthenticationResult;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

/**
 * @author rap
 */
@Component
public class ErniLdapAuthenticationProvider implements AuthenticationProvider {

	LdapService ldap;

	@Autowired
	FileDownloader fileDownloader;

	void createLdapConnection() throws CredentialsNotFoundException, CredentialsFileNotFoundException {
		ldap = new LdapServiceImpl();
	}

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		try {
			createLdapConnection();

			AuthenticationResult authenticationResult = ldap.authenticate(authentication.getName(), authentication.getCredentials().toString());

			UserDetails userDetails = authenticationResult.getUserDetails();

			Path path = fileDownloader.downloadPhoto(userDetails, authentication.getCredentials().toString());
			ErniUserDetails erniUserDetails = new ErniUserDetails(userDetails, path.toString());

			return new ErniAuthentication(erniUserDetails, authentication.getCredentials().toString(), authenticationResult.isAuthenticated());
		} catch (CredentialsNotFoundException e) {
			throw new BadCredentialsException("Could not find credentials information! " + e.getMessage());
		} catch (UserNotFoundException e) {
			throw new BadCredentialsException("Selected credentials are not valid! Either username or password is wrong. " + e.getMessage());
		} catch (CredentialsFileNotFoundException e) {
			throw new BadCredentialsException("System is not configured properly. " + e.getMessage());
		}

	}

	@Override
	public boolean supports(Class<?> aClass) {
		return true;
	}
}
