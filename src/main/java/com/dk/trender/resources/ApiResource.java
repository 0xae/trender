package com.dk.trender.resources;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dk.trender.core.Post;
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

	public ApiResource(PostService postService) {
		this.postService = postService;
	}

	@POST
	@UnitOfWork
	@Path("/post/new")
	public Post create(@Valid Post request) {
		return request;
	}
}
