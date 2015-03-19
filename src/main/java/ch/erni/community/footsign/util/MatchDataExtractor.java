package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.TeamResults;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author rap
 */
// TODO @rap: de-mystify please :)
public class MatchDataExtractor {

	public TeamResults extractUsersToMap(List<Match> matches) {
		final Map<String, Integer> winners = new HashMap<>();
		final Map<String, Integer> losers = new HashMap<>();

		initGraphData(matches, winners, losers);

		List<Match> team1Win = matches.stream().filter(Match::team1Wins).collect(Collectors.toList());
		List<Match> team2Win = matches.stream().filter(Match::team2Wins).collect(Collectors.toList());

		team1Win.forEach(m -> {
			m.getTeam1().forEach(t1 -> {
				addToMap(winners, t1);
			});
			m.getTeam2().forEach(t2 -> {
				addToMap(losers, t2);
			});
		});

		team2Win.forEach(m -> {
			m.getTeam2().forEach(t2 -> {
				addToMap(winners, t2);
			});
			m.getTeam1().forEach(t1 -> {
				addToMap(losers, t1);
			});
		});

		return new TeamResults(winners, losers);
	}

	void initGraphData(List<Match> matches, Map<String, Integer> winners, Map<String, Integer> losers) {
		for (Match match : matches) {
			Set<User> team1 = match.getTeam1();
			Set<User> team2 = match.getTeam2();

			team1.forEach(u -> {
				if (!winners.containsKey(u.getFullName())) {
					winners.put(u.getFullName(), 0);
				}
				if (!losers.containsKey(u.getFullName())) {
					losers.put(u.getFullName(), 0);
				}
			});
			team2.forEach(u -> {
				if (!winners.containsKey(u.getFullName())) {
					winners.put(u.getFullName(), 0);
				}
				if (!losers.containsKey(u.getFullName())) {
					losers.put(u.getFullName(), 0);
				}
			});
		}
	}

	void addToMap(Map<String, Integer> map, User u) {
		if (map != null && u != null) {
			int value = map.get(u.getFullName());
			value++;
			map.put(u.getFullName(), value);
		}
	}

}
