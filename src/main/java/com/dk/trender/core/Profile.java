package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.joda.time.LocalDateTime;

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author ayrton
 *
 */
@Entity
@Table(name="z_profile")
@NamedQueries({
	@NamedQuery(
	    name = "profile.findAll",
	    query = "from Profile p"
	),
	@NamedQuery(
	    name = "profile.byUsername",
	    query = "from Profile p where username = :username"
	),
	@NamedQuery(
	    name = "profile.byId",
	    query = "from Profile p where id = :id"
	)
})
public class Profile {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

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

	@Column(name="likes")
	private int likes;

	@Column(name="listing_id", nullable=false, updatable=false)
	private long listingId;

	@NotEmpty
	@NaturalId
	@Column(name="username", nullable=false, unique=true)
	private String username;

	@Column(name="indexed_at", nullable=false)
	private LocalDateTime indexedAt;

	@Column(name="last_activity")
	private LocalDateTime lastActivity;

	@Column(name="last_update")
	private LocalDateTime lastUpdate;

	public Profile() {
		// TODO
	}

	@JsonProperty
	public void setListingId(long listingId) {
		this.listingId = listingId;
	}

	@JsonProperty
	public long getListingId() {
		return listingId;
	}

	@JsonIgnore
	public int like() {
		likes += 1;
		return likes;
	}

	@JsonProperty
	public void setLikes(int count) {
		this.likes = count;
	}

	@JsonProperty
	public int getLikes() {
		return likes;
	}

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
	public LocalDateTime getIndexedAt() {
		return indexedAt;
	}

	@JsonProperty
	public void setIndexedAt(LocalDateTime indexedAt) {
		this.indexedAt = indexedAt;
	}

	@JsonProperty
	public LocalDateTime getLastActivity() {
		return lastActivity;
	}

	@JsonProperty
	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}

	@JsonProperty
	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}

	@JsonProperty
	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
}
