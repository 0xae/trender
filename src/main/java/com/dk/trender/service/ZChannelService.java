package com.dk.trender.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;
import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZTimeline;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;

import io.dropwizard.hibernate.AbstractDAO;

public class ZChannelService extends AbstractDAO<ZChannel> {
	private static final Logger log = LoggerFactory.getLogger(ZChannelService.class);
	public ZChannelService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public ZChannel byId(long id) {
		return Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
	}

	public ZChannel create(ZChannel obj) {
		ZChannel t = persist(obj);
		log.info("created zchannel {}#{}", t.getId(), t.getName());
		return t;
	}

	public ZChannel update(ZChannel obj) {
		obj.setLastUpdate(DateTime.now());
		currentSession().update(obj);
		return obj;
	}

	public void delete(long id) {
		int affected = currentSession()
		  .createQuery("delete from ZChannel z where id=:id")
		  .setParameter("id", id)
		  .executeUpdate();

		if (affected == 0) {
			throw new NoResultException("No channel with id" + id + " found!");
		}
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> find(String audience) {
		String query = "from ZChannel z"+
						" where z.audience=:audience"+
						" order by lastUpdate desc";

		return list(currentSession()
				     .createQuery(query)
					 .setParameter("audience", audience)
				);
	}

	@SuppressWarnings({"unchecked"})
	public ZChannel findByName(String name, String q) {
		q = q.trim().toLowerCase();
		String query = "from ZChannel z "+
						"where lower(trim(z.name))=lower(trim(:name))";
		log.info("searching for channel {}", name);
		List<ZChannel> l = list(
			currentSession()
		    .createQuery(query)
			.setParameter("name", name)
		);

		// TODO move this search to postgres with json query operators
		//      but first we need to figure out a way to make native queries
		//      and use the same ResultSetTransformer
		ObjectMapper mapper = new ObjectMapper();
		for (ZChannel chan : l) {
			try {
				HashMap<String, Object> queryConf =
					mapper.readValue(chan.getQueryConf(), HashMap.class);
				
				// i know i wont ever find a null here ;)
				String $q = queryConf.get("q").toString();
				if ($q.equalsIgnoreCase(q))
					return chan;
			} catch (IOException e) {
				// XXX: it is not possible to reach here
				// because postgres wont allow invalid json
				// to be created
				// i cant gracefuly recover from this error
				throw new RuntimeException(e);
			}				
		}

		throw new NoResultException();
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> all() {
		return list(currentSession()
					.createQuery("from ZChannel"));
	}

	@SuppressWarnings({"unchecked"})
	public List<ZCollection> collections(long id) {
		String query = "from ZCollection c"+
					   " where c.channelId = :channelId";

		return currentSession()
			.createQuery(query)
			.setParameter("channelId", id)
			.getResultList();		
	}
}
