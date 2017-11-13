package com.dk.trender.resources;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;
import com.dk.trender.service.ZMediaService;
import com.dk.trender.service.ZPostService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.validation.OneOf;

@Path("/api/v1/post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ZPostApi {
	private static final Logger log = LoggerFactory.getLogger(ZPostApi.class);
	private final ZPostService $post;
	private final ZMediaService $media;

	public ZPostApi(ZPostService $post,
				   ZMediaService $media) {
		this.$post = $post;
		this.$media = $media;
	}

	@POST
	@Path("/new")
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

	@Path("/media/{id}/download")
	@GET
	public String updatePostMedia(@PathParam("id") String id,
								  @QueryParam("link") Optional<String> link) {
		ZPost post = $post.byId(id);
		post.setPicture(link.orElse(post.getPicture()));

		if (ZPost.YOUTUBE.equals(post.getType()) && 
			post.getPicture().startsWith("/yts/img/")) {
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
	}

	@GET
	@Path("/{id}")
	public ZPost getPost(@PathParam("id") @NotEmpty String id) {
		return $post.byId(id);
	}

	@POST
	@PermitAll
	@Path("/{id}/{op}/collection/{name}")
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
