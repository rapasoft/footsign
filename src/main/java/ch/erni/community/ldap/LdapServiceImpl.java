package ch.erni.community.ldap;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.footsign.util.PhotoPathBuilder;
import ch.erni.community.ldap.data.*;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import ch.erni.community.ldap.exception.UserNotFoundException;
import com.unboundid.ldap.sdk.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rap
 */
@Component()
public class LdapServiceImpl implements LdapService {

	@Autowired
	private PhotoPathBuilder photoPathBuilder;

	public LdapServiceImpl(PhotoPathBuilder photoPathBuilder) {
		this.photoPathBuilder = photoPathBuilder;
	}

	public List<UserDetails> fetchEskEmployees() {
		ReadOnlySearchRequest readOnlySearchRequest;
		SearchResult searchResult;
		Connection connection;

		try {
			connection = createConnection(new DefaultCredentials().getCredentials());
		} catch (CredentialsNotFoundException | CredentialsFileNotFoundException e) {
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

	Connection createConnection(Credentials credentials) {
		try {
			return Connection.forCredentials(credentials);
		} catch (LDAPException e) {
			throw new RuntimeException(e.getDiagnosticMessage());
		}
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

		Connection connection = createConnection(new Credentials(userDetails.getDN(), password));

		try {
			boolean result = connection.getLdapConnection().isConnected();

			return new AuthenticationResult(userDetails, result);
		} finally {
			connection.close();
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
				try {
					setPhotoPathToUserDetail(detail);
				} catch (PropertyFileNotFound propertyFileNotFound) {
					propertyFileNotFound.printStackTrace();
				}

				userDetailsList.add(detail);
			}
		}

		return userDetailsList;
	}

	private void setPhotoPathToUserDetail(UserDetails detail) throws PropertyFileNotFound {
		if (this.photoPathBuilder != null && detail != null) {
			Path target = new File(photoPathBuilder.buildAvatarsPath() + photoPathBuilder.buildPhotoName(detail)).toPath();
			if (target.toFile().exists()) {
				String photoPath = photoPathBuilder.buildRelativePath(detail);
				detail.setPhoto(photoPath);
			}
		}
	}

}
