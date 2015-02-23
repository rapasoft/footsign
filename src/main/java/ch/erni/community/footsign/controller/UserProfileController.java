package ch.erni.community.footsign.controller;

import ch.erni.community.ldap.data.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rap
 */
@Controller(value = "profile")
public class UserProfileController {

	@RequestMapping("/user_profile")
	public String index(Model model, Authentication authentication) {
		UserDetails principal = (UserDetails) authentication.getPrincipal();
		model.addAttribute("name", principal.getFirstName() + " " + principal.getSecondName());
		model.addAttribute("title", principal.getTitle());
		model.addAttribute("department", principal.getDepartment());

		return "user_profile";
	}

}
