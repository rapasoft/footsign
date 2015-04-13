package ch.erni.community.footsign.util;

import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by cepe on 17.03.2015.
 */

@Component
public class CalendarHelper {
    
    public Calendar getToday() {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c;
    }

    public Calendar getSpecificDate(String date, String format) throws ParseException {
        if (format == null || format.isEmpty()) {
            format = "dd.MM.yyyy";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date d = sdf.parse(date);
        
        Calendar c = getToday();
        c.setTime(d);
        
        return c;
    }
}
