package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.validator.ResultMap;
import ch.erni.community.footsign.validator.UserMap;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cepe on 24.02.2015.
 */
public class ClientMatch {

    private final String KEY_TEAM1 = "team1";
    private final String KEY_TEAM2 = "team2";
    
    private String gameType;
    
    @NotEmpty @Size(min = 2, max = 2) @UserMap
    private Map<String, List<String>> teams;
    
    @NotEmpty @Size(min = 2, max = 2) @ResultMap
    private Map<String, List<String>> results;

    public ClientMatch() {
        gameType = "2";
        
        results = new HashMap<>();
        results.put(KEY_TEAM1, new ArrayList<String>(){{add("0"); add("0"); add("0");}});
        results.put(KEY_TEAM2, new ArrayList<String>(){{add("0"); add("0"); add("0");}});

        teams = new HashMap<>();
        teams.put(KEY_TEAM1, new ArrayList<String>() {{add(""); add("");}});
        teams.put(KEY_TEAM2, new ArrayList<String>() {{add(""); add("");}});
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public Map<String, List<String>> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, List<String>> teams) {
        this.teams = teams;
    }

    public Map<String, List<String>> getResults() { return results; }

    public void setResults(Map<String, List<String>> results) { this.results = results; }

    public List<String> getTeam1() { 
        return teams.get(KEY_TEAM1);
    }

    public List<String> getTeam2() {
        return teams.get(KEY_TEAM2);
    }

    public List<String> getResultTeam2() {
        
        return results.get(KEY_TEAM2);
    }

    public List<String> getResultTeam1() {
        
        return results.get(KEY_TEAM1);
    }


   

}
