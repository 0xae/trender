package com.dk.trender.auth;

import static org.jose4j.jws.AlgorithmIdentifiers.HMAC_SHA256;

import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.HmacKey;
import org.jose4j.lang.JoseException;

import com.dk.trender.core.ZUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @author ayrton
 */
public class JwtService {
	private final byte[] tokenSecret;
	private final String authorizationPrefix;
	public static final int JWT_EXPIRATION_TIME = 60 * 24 * 10; // days
	private static final ObjectMapper mapper = new ObjectMapper();

	public JwtService(final byte[] tokenSecret, final String authorizationPrefix) {
		this.tokenSecret = tokenSecret;
		this.authorizationPrefix = authorizationPrefix;
	}

	public String getJwtToken(final ZUser user) {
        try {
            final JwtClaims claims = new JwtClaims();
            claims.setSubject(mapper.writeValueAsString(user));
            claims.setExpirationTimeMinutesInTheFuture(JWT_EXPIRATION_TIME);

            final JsonWebSignature jws = new JsonWebSignature();
            jws.setPayload(claims.toJson());
            jws.setAlgorithmHeaderValue(HMAC_SHA256);
            jws.setKey(new HmacKey(tokenSecret));
            return jws.getCompactSerialization();        	
        } catch (JoseException | JsonProcessingException e) {
        	throw new RuntimeException(e);
        }
	}
	
	public ZUser getUserFromToken(String token) {
		final JsonWebSignature jwe = new JsonWebSignature();
        jwe.setKey(new HmacKey(tokenSecret));

        try {
			jwe.setCompactSerialization(token);
	        final JwtClaims claims = JwtClaims.parse(jwe.getPayload());
	        return mapper.readValue(claims.getSubject(), ZUser.class);        	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public byte[] getTokenSecret() {
		return tokenSecret;
	}

	public String getAuthorizationPrefix() {
		return authorizationPrefix;
	}	
}
