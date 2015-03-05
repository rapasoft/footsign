package ch.erni.community.footsign.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author rap
 */
public class ErniAuthentication implements Authentication {

	private final ErniUserDetails userDetails;

	private String credentials;

	private boolean authenticated;

	public ErniAuthentication(ErniUserDetails userDetails, String credentials, boolean authenticated) {
		this.userDetails = userDetails;
		setAuthenticated(authenticated);
		this.credentials = credentials;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	@Override
	public String getCredentials() {
		return credentials;
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
	public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
		this.authenticated = authenticated;
	}

	public void clearCredentials() {
		this.credentials = null;
	}

	@Override
	public String getName() {
		return userDetails.getFirstName() + " " + userDetails.getSecondName();
	}

}
