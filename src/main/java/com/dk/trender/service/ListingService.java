package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:47
 */
public class ListingService extends AbstractDAO<Listing> {
	private final PostService postService;

	public ListingService(SessionFactory factory, PostService postService) {
        super(factory);
        this.postService = postService;
    }

    @SuppressWarnings("unchecked")
    public List<Listing> findAll() {
        return list(namedQuery("listing.findAll"));
    }

    public Listing create(Listing listing) {
    	if (listing.getCreatedAt() == null) {
    		listing.setCreatedAt(DateTime.now());
    	}
        return persist(listing);
    }

    public Post createPost(long listingId, Post post) {
    	post.setListingId(listingId);
		// create post
    	Listing listing = getById(listingId);
    	final Post postCreated = postService.create(post);

    	// update listing
    	listing.setUpdatedAt(DateTime.now());
    	currentSession().save(listing);

    	currentSession().flush();
    	return postCreated;
    }

    public Listing getById(long id) {
    	final Listing l = get(id);
    	if (l == null) {
    		throw new NotFoundException();
    	}
    	return l;
    }

    public List<Post> fetchPosts(long listingId) {
    	return postService.fetchPostsByListing(listingId);
    }
    
    public List<Post> todayPosts(long listingId) {
    	final DateTime today = DateTime.now();
    	return postService.filterPosts(listingId, today.minusMinutes(15), 
    								   today.plusMinutes(30), 20L);
    }
}
