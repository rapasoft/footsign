package ch.erni.community.footsign.controller;

import ch.erni.community.TestDataConfiguration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
public class HomeControllerTest {


	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	@AfterClass
	public static void afterClass() throws IOException {
		FileUtils.deleteRecursively(new File("target/test-neo4j.db"));
	}

	@Before
	public void before() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void testHome() throws Exception {
		this.mockMvc
				.perform(get("/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.TEXT_HTML + ";charset=UTF-8"));
	}


}