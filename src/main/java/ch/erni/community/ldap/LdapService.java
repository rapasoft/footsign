package ch.erni.community.ldap;

import ch.erni.community.ldap.data.AuthenticationResult;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;

import java.util.List;

/**
 * @author rap
 */
public interface LdapService {

	List<UserDetails> fetchEskEmployees();

	UserDetails findByDomainUserName(String domainUserName) throws UserNotFoundException;

	UserDetails findByEmail(String email) throws UserNotFoundException;

	UserDetails findByName(String firstName, String lastName) throws UserNotFoundException;

	/**
	 * Authenticates user within ERNI Employee Active Directory group.
	 * The user must be within ERNI ESK Employee group, otherwise he cannot log in.
	 *
	 * @param domainUserName - shortcut used for domain login (e.g. rap, veda, etc.)
	 * @param password       - current domain password
	 * @return {@link AuthenticationResult} with details of user and whether he is successfully authenticated
	 * @throws UserNotFoundException when username cannot be found in the ActiveDirectory or the password is wrong
	 */
	AuthenticationResult authenticate(String domainUserName, String password) throws UserNotFoundException;
}
