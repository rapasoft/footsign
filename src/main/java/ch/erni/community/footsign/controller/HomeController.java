package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.ClientMatch;
import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.ldap.LdapService;
import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.data.UserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

	@RequestMapping("/")
	public String index(@RequestParam(value = "name", required = false, defaultValue = "World") String name, Model model) {

		model.addAttribute("clientMatch", new ClientMatch());
		return "home";
	}

	@RequestMapping("/user_list")
	public @ResponseBody String getUserList() {

		LdapService ldapService = new LdapServiceImpl();

		List<UserDetails> list = ldapService.fetchEskEmployees();

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
	public String saveGame(@ModelAttribute ClientMatch clientMatch) {
		
		if (clientMatch != null) {
			List<String> team1 = clientMatch.getTeam1();
			List<String> team2 = clientMatch.getTeam2();

			List<String> result1 = clientMatch.getResultTeam1();
			List<String> result2 = clientMatch.getResultTeam2();

			//TODO: here is missing some validation
			
			Match match = new Match();
			match.setDateOfMatch(new Date());

			setPlayersToTeam(team1, match, true);
			setPlayersToTeam(team2, match, false);
			setGamesToMatch(result1, result2, match);
			
			matchRepository.save(match);
		}

		return "redirect:dashboard";
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
				
				int r1 = Integer.parseInt(result1.get(i));
				int r2 = Integer.parseInt(result2.get(i));
				
				g.setTeam1Result(r1);
				g.setTeam2Result(r2);
				
				match.addGame(g);

			}
		}
		
	}

}
