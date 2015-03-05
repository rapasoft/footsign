package ch.erni.community.footsign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "matchStatistics")
public class MatchStatsController {
    
    @RequestMapping("/stats_match")
    public String teamStats(Model model) {
        return "stats_match";
    }
}
