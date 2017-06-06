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
    		listing.setCreatedAt(new LocalDateTime());
    	}

        return persist(listing);
    }

	public List<Post> getPostsNewerThan(final LocalDateTime time) {
		return getPostsFromArchive(time, '>');
	}

	public List<Post> getPostsOlderThan(final LocalDateTime time) {
		return getPostsFromArchive(time, '<');
	}

    public Post addPost(PostRequest request) {
		final Profile profile = profileService.findOrCreate(request.getProfile());
		try {
			return postService.create(request.getPost(), profile);			
		} catch (ConstraintViolationException e) {
			currentSession().getTransaction().rollback();
			return postService.updateLikes(
					request.getPost().getPostReaction().getCountLikes(), 
					request.getPost().getFacebookId() 
				);
		} finally {
//	    	updateLastActivity(findById(profile.getListingId()));
//	    	updateLastActivity(profile);			
		}
    }

    public Listing updateTitle(Listing obj) {
    	namedQuery("listing.updateTitle")
    	.setParameter("title", obj.getTitle())
    	.setParameter("id", obj.getId())
    	.executeUpdate();
    	return obj;
    }

	private List<Post> getPostsFromArchive(final LocalDateTime time, char op) {
		final String query = "select p from Post p where time "+op+" to_timestamp(:ts, 'YYYY-MM-dd HH24:MI:ss') "+
				             "order by time desc ";
		return currentSession()
			   .createQuery(query, Post.class)
			   .setParameter("ts", forPattern("YYYY-MM-dd HH:mm:ss").print(time))
			   .setMaxResults(20)
			   .getResultList();
	}

	/**
	 * XXX: work out these names
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
