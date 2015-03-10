package ch.erni.community.footsign.test;

import ch.erni.community.footsign.nodes.Game;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by veda on 3/10/2015.
 */
@Component
public class TestTeamGenerator {

    @Autowired
    TestDataGenerator testDataGenerator;

    @Autowired
    MatchRepository matchRepository;

    public void generateteamData(){


        User selectUser1 = new User();
        selectUser1.setDomainShortName("ban");

        User selectUser2 = new User();
        selectUser2.setDomainShortName("veda");

        User selectUser3 = new User();
        selectUser3.setDomainShortName("rap");
        User selectUser4 = new User();
        selectUser4.setDomainShortName("cepe");
        List<Match> matches = new ArrayList<>();
        for(int i=0 ; i< 10; i++ ) {
            Match match = new Match();
            match.setDateOfMatch(Date.from(Instant.parse("2015-01-" + String.format("%02d", ((i % 30) + 1)) + "T" +
                    String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z")));
            match.addPlayersToTeam1(selectUser1);
            match.addPlayersToTeam1(selectUser2);
            match.addPlayersToTeam2(selectUser3);
            match.addPlayersToTeam2(selectUser4);

            match.addGame(generateGame(8,0));
            match.addGame(generateGame(8,0));


            matches.add(match);
        }

        for(int i=0 ; i< 5; i++ ) {
            Match match = new Match();
            match.setDateOfMatch(Date.from(Instant.parse("2015-01-" + String.format("%02d", ((i % 30) + 1)) + "T" +
                    String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z")));
            match.addPlayersToTeam1(selectUser1);
            match.addPlayersToTeam1(selectUser2);

            match.addPlayersToTeam2(selectUser3);
            match.addPlayersToTeam2(selectUser4);


            match.addGame(generateGame(8,0));
            match.addGame(generateGame(7,8));
            match.addGame(generateGame(8,0));

            matches.add(match);
        }

        for(int i=0 ; i< 5; i++ ) {
            Match match = new Match();
            match.setDateOfMatch(Date.from(Instant.parse("2015-01-" + String.format("%02d", ((i % 30) + 1)) + "T" +
                    String.format("%02d", (int) (Math.random() * 11) + 1) + ":00:00.00Z")));


            match.addPlayersToTeam1(selectUser2);
            match.addPlayersToTeam1(selectUser4);

            match.addPlayersToTeam2(selectUser1);
            match.addPlayersToTeam2(selectUser3);

            match.addGame(generateGame(8,0));
            match.addGame(generateGame(7,8));
            match.addGame(generateGame(8,0));

            matches.add(match);
        }



        matches.forEach(matchRepository::save);
    }

    Game generateGame(int team1Result, int team2Result) {
        Game game = new Game();
        game.setTeam1Result(team1Result);
        game.setTeam2Result(team2Result);
        return game;
    }
}
