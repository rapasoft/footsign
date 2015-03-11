package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "teamStatistics")
public class TeamStatsController {

    @Autowired
    UserRepository userRepository;

    List<TeamPlayersDTO> mostPlayedTeam;

    List<TeamPlayersDTO> bestTeams;

    List<TeamPlayersDTO> worstTeams;

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
}
