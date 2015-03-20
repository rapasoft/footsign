package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
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

	@Query("MATCH (m {planed : false})-[r:TEAM1]->(u) RETURN DISTINCT m ORDER BY m.dateOfMatch DESC LIMIT 10")
	List<Match> findlastMatches();

	@Query("MATCH (user:User {domainShortName: {0}})--(m:Match {planed : false}) RETURN DISTINCT m ORDER BY m.dateOfMatch DESC LIMIT 10")
	List<Match> findLastMatchesByDomainName(String domainName);

	@Query("match (u:User)--(m:Match {planed : false}) with u,count(m) as matches return u order by matches desc limit 1")
	User findPlayerWithMostPlayedMatches();

	@Query("match (user:User)--(m:Match {planed : false}) with user,count(m) as value return user , value order by value desc limit 10")
	List<CustomPlayer> findTenPlayerWithMostMatches();

	@Query("match (user:User)<--(m:Match {planed : false})-->(g:Game)\n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with user,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"match (user)<--(m2:Match {planed : false}) with user,count(m2) as allMatches, wonMatches\n" +
			"return user, wonMatches*(1.0) / allMatches*(1.0) as value\n" +
			"order by value desc limit 10")
	List<CustomPlayer> findTenPlayersWithHighestRatio();

	@Query("match (u:User)<--(m:Match {planed : false})-->(g:Game) \n" +
			"where (((u)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((u)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with u,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with u,count(distinct m) as matches\n" +
			"return u " +
			"order by matches desc limit 1")
	User findPlayerWithMostWins();

	@Query("match (user:User)<--(m:Match {planed : false})-->(g:Game) \n" +
			"where (((user)-[:TEAM1]-(m)-->(g) and g.team1Result = 8) OR ((user)-[:TEAM2]-(m)-->(g) and g.team2Result = 8))\n" +
			"with user,m,count(g) as countGames \n" +
			"where countGames >= 2\n" +
			"with user,count(distinct m) as value\n" +
			"return user,  value " +
			"order by value desc limit 10")
	List<CustomPlayer> findPlayerBestTenPlayers();

	@Query("Match (m:Match {planed : true} ) return m")
	List<Match> findAllPlanMatches();

	@Query("match(user:User {domainShortName: {0}})--(m:Match {planed : true}) return m")
	List<Match> findAllPlanMatchesForUser(String domainShortName);

	/**
	 * Input parameters
	 * long today = Calendar.getInstance().getTimeInMillis();
	 * Calendar tomorrow = Calendar.getInstance();
	 * tomorrow.add(Calendar.DATE, 1);
	 * tomorrow.set(Calendar.HOUR_OF_DAY, 0);
	 * tomorrow.set(Calendar.MINUTE, 0);
	 * tomorrow.set(Calendar.SECOND, 0);
	 * tomorrow.set(Calendar.MILLISECOND, 0);
	 */
	@Query("match (m:Match {planed : true}) \n" +
			"where m.dateOfMatch >= {0} and m.dateOfMatch < {1} \n" +
			"return distinct m")
	List<Match> findAllPlanMatchesForToday(long from, long to);
	//is ocupated time

	@Query("Match (m:Match {planed : true} ) where m.dateOfMatch = {0} return m")
	Match findMatchForThisDate(long time);


}
