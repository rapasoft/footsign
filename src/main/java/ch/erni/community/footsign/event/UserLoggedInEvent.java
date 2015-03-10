package ch.erni.community.footsign.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author rap
 */
public class UserLoggedInEvent extends ApplicationEvent {

	public UserLoggedInEvent(Object source) {
		super(source);
	}

}
