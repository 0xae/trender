package com.dk.trender.core;

import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import com.dk.trender.service.utils.TimeParser;

public class TimeParserTest {
	public static final String MINUTES = TimeParser.MINUTES;
	public static final String HOURS = TimeParser.HOURS;
	public static final String DATE = TimeParser.DATE;
	private static final String NOW = TimeParser.NOW;

	//@Test
	public void testMinute() {
		String t1 = "1 min";
		String t2s = "2 mins";
		String t10 = "10 min";
		String t20s = "20 mins";

		assertThat(extractFromRE(MINUTES, t1)).containsExactly("1", "min");
		assertThat(extractFromRE(MINUTES, t2s)).containsExactly("2", "mins");
		assertThat(extractFromRE(MINUTES, t10)).containsExactly("10", "min");
		assertThat(extractFromRE(MINUTES, t20s)).containsExactly("20", "mins");
	}

	//@Test
	public void testHour() {	
		String t1 = "1 hr";
		String t2 = "2 hrs";
		String t3 = "20 hr";
		String t4 = "20 hrs";		

		assertThat(extractFromRE(HOURS, t1)).containsExactly("1", "hr");
		assertThat(extractFromRE(HOURS, t2)).containsExactly("2", "hrs");
		assertThat(extractFromRE(HOURS, t3)).containsExactly("20", "hr");
		assertThat(extractFromRE(HOURS, t4)).containsExactly("20", "hrs");
	}
	
	//@Test
	public void testDateTime() {	
		String d1 = "April 1 10:20pm";
		String d2 = "April 1 10:20am";
		String d3 = "vsd 21 1:2pm";
		String d4 = "asd 31 1:2am";

		assertThat(extractFromRE(DATE, d1)).containsExactly("April", "1", "10", "20", "pm");
		assertThat(extractFromRE(DATE, d2)).containsExactly("April", "1", "10", "20", "am");
		assertThat(extractFromRE(DATE, d3)).containsExactly("vsd", "21", "1", "2", "pm");
		assertThat(extractFromRE(DATE, d4)).containsExactly("asd", "31", "1", "2", "am");
	}

	//@Test
	public void testNow() {		
		final String now = "Just Now";
		assertThat(now).matches(NOW);
	}
	
	public List<String> extractFromRE(String weapon, String person) {	
		return new TimeParser().extractWithRE(weapon, person);
	}
}
