package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
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

    @Autowired
    UserRepository userRepository;

    List<CustomPlayerDTO<Double>> ratioPlayers;

    List<CustomPlayerDTO<Long>> bestPlayers;

    List<TeamPlayersDTO<Long>> bestTeam;

    List<TeamPlayersDTO<Long>> worstTeam;

    @RequestMapping("/stats_overview")
    public String teamStats(Model model) {
        ratioPlayers = matchRepository.findPlayerWithHighestRatioCustom();
        bestPlayers = matchRepository.findBestPlayerCustom();
        bestTeam = userRepository.findBestTeamsCustom();
        worstTeam = userRepository.findWorstTeamsCustom();


        model.addAttribute("highest_ratio",ratioPlayers );
        model.addAttribute("highest_ratio_value", !ratioPlayers.isEmpty() ? ratioPlayers.get(0).getValue() : "");
        model.addAttribute("best_player",bestPlayers );
        model.addAttribute("best_player_score", !bestPlayers.isEmpty() ? bestPlayers.get(0).getValue()  : "");
        model.addAttribute("best_team",bestTeam );
        model.addAttribute("best_team_score", !bestTeam.isEmpty() ? bestTeam.get(0).getValue() : "" );

        model.addAttribute("worst_team",worstTeam );
        model.addAttribute("worst_team_score",!worstTeam.isEmpty() ? worstTeam.get(0).getValue() :"" );

        return "stats_overview";
    }

}
