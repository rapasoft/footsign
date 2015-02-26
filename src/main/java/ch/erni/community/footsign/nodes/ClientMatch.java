package ch.erni.community.footsign.nodes;

import ch.erni.community.footsign.validator.ResultList;
import ch.erni.community.footsign.validator.UserList;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cepe on 24.02.2015.
 */
public class ClientMatch {


    @NotEmpty @Size(min = 1, max = 2) @UserList()
    private List<String> team1 = new ArrayList<>();
    @NotEmpty @Size(min = 1, max = 2) @UserList()
    private List<String> team2 = new ArrayList<>();

    @NotEmpty @Size(min = 2, max = 3) @ResultList()
    private List<String> resultTeam1 = new ArrayList<>();
    @NotEmpty @Size(min = 2, max = 3) @ResultList()
    private List<String> resultTeam2 = new ArrayList<>();

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
