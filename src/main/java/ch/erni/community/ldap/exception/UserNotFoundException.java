package ch.erni.community.ldap.exception;

import java.util.function.Supplier;

/**
 * @author rap
 */
public class UserNotFoundException extends Exception implements Supplier<UserNotFoundException> {

	public UserNotFoundException(String s) {
		super(s);
	}

	@Override
	public UserNotFoundException get() {
		return this;
	}
}
