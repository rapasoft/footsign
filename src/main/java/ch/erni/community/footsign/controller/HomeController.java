package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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
//		List<String> stuffDone = doStuff();
//		model.addAttribute("stuff", stuffDone);
		return "home";
	}

	/**
	 * Just a little showcase on what can be done. Taken from Spring Boot Neo4j example site
	 */
	private List<String> doStuff() {
		List<String> stuff = new ArrayList<String>();

		User greg = new User("Greg");
		User roy = new User("Roy");
		User craig = new User("Craig");
		User john = new User("John");

		Match match = new Match();

		match.addPlayersToTeam1(greg);
		match.addPlayersToTeam1(roy);
		match.addPlayersToTeam2(craig);
		match.addPlayersToTeam2(john);


		stuff.add("Before linking up with Neo4j...");
		for (User user : new User[]{greg, roy, craig}) {
			stuff.add(user.toString());
		}

		Transaction tx = graphDatabase.beginTx();
		try {
			userRepository.save(greg);
			userRepository.save(roy);
			userRepository.save(craig);

			greg = userRepository.findByName(greg.name);
			greg.playsWith(roy);
			greg.playsWith(craig);
			userRepository.save(greg);

			roy = userRepository.findByName(roy.name);
			roy.playsWith(craig);
			// We already know that roy plays with greg
			userRepository.save(roy);


			matchRepository.save(match);
			// We already know craig plays with roy and greg

			stuff.add("Lookup each user by name...");
			for (String name : new String[]{greg.name, roy.name, craig.name}) {
				stuff.add(userRepository.findByName(name).toString());
			}

			stuff.add("Looking up who plays with Greg...");
			for (User user : userRepository.findByTeammatesName("Greg")) {
				stuff.add(user.name + " plays with Greg.");
			}

			stuff.add("First match plays in TEAM 1:");
			for(User player : match.getTeam1()) {
				stuff.add(player.name);
			}
			stuff.add("In TEAM 2:");
			for(User player : match.getTeam2()) {
				stuff.add(player.name);
			}

			tx.success();
		} finally {
			tx.close();
		}
		return stuff;
	}

}
