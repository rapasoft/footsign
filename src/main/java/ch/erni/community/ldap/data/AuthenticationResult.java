package ch.erni.community.ldap.data;

/**
 * @author rap
 */
public class AuthenticationResult {

	private final boolean authenticated;

	private final UserDetails userDetails;

	public AuthenticationResult(UserDetails userDetails, boolean authenticated) {
		this.userDetails = userDetails;
		this.authenticated = authenticated;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
}
