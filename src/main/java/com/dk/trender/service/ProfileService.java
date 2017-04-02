package com.dk.trender.service;

import java.util.List;

import org.hibernate.SessionFactory;
import org.joda.time.DateTime;

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

    public Profile create(Profile obj) {
    	final DateTime time = DateTime.now();
    	if (obj.getIndexedAt() == null)
    		obj.setIndexedAt(time);
    	obj.setLastUpdate(time);
		return persist(obj);
	}

	public Profile update(Profile p) {
		p.setLastUpdate(DateTime.now());
		currentSession().save(p);
		return p;
	}
	
	public Profile updateLastActivity(Profile p) {
		p.setLastActivity(DateTime.now());
		currentSession().save(p);
		return p;
	}
}
