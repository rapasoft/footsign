package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.util.ConfirmationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author rap
 */
@RestController
@RequestMapping("/unconfirmed_matches")
public class UnconfirmedMatchesController {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private ConfirmationHelper confirmationHelper;

	@Autowired
	private UserHolder userHolder;


	@RequestMapping(method = RequestMethod.GET)
	public int getUnconfirmedMatchesCount() {
		User user = userHolder.getLoggedUser();
		if (user != null) {
			List<Match> allMatches = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());
			return confirmationHelper.getMatchesForConfirmation(user, allMatches).size();
		}

		return 0;
	}

}
