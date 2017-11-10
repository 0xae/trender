package com.dk.trender.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;

import com.dk.trender.core.ZCollection;

import io.dropwizard.hibernate.AbstractDAO;

public class ZCollectionService extends AbstractDAO<ZCollection>{
	public ZCollectionService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ZCollection byId(long id) {
		return Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
	}
	
	public ZCollection create(ZCollection col) {		
		return persist(col);
	}

	public ZCollection update(ZCollection col) {
		currentSession().update(col);
		return col;
	}
	
	public void delete(long id) {
		ZCollection col = byId(id);
		currentSession().delete(col);
	}
	
	@SuppressWarnings({"unchecked"})
	public List<ZCollection> all(String audience) {
		String query = "from ZCollection z "+
			    "where z.audience=:audience "+
				"order by lastUpdate desc";
		return list(currentSession().createQuery(query)
					.setParameter("audience", audience));
	}
}
