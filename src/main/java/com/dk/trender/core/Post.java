package com.dk.trender.core;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:42
 */
public class Post {	
    private @NotEmpty String id;
	private @NotEmpty String description;
	private String picture;
	private @NotNull LocalDateTime timestamp;
	private @NotEmpty String type;
	private @NotEmpty String authorName;
	private String authorPicture;
	private @NotEmpty String source;	
	private @NotEmpty String link;
	private String data = "{}";	

	public Post() {
	}

	@JsonProperty
	public String getId() {
		return id;
	}

	@JsonProperty
	public Post setId(String id) {
		this.id = id;
		return this;
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
	public Post setPicture(String picture) {
		this.picture = picture;
		return this;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	@JsonProperty
	public void setTimestamp(String timestamp) {
		this.timestamp = new LocalDateTime(timestamp);
	}

	@JsonProperty
	public LocalDateTime getTimestamp() {
		return timestamp;
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
	public Post setAuthorName(String name) {
		this.authorName = name;
		return this;
	}

	@JsonProperty
	public String getAuthorName() {
		return authorName;
	}

	@JsonProperty
	public Post setAuthorPicture(String pic) {
		this.authorPicture = pic;
		return this;
	}

	@JsonProperty
	public String getAuthorPicture() {
		return authorPicture;
	}	
	
	@JsonProperty
	public Post setSource(String source) {
		this.source = source;
		return this;
	}

	@JsonProperty
	public String getSource() {
		return source;
	}

	@JsonProperty
	public Post setLink(String link) {
		this.link = link;
		return this;
	}

	@JsonProperty
	public String getLink() {
		return link;
	}

	@JsonProperty
	public Post setData(String blob) {
		this.data = blob;
		return this;
	}

	@JsonProperty
	public String getData() {
		return data;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * XXX: what about an update?
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj){
			return true;			
		}

		if (obj == null || getClass() != obj.getClass()){
			return false;
		}

		final Post other = (Post) obj;
		return Objects.equal(this.id, other.id);
	}
}
