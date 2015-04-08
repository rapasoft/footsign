package ch.erni.community.footsign.nodes;

import ch.erni.community.footsign.enums.MatchState;
import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by veda on 2/19/2015.
 */
@NodeEntity
public class Match {

	// TODO @rap: make this configurable
	public static final int NUMBER_OF_WINS_NEEDED = 2;

	@GraphId
	Long matchId;
	
	@Indexed(unique = true, failOnDuplicate = true)
	long dateOfMatch;

	MatchState state = MatchState.PLAYED;

	@RelatedTo(type = "TEAM1", direction = Direction.BOTH)
	private
	@Fetch
	Set<User> team1 = new HashSet<User>();

	@RelatedTo(type = "TEAM2", direction = Direction.BOTH)
	private
	@Fetch
	Set<User> team2 = new HashSet<User>();

	@RelatedTo(type = "GAMES", direction = Direction.OUTGOING)
	private
	@Fetch
	Set<Game> games = new HashSet<Game>();

	@RelatedTo(type = "CONFIRMED_BY", direction = Direction.OUTGOING)
	private
	@Fetch
	Set<User> confirmedBy = new HashSet<>();

	public boolean confirmedByPlayer(User player){
		return confirmedBy.add(player);
	}

	public Set<User> getConfirmedBy() {
		return confirmedBy;
	}

	public Long getMatchId() { return matchId; }

	public long getDateOfMatch() {
		return dateOfMatch;
	}

	public void setDateOfMatch(long dateOfMatch) {
		this.dateOfMatch = dateOfMatch;
	}

	public void addPlayersToTeam1(User player) {
		team1.add(player);
	}

	public void addPlayersToTeam2(User player) {
		team2.add(player);
	}

	public Set<User> getTeam1() {
		return team1;
	}

	public Set<User> getTeam2() {
		return team2;
	}

	public void addGame(Game game) {
		games.add(game);
	}

	public Set<Game> getGames() {
		return games;
	}

	public MatchState getState() {	return state; 	}

	public void setState(MatchState state) { this.state = state; }

	public boolean team1Wins() {
		return games.stream().filter(Game::team1Wins).count() == NUMBER_OF_WINS_NEEDED;
	}

	public boolean team2Wins() {
		return games.stream().filter(Game::team2Wins).count() == NUMBER_OF_WINS_NEEDED;
	}

	public boolean isDraw() {
		return !team1Wins() && !team2Wins();
	}

	public String matchResultToString() {
		return games.stream().filter(Game::team1Wins).count() + " : " + games.stream().filter(Game::team2Wins).count();
	}
	
	public boolean userPlayedMatch(String userDomainShortName) {
		return (
				getTeam1().stream().anyMatch(u -> u.getDomainShortName().equals(userDomainShortName)) ||
				getTeam2().stream().anyMatch(u -> u.getDomainShortName().equals(userDomainShortName))
		);
		
	}
	
	public boolean teamWithUserWins(String userDomainShortName) {
		return (
				(team1Wins() && getTeam1()
						.stream()
						.filter(user -> user.getDomainShortName().equals(userDomainShortName))
						.count() >= 1)
						||
						(team2Wins() && getTeam2()
								.stream()
								.filter(user -> user.getDomainShortName().equals(userDomainShortName))
								.count() >= 1)
		);
	}

	public String getFormatedDateOfMatch() {
		Date date = new Date(dateOfMatch);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm', 'dd.MM.yyyy");
		return simpleDateFormat.format(date);
	}

	public String getFormatedDateOfMatchWithoutYear() {
		Date date = new Date(dateOfMatch);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm', 'dd.MM");
		return simpleDateFormat.format(date);
	}

	public String getPlayersOfTeam1InString(){
		StringBuilder sb = new StringBuilder();
		boolean lastUser = false;
		for(User u : team1) {
			sb.append(u.getFullName());
			if(!lastUser) {
				sb.append(",");
				lastUser = true;
			}
		}
		return sb.toString();
	}
	public String getPlayersOfTeam2InString(){
		StringBuilder sb = new StringBuilder();
		boolean lastUser = false;
		for(User u : team2) {
			sb.append(u.getFullName());
			if(!lastUser) {
				sb.append(",");
				lastUser = true;
			}
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		Match match = (Match) o;

		if (dateOfMatch != match.dateOfMatch) return false;
		if (confirmedBy != null ? !confirmedBy.equals(match.confirmedBy) : match.confirmedBy != null) return false;
		if (games != null ? !games.equals(match.games) : match.games != null) return false;
		if (matchId != null ? !matchId.equals(match.matchId) : match.matchId != null) return false;
		if (state != match.state) return false;
		if (team1 != null ? !team1.equals(match.team1) : match.team1 != null) return false;
		if (team2 != null ? !team2.equals(match.team2) : match.team2 != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = matchId != null ? matchId.hashCode() : 0;
		result = 31 * result + (int) (dateOfMatch ^ (dateOfMatch >>> 32));
		result = 31 * result + (state != null ? state.hashCode() : 0);
		result = 31 * result + (team1 != null ? team1.hashCode() : 0);
		result = 31 * result + (team2 != null ? team2.hashCode() : 0);
		result = 31 * result + (games != null ? games.hashCode() : 0);
		result = 31 * result + (confirmedBy != null ? confirmedBy.hashCode() : 0);
		return result;
	}
}

