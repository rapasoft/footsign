package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.User;

/**
 * Created by veda on 3/6/2015.
 */
public class CustomPlayerDTO<VALUE extends Number> implements Valuable<VALUE> {

    private User player;

	private VALUE value;

	public CustomPlayerDTO(User player, VALUE value) {
		this.player = player;
		this.value = value;
	}

    public User getPlayer() {
        return player;
    }

    public void setPlayer(User player) {
        this.player = player;
    }

	public VALUE getValue() {
		return value;
	}

	public void setValue(VALUE value) {
		this.value = value;
	}
}
