package com.dk.trender.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
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

import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;
import com.dk.trender.service.ZMediaService;
import com.dk.trender.service.ZPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.validation.OneOf;

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
	private final ZMediaService $media;
	private final ZChannelService $channel;
	private final ZCollectionService $col;

	public ApiResource(ZPostService post,
					   ZMediaService media,
					   ZChannelService channel,
					   ZCollectionService $col) {
		this.$post = post;
		this.$media = media;
		this.$channel = channel;
		this.$col = $col;
	}

	@POST
	@Path("/collection/new")	
	@UnitOfWork
	public ZCollection createCol(@Valid ZCollection obj) {		
		return $col.create(obj);
	}

	@GET
	@Path("/collection")	
	@UnitOfWork
	public List<ZCollection> getAllCols(@QueryParam("audience")
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

	/**
	 * 
	 * TODO: fix duplicate creation of channels
	 * @return
	 */
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
	public List<ZCollection> getChannelCols(@PathParam("id") long id) {
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
			if (ZPost.YOUTUBE.equals(post.getType()) && 
					post.getPicture().startsWith("/yts/img/"))
			{
				ObjectMapper mapper = new ObjectMapper();
				try {
					Map<String, String> map = mapper.readValue(post.getData(), HashMap.class);
					String videoId = map.get("video_id").toString();
					String picture = "https://img.youtube.com/vi/"+videoId+"/0.jpg";
					post.setPicture(picture);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

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
	
	@GET
	@Path("/channel/test_name/{name}")
	public String test(@PathParam("name") 
					@Pattern(regexp="[a-zA-Z0-9_@.-]+")
					@NotEmpty String name) 
	{
		log.info("{} is a valid chanel name.", name);
		return name;
	}

	@POST
	@Path("/post/{id}/{op}/collection/{name}")
	public void updatePostCollection(
		 @PathParam("id") @NotEmpty 
		 String id,
		 @PathParam("name") @Pattern(regexp=ZCollection.NAMEP) 
		 String collectionName,
		 @PathParam("op") @OneOf({"add", "remove"}) 
		 String op
	) {
		$post.updateCollection(op, id, collectionName);
	}
}

