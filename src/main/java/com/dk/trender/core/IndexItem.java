package com.dk.trender.core;

import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexItem {
	@NotEmpty
	private String fId;

	@NotNull
	private Long postId;

	@NotEmpty
	private Set<String> links;
	
	@JsonProperty
	public String getfId() {
		return fId;
	}
	
	@JsonProperty
	public void setfId(String fId) {
		this.fId = fId;
	}
	
	@JsonProperty
	public long getPostId() {
		return postId;
	}
	
	@JsonProperty
	public void setPostId(long postId) {
		this.postId = postId;
	}
	
	@JsonProperty
	public Set<String> getLinks() {
		return links;
	}
	
	@JsonProperty
	public void setLinks(Set<String> links) {
		this.links = links;
	}				
}