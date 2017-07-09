package com.dk.trender.service;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.Post;
import com.dk.trender.core.PostMedia;
import io.dropwizard.hibernate.AbstractDAO;

public class PostMediaService extends AbstractDAO<PostMedia> {
	public PostMediaService(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	public PostMedia addPostMedia(Post p, PostMedia media) {
		final LocalDateTime today = new LocalDateTime();
    	media.setPostId(p.getId());
		media.setIndexedAt(today);
		media.setTime(today);
        return persist(media);
	}

	public List<PostMedia> getRecentPostMedia(LocalDateTime since, String type, String postFid) {	
		final String sql = 
			"from PostMedia "+
			"where (:type='*' or type=:type) "+
			"and (:postFid='everybody' or post_id=(from Post where facebook_id=:postFid)) "+
			"and time > to_timestamp(:since, 'YYYY-MM-dd HH24:MI:ss') "+
			"order by time DESC ";

		return currentSession()
				   .createQuery(sql, PostMedia.class)
				   .setParameter("type", type)
				   .setParameter("since", forPattern("YYYY-MM-dd HH:mm:ss").print(since))
				   .setParameter("postFid", postFid)
				   .setMaxResults(30)
				   .getResultList();
    }

	public List<PostMedia> getAllPostMedia(long postId, String type) {	
		final String sql = 
			"from PostMedia "+
			"where post_id=:postId "+
			"and (:type='*' or type=:type) "+
			"order by time DESC ";

		return currentSession()
				   .createQuery(sql, PostMedia.class)
				   .setParameter("postId", postId)
				   .setParameter("type", type)
				   .setMaxResults(30)
				   .getResultList();		
    }
}
