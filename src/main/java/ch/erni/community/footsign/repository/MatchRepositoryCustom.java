package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.CustomPlayerDTO;

import java.util.List;

/**
 * Created by veda on 3/6/2015.
 */
public interface MatchRepositoryCustom {

	List<CustomPlayerDTO<Long>> findTenPlayersWithMostMatchesCustom();

	List<CustomPlayerDTO<Double>> findTenPlayersWithHighestRatioCustom();

	/**
	 * Best Ratio
	 * It will return more players, if more players have same ratio.
	 * @return player with highest ratio.
	 *
	 */
	List<CustomPlayerDTO<Double>> findPlayerWithHighestRatioCustom();

	/**
	 * Best Player
	 * It will return more players, if more players have same number of wins matches
	 * @return players with most wins mathes
	 *
	 */
	boolean isDateOccupied(long time);

}
