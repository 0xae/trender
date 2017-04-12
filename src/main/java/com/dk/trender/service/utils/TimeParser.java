package com.dk.trender.service.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

public class TimeParser {
	public static final String MINUTES = "(\\d+) (mins*)";
	public static final String HOURS = "(\\d+) (hrs*)";
	public static final String DATE = "(\\w+) (\\d+) (\\d+):(\\d+)([ap]m)";
	public static final String NOW = "Just [nN]ow";


	public LocalDateTime parseTime(String time) {
		return parseTime(time, new LocalDateTime());
	}
	
	public LocalDateTime parseTime(String time, LocalDateTime start) {
		LocalDateTime dt = new LocalDateTime();
		if (isIt(time, MINUTES)) {
			final List<String> b=extractWithRE(MINUTES, time);
			return dt.minusMinutes(Integer.parseInt(b.get(0)));
		} else if (isIt(time, HOURS)) {
			final List<String> b=extractWithRE(HOURS, time);
			return dt.minusHours(Integer.parseInt(b.get(0)));
		} else if (isIt(time, DATE)) {
			final List<String> b=extractWithRE(DATE, time);
			int month = getMonth(b.get(0));
			int day = Integer.parseInt(b.get(1));
			int hours = Integer.parseInt(b.get(2));
			int minute = Integer.parseInt(b.get(3));

			return dt.withMonthOfYear(month)
					    .withDayOfMonth(day)
					    .withHourOfDay(hours)
					    .withMinuteOfHour(minute);
		} else {
			return dt;
		}		
	}

	public List<String> extractWithRE(String weapon, String person) {	
		 final Matcher m = getMatcher(weapon, person);
		 if (!m.matches()) {
			 System.out.println("NO MATCH");
			 return Collections.emptyList();
		 }		 

		 final int count = m.groupCount();
		 final String[] buf = new String[count];
		 for (int i=0; i<count; i++) {
			 buf[i] = m.group(i+1);
		 }

		 return Arrays.asList(buf);
	}
	
	private boolean isIt(String buf, String regex) {
		return getMatcher(regex, buf)
			   .matches();
	}
	
	private Matcher getMatcher(String regex, String buf) {
		 final Pattern p = Pattern.compile(regex);		 
		 return p.matcher(buf);		
	}

	private int getMonth(String month) {
		if (month.equalsIgnoreCase("january")) {	
			return 1;
		} else if (month.equalsIgnoreCase("february")) {
			return 2;
		} else if (month.equalsIgnoreCase("march")) {
			return 3;
		} else if (month.equalsIgnoreCase("april")) {
			return 4;
		} else if (month.equalsIgnoreCase("may")) {
			return 5;
		} else if (month.equalsIgnoreCase("june")) {
			return 6;
		} else if (month.equalsIgnoreCase("july")) {
			return 7;
		} else if (month.equalsIgnoreCase("august")) {
			return 8;
		} else if (month.equalsIgnoreCase("september")) {			
			return 9;
		} else if (month.equalsIgnoreCase("october")) {			
			return 10;
		} else if (month.equalsIgnoreCase("november")) {			
			return 11;
		} else {			
			return 12;
		}
	}

}
