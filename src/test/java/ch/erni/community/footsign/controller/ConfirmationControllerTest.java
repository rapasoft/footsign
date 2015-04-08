package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniAuthentication;
import ch.erni.community.footsign.security.ErniUserDetails;
import ch.erni.community.footsign.test.TestDataGenerator;
import ch.erni.community.footsign.test.config.TestDataConfiguration;
import ch.erni.community.footsign.util.LdapUserHelper;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by veda on 3/25/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
public class ConfirmationControllerTest {

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    private ConfirmationController confirmationController;

    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testNotConfirmationMatches() throws Exception {
        testDataGenerator.generatePlayedMatches();
        User user = userRepository.findByDomainShortName("veda");
        List<Match> allMatchesForConfirmUser = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());
        List<Match> confirmMatchesUser = confirmationController.getMatchesForConfirmation(user, allMatchesForConfirmUser);
        Assert.assertTrue(allMatchesForConfirmUser.size() == confirmMatchesUser.size());

        Match match = allMatchesForConfirmUser.get(0);
        User oponent = null;
        if(match.getTeam1().contains(user)) {
            oponent = match.getTeam2().iterator().next();
        }else {
            oponent = match.getTeam1().iterator().next();
        }
        List<Match> allMatchesForConfirmOponent = matchRepository.findPlayedMatchesForUser(oponent.getDomainShortName());

        match.confirmedByPlayer(user);
        matchRepository.save(match);
        allMatchesForConfirmUser = matchRepository.findPlayedMatchesForUser(user.getDomainShortName());
        confirmMatchesUser = confirmationController.getMatchesForConfirmation(user, allMatchesForConfirmUser);
        Assert.assertTrue((allMatchesForConfirmUser.size()-1) == confirmMatchesUser.size());

        match.confirmedByPlayer(oponent);
        match.setState(MatchState.CONFIRMED);
        matchRepository.save(match);

        List<Match> allMatchesForConfirmOponentAfter = matchRepository.findPlayedMatchesForUser(oponent.getDomainShortName());
        List<Match> confirmMatchesOponent = confirmationController.getMatchesForConfirmation(oponent, allMatchesForConfirmOponentAfter);
        Assert.assertTrue((allMatchesForConfirmOponent.size()-1) == confirmMatchesOponent.size());

    }

}
