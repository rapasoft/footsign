package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.User;
import org.springframework.data.neo4j.annotation.MapResult;
import org.springframework.data.neo4j.annotation.QueryResult;
import org.springframework.data.neo4j.annotation.ResultColumn;

/**
 * Created by veda on 3/5/2015.
 */
@QueryResult
public interface CustomPlayer {

    @ResultColumn("user")
    public User getUser();
    @ResultColumn("matches")
    public int getMatches();
}
