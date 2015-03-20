package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by cepe on 17.03.2015.
 */
public class PlannedMatch {
    
    private Date date;
    
    private Long timestamp;
    
    private User currentUser;
    
    private List<String> team1 = new ArrayList<>();
    private List<String> team2 = new ArrayList<>();

    
    
    public PlannedMatch(Date date) {
        this.date = date;
    }

    public PlannedMatch() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public List<String> getTeam1() {
        return team1;
    }

    public void setTeam1(List<String> team1) {
        this.team1 = team1;
    }

    public List<String> getTeam2() {
        return team2;
    }

    public void setTeam2(List<String> team2) {
        this.team2 = team2;
    }

    public void addPlayersToTeam1(String player) {
        team1.add(player);
    }

    public void addPlayersToTeam2(String player) { team2.add(player); }
    
    public String getFormattedDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(this.date);
    }
    
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(this.date);
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.date);
    }
}
