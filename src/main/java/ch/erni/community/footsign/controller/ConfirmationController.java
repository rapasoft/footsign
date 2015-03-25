package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.service.MailService;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.mail.MessagingException;
import java.util.ArrayList;
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
    UserRepository userRepository;

    @Autowired
    MailService mailService;

    ErniUserDetails principal;
    
    @RequestMapping(value = "/confirmations", method = RequestMethod.GET)
    public String index(Model model, Authentication authentication ) {

        principal = (ErniUserDetails) authentication.getPrincipal();
        if (principal != null) {
            User user = userRepository.findByDomainShortName(principal.getDomainUserName());
            List<Match> matchesForConfirmation = matchRepository.findPlayedMatchesForUser(principal.getDomainUserName());

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
    public ModelAndView confirmMatch(String matchId, Authentication authentication ) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));

        ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();
        User user = userRepository.findByDomainShortName(principal.getDomainUserName());
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