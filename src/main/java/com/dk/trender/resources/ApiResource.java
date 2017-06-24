package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.dk.trender.api.ListingTrend;
import com.dk.trender.core.Post;

import com.dk.trender.core.PostRequest;
import com.dk.trender.service.ListingService;
import com.dk.trender.service.PostService;
import com.google.common.base.Optional;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-04-02 18:36:53
 */
@Path("/api")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ApiResource {
	private final PostService postService;
	public ApiResource(PostService postService) {
		this.postService = postService;
	}

	@POST
	@UnitOfWork
	@Path("/add_post")
	public Post addPost(@Valid PostRequest request) {
		return postService.addPost(request);
	}
	
	@GET
	@UnitOfWork
	@Path("/trending_topics")
	public List<ListingTrend> getTrendingTopics() {
		return null;
	}

	@GET
	@Path("/trending_lists")
	public List<ListingTrend> getTrendingListings() {
		return null;
	}

	@GET
	@UnitOfWork
	@Path("/recent_posts")
	public List<Post> getRecentPosts(@QueryParam("time") Optional<String> minTime) {
		if (minTime.isPresent()) {
			final LocalDateTime time = new LocalDateTime(minTime.get().replace(' ', 'T'));
			return postService.findPostsNewerThan(time);
		}

		final LocalDateTime end = new LocalDateTime();
		return postService.findPostsNewerThan(end.minusMinutes(15));
	}

	@GET
	@UnitOfWork
	@Path("/search")
	public List<Post> searchPost(@QueryParam("q") @NotEmpty String query,
								 @QueryParam("start") @NotEmpty String startO,
								 @QueryParam("end") @NotEmpty String endO) {
		final LocalDateTime start = new LocalDateTime(startO);
		final LocalDateTime end = new LocalDateTime(endO);
		return postService.searchPosts(query, start, end);
	}
}
