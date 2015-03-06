package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayerDTO;

import java.util.List;

/**
 * Created by veda on 3/6/2015.
 */
public interface MatchRepositoryCustom {

    List<CustomPlayerDTO> findPlayerBestTenPlayersCustom();

    List<CustomPlayerDTO> findTenPlayersWithMostMatchesCustom();
}
