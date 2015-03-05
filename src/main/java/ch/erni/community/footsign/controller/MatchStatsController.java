package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.Match;
import ch.erni.community.footsign.repository.MatchRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cepe on 05.03.2015.
 */

@Controller(value = "matchStatistics")
public class MatchStatsController {
    
    @Autowired
    MatchRepository matchRepository;
    
    private List<Match> matches;
    
    @RequestMapping("/stats_match")
    public String teamStats(Model model) {

        matches = matchRepository.findlastMatches();
        model.addAttribute("last_matches", matches);

        return "stats_match";
    }
    
    private Map<String, Integer> extractUsersToMap(List<Match> matches) {
        Map<String, Integer> result = new HashMap<>();
        result.put("Name 1", 5);
        result.put("Name 2", 2);
        result.put("Name 3", 5);
        result.put("Name 4", 7);
        
        
        return result;
    }
    
    @RequestMapping("/winner_graph_data")
    private @ResponseBody String graphDataForWinners() {

        Map<String, Integer> users = extractUsersToMap(this.matches);
        
        if (users != null) {
            ObjectMapper mapper = new ObjectMapper();

            ArrayNode parentArray = mapper.createArrayNode();
            ArrayNode first = mapper.createArrayNode();
            first.add("Player");
            first.add("Number of winners");
            parentArray.add(first);
            
            users.forEach((k, v) -> {
                ArrayNode arr = mapper.createArrayNode();
                arr.add(k);
                arr.add(v);
                parentArray.add(arr);
            });
            
            return parentArray.toString();
        }
        return "";
    }
    
    @RequestMapping("/looser_graph_data")
    private @ResponseBody String graphDataForLoosers() {
        return "";
    }
}
