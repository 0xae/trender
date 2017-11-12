package com.dk.trender.auth;

import javax.validation.constraints.NotNull;

import com.dk.trender.core.ZUser;

public class ZToken {
	@NotNull
	private ZUser user;

	@NotNull
	private String token;
	
	@NotNull
	private String prefix;
	
	public ZToken(ZUser user, String token) {
		this.user = user;
		this.token = token;
	}
	
	public void setPrefix(String p) {
		this.prefix = p;
	}
	
	public String getPrefix() {
		return prefix;
	}

	public ZUser getUser() {
		return user;
	}

	public void setUser(ZUser user) {
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
