package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayers;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.util.FileDownloader;
import ch.erni.community.footsign.util.LdapUserHelper;
import ch.erni.community.ldap.data.UserDetails;
import ch.erni.community.ldap.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rap
 */
// TODO @rap: write tests that confirm Neo4J query correctness
@Service
public class UserRepositoryImpl implements UserRepositoryCustom {

	@Autowired
	private FileDownloader fileDownloader;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ErniLdapCache erniLdapCache;

	@Autowired
	private LdapUserHelper ldapUserHelper;

	@Override
	public void saveOrUpdateDetailsAndPhoto(String password, ErniUserDetails userDetails) {
		User user = userRepository.findByDomainShortName(userDetails.getDomainUserName());

		if (user == null) {
			user = ldapUserHelper.createUserFromLdapUser(userDetails).get();
		} else {
			user.setDepartment(userDetails.getDepartment());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFullName());
		}

		Path path = fileDownloader.downloadPhoto(userDetails, password);
		user.setPhotoPath(path.toString());

		userRepository.save(user);
	}

	/**
	 * Save User to DB, if doesn't exist yet.
	 * 1. if user exist in DB, do nothing
	 * 2. if doesn't, find LDAP object by domain name
	 * 2.1 create instance of user and fill data from LDAP
	 * 2.2 save new user to DB
	 *
	 * @param users - list of Strings represent user domain names
	 */
	@Override
	public void saveUsersToDB(List<String> users) throws UserNotFoundException {
		if (users == null) {
			throw new IllegalArgumentException("User list cannot be null");
		}
		for (String name : users) {
			if (name != null && !name.isEmpty()) {
				User user = userRepository.findByDomainShortName(name);
				if (user == null) {
					UserDetails detail = erniLdapCache.getEskEmployee(name);

					User newUser = ldapUserHelper.createUserFromLdapUser(detail).get();
					userRepository.save(newUser);
				}
			}
		}

	}

	// TODO @rap: everything that is under this comment should be imho in a separate class

	@Override
	@Transactional
	public List<User> findAllUsers() {
		Iterable<User> iterable = userRepository.findAll();

		List<User> users = new ArrayList<>();
		for (User user : iterable) {
			users.add(user);
		}
		return users;
	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Long>> findPlayersWithWorstScorePlayersCustom() {
		Iterable<CustomPlayer> worst = userRepository.findPlayersWithWorstScore();
		List<CustomPlayerDTO<Long>> worstPlayers = new ArrayList<>();
		for (CustomPlayer user : worst) {
			worstPlayers.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
		return worstPlayers;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findTeamWithMostMatchesCustom() {
		Iterable<TeamPlayers> mostTeam = userRepository.findTeamWithMostMatches();
		return extractTeamPlayers(mostTeam);
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findBestTenTeamsCustom() {
		Iterable<TeamPlayers> bestTeams = userRepository.findBestTenTeams();
		return extractTeamPlayers(bestTeams);
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findWorstTenTeamsCustom() {
		Iterable<TeamPlayers> worstTeams = userRepository.findWorstTenTeams();
		return extractTeamPlayers(worstTeams);
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findBestTeamsCustom() {
		List<TeamPlayers> bestTeams = userRepository.findBestTenTeams();
		List<TeamPlayers> filteredTeams = bestTeams.stream().filter(t -> !bestTeams.isEmpty() && t.getValue() == bestTeams.get(0).getValue()).collect(Collectors.toList());
		return extractTeamPlayers(filteredTeams);
	}


	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findWorstTeamsCustom() {
		List<TeamPlayers> worstTeams = userRepository.findWorstTenTeams();
		List<TeamPlayers> filteredTeam = worstTeams.stream().filter(t -> !worstTeams.isEmpty() && t.getValue() == worstTeams.get(0).getValue()).collect(Collectors.toList());
		return extractTeamPlayers(filteredTeam);
	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Long>> findWorstPlayersCustom() {
		List<CustomPlayer> worstPlayers = userRepository.findPlayersWithWorstScore();
		return extractCustomPlayers(worstPlayers);

	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Long>> findTenMostUnderTablePlayersCustom() {
		List<CustomPlayer> underTablePlayers = userRepository.findTenMostUnderTablePlayers();
		return extractCustomPlayers(underTablePlayers);
	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Long>> findBestPlayerCustom() {
		List<CustomPlayer> best =  userRepository.findPlayerBestTenPlayers();
		List<CustomPlayer> filteredPlayer = best.stream().filter(p -> !best.isEmpty() && p.getValue().equals(best.get(0).getValue())).collect(Collectors.toList());
		return filteredPlayer.stream().map(user -> new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue())).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public List<CustomPlayerDTO<Long>> findPlayerBestTenPlayersCustom() {
		Iterable<CustomPlayer> best =  userRepository.findPlayerBestTenPlayers();
		List<CustomPlayerDTO<Long>> bestPlayers = new ArrayList<>();
		for (CustomPlayer user : best) {
			bestPlayers.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
		return bestPlayers;
	}

	@Transactional
	@Override
	public List<CustomPlayerDTO<Long>> findTenPlayersWithMostMatchesCustom() {
		Iterable<CustomPlayer> mostPlayedUsers =  userRepository.findTenPlayerWithMostMatches();
		List<CustomPlayerDTO<Long>> mostPlayedUsersCustom = new ArrayList<>();
		for (CustomPlayer user : mostPlayedUsers) {
			mostPlayedUsersCustom.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
		return mostPlayedUsersCustom;
	}

	// Helper methods for extracting list of teams/players (to keep it DRY)

	private List<TeamPlayersDTO<Long>> extractTeamPlayers(Iterable<TeamPlayers> teamPlayers) {
		List<TeamPlayersDTO<Long>> mostPlayedTeam = new ArrayList<>();
		for (TeamPlayers team : teamPlayers) {
			mostPlayedTeam.add(new TeamPlayersDTO<>(team.getFirstPlayer(), team.getSecondPlayer(), (long) team.getValue()));
		}
		return mostPlayedTeam;
	}

	private List<CustomPlayerDTO<Long>> extractCustomPlayers(List<CustomPlayer> players) {
		List<CustomPlayer> filteredPlayers = players.stream().filter(p -> !players.isEmpty() && p.getValue().equals(players.get(0).getValue())).collect(Collectors.toList());
		return filteredPlayers.stream().map(player -> new CustomPlayerDTO<>(player.getUser(), (Long) player.getValue())).collect(Collectors.toList());
	}
}
