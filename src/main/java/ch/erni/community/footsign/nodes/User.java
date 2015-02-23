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
	Set<User> teammates = new HashSet<User>();

	@GraphId
	Long id;

	/**
	 * Domain shortcut, e.g. rap, ban, etc.
	 */
	private String domainShortName;

	private String fullName;

	public User() {
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

}
