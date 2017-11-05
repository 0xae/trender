package com.dk.trender.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.Timeline;
import com.dk.trender.core.ZChannel;

import io.dropwizard.hibernate.AbstractDAO;

public class SZChannel extends AbstractDAO<ZChannel> {
	private static final Logger log = LoggerFactory.getLogger(SZChannel.class);

	public SZChannel(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ZChannel byId(long id) {
		return Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
	}

	public void deleteById(long id) {
		String query = "delete from ZChannel z where id=:id";
		Query q = currentSession()
				  .createQuery(query)
				  .setParameter("id", id);
		q.executeUpdate();
	}
	
	public ZChannel create(ZChannel obj) {
		ZChannel t = persist(obj);
		log.info("create zchannel {}#{}", t.getId(), t.getName());
		return t;
	}

	public ZChannel update(ZChannel obj) {
		obj.setLastUpdate(DateTime.now());
		currentSession().update(obj);
		return obj;
	}
	
	public ZChannel delete(long id) {
		ZChannel obj = byId(id);
		currentSession().delete(obj);
		return obj;
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> find(String audience) {
		String query = "from ZChannel z"+
						" where z.audience=:audience"+
						" order by lastUpdate desc";
		return list(currentSession().createQuery(query)
						.setParameter("audience", audience));
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> all() {
		String query = "from ZChannel";
		Query<ZChannel> q = currentSession()
							.createQuery(query);
		return list(q);
	}
}
