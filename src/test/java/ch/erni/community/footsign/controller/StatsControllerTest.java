package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.component.ErniLdapCache;
import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.test.config.TestDataConfiguration;
import ch.erni.community.ldap.data.UserDetails;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by veda on 3/6/2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
@Transactional
public class StatsControllerTest {
    @Autowired
    ErniLdapCache erniLdapCache;

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void testBestPlayer() throws Exception {
        List<Match> matches = new ArrayList<>();
        User winnerUser = selectUser();

        for (int i = 0; i < 20; i++) {
            Match match = new Match();

            Date date = Date.from(Instant.parse("2015-01-" + String.format("%02d", ((i % 30) + 1)) + "T" +
                    String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z"));

            match.setDateOfMatch(date.getTime());

            Game game1 = new Game();
            game1.setTeam1Result(8);
            game1.setTeam2Result(2);
            Game game2 = new Game();
            game2.setTeam1Result(8);
            game2.setTeam2Result(2);
            match.addGame(game1);
            match.addGame(game2);

            User selectUser2 = selectUser(winnerUser);
            User selectUser3 = selectUser(winnerUser, selectUser2);
            User selectUser4 = selectUser(winnerUser, selectUser2, selectUser3);


            match.addPlayersToTeam1(winnerUser);
            match.addPlayersToTeam1(selectUser2);
            match.addPlayersToTeam2(selectUser3);
            match.addPlayersToTeam2(selectUser4);
            matches.add(match);
        }
        matches.forEach(matchRepository::save);

        User bestPlayer = matchRepository.findPlayerWithMostWins();
        Assert.assertTrue(winnerUser.equals(bestPlayer));

    }

    private void addUsers(Match match) {
        User selectUser1 = selectUser();
        User selectUser2 = selectUser(selectUser1);
        match.addPlayersToTeam1(selectUser1);
        match.addPlayersToTeam1(selectUser2);

        User selectUser3 = selectUser(selectUser1, selectUser2);
        User selectUser4 = selectUser(selectUser1, selectUser2, selectUser3);
        match.addPlayersToTeam2(selectUser3);
        match.addPlayersToTeam2(selectUser4);
    }

    User selectUser(User... excludes) {
        List<UserDetails> filtered = erniLdapCache
                .fetchEskEmployees()
                .stream()
                .filter(userDetails -> userDetails.getDomainUserName().matches("veda|rap|ban|cepe"))
                .collect(Collectors.toList());

        Collections.shuffle(filtered);

        UserDetails userDetails = filtered
                .stream()
                .filter(
                        ud -> Arrays.asList(excludes)
                                .stream()
                                .allMatch(user -> !user.getDomainShortName().equals(ud.getDomainUserName())))
                .findAny()
                .get();

        User user = userRepository.findByDomainShortName(userDetails.getDomainUserName());

        if (user != null) {
            return user;
        } else {
            User userNew = new User(userDetails.getDomainUserName(), userDetails.getFirstName() + " " + userDetails.getSecondName(),
                    userDetails.getEmail(), userDetails.getDepartment(), new File("/").getPath());

            userRepository.save(userNew);

            return userNew;
        }
    }
}
