package com.dk.trender.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import static org.joda.time.format.DateTimeFormat.forPattern;

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
	public ListingService(SessionFactory factory) {
        super(factory);
    }
	
    public Listing create(Listing listing) {
    	if (listing.getCreatedAt() == null) {
    		listing.setCreatedAt(new LocalDateTime());
    	}

        return persist(listing);
    }
    
    public Listing findById(long id) {
    	return (Listing) namedQuery("listing.findById")
    					 .setParameter("id", id)
			    		 .getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<Listing> findAll() {
        return namedQuery("listing.findAll")
        	   .getResultList();
    }

	/**
	 * TODO: work out these names
	 * @param obj
	 * @return
	 */
    private Listing updateActivity(Listing obj) {
    	obj.setLastActivity(new LocalDateTime());
    	currentSession()
    	.createQuery("update Listing set last_activity=now() where id=:objId")
    	.setParameter("objId", obj.getId())
    	.executeUpdate();
    	return obj;
    }

	private Profile updateProfile(Profile obj) {
    	obj.setLastActivity(new LocalDateTime());
    	currentSession()
    	.createQuery("update Profile set last_activity=now() where id=:objId")
    	.setParameter("objId", obj.getId())
    	.executeUpdate();
    	return obj;
    }
}
