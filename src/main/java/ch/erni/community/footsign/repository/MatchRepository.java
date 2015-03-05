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
public interface MatchRepository extends CrudRepository<Match, Long> {

	@Query("match(user:User {domainShortName: {0}})--(m:Match) return m")
	List<Match> findAllByUserDomainShortName(String domainShortName);

	@Query("MATCH (user:User)--(m: Match) RETURN user")
	List<User> findAllPlayedPlayers();

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) return g")
	List<Game> findAllGamesByUserDomainShortName(String domainShortName);

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) WHERE g.team1Result = 8 return g")
	List<Game>findAllTeam1WinsGameByUserDomainShortName(String doma);

	@Query("match(user:User {domainShortName: {0}})--(m:Match)-[r:GAMES]->(g) WHERE g.team2Result = 8 return g")
	List<Game>findAllTeam2WinsGameByUserDomainShortName(String doma);

	@Query("MATCH (n)-[r:TEAM1]->(m) RETURN DISTINCT n ORDER BY n.dateOfMatch DESC LIMIT 10")
	List<Match> findlastMatches();
}
