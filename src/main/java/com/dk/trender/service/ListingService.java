package com.dk.trender.service;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;

import com.dk.trender.api.PostRequest.ListingDetails;
import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:47
 */
public class ListingService extends AbstractDAO<Listing> {
	public ListingService(SessionFactory factory) {
        super(factory);
    }

    public Listing create(Listing listing) {
		listing.setCreatedAt(new LocalDateTime());
        return persist(listing);
    }

    public Listing findById(long id) {
    	return (Listing) namedQuery("listing.findById")
    					 .setParameter("id", id)
			    		 .getSingleResult();
    }

    public Listing findByName(String name) {
    	return (Listing) namedQuery("listing.findByName")
    					 .setParameter("name", name.toLowerCase().trim())
			    		 .getSingleResult();
    }

    public List<ListRank> getListingRank(int limit, int offset) {
		final String query = 
				"select title as name,count(1) as count from Listing l ";

		return currentSession()
		  .createQuery(query, ListRank.class)
		  .setMaxResults(Math.min(100, limit))
		  .setFirstResult(Math.min(0, offset))
		  .getResultList();
    	
    }

    public static class ListRank {
    	private String name;
    	private int count;

    	public void setCount(int count) {
			this.count = count;
		}
    	
    	public void setName(String name) {
			this.name = name;
		}
    	public int getCount() {
			return count;
		}
    	
    	public String getName() {
			return name;
		}
    }

    @SuppressWarnings("unchecked")
    public List<Listing> findAll() {
        return namedQuery("listing.findAll")
        	   .getResultList();
    }
}
