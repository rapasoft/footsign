package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.CustomPlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veda on 3/6/2015.
 */
@Service
public class MatchRepositoryImpl implements MatchRepositoryCustom {

    @Autowired
    MatchRepository matchRepository;

    @Transactional
    @Override
    public List<CustomPlayerDTO> findPlayerBestTenPlayersCustom() {
        Iterable<CustomPlayer> best =  matchRepository.findPlayerBestTenPlayers();
        List<CustomPlayerDTO> bestPlayers = new ArrayList<>();
        for (CustomPlayer user : best) {
            bestPlayers.add(new CustomPlayerDTO(user.getUser(), user.getMatches()));
        }
        return bestPlayers;
    }

    @Transactional
    @Override
    public List<CustomPlayerDTO> findTenPlayersWithMostMatchesCustom() {
        Iterable<CustomPlayer> mostPlayedUsers =  matchRepository.findTenPlayerWithMostMatches();
        List<CustomPlayerDTO> mostPlayedUsersCustom = new ArrayList<>();
        for (CustomPlayer user : mostPlayedUsers) {
            mostPlayedUsersCustom.add(new CustomPlayerDTO(user.getUser(), user.getMatches()));
        }
        return mostPlayedUsersCustom;
    }
}
