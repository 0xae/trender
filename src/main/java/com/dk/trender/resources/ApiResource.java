package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
import com.dk.trender.core.PostMedia;
import com.dk.trender.core.PostRequest;
import com.dk.trender.service.IndexService;
import com.dk.trender.service.PostService;
import com.google.common.base.Optional;

import io.dropwizard.hibernate.UnitOfWork;
import net.bytebuddy.implementation.bind.annotation.DefaultCall;

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
	private final IndexService indexService;

	public ApiResource(PostService postService, IndexService indexService) {
		this.postService = postService;
		this.indexService = indexService;
	}
	
	@GET
	@UnitOfWork
	@Path("/recent_posts")
	public List<Post> getRecentPosts(@QueryParam("since") @NotEmpty String minTime,
									 @QueryParam("limit") @NotNull Integer limit,
									 @QueryParam("offset") Optional<Integer> offset,
									 @QueryParam("o") Optional<String> sortOrder) {
		final LocalDateTime time = new LocalDateTime(minTime.replace(' ', 'T'));
		return postService.findPostsNewerThan(time, limit, offset.or(0), sortOrder.or("asc"));
	}

	@GET
	@UnitOfWork
	@Path("/search_posts")
	public List<Post> searchPost(@QueryParam("q") @NotEmpty String query,
								 @QueryParam("start") @NotEmpty String startO,
								 @QueryParam("end") @NotEmpty String endO) {
		final LocalDateTime start = new LocalDateTime(startO);
		final LocalDateTime end = new LocalDateTime(endO);
		return postService.searchPosts(query, start, end);
	}

	@POST
	@UnitOfWork
	@Path("/add_post")
	public Post addPost(@Valid PostRequest request) {
		return postService.addPost(request);
	}
	
	@POST
	@UnitOfWork
	@Path("/add_post_media")
	public PostMedia addPostMedia(@Valid PostMedia request) {
		return postService.addPostMedia(request);
	}
	
	@POST
	@UnitOfWork
	@Path("/media/index")
	public void indexThesePosts(@Valid List<String> urls) {
		indexService.addToIndex(urls);
	}

	@GET
	@UnitOfWork
	@Path("/media/to_index")
	public List<String> getPostsInIndex() {
		return indexService.retrieveIndex();
	}

	@GET
	@UnitOfWork
	@Path("/media/index_sz")
	public int getIndexSize() {
		return indexService.indexSize();
	}
	
	@GET
	@UnitOfWork
	@Path("/post/{postId}/media")
	public List<PostMedia> getPostMedia(@QueryParam("postId") @NotNull Long postId,
										@QueryParam("since") @NotEmpty String sinceP,
										@QueryParam("type") @NotEmpty String mediaType) {
		final LocalDateTime since = new LocalDateTime(sinceP);
		return postService.getPostMediaObjects(postId, since, mediaType);
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
}
