package ch.erni.community.ldap.data;

/**
 * @author rap
 */
public class Credentials {

	private String user;

	private String password;

	public Credentials(String user, String password) {
		this.user = user;
		this.password = password;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

}
