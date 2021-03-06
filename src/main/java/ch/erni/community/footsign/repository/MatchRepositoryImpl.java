package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.CustomPlayerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by veda on 3/6/2015.
 */
// TODO @rap: write tests that confirm Neo4J query correctness
@Service
public class MatchRepositoryImpl implements MatchRepositoryCustom {

    @Autowired
    MatchRepository matchRepository;

    @Transactional
    @Override
	public List<CustomPlayerDTO<Long>> findPlayerBestTenPlayersCustom() {
		Iterable<CustomPlayer> best =  matchRepository.findPlayerBestTenPlayers();
		List<CustomPlayerDTO<Long>> bestPlayers = new ArrayList<>();
		for (CustomPlayer user : best) {
			bestPlayers.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
        return bestPlayers;
    }

    @Transactional
    @Override
	public List<CustomPlayerDTO<Long>> findTenPlayersWithMostMatchesCustom() {
		Iterable<CustomPlayer> mostPlayedUsers =  matchRepository.findTenPlayerWithMostMatches();
		List<CustomPlayerDTO<Long>> mostPlayedUsersCustom = new ArrayList<>();
		for (CustomPlayer user : mostPlayedUsers) {
			mostPlayedUsersCustom.add(new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue()));
		}
        return mostPlayedUsersCustom;
    }

	@Transactional
	@Override
	public List<CustomPlayerDTO<Double>> findTenPlayersWithHighestRatioCustom() {
		Iterable<CustomPlayer> mostPlayedUsers = matchRepository.findTenPlayersWithHighestRatio();
		List<CustomPlayerDTO<Double>> highestRatioCustom = new ArrayList<>();
		for (CustomPlayer user : mostPlayedUsers) {
			highestRatioCustom.add(new CustomPlayerDTO<>(user.getUser(), (Double) user.getValue()));
		}
		return highestRatioCustom;
	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Double>> findPlayerWithHighestRatioCustom() {
		List<CustomPlayer> highestRatioPlayers = matchRepository.findTenPlayersWithHighestRatio();

		List<CustomPlayer> filteredRatio = highestRatioPlayers.stream().filter(r -> !highestRatioPlayers.isEmpty() && r.getValue().equals(highestRatioPlayers.get(0).getValue()))
				.collect(Collectors.toList());

		return filteredRatio.stream().map(user -> new CustomPlayerDTO<>(user.getUser(), (Double) user.getValue())).collect(Collectors.toList());
	}

	@Override
	@Transactional
	public List<CustomPlayerDTO<Long>> findBestPlayerCustom() {
		List<CustomPlayer> best =  matchRepository.findPlayerBestTenPlayers();
		List<CustomPlayer> filteredPlayer = best.stream().filter(p -> !best.isEmpty() && p.getValue().equals(best.get(0).getValue())).collect(Collectors.toList());
		return filteredPlayer.stream().map(user -> new CustomPlayerDTO<>(user.getUser(), (Long) user.getValue())).collect(Collectors.toList());
	}
}
