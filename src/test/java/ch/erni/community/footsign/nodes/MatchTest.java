package ch.erni.community.footsign.nodes;

import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class MatchTest {

	public static Game generateTeam1WinningGame() {
		Game gameTeam1Wins = new Game();
		gameTeam1Wins.setTeam1Result(8);
		gameTeam1Wins.setTeam2Result(0);
		return gameTeam1Wins;
	}

	public static Game generateTeam2WinningGame() {
		Game gameTeam1Wins = new Game();
		gameTeam1Wins.setTeam2Result(8);
		gameTeam1Wins.setTeam1Result(0);
		return gameTeam1Wins;
	}

	public static User generateUser(String s) {
		User user = mock(User.class);
		Mockito.when(user.getDomainShortName()).thenReturn(s);
		return user;
	}

	@Test
	public void testTeam1Wins() throws Exception {
		Match match = new Match();

		match.addGame(generateTeam1WinningGame());
		match.addGame(generateTeam1WinningGame());

		assertTrue(match.team1Wins());
	}

	@Test
	public void testTeam2Wins() throws Exception {
		Match match = new Match();

		match.addGame(generateTeam2WinningGame());
		match.addGame(generateTeam2WinningGame());

		assertTrue(match.team2Wins());
	}

	@Test
	public void testIsDraw() throws Exception {
		Match match = new Match();

		match.addGame(generateTeam2WinningGame());
		match.addGame(generateTeam1WinningGame());

		assertTrue(match.isDraw());
	}

	@Test
	public void testTeamWithUserWins() throws Exception {
		Match match = new Match();

		User user1 = generateUser("rap");
		User user2 = generateUser("cepe");
		User user3 = generateUser("bad");
		User user4 = generateUser("veda");

		match.addPlayersToTeam1(user1);
		match.addPlayersToTeam1(user2);

		match.addPlayersToTeam2(user3);
		match.addPlayersToTeam2(user4);

		match.addGame(generateTeam1WinningGame());
		match.addGame(generateTeam1WinningGame());

		assertTrue(match.teamWithUserWins("rap"));
		assertTrue(match.teamWithUserWins("cepe"));
		assertFalse(match.teamWithUserWins("bad"));
		assertFalse(match.teamWithUserWins("veda"));
	}

}