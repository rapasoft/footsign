package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.TeamPlayers;
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
			"where countGames >= 2 \n" +
			"with u as user, count(distinct m) as matches \n" +
			"return user, matches \n" +
			"order by matches desc limit 10")
	List<CustomPlayer> findPlayersWithWorstScore();

	/*
	MOst player team
	 */
	@Query("match (user1:User)--(m:Match)--(user2:User)\n" +
			"where ((user1)<-[:TEAM2]-(m)-[:TEAM2]->(user2) OR(user1)<-[:TEAM1]-(m)-[:TEAM1]->(user2) ) AND ID(user1) < ID(user2)\n" +
			"with user1, user2, count(distinct m) as matches\n" +
			"return user1, user2,  matches\n" +
			"order by matches desc Limit 10")
	List<TeamPlayers> findTeamWithMostMatches();


}
