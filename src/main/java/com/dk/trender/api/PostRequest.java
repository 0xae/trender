package com.dk.trender.api;

import javax.validation.constraints.NotNull;

import com.dk.trender.core.Post;
import com.dk.trender.core.Profile;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PostRequest {
	@NotNull
	private Post post;

	@NotNull
	private Profile profile;

	@NotNull
	private ListingDetails listing;

	public PostRequest() {
		// TODO
	}

	@JsonProperty
	public void setListing(ListingDetails listing) {
		this.listing = listing;
	}

	@JsonProperty
	public ListingDetails getListing() {
		return listing;
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

	public static class ListingDetails {
		private long id;
		private String name;

		public void setId(long id) {
			this.id = id;
		}
		
		public long getId() {
			return id;
		}
		
		public void setName(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
	}
}