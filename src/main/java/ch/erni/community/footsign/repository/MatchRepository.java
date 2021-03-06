package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by veda on 2/19/2015.
 */
public interface MatchRepository extends CrudRepository<Match, Long>, MatchRepositoryCustom {

	@Query("match(user:User {domainShortName: {0}})--(m:Match) return distinct m")
	List<Match> findAllByUserDomainShortName(String domainShortName);

	@Query("MATCH (n)-[r:TEAM1]->(m) RETURN DISTINCT n ORDER BY n.dateOfMatch DESC LIMIT 10")
	List<Match> findlastMatches();

	@Query("MATCH (user:User {domainShortName: {0}})--(m:Match) RETURN DISTINCT m ORDER BY m.dateOfMatch DESC LIMIT 10")
	List<Match> findLastMatchesByDomainName(String domainName);

	@Query("match (u:User)--(m:Match) with u,count(distinct m) as matches return u order by matches desc limit 1")
	User findPlayerWithMostPlayedMatches();

	@Query("match (user:User)--(m:Match) with user,count(m) as value return user , value order by value desc limit 10")
	List<CustomPlayer> findTenPlayerWithMostMatches();

	@Query("match (user:User)<--(m:Match)-->(g:Game)\n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with user,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with user,count(distinct m) as wonMatches \n" +
			"match (user)<--(m2:Match) with user,count(m2) as allMatches, wonMatches\n" +
			"where allMatches > 4\n" +
			"return user, wonMatches*(1.0) / allMatches*(1.0) as value\n" +
			"order by value desc limit 10")
	List<CustomPlayer> findTenPlayersWithHighestRatio();
	
	@Query("match (u:User)<--(m:Match)-->(g:Game) \n" +
			"where (((u)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((u)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with u,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with u,count(distinct m) as matches\n" +
			"return u " +
			"order by matches desc limit 1")
	User findPlayerWithMostWins();

	@Query("match (user:User)<--(m:Match)-->(g:Game) \n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with user,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with user,count(distinct m) as value\n" +
			"return user,  value " +
			"order by value desc limit 10")
	List<CustomPlayer> findPlayerBestTenPlayers();

}
