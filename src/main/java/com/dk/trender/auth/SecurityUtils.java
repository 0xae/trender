package com.dk.trender.auth;

/**
 * 
 * @author ayrton
 */
public class SecurityUtils {
	public static final String CHECKPW_HEADER = "X-With-Password";
    public static String bcryptData(final String p) {
		final String encrypted = BCrypt.hashpw(p, BCrypt.gensalt(14));
    	return encrypted;
	}

    public static String bcryptData(final String p, final int logComplexity) {
		final String encrypted = BCrypt.hashpw(p, BCrypt.gensalt(logComplexity));
    	return encrypted;
	}
}
