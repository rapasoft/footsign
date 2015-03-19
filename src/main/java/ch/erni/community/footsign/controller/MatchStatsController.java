package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.util.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "matchStatistics")
public class MatchStatsController {
    
    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private GraphBuilder graphBuilder;
    
    private List<Match> matches;
    private Map<String, Integer> winners;

	private Map<String, Integer> losers;

	@RequestMapping("/stats_match")
    public String teamStats(Model model) {

        matches = matchRepository.findlastMatches();
        extractUsersToMap();
        model.addAttribute("last_matches", matches);

        return "stats_match";
    }
    
    private void extractUsersToMap() {

        initGraphData();

		List<Match> team1Win = this.matches.stream().filter(Match::team1Wins).collect(Collectors.toList());
		List<Match> team2Win = this.matches.stream().filter(Match::team2Wins).collect(Collectors.toList());

		Consumer<Match> determineMatchWinners = m -> {
			m.getTeam1().forEach(t1 -> addToMap(winners, t1));
			m.getTeam2().forEach(t2 -> addToMap(losers, t2));
		};

		team1Win.forEach(determineMatchWinners);
		team2Win.forEach(determineMatchWinners);

    }
    
    private void initGraphData() {
        this.winners = new HashMap<>();
		this.losers = new HashMap<>();

		for (Match match : this.matches) {
            Set<User> team1 = match.getTeam1();
            Set<User> team2 = match.getTeam2();

			Consumer<User> determineWinnersAndLosers = u -> {
				if (!winners.containsKey(u.getFullName())) {
					winners.put(u.getFullName(), 0);
				}
				if (!losers.containsKey(u.getFullName())) {
					losers.put(u.getFullName(), 0);
				}
			};

			team1.forEach(determineWinnersAndLosers);
			team2.forEach(determineWinnersAndLosers);
		}
    }
    
    private void addToMap(Map<String, Integer> map, User u) {
        if (map != null && u != null) {
            int value = map.get(u.getFullName());
            value++;
            map.put(u.getFullName(), value);
        }
        
    }
    
    @RequestMapping("/winner_graph_data")
    private @ResponseBody String graphDataForWinners() {
		return graphBuilder.serializeDataForChart("Player", "Number of victories", this.winners);
	}
    
    @RequestMapping("/looser_graph_data")
	private
	@ResponseBody
	String graphDataForLosers() {
		return graphBuilder.serializeDataForChart("Player", "Number of losses", this.losers);
	}
}
