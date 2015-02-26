package ch.erni.community.footsign.nodes;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rap
 */
@NodeEntity
public class User {

	@RelatedTo(type = "TEAMMATE", direction = Direction.BOTH)
	public
	@Fetch
	Set<User> teammates = new HashSet<>();

	@GraphId
	Long id;

	/**
	 * Domain shortcut, e.g. rap, ban, etc.
	 */
	private String domainShortName;

	private String fullName;

	private String email;

	private String department;

	private String photoPath;

	public User() {
	}

	public User(String domainShortName, String fullName, String email, String department, String photoPath){
		this.domainShortName = domainShortName;
		this.fullName = fullName;
		this.email = email;
		this.department = department;
		this.photoPath = photoPath;
	}

	public void playsWith(User person) {
		teammates.add(person);
	}

	public Set<User> getTeammates() {
		return teammates;
	}

	public void setTeammates(Set<User> teammates) {
		this.teammates = teammates;
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
}
