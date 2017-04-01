package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ayrton
 * @date 2017-04-01 19:07:14
 */
@Entity
@Table(name="z_profile")
public class Profile {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@NotEmpty
	@NaturalId
	@Column(name="username", nullable=false, unique=true)
	private String username;	
	
	@NotEmpty
	@Column(name="title", nullable=false)
	private String title;

	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="link", nullable=false)
	private String link;

	@Column(name="picture")
	private String picture;
	
	@NotNull
	@Column(name="indexed_at", nullable=false)
	private DateTime indexedAt;
	
	@Column(name="last_activity")
	private DateTime lastActivity;
	
	@Column(name="last_update")
	private DateTime lastUpdate;

	public void setId(long id) {
		this.id = id;
	}
	
	@JsonProperty
	public long getId() {
		return id;
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
	public String getLink() {
		return link;
	}

	@JsonProperty
	public void setLink(String link) {
		this.link = link;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@JsonProperty
	public String getUsername() {
		return username;
	}

	@JsonProperty
	public void setUsername(String username) {
		this.username = username;
	}

	@JsonProperty
	public DateTime getIndexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public void setIndexedAt(DateTime indexedAt) {
		this.indexedAt = indexedAt;
	}

	@JsonProperty
	public DateTime getLastActivity() {
		return lastActivity;
	}

	@JsonProperty
	public void setLastActivity(DateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	@JsonProperty
	public DateTime getLastUpdate() {
		return lastUpdate;
	}

	@JsonProperty
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
