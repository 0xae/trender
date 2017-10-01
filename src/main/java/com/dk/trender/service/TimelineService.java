package com.dk.trender.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.NoResultException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.Post;
import com.dk.trender.core.Timeline;

import io.dropwizard.hibernate.AbstractDAO;

public class TimelineService extends AbstractDAO<Timeline> {
//	private static final Map<Long, Timeline> map = new ConcurrentHashMap<>();
	private static final Logger log = LoggerFactory.getLogger(TimelineService.class);

	// XXX: sortBy should be by indexedAt
	private static final String SORT_ORDER = "timestamp asc";
	
	private static final int POSTS_PER_REQUEST = 50;

	private final PostService post;
	
	public TimelineService(SessionFactory sessionFactory, PostService post) {
		super(sessionFactory);
		this.post = post;
	}

	public Timeline byId(long id) {
//		 Timeline t = Optional
//				.ofNullable(map.get(id))
//				.orElseGet(() -> {
//					Timeline v = get(id);
//					if (v == null)
//						throw new NoResultException("Timeline not found");
//					map.put(v.getId(), v);
//					return v;
//				});

		 Timeline t = Optional
				.ofNullable(get(id))
				.orElseThrow(NoResultException::new);
		 log.info("get timeline {}#{}", t.getId(), t.getName());
		 return t;
	}

	public Timeline create(Timeline obj) {	
		Timeline t = persist(obj);
//		map.put(t.getId(), t);
		log.info("create timeline {}", t.getId(), t.getName());
		return t;
	}

	public Timeline update(Timeline obj) {
		obj.setUpdateDate(DateTime.now());
		currentSession().update(obj);
//		map.put(obj.getId(), obj);
		log.info("update timeline {}#{}", obj.getId(), obj.getName());
		return obj;
	}

	public Timeline delete(Timeline obj) {
		obj.setIsActive(false);
		currentSession().update(obj);
//		map.remove(obj.getId());
		log.info("delete timeline {}#{}", obj.getId(), obj.getName());
		return obj;
	}

	public List<Timeline> all() {
		log.info("get timeline {}#{}");
		return list(namedQuery("timeline.findAll"));
	}

	public Timeline.Stream stream(long timelineId, int limit) {
		Timeline t = byId(timelineId);

		List<Post> posts = post.filter(
			  t.getTopic(), Math.min(POSTS_PER_REQUEST, limit), 
			  t.getIndex(), SORT_ORDER
		);

		if (!posts.isEmpty()) {
			int start = t.getIndex() + posts.size();
			t.index(start);
			update(t);			
		}

		return Timeline.Stream.of(t, posts);
	}

	public Timeline.Stream fetch(long timelineId, int start, int limit) {
		Timeline t = byId(timelineId);

		List<Post> list = post.filter(
			  t.getTopic(), Math.min(POSTS_PER_REQUEST, limit), 
			  start, SORT_ORDER
		);		
		
		return Timeline.Stream.of(t, list);
	}	
}