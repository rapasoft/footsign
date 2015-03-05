package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.nodes.ClientMatch;
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
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	private ErniLdapCache erniLdapCache;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	@RequestMapping("/home")
	public String home(Model model) {

		model.addAttribute("clientMatch", new ClientMatch());
		return "home";
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

	@RequestMapping(value = "/saveGame", method = RequestMethod.POST)
	public ModelAndView saveGame(@ModelAttribute @Valid ClientMatch clientMatch, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("home");
		modelAndView.addObject("clientMatch", clientMatch);

		if (bindingResult.hasErrors()) {
			return modelAndView;
		}

		if (clientMatch != null) {
			List<String> team1 = clientMatch.getTeam1();
			List<String> team2 = clientMatch.getTeam2();

			List<String> result1 = clientMatch.getResultTeam1();
			List<String> result2 = clientMatch.getResultTeam2();

			try {
				userRepository.saveUsersToDB(team1);
				userRepository.saveUsersToDB(team2);
			} catch (Exception e) {
				ObjectError error = new ObjectError("[global]", e.getMessage());
				bindingResult.addError(error);
				return modelAndView;
			}

			Match match = new Match();
			match.setDateOfMatch(new Date());

			setPlayersToTeam(team1, match, true);
			setPlayersToTeam(team2, match, false);
			setGamesToMatch(result1, result2, match);

			matchRepository.save(match);
		}

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

}
