package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import org.springframework.data.neo4j.annotation.Query;

import java.util.List;

/**
 * User: ban
 * Date: 9. 3. 2015
 * Time: 15:54
 */

public interface TeamRepository {

	@Query("match (user:User)<--(m:Match)-->(g:Game) \n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with user,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with user,count(distinct m) as matches\n" +
			"return user.,  matches " +
			"order by matches desc limit 10")
	List<CustomPlayer> findBestTenTeams();
}
