package com.dk.trender.auth;

import java.security.Principal;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.JsonSnakeCase;

/**
 * 
 * @author ayrton
 */
public class ZLogin implements Principal {
	@Email
	@NotEmpty
	private String email;

	@NotEmpty
	@Length(min=6, max=50)
	private String password;

	@JsonProperty
	public String getEmail() {
		return email;
	}

	@JsonProperty
	public void setEmail(final String email) {
		this.email = email;
	}

	@JsonProperty
	public String getPassword() {
		return password;
	}

	@JsonProperty	
	public void setPassword(final String password) {
		this.password = password;
	}

	@JsonIgnore
	@Override
	public String getName() {
		return email;
	}
}
