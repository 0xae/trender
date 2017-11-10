package com.dk.trender.resources;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZPost;
import com.dk.trender.core.ZTimeline;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.service.ZMediaService;
import com.dk.trender.service.ZPostService;
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;
import com.dk.trender.service.ZTimelineService;
import com.fasterxml.jackson.core.JsonProcessingException;

import static com.dk.trender.service.ZTimelineService.DEFAULT_STARTL;
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
	private static final Logger log = LoggerFactory.getLogger(ApiResource.class);
	private final ZPostService $post;
	private final ZTimelineService timeline;
	private final ZMediaService $media;
	private final ZChannelService $channel;
	private final ZCollectionService $col;

	public ApiResource(ZPostService postService,
					   ZTimelineService timeline,
					   ZMediaService media,
					   ZChannelService channel,
					   ZCollectionService $col) {
		this.$post = postService;
		this.timeline = timeline;
		this.$media = media;
		this.$channel = channel;
		this.$col = $col;
	}

	@POST
	@Path("/collection/new")	
	@UnitOfWork
	public ZCollection createCol(@Valid ZCollection req) {		
		return $col.create(req);
	}

	@GET
	@Path("/collection")	
	@UnitOfWork
	public List<ZCollection> allCols(@QueryParam("audience")
									 @NotEmpty String audience) {
		return $col.all(audience);
	}

	@GET
	@Path("/collection/{id}")	
	@UnitOfWork
	public ZCollection colById(@PathParam("id") long id) {
		return $col.byId(id);
	}

	@PUT
	@Path("/collection/{id}")	
	@UnitOfWork
	public ZCollection colUpdate(@PathParam("id") long id,
								 @Valid ZCollection col) {
		if (col.getId()==0) 
			col.setId(id);
		return $col.update(col);
	}

	@POST
	@Path("/channel/new")	
	@UnitOfWork
	public ZChannel createChannel(@Valid ZChannel req) {		
		return $channel.create(req);
	}

	@GET
	@Path("/channel")	
	@UnitOfWork
	public List<ZChannel> allChannels() {
		return $channel.all();
	}

	@GET
	@Path("/channel/find")	
	@UnitOfWork
	public List<ZChannel> findChannel(@QueryParam("audience") 
									 @DefaultValue(ZChannel.PUBLIC)
									 String audience) {
		return $channel.find(audience);
	}

	@GET
	@Path("/channel/find_by")
	@UnitOfWork
	public ZChannel findBy(@QueryParam("name") 
						   @NotEmpty String name,
						   @QueryParam("q") 
	   					   @NotEmpty String q) {
		return $channel.findByName(name, q);
	}	
	
	@GET
	@Path("/channel/{id}")	
	@UnitOfWork
	public ZChannel getChannel(@PathParam("id") long id) {
		return $channel.byId(id);
	}
	
	@GET
	@Path("/channel/{id}/collections")	
	@UnitOfWork
	public List<ZChannel> getChannelCols(@PathParam("id") long id) {
		return $channel.collections(id);
	}

	@POST
	@Path("/channel/{id}")	
	@UnitOfWork
	public ZChannel saveChannel(@PathParam("id") long id,
								@Valid ZChannel chan) {
		return $channel.update(chan);
	}

	@POST
	@Path("/channel/{id}/delete")	
	@UnitOfWork
	public void deleteChannel(@PathParam("id") long id) {
		$channel.deleteById(id);
	}
	
	@POST
	@Path("/post/new")
	public int createPost(@Valid List<ZPost> request,
						   @QueryParam("debug") String debug) {
		if (request.isEmpty()) {
			log.info("Empty request for {}", debug);
			return 0;
		} else {
			log.info("Indexing {} items for {}", request.size(), debug);
		}

 		return $post.create(request);
	}

	@Path("/post/media/{id}/download")
	@GET
	public String updatePostMedia(@PathParam("id") String id,
								  @QueryParam("link") Optional<String> link) {
		ZPost post = $post.byId(id);
		post.setPicture(link.orElse(post.getPicture()));

		// if it is already cached
		// XXX: ellaborate on this problem
		// if (!"".equals(p.getCached()) && !"none".equals(p.getCached()) &&
		//			!"/opt/lampp/htdocs".startsWith(p.getCached()) &&
		//			!link.isPresent()) {
		//	return p.getCached()
		//			.replace("/opt/lampp/htdocs/trender/", "");
		// }

		try {
			String stored = $media.store(post.getPicture(), post.getType(), id);
			post.setCached(stored);
			$post.update(post);
			return stored;
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	@GET
	@Path("/post/{id}")
	public ZPost getPost(@PathParam("id") @NotEmpty String id) {
		return $post.byId(id);
	}

	@POST
	@UnitOfWork
	@Path("/timeline/new")
	public ZTimeline createTimeline(@Valid ZTimeline request) {
		return timeline.create(request);
	}

	@DELETE
	@UnitOfWork
	@Path("/timeline/{id}")
	public void deleteTimeline(@PathParam("id") long id) {
		timeline.delete(id);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/")
	public List<ZTimeline> getTimeline(@QueryParam("state") 
									  @DefaultValue("created") 
									  String state) {
		return timeline.all(state);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}/stream")
	public ZTimeline.Stream streamTimeline(@PathParam("id") long id,
										 @QueryParam("limit") @DefaultValue("10") int limit,
										 @QueryParam("start") @DefaultValue(DEFAULT_STARTL) int start) {
		return timeline.stream(id, limit, start);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/topic/{q}")
	public ZTimeline.Stream streamTimeline(@PathParam("q") String topic,
										  @QueryParam("limit") @DefaultValue("10") int limit) {
		log.info("q is {}", topic);
		return timeline.stream(topic, limit);
	}

	@GET
	@UnitOfWork
	@Path("/timeline/{id}")
	public ZTimeline getTimeline(@PathParam("id") long id,
								@QueryParam("start") @Min(0) int start) {
		return timeline.byId(id);
	}
}

