package ch.erni.community.footsign.nodes;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author rap
 */
@RunWith(Parameterized.class)
public class UserWonMatchTest {

	private String userName1;

	private String userName2;

	private String userName3;

	private String userName4;

	private boolean user1Wins;

	private boolean user2Wins;

	private boolean user3Wins;

	private boolean user4Wins;

	public UserWonMatchTest(String userName1, String userName2, String userName3, String userName4, boolean user1Wins, boolean user2Wins, boolean user3Wins, boolean user4Wins) {
		this.userName1 = userName1;
		this.userName2 = userName2;
		this.userName3 = userName3;
		this.userName4 = userName4;
		this.user1Wins = user1Wins;
		this.user2Wins = user2Wins;
		this.user3Wins = user3Wins;
		this.user4Wins = user4Wins;
	}

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(new Object[][]{
				{"rap", "bad", "cepe", "veda", true, true, false, false},
				{"rap", "bad", "cepe", "veda", false, false, true, true},
		});
	}

	@Test
	public void testTeamWithUserWins() throws Exception {
		Match match = new Match();

		User user1 = MatchTest.generateUser(userName1);
		User user2 = MatchTest.generateUser(userName2);
		User user3 = MatchTest.generateUser(userName3);
		User user4 = MatchTest.generateUser(userName4);

		match.addPlayersToTeam1(user1);
		match.addPlayersToTeam1(user2);

		match.addPlayersToTeam2(user3);
		match.addPlayersToTeam2(user4);

		if (user1Wins && user2Wins) {
			match.addGame(MatchTest.generateTeam1WinningGame());
			match.addGame(MatchTest.generateTeam1WinningGame());
		} else if (user3Wins && user4Wins) {
			match.addGame(MatchTest.generateTeam2WinningGame());
			match.addGame(MatchTest.generateTeam2WinningGame());
		}

		assertEquals(user1Wins, match.teamWithUserWins(userName1));
		assertEquals(user2Wins, match.teamWithUserWins(userName2));
		assertEquals(user3Wins, match.teamWithUserWins(userName3));
		assertEquals(user4Wins, match.teamWithUserWins(userName4));
	}

}
