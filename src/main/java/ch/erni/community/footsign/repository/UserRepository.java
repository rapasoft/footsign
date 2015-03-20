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
			"with u as user, count(distinct m) as value \n" +
			"return user, value \n" +
			"order by value desc limit 10")
	List<CustomPlayer> findPlayersWithWorstScore();

	/*
	MOst player team
	 */
	@Query("match (user1:User)--(m:Match)--(user2:User)\n" +
			"where ((user1)<-[:TEAM2]-(m)-[:TEAM2]->(user2) OR(user1)<-[:TEAM1]-(m)-[:TEAM1]->(user2) ) AND ID(user1) < ID(user2)\n" +
			"with user1, user2, count(distinct m) as value\n" +
			"return user1, user2,  value\n" +
			"order by value desc Limit 10")
	List<TeamPlayers> findTeamWithMostMatches();

	@Query("match (user1:User)--(m:Match)--(user2:User), (m)-->(g:Game)\n" +
			"where ((\n" +
			"(user1)-[:TEAM2]-(m)-->(g)\n" +
			"and (user2)-[:TEAM2]-(m)-->(g)\n" +
			"and g.team2Result =8)\n" +
			"OR (\n" +
			" (user1)-[:TEAM1]-(m)-->(g)\n" +
			"and (user2)-[:TEAM1]-(m)-->(g)\n" +
			"and g.team1Result =8\n" +
			")) AND ID(user1) < ID(user2)\n" +
			"with user1, user2, count(distinct g) as games, m\n" +
			"where games >= 2\n" +
			"with user1, user2, count(distinct m) as value\n" +
			"return user1, user2, value\n" +
			"order by value desc LIMIT 10")
	List<TeamPlayers> findBestTenTeams();

	@Query("match (user1:User)--(m:Match)--(user2:User), (m)-->(g:Game)\n" +
			"where ((\n" +
			"(user1)-[:TEAM2]-(m)-->(g)\n" +
			"and (user2)-[:TEAM2]-(m)-->(g)\n" +
			"and g.team2Result <8)\n" +
			"OR (\n" +
			" (user1)-[:TEAM1]-(m)-->(g)\n" +
			"and (user2)-[:TEAM1]-(m)-->(g)\n" +
			"and g.team1Result <8\n" +
			")) AND ID(user1) < ID(user2)\n" +
			"with user1, user2, count(distinct g) as games, m\n" +
			"where games >= 2\n" +
			"with user1, user2, count(distinct m) as value\n" +
			"return user1, user2,  value\n" +
			"order by value desc LIMIT 10")
	List<TeamPlayers> findWorstTenTeams();

	@Query("match (user:User)<--(m:Match)-->(g:Game)\n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 0) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 0))\n" +
			"return  user,count(distinct g)  as value\n" +
			"order by value desc LIMIT 10")
	List<CustomPlayer> findTenMostUnderTablePlayers();
}
