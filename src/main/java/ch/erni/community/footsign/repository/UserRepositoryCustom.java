package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;

import java.util.List;

/**
 * @author rap
 */
public interface UserRepositoryCustom {

	void saveOrUpdateDetailsAndPhoto(String password, ErniUserDetails userDetails);

	void saveUsersToDB(List<String> team) throws UserNotFoundException;

	List<User> findAllUsers();

	List<CustomPlayerDTO<Long>> findPlayersWithWorstScorePlayersCustom();

	List<TeamPlayersDTO<Long>> findTeamWithMostMatchesCustom();

	List<TeamPlayersDTO<Long>> findBestTenTeamsCustom();

	List<TeamPlayersDTO<Long>> findWorstTenTeamsCustom();

	List<TeamPlayersDTO<Long>> findBestTeamsCustom();

	List<TeamPlayersDTO<Long>> findWorstTeamsCustom();

	List<CustomPlayerDTO<Long>> findWorstPlayersCustom();

	List<CustomPlayerDTO<Long>> findTenMostUnderTablePlayersCustom();

	List<CustomPlayerDTO<Long>> findBestPlayerCustom();

	List<CustomPlayerDTO<Long>> findPlayerBestTenPlayersCustom();
}
