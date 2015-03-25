package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * @author rap
 */
@Controller(value = "profile")
public class UserProfileController {

	private static final String USER_PROFILE = "user_profile";

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserHolder userHolder;

	@RequestMapping("/user_profile")
	public String index(Model model) {

		User user = userHolder.getLoggedUser();

		String domainUserName = user.getDomainShortName();
		List<Match> lastMatches = matchRepository.findLastMatchesByDomainName(domainUserName);

		model.addAttribute("domain_name", domainUserName);
		model.addAttribute("full_name", user.getFullName());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("department", user.getDepartment());
		model.addAttribute("photo", user.getPhotoPath());

		List<Match> matchList = matchRepository.findAllByUserDomainShortName(domainUserName);
		long countWon = countWon(matchList, domainUserName);
		long countLost = matchList.size() - countWon;

		model.addAttribute("numberOfMatches", matchList.size());
		model.addAttribute("won", countWon);
		model.addAttribute("lost", countLost);
		model.addAttribute("user", userRepository.findByDomainShortName(domainUserName));
		model.addAttribute("wonPlayedRatio", countRatio(countWon, matchList.size()));
		model.addAttribute("wonLostRatioPercent", countRatio(countWon, matchList.size()) * 100);


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
	public ModelAndView editUser(@ModelAttribute User user, Model model) {

		User currentUser = userHolder.getLoggedUser();
		
		currentUser.setRating(user.getRating());
		currentUser.setPlannedMatchNofitication(user.isPlannedMatchNofitication());
		currentUser.setCancelledMatchNotification(user.isCancelledMatchNotification());
		currentUser.setConfirmMatchNotification(user.isConfirmMatchNotification());

		userRepository.save(currentUser);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setView(new RedirectView(USER_PROFILE));
		return modelAndView;
	}

	private long countWon(List<Match> matchList, String domainShortName) {
		return matchList.stream()
				.filter(match -> match.teamWithUserWins(domainShortName))
				.count();
	}

}
