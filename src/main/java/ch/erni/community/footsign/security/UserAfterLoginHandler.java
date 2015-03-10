package ch.erni.community.footsign.security;

import ch.erni.community.footsign.event.UserLoggedInEvent;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author rap
 */
@Component
public class UserAfterLoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepository;

	@Autowired
	private ApplicationEventPublisher applicationEventPublisher;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		ErniUserDetails userDetails = (ErniUserDetails) auth.getPrincipal();

		userRepository.saveOrUpdateDetailsAndPhoto(auth.getCredentials().toString(), userDetails);

		applicationEventPublisher.publishEvent(new UserLoggedInEvent(this));

		resp.sendRedirect(req.getContextPath() + "/home");
	}

}
