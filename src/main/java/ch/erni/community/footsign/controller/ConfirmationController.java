package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by cepe on 20.03.2015.
 */

@Controller(value = "confirmationController")
public class ConfirmationController {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private UserHolder userHolder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailService mailService;
    
    @RequestMapping(value = "/confirmations", method = RequestMethod.GET)
    public String index(Model model) {

        User user = userHolder.getLoggedUser();
        if (user != null) {
            List<Match> matchesForConfirmation = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());

            List<Match> confirmationFromTeam1 = matchesForConfirmation.stream().filter(m->m.getTeam1().contains(user))
                    .filter(m->m.getTeam1().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

            List<Match> confirmationFromTeam2 = matchesForConfirmation.stream().filter(m->m.getTeam2().contains(user))
                    .filter(m->m.getTeam2().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

            matchesForConfirmation.removeAll(confirmationFromTeam1);
            matchesForConfirmation.removeAll(confirmationFromTeam2);
            model.addAttribute("matches", matchesForConfirmation);
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
        match.setState(MatchState.CONFIRMED);
        matchRepository.save(match);
        return modelAndView;
    }

    @RequestMapping(value = "/cancel_match", method = RequestMethod.POST)
    public ModelAndView cancelMatch(String matchId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));
        matchRepository.delete(Long.valueOf(matchId));
        return modelAndView;
    }
}
