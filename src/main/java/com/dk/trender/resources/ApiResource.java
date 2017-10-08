package com.dk.trender.resources;

import java.io.File;
import java.util.List;
import java.util.Optional;

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

import org.hibernate.validator.constraints.NotEmpty;

import com.dk.trender.core.Post;
import com.dk.trender.core.Timeline;
import com.dk.trender.service.MediaService;
import com.dk.trender.service.PostService;
import com.dk.trender.service.TimelineService;
import static com.dk.trender.service.TimelineService.DEFAULT_START_L;
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
	private final MediaService media;

	public ApiResource(PostService postService,
					   TimelineService timeline,
					   MediaService media) {
		this.post = postService;
		this.timeline = timeline;
		this.media = media;
	}

	@POST
	@Path("/post/new")
	public void createPost(@NotEmpty @Valid List<Post> request,
						   @QueryParam("debug") String debug) {
		post.create(request);
	}

	@Path("/post/media/{id}/download")
	@GET
	public String updatePostMedia(@PathParam("id") String id,
								  @QueryParam("link") Optional<String> link) {
		Post p = post.byId(id);
		p.setPicture(link.orElse(p.getPicture()));
		// if it is already cached
		if (!"".equals(p.getCached()) && !"none".equals(p.getCached()) &&
					!"/opt/lampp/htdocs".startsWith(p.getCached()) &&
					!link.isPresent()) 
		{
			return p.getCached()
					.replace("/opt/lampp/htdocs/trender/", "");
		}

		String stored = media.store(p, id);
		p.setCached(stored);
		post.update(p);
		return stored;
	}

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
	public List<Timeline> getTimeline(@QueryParam("state") 
									  @DefaultValue("created") 
									  String state) {
		return timeline.all(state);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}/stream")
	public Timeline.Stream streamTimeline(@PathParam("id") long id,
										 @QueryParam("limit") @DefaultValue("10") int limit,
										 @QueryParam("start") @DefaultValue(DEFAULT_START_L) int start) {
		return timeline.stream(id, limit, start);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{name}/stream_name")
	public Timeline.Stream streamTimeline(@PathParam("name") String name,
										 @QueryParam("limit") @DefaultValue("10") int limit) {
		return timeline.stream(name, limit);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}")
	public Timeline getTimeline(@PathParam("id") long id,
								@QueryParam("start") @Min(0) int start) {
		return timeline.byId(id);
	}
}

