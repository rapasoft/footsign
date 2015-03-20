package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.PlannedMatch;
import ch.erni.community.footsign.enums.MatchState;
import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.MatchRepository;
import ch.erni.community.footsign.repository.UserRepository;
import ch.erni.community.footsign.security.ErniUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by cepe on 20.03.2015.
 */

@Component
@PropertySource("classpath:application.properties")
public class PlannedMatchConfigurator {

    @Value("${planning.interval}")
    private int INTERVAL;

    @Value("${planning.start_hour}")
    private int START_HOUR;

    @Value("${planning.end_hour}")
    private int END_HOUR;

    @Autowired
    private MatchRepository matchRepository;
    
    @Autowired
    private UserRepository userRepository;

    public PlannedMatch createDefaultPlannedMatch(Authentication authentication) {

        PlannedMatch pm = new PlannedMatch();
        try {

            ErniUserDetails principal = (ErniUserDetails) authentication.getPrincipal();
            String  domainUserName = principal.getDomainUserName();
            User u = userRepository.findByDomainShortName(domainUserName);

            pm.setCurrentUser(u);

            pm.addPlayersToTeam1(u.getDomainShortName());
            pm.addPlayersToTeam1("");
            pm.addPlayersToTeam2("");
            pm.addPlayersToTeam2("");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pm;
    }
    
    public Map<Long, PlannedMatch> createPlannedMap(Calendar day) {
        Map<Long, PlannedMatch> result = new TreeMap<>();
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        startCal.setTime(day.getTime());
        startCal.set(Calendar.HOUR_OF_DAY, START_HOUR);
        setStartTime(day, startCal);

        endCal.setTime(day.getTime());
        endCal.set(Calendar.HOUR_OF_DAY, END_HOUR);

        List<Match> plannedMatches = matchRepository.findAllPlanMatchesForToday(startCal.getTimeInMillis(), endCal.getTimeInMillis());

        while (startCal.getTimeInMillis() <= endCal.getTimeInMillis()) {
            PlannedMatch pl = new PlannedMatch(new Date(startCal.getTimeInMillis()));
            if (plannedMatches != null && !plannedMatches.isEmpty()) {
                Optional<Match> match = plannedMatches.stream().filter(m -> m.getDateOfMatch() == startCal.getTimeInMillis()).findFirst();
                pl.setTimestamp(match.isPresent() ? match.get().getDateOfMatch() : null);
            }

            result.put(startCal.getTimeInMillis(), pl);

            startCal.add(Calendar.MINUTE, INTERVAL);
        }


        return result;
    }

    private void setStartTime(Calendar initialDate, Calendar startDate) {
        Calendar currentDate = Calendar.getInstance();
        if (currentDate.get(Calendar.DAY_OF_MONTH) == initialDate.get(Calendar.DAY_OF_MONTH) && currentDate.get(Calendar.MONTH) == initialDate.get(Calendar.MONTH)) {
            int hoursValue = currentDate.get(Calendar.HOUR_OF_DAY);
            int minutesValue = currentDate.get(Calendar.MINUTE);

            if (hoursValue < END_HOUR) {
                int min = minutesValue % INTERVAL;
                int minutes = minutesValue - min + INTERVAL;

                startDate.set(Calendar.MINUTE, minutes == 60 ? 0 : minutes);
                startDate.set(Calendar.HOUR_OF_DAY, minutes == 60 ? hoursValue + 1 : hoursValue);
            } else {
                startDate.set(Calendar.HOUR_OF_DAY, END_HOUR);
            }
        }
    }



    public void validPlannedMatch(PlannedMatch plannedMatch) {
        if (plannedMatch == null) {
            System.err.println(this.getClass().getSimpleName() + ", validPlannedMatch -> PlannedMatch is null");
            throw new IllegalArgumentException("Some strange error is occurred. Please contact your most favorite Java team :)");
        }

        if (plannedMatch.getTimestamp() == null)
            throw new IllegalArgumentException("Time of planned match is not specified");

        List<String> team1 = plannedMatch.getTeam1();
        List<String> team2 = plannedMatch.getTeam2();

        /*if (team1 == null)
            throw new IllegalArgumentException("Team 1 is not specified");

        if (team2 == null)
            throw new IllegalArgumentException("Team 2 is not specified");
        long count1 = team1.stream().filter(p -> !p.isEmpty()).count();
        long count2 = team2.stream().filter(p -> !p.isEmpty()).count();

        if (count1 != count2)
            throw new IllegalArgumentException("Number of players have to be equals!");

        if (count1 < 1 || count1 > 2)
            throw new IllegalArgumentException("Number of players in team 1 have to be 1 or 2! Current number is " + count1);


        if (count2 < 1 || count2 > 2)
            throw new IllegalArgumentException("Number of players in team 2 have to be 1 or 2! Current number is " + count2);*/

        String users = "";
        for (String player : team1) {
            if (!player.isEmpty() && users.contains(player))
                throw new IllegalArgumentException("User can not play more than once: " + player);

            users += (player + ";");
        }

        for (String player : team2) {
            if (!player.isEmpty() && users.contains(player))
                throw new IllegalArgumentException("User can not play more than once: " + player);

            users += (player + ";");
        }

    }

    public Match createMatch(PlannedMatch plannedMatch) {
        if (plannedMatch != null) {
            Match match = new Match();
            match.setState(MatchState.PLANNED);
            match.setDateOfMatch(plannedMatch.getTimestamp());

            plannedMatch.getTeam1().stream().filter(p -> !p.isEmpty()).forEach(p -> {
                User u = userRepository.findByDomainShortName(p);
                match.addPlayersToTeam1(u);
            });

            plannedMatch.getTeam2().stream().filter(p -> !p.isEmpty()).forEach(p -> {
                User u = userRepository.findByDomainShortName(p);
                match.addPlayersToTeam2(u);
            });

            return match;
        }
        return null;

    }
}
