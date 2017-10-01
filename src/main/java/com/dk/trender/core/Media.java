package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Media {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@Column(name="title")
	private String title;

	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="type", nullable=false)
	private String type;

	@Column(name="timestamp", nullable=false)
	private DateTime timestamp = new DateTime();

	@Column(name="data", nullable=false)
	private String data = "{}";

	@NotEmpty
	@Column(name="post_id", nullable=false)
	private String postId;

	@NotEmpty
	@Column(name="source", nullable=false)
	private String source;

	@Column(name="cached_at")
	private String cachedAt;

	@Column(name="indexed_at", nullable=false)
	private DateTime indexedAt;

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
	}
	
	@JsonProperty
	public String getTitle() {
		return title;
	}

	@JsonProperty
	public void setTitle(String title) {
		this.title = title;
	}
	
	@JsonProperty
	public String getDescription() {
		return description;
	}
	
	@JsonProperty
	public void setDescription(String description) {
		this.description = description;
	}
	
	@JsonProperty
	public String getType() {
		return type;
	}
	
	@JsonProperty
	public void setType(String type) {
		this.type = type;
	}
	
	@JsonProperty
	public DateTime getTimestamp() {
		return timestamp;
	}
	
	@JsonProperty
	public void setTimestamp(DateTime timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty
	public String getData() {
		return data;
	}

	@JsonProperty
	public void setData(String data) {
		this.data = data;
	}

	@JsonProperty
	public String getPostId() {
		return postId;
	}

	@JsonProperty
	public void setPostId(String postId) {
		this.postId = postId;
	}

	@JsonProperty
	public String getSource() {
		return source;
	}

	@JsonProperty
	public void setSource(String source) {
		this.source = source;
	}

	@JsonProperty
	public String getCachedAt() {
		return cachedAt;
	}

	@JsonProperty
	public void setCachedAt(String cachedAt) {
		this.cachedAt = cachedAt;
	}

	@JsonProperty
	public DateTime getIndexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public void setIndexedAt(DateTime indexedAt) {
		this.indexedAt = indexedAt;
	}
}
