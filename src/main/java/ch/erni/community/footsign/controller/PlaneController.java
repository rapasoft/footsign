package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.ClientMatch;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by cepe on 16.03.2015.
 */
@Controller(value = "planingMatch")
public class PlaneController {

    @RequestMapping("/plane_match")
    public String home(Model model) {

        return "plane_match";
    }
}
