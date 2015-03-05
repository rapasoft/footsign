package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by cepe on 02.03.2015.
 */

@Controller(value = "userStatistics")
public class UserStatsController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MatchRepository matchRepository;
    
    @RequestMapping("/stats")
    public String index(Model model) {
        return userStats(model);
        
    }
    
    @RequestMapping("/stats_user")
    public String userStats(Model model) {

        List<User> allPlayers = matchRepository.findAllPlayedPlayers();

        List<Match> mathes = matchRepository.findlastMatches();
        model.addAttribute("last_matches", mathes);


        User userWmostPlayed = null;
        User userWmostWins = null;
        int countMatches = 0, countWins = 0;
        for (User user : allPlayers) {
            List<Match> playersMatch = matchRepository.findAllByUserDomainShortName(user.getDomainShortName());
            if (playersMatch.size() > countMatches) {
                userWmostPlayed = user;
                countMatches = playersMatch.size();
            }
            List<Match> matchWins = playersMatch.stream().filter(match -> match.teamWithUserWins(user.getDomainShortName())).collect(Collectors.toList());
            if (matchWins.size() > countWins) {
                userWmostWins = user;
                countWins = matchWins.size();
            }
        }
        if (userWmostPlayed != null) {
            model.addAttribute("user_name", userWmostPlayed.getFullName()+ " , " + userWmostPlayed.getDomainShortName());
            model.addAttribute("number_of_matches", countMatches);
        }
        if(userWmostWins != null) {
            model.addAttribute("user_name_wins", userWmostWins.getFullName()+ " , "+userWmostWins.getDomainShortName());
            model.addAttribute("number_of_matches_wins", countWins);
        }

        /*List<Game> gamesWins = matchRepository.findAllTeam1WinsGameByUserDomainShortName("veda");
        model.addAttribute("win_gams", gamesWins);*/

        return "stats_user";
    }
}
