package com.dk.trender.auth;

import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.jackson.Jackson;

import java.util.Optional;

import com.dk.trender.core.ZUser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jose4j.jwt.consumer.JwtContext;

/**
 * 
 * @author ayrton
 */
public class TrenderAuthenticator implements Authenticator<JwtContext, ZUser> {
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper()
					.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

	@Override
	public Optional<ZUser> authenticate(JwtContext context) throws AuthenticationException {
		try {
			final String subject = context.getJwtClaims().getSubject();
			final ZUser user = MAPPER.readValue(subject, ZUser.class);
			return Optional.of(user);
		} catch (final Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
}
