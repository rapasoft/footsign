package ch.erni.community.footsign.security;

import ch.erni.community.ldap.data.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author rap
 */
public class ErniAuthentication implements Authentication {

	private UserDetails userDetails;

	private boolean authenticated;

	public ErniAuthentication(UserDetails userDetails, boolean authenticated) {
		this.userDetails = userDetails;
		this.authenticated = authenticated;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getDetails() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return userDetails;
	}

	@Override
	public boolean isAuthenticated() {
		return authenticated;
	}

	@Override
	public void setAuthenticated(boolean b) throws IllegalArgumentException {
		this.authenticated = b;
	}

	@Override
	public String getName() {
		return userDetails.getFirstName() + " " + userDetails.getSecondName();
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
}
