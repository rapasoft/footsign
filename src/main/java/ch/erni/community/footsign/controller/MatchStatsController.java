package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.TeamResults;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.util.GraphBuilder;
import ch.erni.community.footsign.util.MatchDataExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "matchStatistics")
public class MatchStatsController {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private GraphBuilder graphBuilder;

	private TeamResults teamResults;

	@RequestMapping("/stats_match")
    public String teamStats(Model model) {
		List<Match> matches = matchRepository.findlastMatches();
		teamResults = new MatchDataExtractor().extractUsersToMap(matches);
		model.addAttribute("last_matches", matches);

        return "stats_match";
    }
    
    @RequestMapping("/winner_graph_data")
    private @ResponseBody String graphDataForWinners() {
		return graphBuilder.serializeDataForChart("Player", "Number of victories", teamResults.getWinners());
	}
    
    @RequestMapping("/looser_graph_data")
	private
	@ResponseBody
	String graphDataForLosers() {
		return graphBuilder.serializeDataForChart("Player", "Number of losses", teamResults.getLosers());
	}
}
