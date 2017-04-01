package com.dk.trender.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.dk.trender.core.Post;
import com.dk.trender.service.PostService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-04-01 06:45:23
 */
@Path("/post")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PostResource {
	private final PostService service;
	public PostResource(final PostService service) {
		this.service = service;
	}

	@GET
	@UnitOfWork
	@Path("/view")
	public Post viewPost(@QueryParam("fbid") String facebookId) {
		return service.getByFacebookId(facebookId);
	}
}
