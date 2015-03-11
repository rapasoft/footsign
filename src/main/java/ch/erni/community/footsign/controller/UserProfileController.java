package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author rap
 */
@Controller(value = "profile")
public class UserProfileController {

	private static final String USER_PROFILE = "user_profile";

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	UserRepository userRepository;


	@RequestMapping("/user_profile")
	public String index(Model model, Authentication authentication) {

		ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();

		String domainUserName = principal.getDomainUserName();
		List<Match> lastMatches = matchRepository.findLastMatchesByDomainName(domainUserName);

		model.addAttribute("domain_name", domainUserName);
		model.addAttribute("full_name", principal.getFullName());
		model.addAttribute("email", principal.getEmail());
		model.addAttribute("department", principal.getDepartment());
		model.addAttribute("photo", principal.getPhotoPath());

		List<Match> matchList = matchRepository.findAllByUserDomainShortName(domainUserName);
		long countWon = countWon(matchList, domainUserName);
		long countLost = matchList.size() - countWon;

		model.addAttribute("numberOfMatches", matchList.size());
		model.addAttribute("won", countWon);
		model.addAttribute("lost", countLost);
		model.addAttribute("user", userRepository.findByDomainShortName(domainUserName));
		model.addAttribute("wonPlayedRatio", countRatio(countWon, matchList.size()));

		model.addAttribute("last_matches", lastMatches);
		return USER_PROFILE;
	}

	private double countRatio(long countWon, long countLost) {
		if (countWon == 0) {
			return 0;
		} else if (countLost == 0) {
			return 1;
		}
		return countWon / (double) countLost;
	}

	@RequestMapping(value = "/edit_user", method = RequestMethod.POST)
	public String editUser(@ModelAttribute User param, Model model, Authentication authentication) {

		UserDetails principal = (UserDetails) authentication.getPrincipal();
		User user = userRepository.findByDomainShortName(principal.getDomainUserName());
		user.setRating(param.getRating());

		userRepository.save(user);

		return index(model, authentication);
	}

	private long countWon(List<Match> matchList, String domainShortName) {
		return matchList.stream()
				.filter(match -> match.teamWithUserWins(domainShortName))
				.count();
	}

}
