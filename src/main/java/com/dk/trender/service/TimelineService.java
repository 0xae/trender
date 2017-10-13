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

import com.dk.trender.core.Post;
import com.dk.trender.core.Timeline;
import com.dk.trender.exceptions.SolrExecutionException;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-10-01
 * TODO: cache timelines
 * XXX: sortBy should be by indexedAt
 */
public class TimelineService extends AbstractDAO<Timeline> {
	public static final int DEFAULT_START = -1;
	public static final String DEFAULT_START_L = "-1";
	private static final Logger log = LoggerFactory.getLogger(TimelineService.class);
	private static final String SORT_ORDER = "indexedAt asc";
	private static final int POSTS_PER_REQUEST = 50;
	private final ConcurrentUpdateSolrClient solr;

	public TimelineService(SessionFactory sessionFactory, 
						   ConcurrentUpdateSolrClient solr) {
		super(sessionFactory);
		this.solr = solr;
	}

	public Timeline byId(long id) {
		 Timeline t = Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
		 log.info("get timeline {}#{}", t.getId(), t.getName());
		 return t;
	}

	public Timeline create(Timeline obj) {
		Timeline t = persist(obj);
		log.info("create timeline {}", t.getId(), t.getName());
		return t;
	}

	public Timeline update(Timeline obj) {
		obj.setUpdateDate(DateTime.now());
		currentSession().update(obj);
		return obj;
	}

	public Timeline delete(Timeline obj) {
		obj.setIsActive(false);
		currentSession().update(obj);
		return obj;
	}

	@SuppressWarnings({"unchecked"})
	public List<Timeline> all(String state) {
		String query = "from Timeline t where :state='*' or state=:state "+
					   "order by creationDate desc";
		Query<Timeline> q = currentSession().createQuery(query)
							.setParameter("state", state);
		return list(q);
	}

	public Timeline.Stream stream(String name, int limit) {
		Timeline t = getTimeline(name);
		return stream(t.getId(), limit, DEFAULT_START);
	}
	
	public Timeline.Stream stream(long timelineId, int limit, int streamStart) {
		Timeline t = byId(timelineId);
		int index = (streamStart == DEFAULT_START) ? 
					t.getIndex() :
					streamStart;

		QueryResponse resp = query(
			  t.getTopic(), 
			  Math.min(POSTS_PER_REQUEST, limit), 
			  index, SORT_ORDER
		);

		List<Post> posts = toPosts(resp.getResults());
		if (!posts.isEmpty() && streamStart < 0) {
			int start = t.getIndex() + posts.size();
			t.index(start);
			update(t);			
		}

		Timeline.Stream stream = new Timeline.Stream(t, posts);

		for (FacetField f : resp.getFacetFields()) {
			for (Count pivot : f.getValues()) {
				if (pivot.getCount() == 0) 
					continue;
				Timeline.Topic topic = new Timeline.Topic(pivot.getName(), (int)pivot.getCount());
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

	public Timeline getTimeline(String name) {
		try {
			 Timeline t = (Timeline)currentSession()
				 		.createQuery("from Timeline t where lower(trim(name))=lower(trim(:name))")
				 		.setParameter("name", name)
		 				.getSingleResult();
			 return t;
		} catch (NoResultException e) {
			log.info("creating timeline {}", name);
			Timeline t = new Timeline();
			t.setName(name);
			t.setTopic(name);
			t.setPostTypes("steemit-post,twitter-post,bbc-post,youtube-post");
			t.setDescription(name + " timeline created by trender.");
			t.setState("temp");
			return create(t);
		}
	}

	private List<Post> toPosts(SolrDocumentList list) {
		List<Post> res = new ArrayList<>();
		for (Iterator<SolrDocument> itr=list.iterator(); itr.hasNext(); ) {
			Post p = Post.fromDoc(itr.next());
			res.add(p);				
		}
		return res;		
	}
}