package ch.erni.community.footsign.security;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rap
 */
@Component
public class UserAfterLoginHandler implements AuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userRepository.findByDomainShortName(userDetails.getDomainUserName());

		// Create new user after first successful login
		if (user == null) {
			user = new User();
		}

		user.setDomainShortName(userDetails.getDomainUserName());
		user.setFullName(userDetails.getFirstName() + " " + userDetails.getSecondName());

		userRepository.save(user);
	}

}
