package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author rap
 */
public interface UserRepository extends CrudRepository<User, String>, UserRepositoryCustom {

	User findByDomainShortName(String name);

	@Query("match (u:User)<--(m:Match)-->(g:Game) \n" +
			"where (((u)-[:TEAM1]-(m)-->(g) and g.team1Result < 8) OR ((u)-[:TEAM2]-(m)-->(g) and g.team2Result < 8)) \n" +
			"with u,m,count(g) as countGames \n" +
			"where countGames <= 1 \n" +
			"with u as user, count(distinct m) as matches \n" +
			"order by matches desc \n" +
			"return user, matches")
	List<CustomPlayer> findPlayersWithWorstScore();

}
