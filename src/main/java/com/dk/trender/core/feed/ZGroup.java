package com.dk.trender.core.feed;

import java.util.Objects;

import org.joda.time.DateTime;

public class ZGroup {
	private String id;
	private final String label;
	private DateTime time = DateTime.now();

	public ZGroup(String label) {
		Objects.requireNonNull(label);
		this.label = label;
	}

	public ZGroup setId(String id) {
		this.id = id;
		return this;
	}

	public String getId() {
		return id;
	}

	public DateTime getTime() {
		return time;
	}
	
	public ZGroup setTime(DateTime time) {
		this.time = time;
		return this;
	}
	
	public String getLabel() {
		return label;
	}
}
