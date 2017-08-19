package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;

import com.dk.trender.core.Channel;
import com.dk.trender.core.Post;
import com.dk.trender.service.ChannelService;
import com.dk.trender.service.PostService;

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
	private final ChannelService channelService;

	public ApiResource(PostService postService,
					   ChannelService channelService) {
		this.postService = postService;
		this.channelService = channelService;
	}

	@POST
	@Path("/post/new")
	public void createPost(@NotEmpty @Valid List<Post> request,
						   @QueryParam("debug") String debug) {
		postService.create(request);
	}

	@POST
	@UnitOfWork
	@Path("/channel/new")
	public Channel createChannel(@Valid Channel request) {
		return channelService.create(request);
	}

	@GET
	@UnitOfWork
	@Path("/channel/")
	public List<Channel> getAllChannels() {
		return channelService.findAll();
	}

	@GET
	@UnitOfWork
	@Path("/channel/{id}")
	public Channel getChannel(@PathParam("id") long id) {
		return channelService.findById(id);
	}
}

