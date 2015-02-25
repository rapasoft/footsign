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

		model.addAttribute("domain_name", principal.getDomainUserName());
		model.addAttribute("full_name", principal.getFirstName() + " " + principal.getSecondName());
		model.addAttribute("email", principal.getEmail());
		model.addAttribute("department", principal.getDepartment());
		model.addAttribute("photo", principal.getPhoto());

		return "user_profile";
	}

}
