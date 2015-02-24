package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.Game;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by veda on 2/24/2015.
 */
public interface GameRepository  extends CrudRepository<Game, String> {

    
}
