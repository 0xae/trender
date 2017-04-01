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
	    query = "select zl from Listing zl"
	)
})
public class Listing {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

	@NotEmpty
	@Column(name="name", nullable=false)
	private String name;

	@Column(name="description")
	private String description;

	@Column(name="created_at", updatable=false)
	private DateTime createdAt;

	@Column(name="updated_at")
	private DateTime updatedAt;

	@JsonProperty
	public DateTime getUpdatedAt() {
		return updatedAt;
	}

	@JsonProperty
	public String getUpdatedAtFmt() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
			   .print(updatedAt);
	}
	
	@JsonProperty
	public String getCreatedAtFmt() {
		return DateTimeFormat.forPattern("YYY-MM-d HH:mm:ss")
			   .print(createdAt);
	}
	
	@JsonProperty
	public void setUpdatedAt(DateTime updatedAt) {
		this.updatedAt = updatedAt;
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
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
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
}
