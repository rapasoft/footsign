package ch.erni.community.footsign.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * @author rap
 */
@NodeEntity
public class User {

	@GraphId
	Long id;

	/**
	 * Domain shortcut, e.g. rap, ban, etc.
	 */
	@Indexed(unique = true)
	private String domainShortName;

	private String fullName;

	private String email;

	private String department;

	private String photoPath = "avatars/default_profile_photo.png";

	private Float rating = 0.0f;

	public User() {
	}

	public User(String domainShortName, String fullName, String email, String department, String photoPath) {
		this.domainShortName = domainShortName;
		this.fullName = fullName;
		this.email = email;
		this.department = department;
		this.photoPath = photoPath;
	}

	public String getDomainShortName() {
		return domainShortName;
	}

	public void setDomainShortName(String domainShortName) {
		this.domainShortName = domainShortName;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getPhotoPath() {
		return photoPath;
	}

	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Float getRating() {
		return rating;
	}

	public void setRating(Float rating) {
		this.rating = rating;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		User user = (User) o;

		if (id != null ? !id.equals(user.id) : user.id != null) return false;
		if (domainShortName != null ? !domainShortName.equals(user.domainShortName) : user.domainShortName != null) return false;
		if (fullName != null ? !fullName.equals(user.fullName) : user.fullName != null) return false;
		if (email != null ? !email.equals(user.email) : user.email != null) return false;
		if (department != null ? !department.equals(user.department) : user.department != null) return false;
		if (rating != null ? !rating.equals(user.rating) : user.rating != null) return false;
		return !(photoPath != null ? !photoPath.equals(user.photoPath) : user.photoPath != null);

	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (domainShortName != null ? domainShortName.hashCode() : 0);
		result = 31 * result + (fullName != null ? fullName.hashCode() : 0);
		result = 31 * result + (email != null ? email.hashCode() : 0);
		result = 31 * result + (department != null ? department.hashCode() : 0);
		result = 31 * result + (photoPath != null ? photoPath.hashCode() : 0);
		result = 31 * result + (rating != null ? rating.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return fullName + "(" + domainShortName + ")";
	}
}

