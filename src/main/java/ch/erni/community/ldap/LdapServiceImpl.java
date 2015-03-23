package ch.erni.community.ldap;

import ch.erni.community.ldap.data.*;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import com.unboundid.ldap.sdk.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rap
 */
public class LdapServiceImpl implements LdapService {

	public List<UserDetails> fetchEskEmployees() {
		ReadOnlySearchRequest readOnlySearchRequest;
		SearchResult searchResult;
		Connection connection;

		try {
			connection = createConnection(new DefaultCredentials().getCredentials());
		} catch (CredentialsNotFoundException | CredentialsFileNotFoundException | LDAPException e) {
			throw new RuntimeException(e);
		}

		try {
			readOnlySearchRequest = new SearchRequest(ErniLdapConstants.ERNI_EMPLOYEES_USERS_GROUP_DN, SearchScope.SUB, ErniLdapConstants.FILTER);
			searchResult = connection.getLdapConnection().search(readOnlySearchRequest);
		} catch (LDAPException e) {
			throw new RuntimeException(e.getDiagnosticMessage());
		} finally {
			connection.close();
		}

		return extractUserDetails(searchResult);
	}

	Connection createConnection(Credentials credentials) throws LDAPException {
		return Connection.forCredentials(credentials);
	}

	@Override
	public UserDetails findByDomainUserName(String domainUserName) throws UserNotFoundException {
		return fetchEskEmployees()
				.stream()
				.filter(userDetails -> userDetails.getDomainUserName().equals(domainUserName))
				.findFirst()
				.orElseThrow(new UserNotFoundException("User with domain username: " + domainUserName + " not found in the AD."));
	}

	@Override
	public UserDetails findByEmail(String email) throws UserNotFoundException {
		return fetchEskEmployees()
				.stream()
				.filter(userDetails -> userDetails.getEmail().equalsIgnoreCase(email))
				.findFirst()
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
				.orElseThrow(new UserNotFoundException("User with name: " + firstName + " " + lastName + " not found in the AD."));
	}

	@Override
	public AuthenticationResult authenticate(String domainUserName, String password) throws UserNotFoundException {
		UserDetails userDetails = findByDomainUserName(domainUserName);

		Connection connection = null;

		try {
			connection = createConnection(new Credentials(userDetails.getDN(), password));
			boolean result = connection.getLdapConnection().isConnected();

			return new AuthenticationResult(userDetails, result);
		} catch (LDAPException e) {
			throw new UserNotFoundException(e.getDiagnosticMessage());
		} finally {
			if (connection != null) {
				connection.close();
			}
		}
	}

	private List<UserDetails> extractUserDetails(SearchResult searchResult) {
		List<UserDetails> userDetailsList = new ArrayList<>();

		for (SearchResultEntry searchResultEntry : searchResult.getSearchEntries()) {
			String description = searchResultEntry.getAttributeValue("description");
			if (description != null && description.equals("ESK")) {
				Optional<String> firstName = Optional.ofNullable(searchResultEntry.getAttributeValue("givenName"));
				Optional<String> secondName = Optional.ofNullable(searchResultEntry.getAttributeValue("sn"));
				Optional<String> domainUserName = Optional.ofNullable(searchResultEntry.getAttributeValue("sAMAccountName"));
				Optional<String> email = Optional.ofNullable(searchResultEntry.getAttributeValue("mail"));
				Optional<String> title = Optional.ofNullable(searchResultEntry.getAttributeValue("title"));
				Optional<String> department = Optional.ofNullable(searchResultEntry.getAttributeValue("department"));

				UserDetails detail = new UserDetails(firstName, secondName, domainUserName, email, title, department);

				userDetailsList.add(detail);
			}
		}

		return userDetailsList;
	}

}
