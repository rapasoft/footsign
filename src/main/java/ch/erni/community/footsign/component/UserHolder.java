package ch.erni.community.footsign.component;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import sun.plugin.liveconnect.SecurityContextHelper;

/**
 * Created by cepe on 25.03.2015.
 */

@Component
public class UserHolder {

    @Autowired
    private UserRepository userRepository;
    
    private User currentUser;

    public User getLoggedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() != null) {
            ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();
    
            if (currentUser == null || !currentUser.getDomainShortName().equals(principal.getDomainUserName())) {
                currentUser = userRepository.findByDomainShortName(principal.getDomainUserName());
            }

            return currentUser;
        }

        return null;
    }
    
}
