package ch.erni.community.footsign.nodes;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by veda on 2/19/2015.
 */
@NodeEntity
public class Match {

    @GraphId
    Long id;

    Date dateOfMatch;

    @RelatedTo(type = "TEAM1", direction = Direction.BOTH)
    private
    @Fetch
    Set<User> team1 = new HashSet<User>();

    @RelatedTo(type = "TEAM2", direction = Direction.BOTH)
    private
    @Fetch
    Set<User> team2 = new HashSet<User>();

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
}
