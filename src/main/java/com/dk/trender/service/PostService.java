package com.dk.trender.service;

import java.util.List;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import static org.joda.time.format.DateTimeFormat.forPattern;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostReaction;
import com.dk.trender.core.PostRequest;
import com.dk.trender.core.Profile;
import com.dk.trender.service.utils.TimeParser;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:37
 */
public class PostService extends AbstractDAO<Post> {
	private final ProfileService profileService;

	public PostService(final SessionFactory factory,
					   final ProfileService service) {
        super(factory);
        this.profileService = service;
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
    	return list(namedQuery("post.findAll"));
    }

    public Post addPost(PostRequest request) {
		final Profile profile = profileService.findOrCreate(request.getProfile());
		try {
			return create(request.getPost(), profile);			
		} catch (ConstraintViolationException e) {
			currentSession().getTransaction().rollback();
			return updateLikes(
					request.getPost().getPostReaction().getCountLikes(), 
					request.getPost().getFacebookId() 
				);
		}
    }
    
    /**
     * XXX: move lines 50,57 to Post class
     * @param post
     * @param profile
     * @return
     */
    private Post create(Post post, Profile profile) {
    	post.setProfileId(profile.getId());
    	post.setListingId(profile.getListingId());
		post.setIndexedAt(new LocalDateTime());
		post.setTimming("");
    	return persist(post);
    }

	public List<Post> getPostsNewerThan(final LocalDateTime time) {
		return getPostsFromArchive(time, '>');
	}

	public List<Post> getPostsOlderThan(final LocalDateTime time) {
		return getPostsFromArchive(time, '<');
	}

    public Listing updateTitle(Listing obj) {
    	namedQuery("listing.updateTitle")
    	.setParameter("title", obj.getTitle())
    	.setParameter("id", obj.getId())
    	.executeUpdate();
    	return obj;
    }

    public Post getById(long id) {
    	Post p = get(id);
    	if (p == null) {
    		throw new NotFoundException();    		
    	}
    	return p;
    }

	public Post getByFacebookId(final String facebookId) {
		return (Post) currentSession()
					  .getNamedQuery("post.findByFacebook")
				      .setParameter("facebookId", facebookId)
				      .getSingleResult();
	}

	/**
	 * XXX: time format must be adjustable
	 * @param query
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Post> searchPosts(String query, LocalDateTime start, LocalDateTime end) {
		final String sql = 
				"from Post where description like concat('%',:query,'%') " +
//				"and time >=  to_timestamp(:start, 'YYYY-MM-dd HH24:MI:ss') "+
//				"and time <=  to_timestamp(:end, 'YYYY-MM-dd HH24:MI:ss')  "+
				"and time >=  to_timestamp(:start, 'YYYY-MM-dd') "+
				"and time <=  to_timestamp(:end, 'YYYY-MM-dd')  "+
				"order by time DESC ";

		return currentSession()
				   .createQuery(sql, Post.class)
				   .setParameter("query", query)
				   .setParameter("start", forPattern("YYYY-MM-dd ").print(start))
				   .setParameter("end", forPattern("YYYY-MM-dd ").print(end))
				   .setMaxResults(30)
				   .getResultList();
	}

	public Post updateLikes(long likes, String facebookId) {
		final Post p = getByFacebookId(facebookId);
		final PostReaction r = p.getPostReaction();
		if (r.getCountLikes() != likes) {
			r.setCountLikes(likes);
			p.setTimestamp(p.getTimestamp().plusMinutes(5));
			currentSession().save(p);			
		}
		return p;
	}

	/**
	 * XXX: maybe we should remove this
	 * @param time
	 * @param op
	 * @return
	 */
	private List<Post> getPostsFromArchive(final LocalDateTime time, char op) {
		final String query = "select p from Post p where time "+op+" to_timestamp(:ts, 'YYYY-MM-dd HH24:MI:ss') "+
				             "order by time desc ";
		return currentSession()
			   .createQuery(query, Post.class)
			   .setParameter("ts", forPattern("YYYY-MM-dd HH:mm:ss").print(time))
			   .setMaxResults(20)
			   .getResultList();
	}
}
