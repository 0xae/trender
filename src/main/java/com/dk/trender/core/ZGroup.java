package com.dk.trender.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

import org.joda.time.DateTime;

public class ZGroup {
	private static AtomicLong c = new AtomicLong();
	private final long id;
	private final String label;
	private DateTime time = DateTime.now();
	private List<ZPost> posts = new ArrayList<>();

	public ZGroup(String label) {
		// XXX: is this a good idea ?
		id = c.incrementAndGet();
		Objects.requireNonNull(label);
		this.label = label;
	}

	public long getId() {
		return id;
	}

	public DateTime getTime() {
		return time;
	}
	
	public void setTime(DateTime time) {
		this.time = time;
	}
	
	public String getLabel() {
		return label;
	}
	
	public List<ZPost> getPosts() {
		return posts;
	}

	public void setPosts(List<ZPost> posts) {
		this.posts = posts;
	}
}
