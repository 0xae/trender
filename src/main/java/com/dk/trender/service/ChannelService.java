package com.dk.trender.service;

import java.util.List;
import java.util.Optional;

import org.hibernate.SessionFactory;

import com.dk.trender.core.Channel;

import io.dropwizard.hibernate.AbstractDAO;

public class ChannelService  extends AbstractDAO<Channel> {	
	public ChannelService(final SessionFactory factory) {
		super(factory);
	}

	public Channel findById(long id) {
		 return Optional
				.ofNullable(get(id))
				.get();
	}

	public List<Channel> findAll() {
		String query = "from Channel order by creation_date desc";
		return currentSession()
			  .createQuery(query, Channel.class)
			  .getResultList();
	}

	public Channel create(Channel obj) {
		return persist(obj);
	}

	public Channel update(Channel obj) {
		currentSession().save(obj);
		return obj;
	}
}
