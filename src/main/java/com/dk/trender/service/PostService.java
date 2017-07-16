package com.dk.trender.service;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.NotFoundException;

import org.hibernate.SessionFactory;
import org.hibernate.exception.ConstraintViolationException;
import org.joda.time.LocalDateTime;
import static org.joda.time.format.DateTimeFormat.forPattern;

import com.dk.trender.core.Post;
import com.dk.trender.core.PostMedia;
import com.dk.trender.core.PostRequest;
import com.dk.trender.core.Profile;

import io.dropwizard.hibernate.AbstractDAO;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 14:11:37
 */
public class PostService extends AbstractDAO<Post> {
	private final ProfileService profileService;
	private final MediaService postMediaService;

	public PostService(final SessionFactory factory,
					   final ProfileService service,
					   final MediaService postMedia) {
        super(factory);
        this.profileService = service;
        this.postMediaService = postMedia;
    }
	
    public PostMedia addPostMedia(PostMedia media, String fid) {
    	final Post p = findByFacebookId(fid);
    	return postMediaService.addPostMedia(p, media);
    }

    public List<PostMedia> getPostMediaObjects(long postId, String type) {    	
    	return postMediaService.getAllPostMedia(postId, type);
    }

    public List<PostMedia> getRecentPostMedia(LocalDateTime since, String type, String postFid) {    	
    	return postMediaService.getRecentPostMedia(since, type, postFid);
    }
    
    /**
     * TODO: log this
     * @param request
     * @return
     */
    public Post addPost(PostRequest request) {
		final Profile profile = profileService.findOrCreate(request.getProfile());
		try {
			return create(request.getPost(), profile);			
		} catch (ConstraintViolationException e) {
			currentSession().getTransaction().rollback();
			updatePostActivity(request.getPost());
			return request.getPost();
		}
    }

    @SuppressWarnings("unchecked")
    public List<Post> findAll() {
    	return list(namedQuery("post.findAll"));
    }

	public List<Post> findPostsNewerThan(final LocalDateTime time,
										 final Integer limit,
										 final Integer offset,
										 final String sortOrder) {
		final String query = 
				"select p from Post p "+
				"where time > to_timestamp(:ts, 'YYYY-MM-dd HH24:MI:ss') "+
	            "order by time " + (sortOrder!="desc" ? "asc" : "desc");

		return currentSession()
		  .createQuery(query, Post.class)
		  .setParameter("ts", forPattern("YYYY-MM-dd HH:mm:ss").print(time))
		  .setMaxResults(Math.min(limit, 100))
		  .setFirstResult(offset)
		  .getResultList();
	}

	public Post findById(long id) {
    	Post p = get(id);
    	if (p == null) {
    		throw new NotFoundException();    		
    	}
    	return p;
    }

	public Post findByFacebookId(final String facebookId) {
		return (Post) currentSession()
					  .getNamedQuery("post.findByFacebook")
				      .setParameter("facebookId", facebookId)
				      .getSingleResult();
	}

	/**
	 * XXX: time format must be adjustable
	 * datetime param is YYYY-MM-dd HH24:MI:ss
	 * @param query
	 * @param start
	 * @param end
	 * @return
	 */
	public List<Post> searchPosts(String query, LocalDateTime start, LocalDateTime end) {
		final String sql = 
				"from Post where description like concat('%',:query,'%') " +
				"and time >=  to_timestamp(:start, 'YYYY-MM-dd') "+
				"and time <=  to_timestamp(:end, 'YYYY-MM-dd')  "+
				"order by time DESC ";

		return currentSession()
				   .createQuery(sql, Post.class)
				   .setParameter("query", query)
				   .setParameter("start", forPattern("YYYY-MM-dd").print(start))
				   .setParameter("end", forPattern("YYYY-MM-dd").print(end))
				   .setMaxResults(30)
				   .getResultList();
	}

	/**
	 * @param likes
	 * @param facebookId
	 * @return
	 */
	private Post updatePostActivity(Post newPost) {
		final Post update = findByFacebookId(newPost.getFacebookId());
		final long newLikes = newPost.getPostReaction().getCountLikes();
		final long oldLikes = update.getPostReaction().getCountLikes();

		final boolean likesChanged = oldLikes != newLikes; 
		if (likesChanged) {
			update.getPostReaction().setCountLikes(newLikes);			
		}

		final boolean pictureChanged =!newPost.getPicture().equals(update.getPicture()); 
		if (pictureChanged) {
			update.setPicture(newPost.getPicture());
		}
		
		if (likesChanged || pictureChanged)
			currentSession().save(update);

		return update;
	}

    private Post create(Post post, Profile profile) {
    	post.setProfileId(profile.getId());
    	post.setListingId(profile.getListingId());
		post.setIndexedAt(new LocalDateTime());
    	return persist(post);
    }
}
