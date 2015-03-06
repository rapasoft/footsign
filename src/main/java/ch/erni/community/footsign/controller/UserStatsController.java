package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

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

	@RequestMapping("/stats")
	public String index(Model model) {
		return userStats(model);

	}


	@RequestMapping("/stats_user")
	public String userStats(Model model) {
		User userWmostPlayed = matchRepository.findPlayerWithMostPlayedMatches();
		User userWmostWins = matchRepository.findPlayerWithMostWins();
		List<CustomPlayerDTO> bestPlayers = matchRepository.findPlayerBestTenPlayersCustom();
		List<CustomPlayerDTO> mostPlayed = matchRepository.findTenPlayersWithMostMatchesCustom();
		List<CustomPlayerDTO> worstPlayers = userRepository.findPlayersWithWorstScorePlayersCustom();


		int countMatches = matchRepository.countPlayedMatches(userWmostPlayed);
		int countWins = matchRepository.countWonMatches(userWmostWins);

		model.addAttribute("user_name", userWmostPlayed.getFullName() + " , " + userWmostPlayed.getDomainShortName());
		model.addAttribute("number_of_matches", countMatches);
		model.addAttribute("user_name_wins", userWmostWins.getFullName() + " , " + userWmostWins.getDomainShortName());
		model.addAttribute("number_of_matches_wins", countWins);


		model.addAttribute("best_players", bestPlayers);
		model.addAttribute("worst_players", worstPlayers);
		model.addAttribute("most_played", mostPlayed);
        /*List<Game> gamesWins = matchRepository.findAllTeam1WinsGameByUserDomainShortName("veda");
		model.addAttribute("win_gams", gamesWins);*/

		return "stats_user";
	}
}
