package ch.erni.community.footsign.test;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.util.LdapUserHelper;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author rap
 */
@Component
public class TestDataGenerator {

	@Autowired
	ErniLdapCache erniLdapCache;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	LdapUserHelper ldapUserHelper;

	public void generateUserData() {
		List<Match> matches = generateMatches(50);
		matches.forEach(matchRepository::save);

		List<Match> planMatches = generatePlanMatches(20);
		planMatches.forEach(matchRepository :: save);
	}

	public void generatePlayedMatches(){
		List<Match> playedMatches = generatePlayedMatches(6);
		playedMatches.forEach(matchRepository::save);
	}

	public List<Match> generateMatches(int numberOfMatches) {
		List<Match> matches = new ArrayList<>();

		for (int i = 0; i < numberOfMatches; i++) {
			Match match = new Match();
			match.setState(MatchState.CONFIRMED);
			Date date = Date.from(Instant.parse("2015-01-" + String.format("%02d", ((i % 30) + 1)) + "T" +
					String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z"));
			match.setDateOfMatch(date.getTime());
			match.addGame(generateGame());
			match.addGame(generateGame());

			if (match.isDraw()) {
				match.addGame(generateGame());
			}

			addUsers(match);

			matches.add(match);
		}

		return matches;
	}

	List<Match> generatePlanMatches(int numberOfMatches) {
		List<Match> matches = new ArrayList<>();
		for (int i = 0; i < numberOfMatches; i++) {
			Match match = new Match();
			match.setState(MatchState.PLANNED);
			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH) +1 ;
			int day = today.get(Calendar.DAY_OF_MONTH) + (int) (Math.random()*10);

			Date date = Date.from(Instant.parse("2015-" + String.format("%02d", month) + "-" + String.format("%02d", (( day % 30 ) + 1)) + "T" +
					String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z"));
			match.setDateOfMatch(date.getTime());
			match.addGame(generateGame());
			match.addGame(generateGame());

			if (match.isDraw()) {
				match.addGame(generateGame());
			}

			addUsers(match);

			matches.add(match);
		}

		return matches;
	}

	List<Match> generatePlayedMatches(int numberOfMatches) {
		List<Match> matches = new ArrayList<>();
		for (int i = 0; i < numberOfMatches; i++) {
			Match match = new Match();
			match.setState(MatchState.PLAYED);
			Calendar today = Calendar.getInstance();
			int month = today.get(Calendar.MONTH) +1 ;
			int day = today.get(Calendar.DAY_OF_MONTH) + (int) (Math.random()*10);

			Date date = Date.from(Instant.parse("2015-" + String.format("%02d", month) + "-" + String.format("%02d", (( i % 30 ) + 1)) + "T" +
					String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z"));
			match.setDateOfMatch(date.getTime());
			match.addGame(generateGame());
			match.addGame(generateGame());

			if (match.isDraw()) {
				match.addGame(generateGame());
			}

			addUsers(match);

			matches.add(match);
		}

		return matches;
	}



	private void addUsers(Match match) {
		User selectUser1 = selectUser();
		User selectUser2 = selectUser(selectUser1);
		match.addPlayersToTeam1(selectUser1);
		match.addPlayersToTeam1(selectUser2);

		User selectUser3 = selectUser(selectUser1, selectUser2);
		User selectUser4 = selectUser(selectUser1, selectUser2, selectUser3);
		match.addPlayersToTeam2(selectUser3);
		match.addPlayersToTeam2(selectUser4);
	}

	public User selectUser(User... excludes) {
		List<UserDetails> filtered = erniLdapCache
				.fetchEskEmployees()
				.stream()
				.filter(userDetails -> userDetails.getDomainUserName().matches("veda|rap|ban|cepe|pee|vap|bol|sup"))
				.collect(Collectors.toList());

		Collections.shuffle(filtered);

		UserDetails userDetails = filtered
				.stream()
				.filter(
						ud -> Arrays.asList(excludes)
								.stream()
								.allMatch(user -> !user.getDomainShortName().equals(ud.getDomainUserName())))
				.findAny()
				.get();

		User user = userRepository.findByDomainShortName(userDetails.getDomainUserName());

		if (user != null) {
			return user;
		} else {
			
			User userNew = ldapUserHelper.createUserFromLdapUser(userDetails).get();
			
			userRepository.save(userNew);

			return userNew;
		}
	}

	public Game generateGame() {
		Game game = new Game();
		int result1 = (int) (Math.random() * 9);
		int result2 = result1;

		while (result2 == result1) {
			result2 = (int) (Math.random() * 9);
		}

		if (result1 > result2) {
			result1 = 8;
		} else {
			result2 = 8;
		}

		game.setTeam1Result(result1);
		game.setTeam2Result(result2);

		return game;
	}
}
