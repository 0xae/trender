package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.dk.trender.core.Post;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:37
 */
public class PostService extends AbstractDAO<Post> {
    public PostService(SessionFactory factory) {
        super(factory);
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
    	return list(namedQuery("post.findAll"));
    }

    public Post create(Post object) {
    	if (object.getTimestamp() == null) {
    		object.setTimestamp(DateTime.now());
    	}

    	try {
        	return persist(object);    		
    	} catch (org.hibernate.exception.ConstraintViolationException e) {
    		throw new BadRequestException("Verify if facebookId is null or if some field is missing!");
    	}
    }

    public Post getById(long id) {
    	Post p = get(id);
    	if (p == null) {
    		throw new NotFoundException();    		
    	}
    	return p;
    }

    public List<Post> fetchPostsByListing(long listingId) {
    	return currentSession()
	    	   .createQuery("select p from Post p where listing_id = :listingId", Post.class)
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
