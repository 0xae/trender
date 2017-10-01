package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.apache.solr.common.SolrDocumentList;
import org.hibernate.validator.constraints.NotEmpty;

import com.dk.trender.core.Timeline;
import com.dk.trender.core.Post;
import com.dk.trender.service.PostService;
import com.dk.trender.service.TimelineService;

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
	private final PostService post;
	private final TimelineService timeline;

	public ApiResource(PostService postService,
					   TimelineService timeline) {
		this.post = postService;
		this.timeline = timeline;
	}

	@POST
	@Path("/post/new")
	public void createPost(@NotEmpty @Valid List<Post> request,
						   @QueryParam("debug") String debug) {
		post.create(request);
	}

//	@GET
//	@Path("/post/search")
//	public List<Post> searchPost(@QueryParam("q") @NotEmpty String query,
//						         @QueryParam("limit") @DefaultValue("50") int limit) {
//		return post.filter(query, limit);
//	}

	@GET
	@Path("/post/{id}")
	public Post getPost(@PathParam("id") @NotEmpty String id) {
		return post.byId(id);
	}	

	@POST
	@UnitOfWork
	@Path("/timeline/new")
	public Timeline createTimeline(@Valid Timeline request) {
		return timeline.create(request);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/")
	public List<Timeline> getTimeline() {
		return timeline.all();
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}/stream")
	public Timeline.Stream streamTimeline(@PathParam("id") long id,
										 @QueryParam("limit") @DefaultValue("10") int limit) {
		return timeline.stream(id, limit);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}/more")
	public Timeline.Stream loadTimeline(@PathParam("id") long id,
			@QueryParam("start") @Min(0) int start,
			@QueryParam("limit") @DefaultValue("10") int limit) {
		return timeline.fetch(id, start, limit);
	}
	
	@GET
	@UnitOfWork
	@Path("/timeline/{id}")
	public Timeline getTimeline(@PathParam("id") long id,
								@QueryParam("start") @Min(0) int start) {
		return timeline.byId(id);
	}
}

