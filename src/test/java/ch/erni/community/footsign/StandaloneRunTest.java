package ch.erni.community.footsign;

import org.junit.Test;

/**
 * @author rap
 */
public class StandaloneRunTest {

	@Test
	public void testCanBeRunAsStandaloneApp() throws Exception {
		Application.main(new String[]{"--server.port=12345"});
	}

}
