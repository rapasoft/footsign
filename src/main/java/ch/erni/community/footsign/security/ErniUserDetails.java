package ch.erni.community.footsign.security;

import ch.erni.community.ldap.data.UserDetails;

import java.util.Optional;

/**
 * @author rap
 */
public class ErniUserDetails extends UserDetails {

	private String photoPath;

	public ErniUserDetails(Optional<String> firstName, Optional<String> secondName, Optional<String> domainUserName, Optional<String> email, Optional<String> title, Optional<String> department) {
		super(firstName, secondName, domainUserName, email, title, department);
	}

	public ErniUserDetails(UserDetails userDetails) {
		this(
				Optional.of(userDetails.getFirstName()),
				Optional.of(userDetails.getSecondName()),
				Optional.of(userDetails.getDomainUserName()),
				Optional.of(userDetails.getEmail()),
				Optional.of(userDetails.getTitle()),
				Optional.of(userDetails.getDepartment())
		);
	}

	public ErniUserDetails(UserDetails userDetails, String photoPath) {
		this(userDetails);
		this.photoPath = photoPath;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
}
