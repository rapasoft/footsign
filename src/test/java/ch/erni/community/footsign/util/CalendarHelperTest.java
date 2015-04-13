package ch.erni.community.footsign.util;

import org.junit.Before;
import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * @author rap
 */
public class CalendarHelperTest {

	private CalendarHelper calendarHelper;

	@Before
	public void before() {
		calendarHelper = new CalendarHelper();
	}

	@Test
	public void testGetToday() throws Exception {
		Calendar calendar = calendarHelper.getToday();

		java.util.Date currentData = Date.from(LocalDate.now().atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

		assertEquals(currentData, calendar.getTime());
	}

	@Test
	public void testGetSpecificDate() throws Exception {
		Calendar calendar = calendarHelper.getSpecificDate("29.4.2015", "dd.MM.yyyy");

		assertEquals(Date.from(LocalDate.of(2015, 4, 29).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()), calendar.getTime());
	}

	@Test(expected = ParseException.class)
	public void testGetSpecificDateWrongFormat() throws Exception {
		calendarHelper.getSpecificDate("29-04-2015", "dd.MM.yyyy");
	}


}