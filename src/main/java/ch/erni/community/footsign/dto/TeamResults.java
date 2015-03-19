package ch.erni.community.footsign.dto;

import java.util.Map;

/**
 * @author rap
 */
public class TeamResults {

	private final Map<String, Integer> winners;

	private final Map<String, Integer> losers;

	public TeamResults(Map<String, Integer> winners, Map<String, Integer> losers) {
		this.winners = winners;
		this.losers = losers;
	}

	public Map<String, Integer> getWinners() {
		return winners;
	}

	public Map<String, Integer> getLosers() {
		return losers;
	}
}
