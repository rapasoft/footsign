package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
		model.addAttribute("gameType", 2);
		return "home";
	}

}
