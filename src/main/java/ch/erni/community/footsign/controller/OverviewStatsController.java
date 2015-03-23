package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.Valuable;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller(value = "overviewStatistic")
public class OverviewStatsController {
	@Autowired
	MatchRepository matchRepository;

	@Autowired
	UserRepository userRepository;

	@RequestMapping("/stats_overview")
	public String teamStats(Model model) {
		addToModel("highest_ratio", "highest_ratio_value", matchRepository.findPlayerWithHighestRatioCustom(), model);
		addToModel("best_player", "best_player_score", matchRepository.findBestPlayerCustom(), model);
		addToModel("worst_player", "worst_player_score", userRepository.findWorstPlayersCustom(), model);
		addToModel("best_team", "best_team_score", userRepository.findBestTeamsCustom(), model);
		addToModel("worst_team", "worst_team_score", userRepository.findWorstTeamsCustom(), model);
		addToModel("best_underTable", "best_underTable_score", userRepository.findTenMostUnderTablePlayersCustom(), model);

		return "stats_overview";
	}

	private void addToModel(String key, String value, List<? extends Valuable> sourceCollection, Model model) {
		if (!sourceCollection.isEmpty()) {
			model.addAttribute(key, sourceCollection);
			model.addAttribute(value, !sourceCollection.isEmpty() ? sourceCollection.get(0).getValue() : "");
		}
	}

}
