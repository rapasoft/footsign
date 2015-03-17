package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.ClientMatch;
import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.util.CalendarHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private CalendarHelper calendarHelper;
    
    @RequestMapping("/plane_match")
    public String home(Model model) {

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
                pl.setMatch(match.get());
            }

            result.put(cal.getTimeInMillis(), pl);

            cal.add(Calendar.MINUTE, INTERVAL);
        }
        
        
        return result;
    }

    @RequestMapping(value = "/saveMatchAsPlanned", method = RequestMethod.POST)
    public ModelAndView saveGame(@ModelAttribute @Valid Match match, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/plane_match");

        System.out.println(match);

        return  modelAndView;
    }
    
    @RequestMapping("plannedMatchesOneDay")
    public String getPlannedMatchForDay(@RequestParam(value = "dateToShow") String dateToShow, Model model) {
        try {
            Calendar calendar = calendarHelper.getSpecificDate(dateToShow, "dd.MM.yyyy");

            Map<Long, PlannedMatch> plannedMatchMap = createPlannedMap(calendar);
            model.addAttribute("matches", plannedMatchMap);
            
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return "fragments/ajax_templates :: #plannedMatchesOneDay";
    }
}
