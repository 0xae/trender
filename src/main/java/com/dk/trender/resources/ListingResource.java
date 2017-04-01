package com.dk.trender.resources;

import java.util.List;

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

import org.joda.time.DateTime;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.service.ListingService;
import com.dk.trender.service.PostService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 15:50:16
 */
@Path("/listings")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListingResource {
	private final ListingService service;
	public ListingResource(final ListingService service) {
		this.service = service;
	}

	@POST
	@UnitOfWork
	public Listing create(@Valid Listing obj) {
		final Listing l = service.create(obj);
		return l;
	}

	@GET
	@UnitOfWork
	public List<Listing> getAll() {
		return service.findAll();
	}

	@GET
	@UnitOfWork
	@Path("/{id}/now")
	public List<Post> todayPosts(@PathParam("id") long listingId) {
		return service.todayPosts(listingId);
	}
	
	@GET
	@UnitOfWork
	@Path("/{id}/details")
	public Listing viewListingDetails(@PathParam("id") long listingId) {
		return service.getById(listingId);
	}

	@POST
	@UnitOfWork
	@Path("/{id}/{name}/add_post")
	public Post create(
			@PathParam("id") final long listingId,
			@PathParam("name") @NotNull final String listingName,
			@Valid @NotNull final Post post) {
		final Post postCreated = service.createPost(listingId, post);
		return postCreated;
	}

	@GET
	@UnitOfWork
	@Path("/{id}/{name}/posts")
	public List<Post> listingPosts(@PathParam("id") long listingId,
								   @PathParam("name") String listingName) {
		return service.fetchPosts(listingId);
	}
}
