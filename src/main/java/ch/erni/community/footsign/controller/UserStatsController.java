package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.util.GraphBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by cepe on 02.03.2015.
 */

@Controller(value = "userStatistics")
public class UserStatsController {

	@Autowired
	private MatchRepository matchRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private GraphBuilder graphBuilder;

	private List<CustomPlayerDTO<Long>> bestPlayers;

	private List<CustomPlayerDTO<Long>> mostPlayed;

	private List<CustomPlayerDTO<Long>> worstPlayers;

	private List<CustomPlayerDTO<Double>> playersWithHighestRatio;

	@RequestMapping("/stats")
	public String index(Model model) {
		return userStats(model);

	}


	@RequestMapping("/stats_user")
	public String userStats(Model model) {
		User userWmostPlayed = matchRepository.findPlayerWithMostPlayedMatches();
		User userWmostWins = matchRepository.findPlayerWithMostWins();
		bestPlayers = matchRepository.findPlayerBestTenPlayersCustom();
		mostPlayed = matchRepository.findTenPlayersWithMostMatchesCustom();
		worstPlayers = userRepository.findPlayersWithWorstScorePlayersCustom();
		playersWithHighestRatio = matchRepository.findTenPlayersWithHighestRatioCustom();

		if (userWmostPlayed != null) {
			model.addAttribute("user_name", userWmostPlayed.getFullName() + " , " + userWmostPlayed.getDomainShortName());
		}

		if (userWmostWins != null) {
			model.addAttribute("user_name_wins", userWmostWins.getFullName() + " , " + userWmostWins.getDomainShortName());
		}
		
		model.addAttribute("best_players", bestPlayers);
		model.addAttribute("worst_players", worstPlayers);
		model.addAttribute("most_played", mostPlayed);
		model.addAttribute("highest_ratio", playersWithHighestRatio);

		return "stats_user";
	}

	@RequestMapping("top_players_graph_data")
	public
	@ResponseBody
	String getDataForTopPlayersChart() {
		return graphBuilder.serializeDataForChart("Player", "Top 10 players", bestPlayers);
	}

	@RequestMapping("worst_players_graph_data")
	public
	@ResponseBody
	String getDataForWorstPlayersChart() {
		return graphBuilder.serializeDataForChart("Player", "Not so good 10 players", worstPlayers);
	}

	@RequestMapping("most_played_graph_data")
	public
	@ResponseBody
	String getDataForMostPlayedChart() {
		return graphBuilder.serializeDataForChart("Player", "Most played players", mostPlayed);
	}

	@RequestMapping("highest_ratio_graph_data")
	public
	@ResponseBody
	String getDataForHighestRatioChart() {
		return graphBuilder.serializeDataForChart("Player", "Ratio", playersWithHighestRatio);
	}
}
