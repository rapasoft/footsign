package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.User;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

/**
 * Created by veda on 3/10/2015.
 */
@QueryResult
public interface TeamPlayers {
    @ResultColumn("user1")
    User getFirstPlayer();
    @ResultColumn("user2")
    User getSecondPlayer();
    @ResultColumn("matches")
    int getNumberOfMatches();
}
