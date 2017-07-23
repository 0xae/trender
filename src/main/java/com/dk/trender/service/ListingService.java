package com.dk.trender.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.Listing;
import com.dk.trender.core.PostRequest.ListingDetails;

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

    @SuppressWarnings("unchecked")
    public List<Listing> findAll() {
        return namedQuery("listing.findAll")
        	   .getResultList();
    }
}
