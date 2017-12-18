package com.dk.trender.service.utils;

import java.io.IOException;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Utils {
	public static String format(DateTime date) {
		return DateTimeFormat.forPattern("YYY-MM-dd HH:mm:ss")
				.print(date)
				.replace(' ', 'T');		
	}
	
	/**
	 * TODO: use a better cloning stragegy
	 * @param instance
	 * @return
	 */
	public static <T> T clone(T instance, Class<T> klass) {
		try {
			ObjectMapper m = new ObjectMapper();
			String buf= m.writeValueAsString(instance);
			return m.readValue(buf, klass);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
} 
