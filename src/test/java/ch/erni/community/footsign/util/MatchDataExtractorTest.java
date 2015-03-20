package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.TeamResults;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.MatchTest;
import ch.erni.community.footsign.nodes.User;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rap
 */
public class MatchDataExtractorTest {

	@Test
	public void testExtractUsersToMap() throws Exception {
		MatchDataExtractor matchDataExtractor = new MatchDataExtractor();

		List<Match> matches = new ArrayList<>();

		matches.add(generateMatchTeam1Wins());
		matches.add(generateMatchTeam1Wins());
		matches.add(generateMatchTeam1Wins());

		matches.add(generateMatchTeam2Wins());
		matches.add(generateMatchTeam2Wins());
		matches.add(generateMatchTeam2Wins());

		TeamResults teamResults = matchDataExtractor.extractUsersToMap(matches);

		Assert.assertEquals(4, teamResults.getWinners().size());

		teamResults.getWinners().entrySet().forEach(entry -> Assert.assertTrue(entry.getValue() == 3 || entry.getValue() == 0));

		Assert.assertEquals(4, teamResults.getLosers().size());

		teamResults.getLosers().entrySet().forEach(entry -> Assert.assertTrue(entry.getValue() == 3 || entry.getValue() == 0));
	}

	private Match generateMatchTeam2Wins() {
		Match match = new Match();

		User user1 = MatchTest.generateUser("rap");
		User user2 = MatchTest.generateUser("cepe");

		User user3 = MatchTest.generateUser("bad");
		User user4 = MatchTest.generateUser("veda");

		match.addPlayersToTeam1(user1);
		match.addPlayersToTeam1(user2);

		match.addPlayersToTeam2(user3);
		match.addPlayersToTeam2(user4);

		match.addGame(MatchTest.generateTeam2WinningGame());
		match.addGame(MatchTest.generateTeam2WinningGame());

		return match;
	}

	private Match generateMatchTeam1Wins() {
		Match match = new Match();

		User user1 = MatchTest.generateUser("rap");
		User user2 = MatchTest.generateUser("cepe");

		User user3 = MatchTest.generateUser("bad");
		User user4 = MatchTest.generateUser("veda");

		match.addPlayersToTeam1(user1);
		match.addPlayersToTeam1(user2);

		match.addPlayersToTeam2(user3);
		match.addPlayersToTeam2(user4);

		match.addGame(MatchTest.generateTeam1WinningGame());
		match.addGame(MatchTest.generateTeam1WinningGame());

		return match;
	}
}