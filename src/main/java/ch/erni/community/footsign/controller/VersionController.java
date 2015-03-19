package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.exception.PropertyFileNotFound;
import ch.erni.community.footsign.util.PropertyLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rap
 */
@RestController
@RequestMapping("/version")
public class VersionController {

	@Autowired
	private PropertyLoader propertyLoader;

	@RequestMapping(method = RequestMethod.GET)
	public String getLatestVersion() throws PropertyFileNotFound {
		return propertyLoader.getProperty("project.version");
	}

}
