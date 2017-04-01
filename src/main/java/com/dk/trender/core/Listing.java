package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

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
	    query = "from Listing l"
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
	private DateTime createdAt;

	@Column(name="last_update")
	private DateTime lastUpdate;

	@Column(name="last_activity")
	private DateTime lastActivity;
	
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
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonProperty
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	@JsonProperty
	public void setLastActivity(DateTime lastActivity) {
		this.lastActivity = lastActivity;
	}
	
	@JsonProperty
	public DateTime getLastActivity() {
		return lastActivity;
	}
	
	@JsonProperty
	public void setLastUpdate(DateTime lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@JsonProperty
	public DateTime getLastUpdate() {
		return lastUpdate;
	}
}
