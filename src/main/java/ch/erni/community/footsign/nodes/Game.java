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
}
