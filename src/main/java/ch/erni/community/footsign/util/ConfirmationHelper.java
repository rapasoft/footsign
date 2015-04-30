package ch.erni.community.footsign.util;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rap
 */
@Component
public class ConfirmationHelper {

	public List<Match> getMatchesForConfirmation(User user, List<Match> allMatches) {

		List<Match> list = new ArrayList<>(allMatches);

		List<Match> confirmationFromTeam1 = list.stream().filter(m -> m.getTeam1().contains(user))
				.filter(m -> m.getTeam1().stream().anyMatch(u -> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

		List<Match> confirmationFromTeam2 = list.stream().filter(m -> m.getTeam2().contains(user))
				.filter(m -> m.getTeam2().stream().anyMatch(u -> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

		list.removeAll(confirmationFromTeam1);
		list.removeAll(confirmationFromTeam2);

		return list;

	}

	public List<Match> getMatchesAlreadyConfirmed(User user, List<Match> allMatches) {

		List<Match> list = new ArrayList<>(allMatches);

		List<Match> confirmationFromTeam1 = list.stream().filter(m -> m.getTeam1().contains(user))
				.filter(m -> m.getTeam1().stream().anyMatch(u -> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

		List<Match> confirmationFromTeam2 = list.stream().filter(m -> m.getTeam2().contains(user))
				.filter(m -> m.getTeam2().stream().anyMatch(u -> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

		confirmationFromTeam1.addAll(confirmationFromTeam2);

		return confirmationFromTeam1;
	}

}
