package com.dk.trender.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.Profile;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-04-01 20:17:47
 */
public class ProfileService extends AbstractDAO<Profile>  {
    public ProfileService(final SessionFactory factory) {
        super(factory);
    }

	@SuppressWarnings("unchecked")
	public List<Profile> findAll() {
		return namedQuery("profile.findAll")
			  .getResultList();
	}

	public Profile findById(long id) {
		return (Profile) namedQuery("profile.byId")
				 	     .setParameter("id", id)
				 	     .getSingleResult();
	}

	public Profile findByUsername(String username) {
		return (Profile) namedQuery("profile.byUsername")
						 .setParameter("username", username)
						 .getSingleResult();
	}

	public Profile findOrCreate(Profile p) {
		try {
			return findByUsername(p.getUsername());
		} catch (javax.persistence.NoResultException e) {
			return create(p);
		}
	}

	public List<Profile> fetchProfileWithoutPictures() {
		return currentSession()
		.createQuery("from Profile p where picture is null", Profile.class)
		.setMaxResults(20)
		.getResultList();
	}

    public Profile create(Profile obj) {
    	final LocalDateTime time = new LocalDateTime();
    	if (obj.getIndexedAt() == null)
    		obj.setIndexedAt(time);
    	if (obj.getListingId() == 0) 
    		obj.setListingId(1);
    	obj.setLastUpdate(time);
		return persist(obj);
	}

	public Profile update(Profile p) {
		p.setLastUpdate(new LocalDateTime());
		currentSession().save(p);
		return p;
	}
}
