package com.dk.trender.core;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRequest {
	@NotNull
	private Post post;

	@NotNull
	private Profile profile;

	public PostRequest() {
		// TODO
	}

	@JsonProperty
	public Post getPost() {
		return post;
	}

	@JsonProperty
	public void setPost(Post post) {
		this.post = post;
	}

	@JsonProperty
	public Profile getProfile() {
		return profile;
	}

	@JsonProperty
	public void setProfile(Profile profile) {
		this.profile = profile;
	}
}
