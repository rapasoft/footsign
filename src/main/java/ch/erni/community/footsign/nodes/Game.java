package ch.erni.community.footsign.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

/**
 * Created by veda on 2/23/2015.
 */
@NodeEntity
public class Game {

    @GraphId
    Long gameId;

    int team1Result;
    int team2Result;

    public int getTeam1Result() {
        return team1Result;
    }

    public void setTeam1Result(int team1Result) {
        this.team1Result = team1Result;
    }

    public int getTeam2Result() {
        return team2Result;
    }

    public void setTeam2Result(int team2Result) {
        this.team2Result = team2Result;
    }
}
