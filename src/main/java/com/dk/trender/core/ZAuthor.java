package com.dk.trender.core;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZAuthor {
	private long userId;
	private String name;

	@JsonProperty
	public long getUserId() {
		return userId;
	}

	@JsonProperty
	public void setUserId(long authorId) {
		this.userId = authorId;
	}

	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String authorName) {
		this.name = authorName;
	}	
}
