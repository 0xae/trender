package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.joda.time.LocalDateTime;
import static org.joda.time.format.DateTimeFormat.forPattern;

import com.dk.trender.core.Post;
import com.dk.trender.core.PostReaction;
import com.dk.trender.core.Profile;
import com.dk.trender.service.utils.TimeParser;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:37
 */
public class PostService extends AbstractDAO<Post> {
    public PostService(final SessionFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
    	return list(namedQuery("post.findAll"));
    }

    public Post create(Post post, Profile profile) {
    	post.setProfileId(profile.getId());
    	post.setListingId(profile.getListingId());
    	return create(post);
    }

    public Post getById(long id) {
    	Post p = get(id);
    	if (p == null) {
    		throw new NotFoundException();    		
    	}
    	return p;
    }

	public Post getByFacebookId(final String facebookId) {
		return (Post) currentSession()
					  .getNamedQuery("post.findByFacebook")
				      .setParameter("facebookId", facebookId)
				      .getSingleResult();
	}

	public List<Post> searchPosts(String query, LocalDateTime start, LocalDateTime end) {
		final String sql = 
				"from Post where description like concat('%',:query,'%') " +
//				"and time >=  to_timestamp(:start, 'YYYY-MM-dd HH24:MI:ss') "+
//				"and time <=  to_timestamp(:end, 'YYYY-MM-dd HH24:MI:ss')  "+
				"and time >=  to_timestamp(:start, 'YYYY-MM-dd') "+
				"and time <=  to_timestamp(:end, 'YYYY-MM-dd')  "+
				"order by time DESC ";

		return currentSession()
				   .createQuery(sql, Post.class)
				   .setParameter("query", query)
				   .setParameter("start", forPattern("YYYY-MM-dd ").print(start))
				   .setParameter("end", forPattern("YYYY-MM-dd ").print(end))
				   .setMaxResults(30)
				   .getResultList();
	}

	public Post updateLikes(long likes, String facebookId) {
		final Post p = getByFacebookId(facebookId);
		final PostReaction r = p.getPostReaction();
		if (r.getCountLikes() != likes) {
			r.setCountLikes(likes);
			p.setTimestamp(p.getTimestamp().plusMinutes(5));
			currentSession().save(p);			
		}
		return p;
	}

    private Post create(Post object) {
		final LocalDateTime now = new LocalDateTime();
    	if (object.getTimestamp() == null) {
    		LocalDateTime postTime = new TimeParser().parseTime(object.getTimming(), now);
    		object.setTimestamp(postTime);
    	}

    	if (object.getIndexedAt() == null) {
    		object.setIndexedAt(now);
    	}

    	return persist(object);    		
    }
}
