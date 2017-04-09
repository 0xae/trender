package com.dk.trender.core;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

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