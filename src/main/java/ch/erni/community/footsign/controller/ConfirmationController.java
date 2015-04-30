package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.service.MailService;
import ch.erni.community.footsign.util.ConfirmationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

/**
 * Created by cepe on 20.03.2015.
 */

@Controller(value = "confirmationController")
public class ConfirmationController {

	@Autowired
	UserRepository userRepository;

    @Autowired
	MailService mailService;

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private ConfirmationHelper confirmationHelper;

    @Autowired
	private UserHolder userHolder;

	@RequestMapping(value = "/confirmations", method = RequestMethod.GET)
    public String index(Model model) {

        User user = userHolder.getLoggedUser();
        if (user != null) {
            List<Match> allMatches = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());
			List<Match> notConfirmedMatches = confirmationHelper.getMatchesForConfirmation(user, allMatches);
			List<Match> confirmedMatches = confirmationHelper.getMatchesAlreadyConfirmed(user, allMatches);

            model.addAttribute("notConfirmedMatches", notConfirmedMatches);
            model.addAttribute("confirmedMatches", confirmedMatches);
        }
        
        return "confirmations";
    }

    @RequestMapping(value = "/confirm_match", method = RequestMethod.POST)
    public ModelAndView confirmMatch(String matchId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));

        User user = userHolder.getLoggedUser();
        Match match = matchRepository.findOne(Long.valueOf(matchId));
        match.confirmedByPlayer(user);
        if(match.getConfirmedBy().size() == 2) {
            match.setState(MatchState.CONFIRMED);
        }
        matchRepository.save(match);
        return modelAndView;
    }

    @RequestMapping(value = "/cancel_match", method = RequestMethod.POST)
    public ModelAndView cancelMatch(String matchId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));
        Match match = matchRepository.findOne(Long.valueOf(matchId));
        matchRepository.delete(Long.valueOf(matchId));
        mailService.sendCancelationMail(match);
        return modelAndView;
    }
}
