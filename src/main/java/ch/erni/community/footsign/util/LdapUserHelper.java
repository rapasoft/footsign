package ch.erni.community.footsign.util;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.ldap.data.UserDetails;

/**
 * Created by cepe on 03.03.2015.
 */
public class LdapUserHelper {
    
    public static User createUserFromLdapUser(UserDetails userDetails) {
        if (userDetails == null) return null;
        
        User result = new User();
        result.setDomainShortName(userDetails.getDomainUserName());
        result.setDepartment(userDetails.getDepartment());
        result.setEmail(userDetails.getEmail());
        result.setFullName(userDetails.getFullName());
        
        return result;
        
    }
}
