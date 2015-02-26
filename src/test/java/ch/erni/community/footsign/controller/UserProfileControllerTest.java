package ch.erni.community.footsign.controller;

import ch.erni.community.TestDataConfiguration;
import ch.erni.community.footsign.security.ErniAuthentication;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
public class UserProfileControllerTest {

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private ErniAuthentication authentication;

	@Before
	public void before() {
		UserDetails userDetails = mockUserDetails();

		authentication = new ErniAuthentication(userDetails, "password", true);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	private UserDetails mockUserDetails() {
		return new UserDetails(
				Optional.of("firstName"), Optional.of("secondName"), Optional.of("dn"), Optional.of("firstName.secondName@erni.sk"),
				Optional.of("Test"), Optional.of("Test"));
	}

	@Test
	public void testHome() throws Exception {
		this.mockMvc
				.perform(get("/user_profile").principal(authentication))
				.andExpect(status().isOk())
				.andExpect(model().attribute("domain_name", "dn"))
				.andExpect(model().attribute("full_name", "firstName secondName"))
				.andExpect(model().attribute("email", "firstName.secondName@erni.sk"))
				.andExpect(model().attribute("department", "Test"))
				.andExpect(content().contentType(MediaType.TEXT_HTML + ";charset=UTF-8"));
	}


}