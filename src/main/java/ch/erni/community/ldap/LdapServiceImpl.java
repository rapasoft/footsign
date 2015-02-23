package ch.erni.community.ldap;

import ch.erni.community.ldap.data.AuthenticationResult;
import ch.erni.community.ldap.data.ErniLdapConstants;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;
import com.unboundid.ldap.sdk.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rap
 */
public class LdapServiceImpl implements LdapService {

	private Connection connection;

	public LdapServiceImpl(Connection connection) {
		this.connection = connection;
	}

	public List<UserDetails> fetchEskEmployees() {
		ReadOnlySearchRequest readOnlySearchRequest;
		SearchResult searchResult;
		try {
			readOnlySearchRequest = new SearchRequest(ErniLdapConstants.ERNI_EMPLOYEES_USERS_GROUP_DN, SearchScope.SUB, ErniLdapConstants.FILTER);
			searchResult = connection.ldapConnection().search(readOnlySearchRequest);
		} catch (LDAPException e) {
			throw new RuntimeException(e.getDiagnosticMessage());
		} finally {
			connection.close();
		}

		return extractUserDetails(searchResult);
	}

	@Override
	public UserDetails findByDomainUserName(String domainUserName) throws UserNotFoundException {
		return fetchEskEmployees()
				.stream()
				.filter(userDetails -> userDetails.getDomainUserName().equals(domainUserName))
				.findFirst()
				.map(userDetails -> userDetails)
				.orElseThrow(new UserNotFoundException("User with domain username: " + domainUserName + " not found in the AD."));
	}

	@Override
	public UserDetails findByEmail(String email) throws UserNotFoundException {
		return fetchEskEmployees()
				.stream()
				.filter(userDetails -> userDetails.getEmail().equalsIgnoreCase(email))
				.findFirst()
				.map(userDetails -> userDetails)
				.orElseThrow(new UserNotFoundException("User with email: " + email + " not found in the AD."));
	}

	@Override
	public UserDetails findByName(String firstName, String lastName) throws UserNotFoundException {
		return fetchEskEmployees()
				.stream()
				.filter(
						userDetails ->
								userDetails.getFirstName().equalsIgnoreCase(firstName.trim()) &&
										userDetails.getSecondName().equalsIgnoreCase(lastName.trim()))
				.findFirst()
				.map(userDetails -> userDetails)
				.orElseThrow(new UserNotFoundException("User with name: " + firstName + " " + lastName + " not found in the AD."));
	}

	@Override
	public AuthenticationResult authenticate(String domainUserName, String password) throws UserNotFoundException {
		UserDetails userDetails = findByDomainUserName(domainUserName);

		Connection connection = Connection.forCredentials(userDetails.getDN(), password);

		try {
			boolean result = connection.ldapConnection().isConnected();

			return new AuthenticationResult(userDetails, result);
		} catch (LDAPException e) {
			throw new UserNotFoundException(e.getDiagnosticMessage());
		} finally {
			connection.close();
		}
	}

	private List<UserDetails> extractUserDetails(SearchResult searchResult) {
		List<UserDetails> userDetailsList = new ArrayList<>();

		for (SearchResultEntry searchResultEntry : searchResult.getSearchEntries()) {
			String description = searchResultEntry.getAttributeValue("description");
			if (description != null && description.equals("ESK")) {
				Optional<String> firstName = Optional.ofNullable(searchResultEntry.getAttributeValue("givenName").trim());
				Optional<String> secondName = Optional.ofNullable(searchResultEntry.getAttributeValue("sn").trim());
				Optional<String> domainUserName = Optional.ofNullable(searchResultEntry.getAttributeValue("sAMAccountName").trim());
				Optional<String> email = Optional.ofNullable(searchResultEntry.getAttributeValue("mail").trim());
				Optional<String> title = Optional.ofNullable(searchResultEntry.getAttributeValue("title").trim());
				Optional<String> department = Optional.ofNullable(searchResultEntry.getAttributeValue("department").trim());

				userDetailsList.add(new UserDetails(firstName, secondName, domainUserName, email, title, department));
			}
		}

		return userDetailsList;
	}

}
