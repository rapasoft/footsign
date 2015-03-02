package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by cepe on 02.03.2015.
 */

@Controller(value = "stats")
public class StatsController {
    
    @Autowired
    private UserRepository userRepository;
    
    @RequestMapping("/stats")
    public String index(Model model) {

        List<User> bestPlayers = userRepository.findAll();
        model.addAttribute("bestPlayers", bestPlayers);
        
        return "stats";
    }
}
