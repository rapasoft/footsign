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

	@GraphId
	Long id;

	public String name;

	public User() {
	}

	public User(String name) {
		this.name = name;
	}

	@RelatedTo(type = "TEAMMATE", direction = Direction.BOTH)
	public
	@Fetch
	Set<User> teammates = new HashSet<User>();

	public void playsWith(User person) {
		teammates.add(person);
	}

	public String toString() {
		String results = name + "'s teammates include\n";
		if (teammates != null) {
			for (User person : teammates) {
				results += "\t- " + person.name + "\n";
			}
		}
		return results;
	}

}
