package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
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

}
