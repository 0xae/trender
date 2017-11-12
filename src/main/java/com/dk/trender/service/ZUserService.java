package com.dk.trender.service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.ws.rs.ForbiddenException;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;

import com.dk.trender.auth.BCrypt;
import com.dk.trender.auth.JwtService;
import com.dk.trender.auth.ZLogin;
import com.dk.trender.auth.ZToken;
import com.dk.trender.core.ZUser;
import com.dk.trender.exceptions.BadRequest;

import io.dropwizard.hibernate.AbstractDAO;

public class ZUserService extends AbstractDAO<ZUser> {
	private final JwtService jwt;
	private final String prefix;

	public ZUserService(SessionFactory sessionFactory, 
						JwtService jwt,
						String prefix) {
		super(sessionFactory);	
		this.jwt = jwt;
		this.prefix = prefix;
	}

	public ZUser create(ZUser req) {
		String encrypted=bcrypt(req.getPassword(), 12);
		req.setPassword(encrypted);
		try {
			return persist(req);			
		} catch(ConstraintViolationException e) {
			String msg;
			msg = String.format("The email '%s' is already taken.", 
							  	req.getEmail());			
			throw new BadRequest(Arrays.asList(msg));
		}
	}

	private String bcrypt(String data, int complexity) {
		return BCrypt.hashpw(data, BCrypt.gensalt(complexity));
	}

	public ZToken login(ZLogin req) {
		String q = "from ZUser where email=:email";
		ZUser user = (ZUser) currentSession()
					.createQuery(q)
					.setParameter("email", req.getEmail())
					.getSingleResult();

		if (BCrypt.checkpw(req.getPassword(), user.getPassword())) {
			ZToken token=new ZToken(user, jwt.getJwtToken(user));
			token.setPrefix(prefix);
			return token;
		} else {
			throw new ForbiddenException("Verify your credentials");
		}
	}

	public ZUser get(long id) {
		return Optional
				.ofNullable(super.get(id))
				.orElseThrow(NoResultException::new);
	}

	@SuppressWarnings({"unchecked"})
	public List<ZUser> list() {
		String query = "from ZUser";
		return list(currentSession().createQuery(query));
	}
}
