package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.component.UserHolder;
import ch.erni.community.footsign.dto.ClientMatch;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

/**
 * @author rap
 */
@Controller(value = "home")
public class HomeController {

	@Autowired
	UserRepository userRepository;

	@Autowired
	GraphDatabase graphDatabase;

	@Autowired
	MatchRepository matchRepository;

	@Autowired
	UserHolder loggedUser;

	@Autowired
	private ErniLdapCache erniLdapCache;

	@Autowired
	private UserHolder userHolder;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public ModelAndView home() {

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		setModelDefaultValues(modelAndView);

		return modelAndView;
	}

	@RequestMapping("/user_list")
	public
	@ResponseBody
	String getUserList() {
		List<ErniUserDetails> list = erniLdapCache.fetchEskEmployees();

		String json = "";
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(list);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}

		return json;

	}

	@RequestMapping(value = "/cancelPlannedMatch", method = RequestMethod.POST)
	public ModelAndView cancelPlannedMatch(String timestamp){
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setView(new RedirectView("home"));

		Match match = matchRepository.findMatchForThisDate(new Long(timestamp));
		if (match != null) {
			matchRepository.delete(match);
		}
		return modelAndView;
	}

	@RequestMapping(value = "/saveGame", method = RequestMethod.POST)
	public ModelAndView saveGame(@ModelAttribute @Valid ClientMatch clientMatch, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("clientMatch", clientMatch);

		if (bindingResult.hasErrors()) {
			return modelAndView;
		}

		// validations. Do for new and existing matches
		if (clientMatch != null) {
			List<String> team1 = clientMatch.getTeam1();
			List<String> team2 = clientMatch.getTeam2();

			List<String> result1 = clientMatch.getResultTeam1();
			List<String> result2 = clientMatch.getResultTeam2();
			
			try {
				validTeams(team1, team2, Integer.parseInt(clientMatch.getGameType()));
				
				userRepository.saveUsersToDB(team1);
				userRepository.saveUsersToDB(team2);
			} catch (Exception e) {
				ObjectError error = new ObjectError("[global]", e.getMessage());
				bindingResult.addError(error);
				return modelAndView;
			}

			
			Match match;
			if (clientMatch.isNewMatch()) {
				// create match. Do only for new matches
				match = new Match();
				match.setDateOfMatch(new Date().getTime());
			} else {
				// load match. Do only for planned matches
				match = matchRepository.findOne(Long.parseLong(clientMatch.getMatchId()));
			}

			// set additional info. Do for new and existing matches
			setPlayersToTeam(team1, match, true);
			setPlayersToTeam(team2, match, false);
			match.setState(MatchState.PLAYED);
			setGamesToMatch(result1, result2, match);
			User u = userHolder.getLoggedUser();
			if(match.getTeam1().contains(u) || match.getTeam2().contains(u)) {
				match.confirmedByPlayer(u);
			}
			matchRepository.save(match);
			modelAndView.addObject("success", "The match was sucessfully saved.");
		}

		setModelDefaultValues(modelAndView);

		return modelAndView;
	}

	private void setPlayersToTeam(List<String> team, Match match, boolean isFirstTeam) {
		team.forEach(name -> {
			User user = userRepository.findByDomainShortName(name);
			if (user != null) {
				if (isFirstTeam)
					match.addPlayersToTeam1(user);
				else
					match.addPlayersToTeam2(user);
			}
		});
	}

	private void setGamesToMatch(List<String> result1, List<String> result2, Match match) {
		if (match != null && result1 != null && result2 != null) {

			int min = Math.min(result1.size(), result2.size());

			for (int i = 0; i < min; i++) {
				Game g = new Game();

				if (result1.get(i).isEmpty() || result2.get(i).isEmpty())
					continue;

				int r1 = Integer.parseInt(result1.get(i));
				int r2 = Integer.parseInt(result2.get(i));

				g.setTeam1Result(r1);
				g.setTeam2Result(r2);

				match.addGame(g);

			}
		}

	}
	
	private void validTeams(List<String> team1, List<String> team2, int gameType) {
		for (int i = 0; i < (gameType); i++) {
			String name = team1.get(i);
			if (name.trim().isEmpty()) throw new IllegalArgumentException("Team 1 have to contain " + gameType + " player(s)");
		}

		for (int i = 0; i < (gameType); i++) {
			String name = team2.get(i);
			if (name.trim().isEmpty()) throw new IllegalArgumentException("Team 2 have to contain " + gameType + " player(s)");
		}
		
	}


	private void setModelDefaultValues(ModelAndView modelAndView) {
		modelAndView.getModelMap().addAttribute("clientMatch", new ClientMatch());
		modelAndView.getModelMap().addAttribute("plannedMatches", matchRepository.findTenUpcomingMatches());
		modelAndView.getModelMap().addAttribute("notFilledMatches", matchRepository.findTenNotFilledMatches());
		modelAndView.getModelMap().addAttribute("loggedUser", loggedUser.getLoggedUser());
	}


}
