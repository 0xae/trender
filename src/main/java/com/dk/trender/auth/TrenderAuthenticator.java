package com.dk.trender.auth;

import java.util.Optional;

import org.jose4j.jwt.consumer.JwtContext;

import com.dk.trender.core.ZUser;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;

/**
 * 
 * @author ayrton
 */
public class TrenderAuthenticator implements Authenticator<JwtContext, ZUser> {
	private static final ObjectMapper MAPPER = new ObjectMapper();

	@Override
	public Optional<ZUser> authenticate(JwtContext context) throws AuthenticationException {
		try {
			final String subject = context.getJwtClaims().getSubject();
			final ZUser user = MAPPER.readValue(subject, ZUser.class);
			return Optional.of(user);
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
