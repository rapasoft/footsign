package ch.erni.community.footsign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rap
 */
@Controller(value = "login")
public class LoginController {

	@RequestMapping("/login")
	public String index() {
		return "login";
	}

}
