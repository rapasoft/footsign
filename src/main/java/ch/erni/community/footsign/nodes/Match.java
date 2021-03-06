package ch.erni.community.footsign.nodes;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

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

	long dateOfMatch;

	boolean planed = false;

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

	public long getDateOfMatch() {
		return dateOfMatch;
	}

	public void setDateOfMatch(long dateOfMatch) {
		this.dateOfMatch = dateOfMatch;
	}

	public boolean isPlaned() {
		return planed;
	}

	public void setPlaned(boolean isPlaned) {
		this.planed = isPlaned;
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
}

