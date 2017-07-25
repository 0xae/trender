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

import com.dk.trender.core.IndexItem;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostMedia;
import com.dk.trender.core.PostRequest;
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
	public List<Post> searchPost(@QueryParam("q") @NotEmpty String query,
								 @QueryParam("start") @NotEmpty String startO,
								 @QueryParam("end") @NotEmpty String endO,
								 @QueryParam("listing") Optional<String> listing) {
		final LocalDateTime start = new LocalDateTime(startO);
		final LocalDateTime end = new LocalDateTime(endO);
		return postService.searchPosts(query, start, end, listing.or("general"));
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
	@Path("/media/post")
	public PostMedia addPostMedia(@Valid PostMedia request,
								  @QueryParam("fid") @NotNull String fId) {
		return postService.addPostMedia(request, fId);
	}

	@GET
	@UnitOfWork
	@Path("/media/index/stats")
	public Map<String, String> getIndexSize() {
		return ImmutableMap.<String,String>builder()
				.put("queue_size", indexService.indexSize()+"")
				.build();
	}

	@GET
	@UnitOfWork
	@Path("/media/recent")
	public List<PostMedia> getMostRecent(@QueryParam("fid") @NotNull String fId,
										 @QueryParam("since") Optional<String> since,
										 @QueryParam("type") Optional<String> type,
										 @QueryParam("o") Optional<Integer> offset) {
		LocalDateTime sinceDate = new LocalDateTime().minusHours(24);

		if (since.isPresent() && !since.get().equals("")) {
			sinceDate = new LocalDateTime(since.get().replace(' ', 'T'));
		}

		return postService.getRecentPostMedia(sinceDate, type.or("*"), fId, offset.or(0));
	}

	@GET
	@UnitOfWork
	@Path("/media/index")
	public List<IndexItem> getPostsInIndex() {
		return indexService.retrieveIndex();
	}

	@GET
	@UnitOfWork
	@Path("/listing/rank")
	public List<ListRank> getListRank() {
		return listingService.getListingRank(40, 0);
	}
	
	@POST
	@UnitOfWork
	@Path("/media/index")
	public void indexMedia(@NotEmpty List<IndexItem> urls) {
		indexService.addToIndex(urls);
	}
}
