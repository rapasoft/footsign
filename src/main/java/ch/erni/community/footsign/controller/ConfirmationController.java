package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
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
 * Created by cepe on 20.03.2015.
 */

@Controller(value = "confirmationController")
public class ConfirmationController {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @RequestMapping(value = "/confirmations", method = RequestMethod.GET)
    public String index(Model model, Authentication authentication) {

        ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();
        if (principal != null) {
            // todo: call function for each unconfirmed matches
            List<Match> matchesForConfirmation = matchRepository.findAllPlanMatchesForUser(principal.getDomainUserName());
            model.addAttribute("matches", matchesForConfirmation);
        }
        
        return "confirmations";
    }
    
    @RequestMapping(value = "/confirm_match", method = RequestMethod.POST)
    public ModelAndView confirmMatch(String matchId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));
        
        //todo: call action for confirmation the match
        
        return modelAndView;
    }

    @RequestMapping(value = "/cancel_match", method = RequestMethod.POST)
    public ModelAndView cancelMatch(String matchId) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("confirmations"));

        //todo: call action for cancellation the match
        
        return modelAndView;
    }
}
