package ch.erni.community.ldap.data;

import java.util.Optional;
import java.util.function.Function;

/**
 * @author rap
 */
public class UserDetails {

	private final Optional<String> firstName;

	private final Optional<String> secondName;

	private final Optional<String> domainUserName;

	private final Optional<String> email;

	private final Optional<String> title;

	private final Optional<String> department;

	private final Optional<String> dn;

	public UserDetails(Optional<String> firstName, Optional<String> secondName, Optional<String> domainUserName, Optional<String> email, Optional<String> title, Optional<String> department, Optional<String> dn) {
		this.firstName = firstName;
		this.secondName = secondName;
		this.domainUserName = domainUserName;
		this.email = email;
		this.title = title;
		this.department = department;
		this.dn = dn;
	}

	public String getFirstName() {
		return firstName.map(Function.identity()).orElse("N/A").trim();
	}

	public String getSecondName() {
		return secondName.map(Function.identity()).orElse("N/A").trim();
	}

	public String getDomainUserName() {
		return domainUserName.map(Function.identity()).orElse("N/A").trim();
	}

	public String getEmail() {
		return email.map(Function.identity()).orElse("N/A").trim();
	}

	public String getTitle() {
		return title.map(Function.identity()).orElse("N/A").trim();
	}

	public String getDepartment() {
		return department.map(Function.identity()).orElse("N/A").trim();
	}

	public String getFullName() {
		return getFirstName() + " " + getSecondName();
	}

	@Override
	public String toString() {
		return firstName + " " + secondName + " (" + department + ")";
	}

	public String getDN() {
		return dn.map(Function.identity()).orElse("N/A").trim();
	}

}
