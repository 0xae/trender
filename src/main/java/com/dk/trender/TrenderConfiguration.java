package com.dk.trender;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 01:11:38
 */
public class TrenderConfiguration extends Configuration {
    @NotEmpty
	private String authorizationPrefix;
	    
	@JsonProperty
	public String getAuthorizationPrefix() {
		return authorizationPrefix;
	}

	@JsonProperty
	public void setAuthorizationPrefix(String authorizationPrefix) {
		this.authorizationPrefix = authorizationPrefix;
	}

	@Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

	@JsonProperty
	public DataSourceFactory getDatabase() {
		return database;
	}

	@JsonProperty
	public void setDatabase(DataSourceFactory database) {
		this.database = database;
	}
}
