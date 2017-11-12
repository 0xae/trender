package com.dk.trender.auth;

import com.dk.trender.core.ZUser;

import io.dropwizard.auth.Authorizer;

/**
 * 
 * @author ayrton
 */
public class TrenderAuthorizer implements Authorizer<ZUser> {
    @Override
	public boolean authorize(final ZUser user, final String role) {
    	return true;
	}
}
