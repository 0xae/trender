package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.joda.time.LocalDateTime;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:54
 */
@Entity
@Table(name="z_listing")
@NamedQueries({
	@NamedQuery(
	    name = "listing.findAll",
	    query = "from Listing"
	),
	@NamedQuery(
	    name = "listing.findById",
	    query = "from Listing where id=:id"
	),
	@NamedQuery(
	    name = "listing.updateTitle",
	    query = "update Listing set title=:title where id=:id"
	),
	@NamedQuery(
	    name = "listing.updateLastActivity",
	    query = "update Listing set last_activity=:time where id=:id"
	)
})
public class Listing {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	@NotEmpty
	@Column(name="title", nullable=false)
	private String title;

	@Column(name="description")
	private String description;

	@Column(name="created_at", updatable=false)
	private LocalDateTime createdAt;

	@Column(name="last_update")
	private LocalDateTime lastUpdate;

	@Column(name="last_activity")
	private LocalDateTime lastActivity;

	public Listing() {
		// TODO
	}

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
	public void setTitle(String t) {
		this.title = t;
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
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@JsonProperty
	public void setLastActivity(LocalDateTime lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	@JsonProperty
	public LocalDateTime getLastActivity() {
		return lastActivity;
	}
	
	@JsonProperty
	public void setLastUpdate(LocalDateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JsonProperty
	public LocalDateTime getLastUpdate() {
		return lastUpdate;
	}
}
