package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.test.config.TestDataConfiguration;
import ch.erni.community.footsign.util.PropertyLoader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author rap
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
public class VersionControllerTest {

	@Autowired
	private WebApplicationContext context;

	@Autowired
	private PropertyLoader propertyLoader;

	private MockMvc mockMvc;

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testHome() throws Exception {
		String version = propertyLoader.getProperty("project.version");

		this.mockMvc
				.perform(get("/version"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_PLAIN + ";charset=UTF-8"))
				.andExpect(content().string(version));
	}

}