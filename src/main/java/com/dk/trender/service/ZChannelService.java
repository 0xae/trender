package com.dk.trender.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;

import io.dropwizard.hibernate.AbstractDAO;

public class ZChannelService extends AbstractDAO<ZChannel> {
	private static final Logger log = LoggerFactory.getLogger(ZChannelService.class);
	private static final int MAX_ITEM_SUGS = 4;
	private final ZSearchService search;

	public ZChannelService(SessionFactory sessionFactory,
						   ZSearchService service) {
		super(sessionFactory);
		this.search = service;
	}

	public ZChannel byId(long id) {
		ZChannel c = Optional
			.ofNullable(get(id))
			.orElseThrow(NoResultException::new)
			.setLastAccess(DateTime.now());
		// update(c);
		return c;
	}

	public ZChannel create(ZChannel obj) {
		ZChannel t = persist(obj);
		log.info("created zchannel {}#{}", 
			t.getId(), t.getName()
		);
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
			throw new NoResultException("No channel with id " + id + " found!");
		}
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> find(String audience) {
		String query = "from ZChannel z"+
						" where z.audience=:audience"+
						" order by lastUpdate desc";

		return currentSession()
			     .createQuery(query)
				 .setParameter("audience", audience)
				 .getResultList();
	}

	// TODO: fix this to avoid duplicate 
	//       channels with the same name
	// TODO move this search to postgres with json query operators
	//      but first we need to figure out a way to make native sql queries
	//      and use the same ResultSetTransformer
	@SuppressWarnings({"unchecked"})
	public ZChannel findByName(String name, String q_) {
		String q = q_.trim().toLowerCase();
		String query = "from ZChannel z "+
						"where lower(trim(z.name))=lower(trim(:name))";
		log.info("searching for channel {}", name);
		List<ZChannel> l = currentSession()
		    .createQuery(query)
			.setParameter("name", name)
			.getResultList();

		for (ZChannel chan : l) {
			String $q = chan.queryConf().getQ();
			if ($q.equalsIgnoreCase(q))
				return chan;
		}

		throw new NoResultException();
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> all() {
		return currentSession()
				.createQuery("from ZChannel")
				.getResultList();
	}

	@SuppressWarnings({"unchecked"})
	public List<ZCollection> collections(long id, int start) {
		String query = "from ZCollection c" + 
					   " where c.channelId = :channelId";

		return currentSession()
			.createQuery(query)
			.setParameter("channelId", id)
			.setMaxResults(10)
			.setFirstResult(start)
			.getResultList();
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> recent() {
		String query = "from ZChannel c"+
				   " order by last_access desc";

		return currentSession()
			.createQuery(query)
			.setMaxResults(25)
			.getResultList();
	}

	@SuppressWarnings({"unchecked"})
	public List<ZChannel> top() {
		String query = "from ZChannel";

		List<ZChannel> list = currentSession()
			.createQuery(query)
			.setMaxResults(10)
			.getResultList();

		return list.parallelStream()
			.map(this::count)
			.sorted((o1,o2) -> o1.totalCount() - o2.totalCount())
			.collect(Collectors.toList())
			.subList(0, MAX_ITEM_SUGS);
	}

	private ZChannel count(ZChannel chan) {
		int count = search.count(chan.queryConf());
		return chan.totalCount(count);
	}
}
