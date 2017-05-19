package com.dk.trender.api;

import org.joda.time.DateTime;

public class SearchTrend {
	private String title;
	private long count=0;
	private DateTime lastActivity;

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public DateTime getLastActivity() {
		return lastActivity;
	}

	public void setLastActivity(DateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
