package ch.erni.community.footsign.nodes;

import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * Created by veda on 2/23/2015.
 */
@NodeEntity
public class Game {

	public static final int MAXIMUM_SCOREABLE_POINTS = 8;

	@GraphId
	Long gameId;

	@Min(0)
	@Max(MAXIMUM_SCOREABLE_POINTS)
	int team1Result;

	@Min(0)
	@Max(MAXIMUM_SCOREABLE_POINTS)
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

	public boolean team1Wins() {
		return team1Result > team2Result && team1Result == MAXIMUM_SCOREABLE_POINTS;
	}

	public boolean team2Wins() {
		return team1Result < team2Result && team2Result == MAXIMUM_SCOREABLE_POINTS;
	}

}
