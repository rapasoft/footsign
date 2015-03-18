package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.ClientMatch;
import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.util.CalendarHelper;
import org.springframework.beans.factory.annotation.Autowired;
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

import javax.validation.Valid;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by cepe on 16.03.2015.
 */
@Controller(value = "planingMatch")
public class PlaneController {

    // todo: @cepe - read from properties file
    private final int INTERVAL = 30;
    private final int START_HOUR = 8;
    private final int END_HOUR = 19;
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CalendarHelper calendarHelper;
    
    private PlannedMatch defaultPlannedMatch;
    
    @RequestMapping("/plane_match")
    public String home(Model model, Authentication authentication) {
        this.defaultPlannedMatch = createDefaultPlannedMatch(authentication);
        model.addAttribute("plannedMatch", defaultPlannedMatch);
        
        return "plane_match";
    }
    
    private Map<Long, PlannedMatch> createPlannedMap(Calendar day) {
        Map<Long, PlannedMatch> result = new TreeMap<>();
        Calendar cal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        cal.setTime(day.getTime());
        cal.set(Calendar.HOUR_OF_DAY, START_HOUR);

        endCal.setTime(day.getTime());
        endCal.set(Calendar.HOUR_OF_DAY, END_HOUR);
        
        List<Match> plannedMatches = matchRepository.findAllPlanMatchesForToday(cal.getTimeInMillis(), endCal.getTimeInMillis());
        
        while (cal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            PlannedMatch pl = new PlannedMatch(new Date(cal.getTimeInMillis()));
            if (plannedMatches != null && !plannedMatches.isEmpty()) {
                Optional<Match> match = plannedMatches.stream().filter(m -> m.getDateOfMatch() == cal.getTimeInMillis()).findFirst();
                pl.setTimestamp(match.isPresent() ? match.get().getDateOfMatch() : null);
            }

            result.put(cal.getTimeInMillis(), pl);

            cal.add(Calendar.MINUTE, INTERVAL);
        }
        
        
        return result;
    }

    @RequestMapping(value = "/saveMatchAsPlanned", method = RequestMethod.POST)
    public ModelAndView saveGame(@ModelAttribute PlannedMatch plannedMatch, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("plane_match");

        if (bindingResult.hasErrors()) {
            return modelAndView;
        }

        try {
            validPlannedMatch(plannedMatch);
            userRepository.saveUsersToDB(plannedMatch.getTeam1());
            userRepository.saveUsersToDB(plannedMatch.getTeam2());
            Match match = createMatch(plannedMatch);

            matchRepository.save(match);
            modelAndView.addObject("success", "The match was sucessfully saved.");
            
        } catch (Exception e) {
            ObjectError error = new ObjectError("[global]", e.getMessage());
            bindingResult.addError(error);
            return modelAndView;
        }

        return  modelAndView;
    }
    
    @RequestMapping("plannedMatchesOneDay")
    public String getPlannedMatchForDay(@RequestParam(value = "dateToShow") String dateToShow, Model model) {
        try {
            Calendar calendar = calendarHelper.getSpecificDate(dateToShow, "dd.MM.yyyy");

            Map<Long, PlannedMatch> plannedMatchMap = createPlannedMap(calendar);
            model.addAttribute("matches", plannedMatchMap);
            model.addAttribute("plannedMatch", defaultPlannedMatch);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "fragments/ajax_templates :: #plannedMatchesOneDay";
    }
    
    private PlannedMatch createDefaultPlannedMatch(Authentication authentication) {

        PlannedMatch pm = new PlannedMatch();
        try {
            
            ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();
            String  domainUserName = principal.getDomainUserName();
            User u = userRepository.findByDomainShortName(domainUserName);

            pm.setCurrentUser(u);

            pm.addPlayersToTeam1(u.getDomainShortName());
            pm.addPlayersToTeam1("");
            pm.addPlayersToTeam2("");
            pm.addPlayersToTeam2("");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
        return pm;
    }
    
    private void validPlannedMatch(PlannedMatch plannedMatch) {
        if (plannedMatch == null) {
            System.err.println(this.getClass().getSimpleName() + ", validPlannedMatch -> PlannedMatch is null");
            throw new IllegalArgumentException("Some strange error is occurred. Please contact your most favorite Java team :)");
        }
        
        if (plannedMatch.getTimestamp() == null)
            throw new IllegalArgumentException("Time of planned match is not specified");

        if (plannedMatch.getTeam1() == null)
            throw new IllegalArgumentException("Team 1 is not specified");

        if (plannedMatch.getTeam2() == null)
            throw new IllegalArgumentException("Team 2 is not specified");
        
        List<String> team1 = plannedMatch.getTeam1();
        List<String> team2 = plannedMatch.getTeam2();
        long count1 = team1.stream().filter(p -> !p.isEmpty()).count();
        long count2 = team2.stream().filter(p -> !p.isEmpty()).count();

        if (count1 != count2)
            throw new IllegalArgumentException("Number of players have to be equals!");
        
        if (count1 < 1 || count1 > 2)
            throw new IllegalArgumentException("Number of players in team 1 have to be 1 or 2! Current number is " + count1);


        if (count2 < 1 || count2 > 2)
            throw new IllegalArgumentException("Number of players in team 2 have to be 1 or 2! Current number is " + count2);

        String users = "";
        for (String player : team1) {
            if (users.contains(player))
                throw new IllegalArgumentException("User can not play more than once: " + player);
            
            users += (player + ";");
        }

        for (String player : team2) {
            if (users.contains(player))
                throw new IllegalArgumentException("User can not play more than once: " + player);

            users += (player + ";");
        }

    }
    
    public Match createMatch(PlannedMatch plannedMatch) {
        if (plannedMatch != null) {
            Match match = new Match();
            match.setPlaned(true);
            match.setDateOfMatch(plannedMatch.getTimestamp());
            
            plannedMatch.getTeam1().forEach(p -> {
                User u = userRepository.findByDomainShortName(p);
                match.addPlayersToTeam1(u);
            });

            plannedMatch.getTeam2().forEach(p -> {
                User u = userRepository.findByDomainShortName(p);
                match.addPlayersToTeam2(u);
            });
            
            return match;
        }
        return null;
        
    }
}
