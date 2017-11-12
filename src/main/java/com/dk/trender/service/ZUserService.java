package com.dk.trender.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.ws.rs.ForbiddenException;

import org.hibernate.SessionFactory;

import com.dk.trender.auth.BCrypt;
import com.dk.trender.auth.JwtService;
import com.dk.trender.auth.ZLogin;
import com.dk.trender.auth.ZToken;
import com.dk.trender.core.ZUser;

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

	public ZUser create(ZUser obj) {
		String encrypted=bcrypt(obj.getPassword(), 12);
		obj.setPassword(encrypted);
		return persist(obj);
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
