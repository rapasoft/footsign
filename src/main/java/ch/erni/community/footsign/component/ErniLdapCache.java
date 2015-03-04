package ch.erni.community.footsign.component;

import ch.erni.community.footsign.util.PhotoPathBuilder;
import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rap
 */
@Component
@Scope("singleton")
public class ErniLdapCache {

	@Autowired
	PhotoPathBuilder photoPathBuilder;
	
	private List<UserDetails> userDetailsList;

	public List<UserDetails> fetchEskEmployees() {
		if (userDetailsList == null) {
			load();
		}
		return userDetailsList;
	}
	
	public UserDetails getEskEmployee(String domainName) {
		if (domainName == null) return null;
		
		if (this.userDetailsList == null) return null;
		
		for (UserDetails detail : userDetailsList) {
			if (detail.getDomainUserName().equals(domainName)) {
				return detail;
			}
		}
		return null;
	}

	void load() {
		userDetailsList = new LdapServiceImpl(photoPathBuilder).fetchEskEmployees();
	}
}
