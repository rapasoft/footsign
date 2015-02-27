package ch.erni.community.footsign.component;

import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rap
 */
@Component
@Scope("singleton")
public class ErniLdapCache {

	private List<UserDetails> userDetailsList;

	public List<UserDetails> fetchEskEmployees() {
		if (userDetailsList == null) {
			load();
		}
		return userDetailsList;
	}

	void load() {
		userDetailsList = new LdapServiceImpl().fetchEskEmployees();
	}
}
