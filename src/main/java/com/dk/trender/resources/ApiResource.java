package com.dk.trender.resources;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.dk.trender.api.PostRequest;
import com.dk.trender.core.IndexItem;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostMedia;
import com.dk.trender.service.IndexService;
import com.dk.trender.service.ListingService;
import com.dk.trender.service.ListingService.ListRank;
import com.dk.trender.service.PostService;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;

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
	private final ListingService listingService;
	private final IndexService indexService;

	public ApiResource(PostService postService, 
					   IndexService indexService,
					   ListingService listingService) {
		this.postService = postService;
		this.indexService = indexService;
		this.listingService = listingService;
	}

	@GET
	@UnitOfWork
	@Path("/post/recent")
	public List<Post> getRecentPosts(@QueryParam("since") Optional<String> minTime,
									 @QueryParam("limit") @NotNull Integer limit,
									 @QueryParam("offset") Optional<Integer> offset,
									 @QueryParam("o") Optional<String> sortOrder) {
		final LocalDateTime time;
		if (minTime.isPresent()) {
			time = new LocalDateTime(minTime.get().replace(' ', 'T'));
		} else {
			time = new LocalDateTime().minusMinutes(5);
		}
		return postService.findPostsNewerThan(time, limit, offset.or(0), sortOrder.or("asc"));
	}

	@GET
	@UnitOfWork
	@Path("/post/search")
	public List<Post> searchPost(@QueryParam("q") @NotEmpty String query) {
		LocalDateTime end = new LocalDateTime();
		LocalDateTime start = end.minusMinutes(30);
		int limit = 30;
		int offset = 0;
		return postService.searchPosts(query, limit, offset, start, end);
	}

	@POST
	@UnitOfWork
	@Path("/post/new")
	public Post addPost(@Valid PostRequest request) {
		return postService.addPost(request);
	}

	@GET
	@UnitOfWork
	@Path("/media/post/{postId}")
	public List<PostMedia> getPostMedia(@PathParam("postId") @NotNull Long postId,
										@QueryParam("type") Optional<String> mediaType) {
		return postService.getPostMediaObjects(postId, mediaType.or("*"));
	}

	@POST
	@UnitOfWork
	@Path("/media/post/{ref}")
	public PostMedia addPostMedia(@PathParam("ref") @NotEmpty String ref,
								  @Valid PostMedia request) {
		return postService.addPostMedia(request, ref);
	}

	@GET
	@UnitOfWork
	@Path("/media/recent")
	public List<PostMedia> getMostRecent(@QueryParam("ref") @NotNull String ref,
										 @QueryParam("since") Optional<String> since,
										 @QueryParam("type") Optional<String> type,
										 @QueryParam("o") Optional<Integer> offset) {
		LocalDateTime sinceDate = new LocalDateTime().minusHours(24);
		if (since.isPresent() && !since.get().equals("")) {
			sinceDate = new LocalDateTime(since.get().replace(' ', 'T'));
		}
		return postService.getRecentPostMedia(sinceDate, type.or("*"), ref, offset.or(0));
	}

	@GET
	@UnitOfWork
	@Path("/media/index/stats")
	public Map<String, Integer> getStats() {
		return indexService.stats();
	}

	@GET
	@UnitOfWork
	@Path("/media/index/{name}")
	public List<IndexItem> getPostsInIndex(@PathParam("name") @NotEmpty String indexName) {
		return indexService.retrieveIndex(indexName);
	}

	@POST
	@UnitOfWork
	@Path("/media/index/{name}")
	public void indexMedia(@PathParam("name") @NotEmpty String indexName,
						   @NotEmpty @Valid List<IndexItem> urls) {
		indexService.addToIndex(indexName, urls);
	}
}
