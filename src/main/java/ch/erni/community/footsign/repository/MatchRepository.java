package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by veda on 2/19/2015.
 */
public interface MatchRepository extends CrudRepository<Match, Long>, MatchRepositoryCustom {

	@Query("match(user:User {domainShortName: {0}})--(m:Match) return m")
	List<Match> findAllByUserDomainShortName(String domainShortName);

	@Query("MATCH (n)-[r:TEAM1]->(m) RETURN m")
	List<User> findAllPlayedPlayers();

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) return g")
	List<Game> findAllGamesByUserDomainShortName(String domainShortName);

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) WHERE g.team1Result = 8 return g")
	List<Game>findAllTeam1WinsGameByUserDomainShortName(String doma);

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) WHERE g.team2Result = 8 return g")
	List<Game>findAllTeam2WinsGameByUserDomainShortName(String doma);

	@Query("MATCH (n)-[r:TEAM1]->(m) RETURN DISTINCT n ORDER BY n.dateOfMatch DESC LIMIT 10")
	List<Match> findlastMatches();

	@Query("match (u:User)--(m:Match) with u,count(m) as matches return u order by matches desc limit 1")
	User findPlayerWithMostPlayedMatches();

	@Query("match (user:User)--(m:Match) with user,count(m) as matches return user , matches order by matches desc limit 10")
	List<CustomPlayer> findTenPlayerWithMostMatches();

	// TODO @rap: make generic for user
	@Query("match (u:User)--(m:Match) with u, count(distinct(m)) as matches return matches order by matches desc limit 1")
	int countPlayedMatches(User userWmostPlayed);

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
			"with user,count(distinct m) as matches\n" +
			"return user,  matches " +
			"order by matches desc limit 10")
	List<CustomPlayer> findPlayerBestTenPlayers();

	// TODO @rap: make generic for user
	@Query("match (u:User)<--(m:Match)-->(g:Game) \n" +
			"where (((u)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((u)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with u,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with u,count(distinct m) as matches\n" +
			"return matches " +
			"order by matches desc limit 1")
	int countWonMatches(User userWmostWins);
}
