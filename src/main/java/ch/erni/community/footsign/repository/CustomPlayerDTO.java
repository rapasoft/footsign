package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.User;

/**
 * Created by veda on 3/6/2015.
 */
public class CustomPlayerDTO {

    private User player;

    private int matches;

    public CustomPlayerDTO(User player, int matches) {
        this.player = player;
        this.matches = matches;
    }

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }
}
