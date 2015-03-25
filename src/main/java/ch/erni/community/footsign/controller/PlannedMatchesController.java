package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.UserHolder;
import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.service.MailService;
import ch.erni.community.footsign.util.CalendarHelper;
import ch.erni.community.footsign.util.PlannedMatchConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.util.*;

/**
 * Created by cepe on 16.03.2015.
 */
@Controller(value = "planingMatch")
public class PlannedMatchesController {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CalendarHelper calendarHelper;
    
    @Autowired
    private PlannedMatchConfigurator configurator;

    @Autowired
    private MailService mailService;
    
    @Autowired
    private UserHolder userHolder;
    
    private PlannedMatch defaultPlannedMatch;

    private Map<Long, PlannedMatch> plannedMatchMap;
    
    @RequestMapping("/plane_match")
    public String home(Model model) {
        User current = userHolder.getLoggedUser();
        this.defaultPlannedMatch = configurator.createDefaultPlannedMatch(current);
        model.addAttribute("plannedMatch", defaultPlannedMatch);
        
        return "plane_match";
    }

    @RequestMapping(value = "/saveMatchAsPlanned", method = RequestMethod.POST)
    public ModelAndView saveGame(@ModelAttribute PlannedMatch plannedMatch, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("plane_match");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        try {
            configurator.validPlannedMatch(plannedMatch);
            userRepository.saveUsersToDB(plannedMatch.getTeam1());
            userRepository.saveUsersToDB(plannedMatch.getTeam2());
            Match match = configurator.createMatch(plannedMatch);

            matchRepository.save(match);

            modelAndView.addObject("success", "The match was sucessfully planned.");
            mailService.sendPlaneMatchMail(match);

            
        } catch (Exception e) {

            if (e instanceof DataIntegrityViolationException) {
                modelAndView.addObject("error", "We are so sorry, but current match is reserved yet. Please, choose another one.");
            } else {
                modelAndView.addObject("error", e.getMessage());
            }
            return modelAndView;
        }

        return  modelAndView;
    }
    
    @RequestMapping(value = "cancelMatchFromPlanned", method = RequestMethod.POST)
    public ModelAndView cancelMatchFromPlanned(String timestamp) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setView(new RedirectView("plane_match"));
        
        if (timestamp != null && plannedMatchMap != null) {
            try {
                PlannedMatch plannedMatch = plannedMatchMap.get(Long.parseLong(timestamp));

                if (plannedMatch != null) {
                    Match match = matchRepository.findMatchForThisDate(plannedMatch.getTimestamp());
                    if (match != null) {
                        matchRepository.delete(match);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        
        return  modelAndView;
    }
    
    @RequestMapping("plannedMatchesOneDay")
    public String getPlannedMatchForDay(@RequestParam(value = "dateToShow") String dateToShow, Model model) {
        try {
            Calendar calendar = calendarHelper.getSpecificDate(dateToShow, "dd.MM.yyyy");

            plannedMatchMap = configurator.createPlannedMap(calendar, defaultPlannedMatch.getCurrentUser().getDomainShortName());
            model.addAttribute("matches", plannedMatchMap);
            model.addAttribute("plannedMatch", defaultPlannedMatch);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "fragments/ajax_templates :: #plannedMatchesOneDay";
    }
    

}
