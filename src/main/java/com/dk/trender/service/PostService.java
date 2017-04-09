package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.dk.trender.core.Post;
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

    private Post create(Post object) {
		final DateTime now = DateTime.now();
    	if (object.getTimestamp() == null) {
    		DateTime postTime = new TimeParser().parseTime(object.getTimming(), now);
    		object.setTimestamp(postTime);
    	}

    	if (object.getIndexedAt() == null) {
    		object.setIndexedAt(now);
    	}

    	return persist(object);    		
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

    public List<Post> fetchRecentPosts(long listingId) {
    	final String query = "from Post p where listing_id = :listingId order by timestamp desc";
    	return currentSession()
	    	   .createQuery(query, Post.class)
	    	   .setParameter("listingId", listingId)
	    	   .getResultList();
    }

    public List<Post> filterPosts(long listingId, DateTime start, DateTime end, Long limit) {
    	final String query = "select p from Post p where listing_id = :listingId "+
                             "and timestamp between :start and :end " +
    			             "order by timestamp desc ";
    	limit = (limit==null) ? 20 : limit;

    	return currentSession()
	    	   .createQuery(query, Post.class)
	    	   .setParameter("listingId", listingId)
	    	   .setParameter("start", start)
	    	   .setParameter("end", end)
	    	   .getResultList();
    }

	public Post getByFacebookId(final String facebookId) {
		return (Post) currentSession()
					  .getNamedQuery("post.findByFacebook")
				      .setParameter("facebookId", facebookId)
				      .getSingleResult();
	}
}
