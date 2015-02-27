package ch.erni.community.footsign.component;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class ErniLdapCacheTest {

	@Test
	public void testLoadOnlyOnce() {
		ErniLdapCache erniLdapCache = spy(new ErniLdapCache());

		assertNotNull(erniLdapCache.fetchEskEmployees());
		verify(erniLdapCache, atLeastOnce()).load();

		assertNotNull(erniLdapCache.fetchEskEmployees());
		verify(erniLdapCache, atMost(1)).load();
	}

}