package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.User;

/**
 * Created by veda on 3/10/2015.
 */
public class TeamPlayersDTO {

    private User player1;

    private User player2;

    public TeamPlayersDTO(User player1, User player2, int matches) {
        this.player1 = player1;
        this.player2 = player2;
        this.matches = matches;
    }

    public int getMatches() {
        return matches;
    }

    public void setMatches(int matches) {
        this.matches = matches;
    }

    public User getPlayer1() {
        return player1;
    }

    public void setPlayer1(User player1) {
        this.player1 = player1;
    }

    public User getPlayer2() {
        return player2;
    }

    public void setPlayer2(User player2) {
        this.player2 = player2;
    }

    private int matches;
}
