package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Created by veda on 3/11/2015.
 */
@Controller(value = "overviewStatistic")
public class OverviewStatsController {
    @Autowired
    MatchRepository matchRepository;

    List<CustomPlayerDTO<Double>> ratioPlayers;

    List<CustomPlayerDTO<Long>> bestPlayers;

    @RequestMapping("/stats_overview")
    public String teamStats(Model model) {
        ratioPlayers = matchRepository.findPlayerWithHighestRatioCustom();
        bestPlayers = matchRepository.findBestPlayerCustom();

        model.addAttribute("highest_ratio",ratioPlayers );
        model.addAttribute("best_player",bestPlayers );

        return "stats_overview";
    }

}
