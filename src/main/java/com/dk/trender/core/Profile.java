package com.dk.trender.core;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Profile {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private long id;

	@NotEmpty
	@NotNull
	@Column(name="data", nullable=false, updatable=false)
	private String username;

	@NotNull
	@Column(name="data", nullable=false)
	private String data = "{}";

	@Column(name="name")	
	private String name;

	@Column(name="picture")	
	private String picture = "";

	@Column(name="indexed_at", nullable=false, updatable=false)	
	private DateTime indexedAt = new DateTime();	
	
	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
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
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
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
	public void setUsername(String username) {
		this.username = username;
	}
	
	@JsonProperty
	public String getUsername() {
		return username;
	}
}

