package ch.erni.community.footsign.security;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ErniLdapServiceTest {

	@Test
	public void testLoadOnlyOnce() {
		ErniLdapService erniLdapService = spy(new ErniLdapService());

		assertNotNull(erniLdapService.fetchEskEmployees());
		verify(erniLdapService, atLeastOnce()).load();

		assertNotNull(erniLdapService.fetchEskEmployees());
		verify(erniLdapService, atMost(1)).load();
	}

}