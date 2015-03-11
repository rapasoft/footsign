package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.util.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "teamStatistics")
public class TeamStatsController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private GraphBuilder graphBuilder;

    List<TeamPlayersDTO<Long>> mostPlayedTeam;

    List<TeamPlayersDTO<Long>> bestTeams;

    List<TeamPlayersDTO<Long>> worstTeams;

    @RequestMapping("/stats_team")
    public String teamStats(Model model) {

        mostPlayedTeam = userRepository.findTeamWithMostMatchesCustom();

        if(mostPlayedTeam != null) {
            model.addAttribute("most_team_players", mostPlayedTeam);
        }

        bestTeams = userRepository.findBestTenTeamsCustom();
        if (bestTeams != null) {
            model.addAttribute("best_teams", bestTeams);
        }

        worstTeams = userRepository.findWorstTenTeamsCustom();
        if (worstTeams != null) {
            model.addAttribute("worst_teams", worstTeams);
        }

        return "stats_team";
    }

    @RequestMapping("top_teams_graph_data")
    public
    @ResponseBody
    String getDataForTopPlayersChart() {
        return graphBuilder.serializeDataForTeamChart("Team", "Top 10 teams", bestTeams);
    }

    @RequestMapping("worst_teams_graph_data")
    public
    @ResponseBody
    String getDataForWorstPlayersChart() {
        return graphBuilder.serializeDataForTeamChart("Team", "Not so good 10 teams", worstTeams);
    }

    @RequestMapping("most_played_team_graph_data")
    public
    @ResponseBody
    String getDataForMostPlayedChart() {
        return graphBuilder.serializeDataForTeamChart("Team", "Most played teams", mostPlayedTeam);
    }
}
