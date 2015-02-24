package ch.erni.community.footsign.nodes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cepe on 24.02.2015.
 */
public class ClientMatch {
    private List<String> team1 = new ArrayList<>(2);
    private List<String> team2 = new ArrayList<>(2);

    private List<String> resultTeam1 = new ArrayList<>(4);
    private List<String> resultTeam2 = new ArrayList<>(4);

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

    public List<String> getResultTeam2() {
        return resultTeam2;
    }

    public void setResultTeam2(List<String> resultTeam2) {
        this.resultTeam2 = resultTeam2;
    }

    public List<String> getResultTeam1() {
        return resultTeam1;
    }

    public void setResultTeam1(List<String> resultTeam1) {
        this.resultTeam1 = resultTeam1;
    }

   

}
