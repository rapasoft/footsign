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

/**
 * @author rap
 */
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
			Path path = fileDownloader.downloadPhoto(userDetails, password);
			user = ldapUserHelper.createUserFromLdapUser(userDetails).get();
			user.setPhotoPath(path.toString());
		} else {
			user.setDepartment(userDetails.getDepartment());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFullName());
		}

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
		Iterable<CustomPlayer> worst =  userRepository.findPlayersWithWorstScore();
		List<CustomPlayerDTO<Long>> worstPlayers = new ArrayList<>();
		for (CustomPlayer user : worst) {
			worstPlayers.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
		return worstPlayers;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findTeamWithMostMatchesCustom(){
		Iterable<TeamPlayers> mostTeam =  userRepository.findTeamWithMostMatches();
		List<TeamPlayersDTO<Long>> mostPlayedTeam = new ArrayList<>();
		for (TeamPlayers team : mostTeam) {
			mostPlayedTeam.add(new TeamPlayersDTO(team.getFirstPlayer(), team.getSecondPlayer(), team.getValue()));
		}
		return mostPlayedTeam;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findBestTenTeamsCustom() {
		Iterable<TeamPlayers> bestTeams =  userRepository.findBestTenTeams();
		List<TeamPlayersDTO<Long>> bestPlayedTeams = new ArrayList<>();
		for (TeamPlayers team : bestTeams) {
			bestPlayedTeams.add(new TeamPlayersDTO(team.getFirstPlayer(), team.getSecondPlayer(), team.getValue()));
		}
		return bestPlayedTeams;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findWorstTenTeamsCustom() {
		Iterable<TeamPlayers> worstTeams =  userRepository.findWorstTenTeams();
		List<TeamPlayersDTO<Long>> worstPlayedTeams = new ArrayList<>();
		for (TeamPlayers team : worstTeams) {
			worstPlayedTeams.add(new TeamPlayersDTO(team.getFirstPlayer(), team.getSecondPlayer(), team.getValue()));
		}
		return worstPlayedTeams;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findBestTeamsCustom() {
		List<TeamPlayers> bestTeams =  userRepository.findBestTenTeams();
		List<TeamPlayersDTO<Long>> bestPlayedTeams = new ArrayList<>();
		TeamPlayers bestTeam = bestTeams.get(0);
		for (TeamPlayers team : bestTeams) {
			if(bestTeam.getValue() == team.getValue()) {
				bestPlayedTeams.add(new TeamPlayersDTO(team.getFirstPlayer(), team.getSecondPlayer(), team.getValue()));
			} else {
				break;
			}
		}
		return bestPlayedTeams;
	}

	@Override
	@Transactional
	public List<TeamPlayersDTO<Long>> findWorstTeamsCustom() {
		List<TeamPlayers> worstTeams =  userRepository.findWorstTenTeams();
		List<TeamPlayersDTO<Long>> worstPlayedTeams = new ArrayList<>();
		TeamPlayers worstTeam = worstTeams.get(0);
		for (TeamPlayers team : worstTeams) {
			if(worstTeam.getValue() == team.getValue()) {
				worstPlayedTeams.add(new TeamPlayersDTO(team.getFirstPlayer(), team.getSecondPlayer(), team.getValue()));
			} else {
				break;
			}
		}
		return worstPlayedTeams;
	}

	@Override
	public List<CustomPlayerDTO<Long>> findPlayersWithWorstCustom() {
		return null;
	}
}
