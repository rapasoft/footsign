package ch.erni.community.footsign.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rap
 */
@Controller(value = "secured")
public class SecuredPageController {

	@RequestMapping("/secured")
	public String index() {
		return "secured";
	}

}
