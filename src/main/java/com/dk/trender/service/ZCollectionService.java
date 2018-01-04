package com.dk.trender.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZSearch;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.BadRequest;

import io.dropwizard.hibernate.AbstractDAO;

public class ZCollectionService extends AbstractDAO<ZCollection>{
	private static final Logger log = LoggerFactory.getLogger(ZCollectionService.class);
	private final ZSearchService search;

	public ZCollectionService(SessionFactory sessionFactory,
							  ZSearchService search) {
		super(sessionFactory);
		this.search = search;
	}

	public ZCollection byId(long id) {
		return Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
	}

	public ZCollection byName(String name) {
		String q = "from ZCollection where name=lower(trim(:name))";
		return currentSession()
				.createQuery(q, ZCollection.class)
				.setParameter("name", name)
				.getSingleResult();
	}

	public ZCollection create(ZCollection col) {
		if (col.getName().toLowerCase().startsWith("t-")) {
			String msg = "Channels starting with t- are reserved.";
			throw new BadRequest(Arrays.asList(msg));
		}

		return persist(col);
	}

	public ZCollection update(ZCollection col) {
		if (!col.isInternal()) {
			currentSession().update(col);
		} else {
			log.info("No updates to internal collection #" + col.getName());
		}

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

	public ZCollection feed(ZChannel chan, ZCollection coll) {
		ZSearch collConf = coll.queryConf();

		// internal collections contains only inteligence configuration
		if (coll.isInternal()) {
			ZSearch mainConf = chan.queryConf();
			collConf.getFq().addAll(mainConf.getFq());
			collConf.setQ(mainConf.getQ())
					.setTypes(coll.getTypes())	
					.setStart(mainConf.getStart());
		}		

		Map<String, List<ZPost>> types=search.groupByType(collConf);
		List<ZPost> posts=new ArrayList<>();

		for (String t : types.keySet()) {
			posts.addAll(types.get(t));
		}

		// design the Feed interface, so fd is not a string
		// it is a feed instance represented by the interface
		// ZFeed fd = coll.getFeed();
		return coll.copy()
			.setPosts(posts);
	}
}
