package com.dk.trender.core;

import java.util.Set;
import java.util.Objects;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fId == null) ? 0 : fId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return Objects.equals(this.fId, ((IndexItem) obj).fId);
	}
}