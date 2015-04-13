package ch.erni.community.footsign.util;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.ldap.data.ErniLdapConstants;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

/**
 * Created by cepe on 03.03.2015.
 */
public class LdapUserHelperTest {

    private LdapUserHelper helper;

    @Before
    public void setup() {
        helper = new LdapUserHelper();
    }

    @Test
    public void testCopy() {
        UserDetails detail = new UserDetails(
                Optional.of("Firstname"), 
                Optional.of("Secondname"), 
                Optional.of("sefi"), 
                Optional.of("firstname.secondname@erni.sk"), 
                null,
				Optional.of("Java team"),
				Optional.of(ErniLdapConstants.ERNI_EMPLOYEES_USERS_GROUP_DN)
		);

		User copy = helper.createUserFromLdapUser(detail).get();

		Assert.assertNotNull("Copy must be not null", copy);
        Assert.assertEquals("Full name must be equal", detail.getFullName(), copy.getFullName());
        Assert.assertTrue("Full name must contains first name", copy.getFullName().contains(detail.getFirstName()));
        Assert.assertEquals("Email must be equal", detail.getEmail(), copy.getEmail());
        
    }
    
    @After
    public void destroy() {
        this.helper = null;
        
    }
}
