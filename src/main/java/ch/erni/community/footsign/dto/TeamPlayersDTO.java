package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.User;

/**
 * Created by veda on 3/10/2015.
 */
public class TeamPlayersDTO<VALUE extends Number> implements Valuable<VALUE> {

    private User player1;

    private User player2;

    private VALUE value;

    public TeamPlayersDTO(User player1, User player2, VALUE value) {
        this.player1 = player1;
        this.player2 = player2;
        this.value = value;
    }

    public VALUE getValue() { return value; }

    public void setValue(VALUE value) { this.value = value; }

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
    
    public String getTeamDomainNames() {
        return getPlayer1().getDomainShortName() + ", " + getPlayer2().getDomainShortName();
    }

}
