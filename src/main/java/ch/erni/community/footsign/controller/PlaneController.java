package ch.erni.community.footsign.controller;

import ch.erni.community.footsign.dto.ClientMatch;
import ch.erni.community.footsign.nodes.Match;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by cepe on 16.03.2015.
 */
@Controller(value = "planingMatch")
public class PlaneController {

    int count = 0;
    @RequestMapping("/plane_match")
    public String home(Model model) {

        return "plane_match";
    }

    @RequestMapping("planedMatchesOneDay")
    public String getPlanedMatchForDay() {

        count++;
        Match m = new Match();
        m.setDateOfMatch(new Date().getTime());
        m.setPlaned(true);
        return "fragments/ajax_templates :: #planedMatchesOneDay (matches=" + count + ")";
    }
}
