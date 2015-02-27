package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author rap
 */
@Controller(value = "profile")
public class UserProfileController {

	@Autowired
	MatchRepository matchRepository;

	@RequestMapping("/user_profile")
	public String index(Model model, Authentication authentication) {
		UserDetails principal = (UserDetails) authentication.getPrincipal();

		String domainUserName = principal.getDomainUserName();

		model.addAttribute("domain_name", domainUserName);
		model.addAttribute("full_name", principal.getFirstName() + " " + principal.getSecondName());
		model.addAttribute("email", principal.getEmail());
		model.addAttribute("department", principal.getDepartment());
		model.addAttribute("photo", principal.getPhoto());

		List<Match> matchList = matchRepository.findAllByUserDomainShortName(domainUserName);
		long countWon = countWon(matchList, domainUserName);
		long countLost = matchList.size() - countWon;

		model.addAttribute("numberOfMatches", matchList.size());
		model.addAttribute("won", countWon);
		model.addAttribute("lost", countLost);

		return "user_profile";
	}

	private long countWon(List<Match> matchList, String domainShortName) {
		return matchList.stream()
				.filter(match -> match.teamWithUserWins(domainShortName))
				.count();
	}

}
