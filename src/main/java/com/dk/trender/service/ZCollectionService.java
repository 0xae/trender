package com.dk.trender.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;

import io.dropwizard.hibernate.AbstractDAO;

public class ZCollectionService extends AbstractDAO<ZCollection>{
	private static final Logger log = LoggerFactory.getLogger(ZCollectionService.class);
	private static final int ROWS_PER_REQ = 10;
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
		return (ZCollection)currentSession().createQuery(q)
				.setParameter("name", name)
				.getSingleResult();
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

	public Map<String, List<ZPost>> feed(ZChannel chan, String collName) {
		ZCollection coll = byName(collName);
		QueryConf mainConf = chan.queryConf();
		QueryConf collConf = coll.queryConf();
		boolean updateChan = false;
		mainConf.setLimit(ROWS_PER_REQ);
		collConf.setLimit(ROWS_PER_REQ);

		collConf.getFq().addAll(mainConf.getFq());
		collConf.setQ(mainConf.getQ());
		collConf.setTypes(coll.getTypes());

		if (collConf.getStart()==0) {
			collConf.setStart(mainConf.getStart());
			updateChan = true;
		}

		if (collConf.getQ()==null || collConf.getQ().trim().isEmpty()) {
			collConf.setQ(mainConf.getQ());
		}

		log.info("conf: " + collConf.toString());
		Map<String, List<ZPost>> types=search.groupByType(collConf);

		// int fetched = 0;
		// XXX: this wont work well, because we split requests
		//      into types remember ?
//		if (updateChan && fetched>0) {
//			QueryConf c = chan.queryConf();
//			c.setStart(c.getStart() + fetched);
//			chan.queryConf(c);
//			// update the index of the channel
//			$channel.update(chan);
//		}
		return types;
	}
}
