package ch.erni.community.footsign.util;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.ldap.data.UserDetails;

import java.util.Optional;

/**
 * Created by cepe on 03.03.2015.
 */
public class LdapUserHelper {

	public static Optional<User> createUserFromLdapUser(UserDetails userDetails) {
		if (userDetails == null) {
			return Optional.empty();
		}
		User result = new User();
        result.setDomainShortName(userDetails.getDomainUserName());
        result.setDepartment(userDetails.getDepartment());
        result.setEmail(userDetails.getEmail());
        result.setFullName(userDetails.getFullName());

		return Optional.of(result);

	}
}
