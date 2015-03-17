package ch.erni.community.footsign.dto;

import ch.erni.community.footsign.nodes.Match;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by cepe on 17.03.2015.
 */
public class PlannedMatch {
    
    private Date date;
    
    private Match match;

    
    
    public PlannedMatch(Date date) {
        this.date = date;
    }

    public PlannedMatch(Date date, Match match) {
        this.date = date;
        this.match = match;
    }

    public PlannedMatch() {

    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }
    
    public String getFormattedDateAndTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return sdf.format(this.date);
    }
    
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        return sdf.format(this.date);
    }

    public String getFormattedTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        return sdf.format(this.date);
    }
}
