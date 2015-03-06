package ch.erni.community.footsign.repository;

import java.util.List;

/**
 * Created by veda on 3/6/2015.
 */
public interface MatchRepositoryCustom {

    List<CustomPlayerDTO> findPlayerBestTenPlayersCustom();
}
