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

import org.hibernate.annotations.NaturalId;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

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
	@Length(min=7, max=250)
	@Column(name="password_hash", nullable=false)
	private String password = "";

	@Length(max=200)
	@Column(name="lang")
	private String lang = "en";

	@Column(name="picture")
	private String picture;
	
	@Length(max=250)
	@Column(name="location")
	private String location = "worldwide";	

	@NotNull
	@Column(name="created_at", nullable=false)
	private DateTime createdAt = DateTime.now();

	@JsonProperty
	public void setLocation(String location) {
		this.location = location;
	}

	@JsonProperty
	public String getLocation() {
		return location;
	}

	@JsonProperty
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@JsonProperty
	public String getPicture() {
		return picture;
	}

	@JsonProperty
	public void setLang(String lang) {
		this.lang = lang;
	}

	@JsonProperty
	public String getLang() {
		return lang;
	}

	@JsonProperty
	public void setCreatedAt(DateTime createdAt) {
		this.createdAt = createdAt;
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
	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	@JsonProperty
	public String getName() {
		return name;
	}

	@JsonProperty
	public void setName(String name) {
		this.name = name;
	}

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public void setEmail(String email) {
		this.email = email;
	}

	@JsonProperty
	public long getId() {
		return id;
	}

	@JsonProperty
	public void setId(long id) {
		this.id = id;
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
		final ZUser other = (ZUser) obj;
		return this.id == other.id &&
				Objects.equals(email, other.email) && 
				Objects.equals(name, other.name); 
	}

	@Override
	public String toString() {
		return "AppUser [id=" + id + 
						 ", name=" + name + 
						 ", email=" + email + 
				"]";
	}
}
