package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.test.TestDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author rap
 */
@Controller(value = "testData")
public class TestDataGeneratorController {

	@Autowired
	private TestDataGenerator testDataGenerator;

	@RequestMapping("/generate")
	public String index() {
		testDataGenerator.generateUserData();
		return "index";
	}

}
