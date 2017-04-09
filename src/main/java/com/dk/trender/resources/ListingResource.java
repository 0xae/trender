package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostRequest;
import com.dk.trender.service.ListingService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-03-31 15:50:16
 */
@Path("/listing")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ListingResource {
	private final ListingService service;
	public ListingResource(ListingService service) {
		this.service = service;
	}

	@GET
	@UnitOfWork
	public List<Listing> getAll() {
		return service.findAll();
	}

	@GET
	@UnitOfWork
	@Path("/{id}")
	public Listing getById(@PathParam("id") long id) {
		return service.findById(id);
	}
	
	@GET
	@UnitOfWork
	@Path("/{id}/posts")
	public List<Post> getPosts(@PathParam("id") long listingId) {
		return service.fetchPosts(listingId);
	}

	@POST
	@UnitOfWork
	public Listing create(@Valid Listing obj) {
		return service.create(obj);
	}

	@PUT
	@UnitOfWork
	@Path("/{id}/title")
	public Listing updateTitle(@PathParam("id") long id,
			                   @NotEmpty String title) {
		Listing l = service.findById(id);
		return service.updateTitle(l);
	}
}
