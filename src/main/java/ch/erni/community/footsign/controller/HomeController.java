package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.ldap.Connection;
import ch.erni.community.ldap.LdapService;
import ch.erni.community.ldap.LdapServiceImpl;
import ch.erni.community.ldap.data.DefaultCredentials;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.CredentialsFileNotFoundException;
import ch.erni.community.ldap.exception.CredentialsNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.GraphDatabase;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
		model.addAttribute("gameType", 2);
		return "home";
	}

	@RequestMapping("/user_list")
	public @ResponseBody
	String getUserList() {

		Connection connection = null;
		try {
			connection = Connection.forCredentials(new DefaultCredentials().getCredentials());
			LdapService ldapService = new LdapServiceImpl(connection);

			List<UserDetails> list = ldapService.fetchEskEmployees();

			String json = "";
			ObjectMapper mapper = new ObjectMapper();
			try {
				json = mapper.writeValueAsString(list);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			return json;

		} catch (CredentialsNotFoundException | CredentialsFileNotFoundException e) {
			e.printStackTrace();
		}

		return "";

	}

}
