package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.nodes.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author rap
 */
public class GraphBuilderTest {

	private GraphBuilder graphBuilder = new GraphBuilder();

	@Test
	public void testSerializeEmptyDataForChart() throws Exception {
		String output = graphBuilder.serializeDataForChart("Test", "Test", new HashMap<>());
		assertNotNull(output);
	}

	@Test
	public void testSerializeDataForChart() throws Exception {
		Map<String, Integer> dataMap = new HashMap<>();

		dataMap.put("Test1", 1);
		dataMap.put("Test2", 2);

		String output = graphBuilder.serializeDataForChart("Test", "Test", dataMap);
		assertNotNull(output);
		assertEquals("[[\"Test\",\"Test\"],[\"Test1\",1],[\"Test2\",2]]", output);
	}

	@Test
	public void testSerializeDataForUsersChart() throws Exception {
		List<CustomPlayerDTO<Integer>> customUsers = new ArrayList<>();

		User mockedUser = mock(User.class);
		when(mockedUser.getFullName()).thenReturn("Test user");

		CustomPlayerDTO<Integer> customPlayerDTO = new CustomPlayerDTO<>(mockedUser, 10);

		customUsers.add(customPlayerDTO);

		String output = graphBuilder.serializeDataForChart("Test", "Test", customUsers);
		assertNotNull(output);
		assertEquals("[[\"Test\",\"Test\"],[\"Test user\",10.0]]", output);
	}

	@Test
	public void testSerializeDataForTeamChart() throws Exception {
		List<TeamPlayersDTO<Integer>> teamUsers = new ArrayList<>();

		User mockedUser1 = mock(User.class);
		when(mockedUser1.getDomainShortName()).thenReturn("usr1");

		User mockedUser2 = mock(User.class);
		when(mockedUser2.getDomainShortName()).thenReturn("usr2");

		TeamPlayersDTO<Integer> teamPlayer = new TeamPlayersDTO<>(mockedUser1, mockedUser2, 10);

		teamUsers.add(teamPlayer);

		String output = graphBuilder.serializeDataForTeamChart("Test", "Test", teamUsers);
		assertNotNull(output);
		assertEquals("[[\"Test\",\"Test\"],[\"usr1, usr2\",10]]", output);
	}
}