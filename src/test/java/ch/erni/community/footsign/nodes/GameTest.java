package ch.erni.community.footsign.nodes;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameTest {

	@Test
	public void testGenerateUnfinishedGame() {
		Game game = new Game();

		game.setTeam2Result(1);
		game.setTeam2Result(0);

		assertFalse(game.team1Wins());
		assertFalse(game.team2Wins());
	}

	@Test
	public void testGenerateGameWithWinner1() {
		Game game = new Game();

		game.setTeam1Result(8);
		game.setTeam2Result(0);

		assertTrue(game.team1Wins());
		assertFalse(game.team2Wins());
	}

	@Test
	public void testGenerateGameWithWinner2() {
		Game game = new Game();

		game.setTeam1Result(0);
		game.setTeam2Result(8);

		assertFalse(game.team1Wins());
		assertTrue(game.team2Wins());
	}


}