package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "matchStatistics")
public class MatchStatsController {
    
    @Autowired
    MatchRepository matchRepository;
    
    @RequestMapping("/stats_match")
    public String teamStats(Model model) {

        List<Match> matches = matchRepository.findlastMatches();
        model.addAttribute("last_matches", matches);
        
        return "stats_match";
    }
}
