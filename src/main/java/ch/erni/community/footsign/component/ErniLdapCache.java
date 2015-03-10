package ch.erni.community.footsign.component;

import ch.erni.community.footsign.event.UserLoggedInEvent;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.util.PhotoPathBuilder;
import ch.erni.community.ldap.LdapService;
import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


/**
 * @author rap
 */
@Component
@Scope("singleton")
public class ErniLdapCache implements ApplicationListener<UserLoggedInEvent> {

	private static final long REFRESH_RATE = 60 * 60 * 1000; // in millis (every hour)

	@Autowired
	UserRepository userRepository;

	@Autowired
	PhotoPathBuilder photoPathBuilder;

	private LdapService ldapService = new LdapServiceImpl();

	private List<ErniUserDetails> userDetailsList;

	public List<ErniUserDetails> fetchEskEmployees() {
		if (userDetailsList == null) {
			load();
		}
		return userDetailsList;
	}

	public ErniUserDetails getEskEmployee(String domainName) throws UserNotFoundException {
		return userDetailsList
				.stream()
				.filter(userDetails -> userDetails.getDomainUserName().equals(domainName))
				.findFirst()
				.orElseThrow(new UserNotFoundException("Could not find username " + domainName));
	}

	@Scheduled(fixedRate = REFRESH_RATE)
	// TODO @rap: Simplify!!!
	void load() {
		userDetailsList = ldapService
				.fetchEskEmployees()
				.stream()
				.map(ErniUserDetails::new).collect(Collectors.toList());
		findPhotos();
	}

	public void findPhotos() {
		final List<User> users = userRepository.findAllUsers();

		userDetailsList.forEach(
				erniUserDetails -> {
					String path;
					try {
						path = users
								.stream()
								.filter(
										user -> user.getDomainShortName().equals(erniUserDetails.getDomainUserName())
								).findFirst().get().getPhotoPath();
					} catch (NoSuchElementException e) {
						try {
							path = photoPathBuilder.buildRelativePath(null);
						} catch (Exception e1) {
							path = "/";
						}
					}
					erniUserDetails.setPhotoPath(path);
				}
		);
	}


	@Override
	public void onApplicationEvent(UserLoggedInEvent event) {
		findPhotos();
	}

}
