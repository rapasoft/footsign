package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.Match;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by veda on 2/19/2015.
 */
public interface MatchRepository extends CrudRepository<Match, String> {
}
