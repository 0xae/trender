package com.dk.trender.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZPost;
import com.dk.trender.core.ZTimeline;
import com.dk.trender.exceptions.SolrExecutionException;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-10-01
 * TODO: cache timelines
 * XXX: sortBy should be by indexedAt
 */
public class ZTimelineService extends AbstractDAO<ZTimeline> {
	private static final Logger log = LoggerFactory.getLogger(ZTimelineService.class);
	private static final String SORT_ORDER = "indexedAt asc";
	private static final int POSTS_PER_REQUEST = 50;
	public static final String DEFAULT_STARTL = "-1";
	public static final int DEFAULT_START = -1;
	private final ConcurrentUpdateSolrClient solr;

	public ZTimelineService(SessionFactory sessionFactory, 
						   ConcurrentUpdateSolrClient solr) {
		super(sessionFactory);
		this.solr = solr;
	}

	public ZTimeline byId(long id) {
		 ZTimeline t = Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
		 log.info("get timeline {}#{}", t.getId(), t.getName());
		 return t;
	}

	public ZTimeline create(ZTimeline obj) {
		ZTimeline t = persist(obj);
		log.info("create timeline {}", t.getId(), t.getName());
		return t;
	}

	public ZTimeline update(ZTimeline obj) {
		obj.setUpdateDate(DateTime.now());
		currentSession().update(obj);
		return obj;
	}

	public ZTimeline delete(long id) {
		ZTimeline obj = byId(id);
		currentSession().delete(obj);
		return obj;
	}

	@SuppressWarnings({"unchecked"})
	public List<ZTimeline> all(String state) {
		String query = "from Timeline t where :state='*' or state=:state "+
					   "order by creationDate desc";
		Query<ZTimeline> q = currentSession()
							.createQuery(query)
							.setParameter("state", state);
		return list(q);
	}

	public ZTimeline.Stream stream(String topic, int limit) {
		ZTimeline t = getTimeline(topic);
		return stream(t.getId(), limit, DEFAULT_START);
	}

	public ZTimeline.Stream stream(long timelineId, int limit, int streamStart) {
		ZTimeline t = byId(timelineId);
		int index = (streamStart == DEFAULT_START) ? 
					t.getIndex() : streamStart;

		QueryResponse resp = query(
			  t.getTopic(), 
			  Math.min(POSTS_PER_REQUEST, limit), 
			  index, SORT_ORDER
		);

		List<ZPost> posts = toPosts(resp.getResults());
		if (!posts.isEmpty() && streamStart < 0) {
			int start = t.getIndex() + posts.size();
			t.index(start);
			update(t);			
		}

		ZTimeline.Stream stream = new ZTimeline.Stream(t, posts);

		for (FacetField f : resp.getFacetFields()) {
			for (Count pivot : f.getValues()) {
				if (pivot.getCount() == 0) {
					continue;
				}
				ZTimeline.Topic topic = new ZTimeline.Topic(pivot.getName(), (int)pivot.getCount());
				stream.addTopic(f.getName(), topic);
			}
		}

		return stream;
	}

	private QueryResponse query(String query, int limit, int start, String sort) {
		SolrQuery sq = new SolrQuery();
		sq.set("q", query);
		sq.set("facet", true);
		sq.set("facet.field", "category", "type");
		sq.set("rows", limit);
		sq.set("start", start);
		sq.set("sort", sort);		

		try {
			return solr.query(sq);
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	public ZTimeline getTimeline(String topic) {
		try {
			 ZTimeline t = (ZTimeline)currentSession()
				 		.createQuery("from Timeline t where lower(trim(name))=lower(trim(:topic))")
				 		.setParameter("topic", topic)
		 				.getSingleResult();
			 return t;
		} catch (NoResultException e) {
			log.info("creating timeline {}", topic);
			ZTimeline t = new ZTimeline();
			t.setName("\""+topic+"\"");
			t.setTopic(topic);
			t.setPostTypes("steemit-post,twitter-post,bbc-post,youtube-post");
			t.setDescription(topic + " timeline.");
			t.setState("temp");
			return create(t);
		}
	}

	private List<ZPost> toPosts(SolrDocumentList list) {
		List<ZPost> res = new ArrayList<>();
		for (Iterator<SolrDocument> itr=list.iterator(); itr.hasNext(); ) {
			ZPost p = ZPost.fromDoc(itr.next());
			res.add(p);				
		}
		return res;		
	}
}