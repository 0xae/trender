package com.dk.trender.core;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Objects;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:42
 */
public class Post {
    private @NotEmpty String id;
	private @NotEmpty String type;
	private @NotEmpty String authorName;
	private @NotEmpty String source;
	private @NotEmpty String link;
	private @NotEmpty String description = "";
	private @NotEmpty String location = "worldwide";
	private @NotNull LocalDateTime timestamp;

	private @NotNull String authorPicture = "";
	private @NotNull String picture = "";
	private @NotNull String data = "{}";
	private @NotNull List<String> category = Collections.emptyList();

	public Post() {
		// TODO
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
	public Post setDescription(String description) {
		this.description = description;
		return this;
	}
	
	@JsonProperty
	public void setLocation(String location) {
		this.location = location;
	}
	
	@JsonProperty
	public String getLocation() {
		return location;
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
	public Post setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
		return this;
	}

	@JsonProperty
	public Post setTimestamp(String ts) {
		this.timestamp = new LocalDateTime(ts.replace(" ", "T"));
		return this;
	}

	@JsonProperty
	public LocalDateTime getTimestamp() {
		return timestamp;
	}

 	@JsonProperty
 	public String formatTs() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
		.print(timestamp)
		.replace(' ', 'T');
 	}
	
	@JsonProperty
	public String getType() {
		return type;
	}

	@JsonProperty
	public Post setType(String type) {
		this.type = type;
		return this;
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
	
	@JsonProperty
	public void setCategory(List<String> category) {
		this.category = category.stream()
				.map(this::norm)
				.collect(Collectors.toList());
	}

	@JsonProperty
	public List<String> getCategory() {
		return category;
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
	
	private String norm(String tag) {
		return tag.toLowerCase()
				.trim();
	}	
}