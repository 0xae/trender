package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostRequest;
import com.dk.trender.core.Profile;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:47
 */
public class ListingService extends AbstractDAO<Listing> {
	private final PostService postService;
	private final ProfileService profileService;

	public ListingService(SessionFactory factory, 
						  PostService postService,
						  ProfileService profileService) {
        super(factory);
        this.postService = postService;
        this.profileService = profileService;
    }

    @SuppressWarnings("unchecked")
    public List<Listing> findAll() {
        return namedQuery("listing.findAll")
        	   .getResultList();
    }

    public Listing findById(long id) {
    	return (Listing) namedQuery("listing.findById")
    					 .setParameter("id", id)
			    		 .getSingleResult();
    }

    public Listing create(Listing listing) {
    	if (listing.getCreatedAt() == null) {
    		listing.setCreatedAt(DateTime.now());
    	}
        return persist(listing);
    }
    
    @SuppressWarnings("unchecked")
    public List<Post> findPosts(Listing l) {
    	return currentSession()
    		   .createQuery("from Post where listing_id = :listingId")
    		   .setParameter("listingId", l.getId())
    		   .getResultList();
    }

    public Post addPost(Listing listing, PostRequest request) {
		final Profile profile = profileService.findOrCreate(request.getProfile());
    	final Post post = postService.create(request.getPost(), listing, profile);
    	
    	updateLastActivity(listing);
    	profileService.updateLastActivity(profile);
    	return post;
    }

    public Listing updateTitle(Listing obj) {
    	namedQuery("listing.updateTitle")
    	.setParameter("title", obj.getTitle())
    	.setParameter("id", obj.getId())
    	.executeUpdate();
    	return obj;
    }

    public Listing updateLastActivity(Listing obj) {
    	obj.setLastActivity(DateTime.now());
    	currentSession().save(obj);
    	return obj;
    }
}
