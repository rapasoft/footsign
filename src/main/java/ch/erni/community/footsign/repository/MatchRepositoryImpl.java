package ch.erni.community.footsign.repository;

import ch.erni.community.footsign.dto.CustomPlayer;
import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.nodes.Match;
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
	public boolean isDateOccupied(long time) {
		Match match = matchRepository.findMatchForThisDate(time);
		return match != null;
	}

}
