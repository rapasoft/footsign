package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.test.config.TestDataConfiguration;
import junit.framework.Assert;
import org.junit.*;
import org.junit.runner.RunWith;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by veda on 3/16/2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestDataConfiguration.class)
@WebAppConfiguration
public class TestPlanMatches {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    UserRepository userRepository;

    List<Match> matches = new ArrayList<>();

    @Before
    public void before(){
        User user1 = new User();
        user1.setDomainShortName("veda");
        User user2 = new User();
        user2.setDomainShortName("rap");
        User user3 = new User();
        user3.setDomainShortName("cepe");
        User user4 = new User();
        user4.setDomainShortName("ban");

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);

        for(int i=0; i<10; i++) {
            Match match = new Match();
            match.addGame(generateGame(8,1));
            match.setPlaned(true);
            match.addPlayersToTeam1(user1);
            match.addPlayersToTeam1(user2);
            match.addPlayersToTeam2(user3);
            match.addPlayersToTeam2(user4);
            Calendar today = Calendar.getInstance();
            int month = today.get(Calendar.MONTH) +1 ;
            int day = today.get(Calendar.DAY_OF_MONTH) + (int) (Math.random()*10);

            Date date = Date.from(Instant.parse("2015-" + String.format("%02d", month) + "-" + String.format("%02d", ((day > 30 ? day % 30 : day) + 1)) + "T" +
                    String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z"));
            match.setDateOfMatch(date.getTime());
            matches.add(match);
        }
        matches.forEach( matchRepository :: save);

    }

    @Test
    public void testUserPlanMatches(){
        Assert.assertEquals(matchRepository.findAllPlanMatchesForUser("veda").size(),matches.size());
        Assert.assertEquals(matchRepository.findAllPlanMatches().size(),matches.size());
        //test already occupied date
        Match testMatch = matches.get(0);
        long bookForThisDate = testMatch.getDateOfMatch();

        //Test reservation 1 month further
        Calendar todayPlusOne = Calendar.getInstance();
        todayPlusOne.add(Calendar.MONTH,1);
        Assert.assertFalse(matchRepository.isDateOccupied(todayPlusOne.getTimeInMillis()));

        List<Match> todayMatches = new ArrayList<>();
        for(int i=0; i<10; i++) {
            Match match = new Match();
            match.addGame(generateGame(8,1));
            match.setPlaned(true);
            Calendar today = Calendar.getInstance();
            int month = today.get(Calendar.MONTH) +1 ;
            int day = today.get(Calendar.DAY_OF_MONTH);
            int time = today.get(Calendar.HOUR_OF_DAY);
            int diff = 24 - time;
            int random = (int) (Math.random() * 11) % diff;
            today.add(Calendar.HOUR_OF_DAY, random);
            today.add(Calendar.MINUTE, 5);

            match.setDateOfMatch(today.getTimeInMillis());
            todayMatches.add(match);
        }
        todayMatches.forEach(matchRepository :: save );

        long today = Calendar.getInstance().getTimeInMillis();
        Calendar tomorrow = Calendar.getInstance();
        tomorrow.add(Calendar.DATE, 1);
        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
        tomorrow.set(Calendar.MINUTE, 0);
        tomorrow.set(Calendar.SECOND, 0);
        tomorrow.set(Calendar.MILLISECOND, 0);
        Assert.assertEquals(matchRepository.findAllPlanMatchesForToday(today, tomorrow.getTimeInMillis()).size(), 10);
    }


    Game generateGame(int team1Result, int team2Result) {
        Game game = new Game();
        game.setTeam1Result(team1Result);
        game.setTeam2Result(team2Result);
        return game;
    }
}
