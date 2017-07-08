package com.dk.trender.service;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.PostMedia;
import io.dropwizard.hibernate.AbstractDAO;

public class PostMediaService extends AbstractDAO<PostMedia> {
	public PostMediaService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}
	
	public PostMedia addPostMedia(PostMedia object) {
		object.setIndexedAt(new LocalDateTime());
        return persist(object);
	}
	
	public List<PostMedia> getAllPostMedia(long postId, LocalDateTime since, String type) {	
		final String sql = 
				"from PostMedia "+
				"where post_id=:postId "+
				"and time >= to_timestamp(:since, 'YYYY-MM-dd HH24:MI:ss') "+
				"and (:type='*' or type=:type) "+
				"order by time DESC ";

		return currentSession()
				   .createQuery(sql, PostMedia.class)
				   .setParameter("postId", postId)
				   .setParameter("since", forPattern("YYYY-MM-dd").print(since))
				   .setParameter("type", type)
				   .setMaxResults(30)
				   .getResultList();		
    }

}
