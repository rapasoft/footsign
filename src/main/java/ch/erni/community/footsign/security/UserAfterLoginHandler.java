package ch.erni.community.footsign.security;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.util.FileDownloader;
import ch.erni.community.ldap.data.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author rap
 */
@Component
public class UserAfterLoginHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	UserRepository userRepository;

	@Autowired
	FileDownloader fileDownloader;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse resp, Authentication auth) throws IOException, ServletException {
		UserDetails userDetails = (UserDetails) auth.getPrincipal();

		User user = userRepository.findByDomainShortName(userDetails.getDomainUserName());

		user = saveOrUpdateDetails(auth, userDetails, user);

		userRepository.save(user);

		userDetails.setPhoto(user.getPhotoPath());
		resp.sendRedirect(req.getContextPath() + "/home");
	}

	private User saveOrUpdateDetails(Authentication auth, UserDetails userDetails, User user) {
		if (user == null) {
			String password = auth.getCredentials().toString();

			Path path = fileDownloader.downloadPhoto(userDetails, password);
			user = new User(userDetails.getDomainUserName(), userDetails.getFirstName() + " " + userDetails.getSecondName(),
					userDetails.getEmail(), userDetails.getDepartment(), path.toString());
		} else {
			user.setDepartment(userDetails.getDepartment());
			user.setEmail(userDetails.getEmail());
			user.setFullName(userDetails.getFirstName() + " " + userDetails.getSecondName());
		}
		return user;
	}

}
