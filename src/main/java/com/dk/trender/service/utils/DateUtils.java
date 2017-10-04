package com.dk.trender.service.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class DateUtils {
	public static String format(DateTime date) {
		return DateTimeFormat.forPattern("YYY-MM-dd HH:mm:ss")
				.print(date)
				.replace(' ', 'T');		
	}
} 
