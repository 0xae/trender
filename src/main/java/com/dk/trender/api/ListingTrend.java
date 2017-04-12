package com.dk.trender.api;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class ListingTrend {
	private long id;
	private String name;
	private DateTime lastActivity;	
	private long rank;
	private long totalCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public DateTime getLastActivity() {
		return lastActivity;
	}

	public String getLastActivityFmt() {
		return DateTimeFormat.forPattern("YYYY-MM-dd HH:mm:ss")
						     .print(lastActivity);
	}

	public void setLastActivity(DateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	public long getRank() {
		return rank;
	}

	public void setRank(long rank) {
		this.rank = rank;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(long posts) {
		this.totalCount = posts;
	}
}
