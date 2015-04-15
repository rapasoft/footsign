package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
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
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserHolder userHolder;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MailService mailService;
    
    @RequestMapping(value = "/confirmations", method = RequestMethod.GET)
    public String index(Model model) {

        User user = userHolder.getLoggedUser();
        if (user != null) {
            List<Match> allMatches = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());
            List<Match> notConfirmedMatches = getMatchesForConfirmation(user, allMatches);
            List<Match> confirmedMatches = getMatchesAlreadyConfirmed(user, allMatches);

            model.addAttribute("notConfirmedMatches", notConfirmedMatches);
            model.addAttribute("confirmedMatches", confirmedMatches);
        }
        
        return "confirmations";
    }

    public List<Match> getMatchesForConfirmation(User user, List<Match> allMatches) {
        
        List<Match> list = new ArrayList<>(allMatches);
        
        List<Match> confirmationFromTeam1 = list.stream().filter(m->m.getTeam1().contains(user))
                .filter(m->m.getTeam1().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

        List<Match> confirmationFromTeam2 = list.stream().filter(m->m.getTeam2().contains(user))
                .filter(m->m.getTeam2().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

        list.removeAll(confirmationFromTeam1);
        list.removeAll(confirmationFromTeam2);
        
        return list;

    }

    public List<Match> getMatchesAlreadyConfirmed(User user, List<Match> allMatches) {

        List<Match> list = new ArrayList<>(allMatches);
        
        List<Match> confirmationFromTeam1 = list.stream().filter(m->m.getTeam1().contains(user))
                .filter(m->m.getTeam1().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

        List<Match> confirmationFromTeam2 = list.stream().filter(m->m.getTeam2().contains(user))
                .filter(m->m.getTeam2().stream().anyMatch(u-> m.getConfirmedBy().contains(u))).collect(Collectors.toList());

        confirmationFromTeam1.addAll(confirmationFromTeam2);

        return confirmationFromTeam1;
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
