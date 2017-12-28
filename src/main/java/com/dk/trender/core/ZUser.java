package com.dk.trender.core;

import java.security.Principal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * @author ayrton
 */
@Entity
@Table(name="z_user")
public class ZUser implements Principal {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)	
	private long id;

	@NotNull
	@Length(min=4, max=250)
	@Column(name="name", nullable=false)
	private String name;

	@Email
	@NotNull
	@Length(max=250)
	@Column(name="email", nullable=false, unique=true)
	private String email;

	@NotNull
	@Length(min=7, max=200)
	@Column(name="password_hash", nullable=false)
	private String password = "";

	@Length(max=50)
	@Column(name="lang")
	private String lang = "en";

	@Column(name="picture")
	private String picture;
	
	@Length(max=100)
	@Column(name="location")
	private String location = "worldwide";	

	@NotNull
	@Column(name="created_at", nullable=false)
	private DateTime createdAt = DateTime.now();

	@JsonProperty
	public ZUser setLocation(String location) {
		this.location = location;
		return this;
	}

	@JsonProperty
	public String getLocation() {
		return location;
	}

	@JsonProperty
	public ZUser setPicture(String picture) {
		this.picture = picture;
		return this;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public ZUser setLang(String lang) {
		this.lang = lang;
		return this;
	}

	@JsonProperty
	public String getLang() {
		return lang;
	}

	@JsonProperty
	public ZUser setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	@JsonProperty
	public DateTime getCreatedAt() {
		return createdAt;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	@JsonProperty
	public ZUser setPassword(String password) {
		this.password = password;
		return this;
	}

	@Override
	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public ZUser setName(String name) {
		this.name = name;
		return this;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public ZUser setEmail(String email) {
		this.email = email;
		return this;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public ZUser setId(long id) {
		this.id = id;
		return this;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name, email);
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		ZUser other = (ZUser) obj;

		return this.id == other.id &&
				Objects.equals(email, other.email) && 
				Objects.equals(name, other.name); 
	}

	@Override
	public String toString() {
		return "AppUser ["+
					 "id = " + id + ","+
					 "name = " + name + "," +
					 "email = " + email +
				"]";
	}
}
