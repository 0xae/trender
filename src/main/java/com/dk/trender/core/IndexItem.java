package com.dk.trender.core;

import java.util.Objects;
import java.util.Set;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexItem {
	@NotEmpty
	private String ref;

	@NotNull
	private Long postId;

	@NotEmpty
	private Set<String> links;
	
	@JsonProperty
	public void setRef(String r) {
		this.ref = r;
	}

	@JsonProperty
	public String getRef() {
		return ref;
	}
	
	@JsonProperty
	public void setPostId(long postId) {
		this.postId = postId;
	}

	@JsonProperty
	public long getPostId() {
		return postId;
	}

	@JsonProperty
	public void setLinks(Set<String> links) {
		this.links = links;
	}
	
	@JsonProperty
	public Set<String> getLinks() {
		return links;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		return prime * 1 + ((ref == null) ? 0 : ref.hashCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		return Objects.equals(this.ref, ((IndexItem) obj).ref);
	}
}