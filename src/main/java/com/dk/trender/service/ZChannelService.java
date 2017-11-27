package com.dk.trender.service;

import static com.dk.trender.core.ZPost.BBC;
import static com.dk.trender.core.ZPost.STEEMIT;
import static com.dk.trender.core.ZPost.TWITTER;
import static com.dk.trender.core.ZPost.YOUTUBE;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.NoResultException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZGroup;
import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.SolrExecutionException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;

import io.dropwizard.hibernate.AbstractDAO;

public class ZChannelService extends AbstractDAO<ZChannel> {
	private static final Logger log = LoggerFactory.getLogger(ZChannelService.class);
	private static final int ROWS_PER_REQ=5;
	private final ConcurrentUpdateSolrClient solr;

	public ZChannelService(SessionFactory sessionFactory,
						   ConcurrentUpdateSolrClient client) {
		super(sessionFactory);
		this.solr = client;
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
			throw new NoResultException("No channel with id " + id + " found!");
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

	// TODO: fix this to avoid duplicate 
	//       channels with the same name
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

	public Map<String, ZCollection> feed(ZChannel chan) {
		QueryConf conf = chan.queryConf();
		conf.setLimit(ROWS_PER_REQ);
		Map<String, List<ZPost>> types=groupByType(conf);

		ZCollection newsfeed = nativeCol("t/newsfeed", "Newsfeed");
		ZCollection videos = nativeCol("t/videos", "Videos");
		ZCollection places = nativeCol("t/places", "Places");
		ZCollection events = nativeCol("t/events", "Events");
		ZCollection more =	nativeCol("t/more", "More...");

		videos.setPosts(types.get(ZPost.YOUTUBE));
		newsfeed.setPosts(types.get(ZPost.STEEMIT));
		places.setPosts(types.get(ZPost.TWITTER));
		more.setPosts(types.get(ZPost.BBC));

		// List for public collections
		List<ZCollection> cols = Arrays.asList(
			videos,
			places,
			newsfeed,
			more
		);

		return cols.stream()
		.filter(col -> !col.getPosts().isEmpty())
		.collect(Collectors.<ZCollection, String, ZCollection>toMap(
				ZCollection::getName, 
				col -> col
		));
	}
	
	public static interface PostFilter {
		boolean filter(ZPost p);
	}

	private Map<String, List<ZPost>> groupByType(QueryConf conf) {		
		return Arrays
		.asList(BBC, STEEMIT, TWITTER, YOUTUBE)
		.parallelStream()
		.collect(Collectors.<String, String, List<ZPost>>toMap(
			type -> type,
			type -> {
				return search(conf,type)
				  .getResults()
				  .stream()
				  .map(ZPost::fromDoc)
				  .collect(Collectors.toList());
			})
		);
	}

	private ZCollection nativeCol(String name, String label) {
		ZCollection col = new ZCollection();
		col.setId(1);
		col.setName(name);
		col.setLabel(label);
		col.setDisplay(true);
		col.setAudience(ZChannel.PRIVATE);
		col.setCuration(BigDecimal.ONE);
		return col;
	}

	private QueryResponse search(QueryConf conf, String type) {
		SolrQuery sq = new SolrQuery();
		sq.set("q", conf.getQ());
		sq.set("rows", conf.getLimit());
		sq.set("start", conf.getStart());
		sq.set("sort", conf.getSort());
		sq.set("facet", true);
		sq.set("facet.field", "category", "type");

		List<String> fq = new ArrayList<>(conf.getFq());
		fq.add("type:" + type);
		fq.add("!cached:none");
		sq.set("fq", fq.toArray(new String[]{}));

		log.info("conf: {}", sq);
		try {
			return solr.query(sq);
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
}
