package ch.erni.community.footsign.test;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TestDataGeneratorTest {

	private TestDataGenerator testDataGenerator;

	@Before
	public void before() {
		testDataGenerator = new TestDataGenerator();
		testDataGenerator.erniLdapCache = new ErniLdapCache();
		UserRepository userRepository = mock(UserRepository.class);
		when(userRepository.findByDomainShortName(anyString())).thenReturn(null);
		testDataGenerator.userRepository = userRepository;
	}

	@Test
	public void testGenerateMatches() throws Exception {
		List<Match> matchList = testDataGenerator.generateMatches(5);

		assertNotNull(matchList);

		for (Match match : matchList) {
			assertNotNull(match.getDateOfMatch());

			if (match.getGames().size() == 2) {
				Set<Game> games = match.getGames();

				Iterator<Game> iterator = games.iterator();

				boolean team1Wins = iterator.next().team1Wins();
				boolean team2Wins = iterator.next().team2Wins();

				if (team1Wins && team2Wins) {
					fail("Both teams have two games each won. One another should be generated.");
				}

			}

			assertTrue(match.getGames().size() >= 2);

		}
	}

	@Test
	public void testManualMatchMaking() {
		Match match = new Match();

		Game game1 = new Game();
		game1.setTeam1Result(8);
		game1.setTeam2Result(0);

		match.addGame(game1);

		Game game2 = new Game();
		game2.setTeam1Result(0);
		game2.setTeam2Result(8);

		match.addGame(game2);

		assertTrue(match.isDraw());
	}

	@Test
	public void testSelectOneUser() {
		User user = testDataGenerator.selectUser();

		assertNotNull(user);
	}

	@Test
	public void testSelectMultipleUsers() {
		Set<User> users = new HashSet<>();

		User user1 = testDataGenerator.selectUser();
		User user2 = testDataGenerator.selectUser(user1);
		User user3 = testDataGenerator.selectUser(user1, user2);
		User user4 = testDataGenerator.selectUser(user1, user2, user3);

		users.addAll(Arrays.asList(user1, user2, user3, user4));

		assertEquals(4, users.size());
	}
}