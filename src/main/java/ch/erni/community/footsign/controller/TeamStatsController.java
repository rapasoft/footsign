package ch.erni.community.footsign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "teamStatistics")
public class TeamStatsController {

    @RequestMapping("/stats_team")
    public String teamStats(Model model) {
        return "stats_team";
    }
}
