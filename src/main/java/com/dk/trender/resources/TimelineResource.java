package com.dk.trender.resources;

import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.dk.trender.core.Listing;
import com.dk.trender.core.Post;
import com.dk.trender.core.PostRequest;
import com.dk.trender.service.ListingService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-04-02 18:36:53
 */
@Path("/timeline")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TimelineResource {
	private final ListingService service;
	public TimelineResource(final ListingService service) {
		this.service = service;
	}

	/**
	 * TODO
	 * @return
	 */
	@GET
	@UnitOfWork
	@Path("/trending_lists")
	public List<Listing> getTrendingListings() {
		return null;
	}

	/**
	 * TODO
	 * @return
	 */
	@GET
	@UnitOfWork
	@Path("/newsfeed")
	public List<Post> getNewsfeed() {
		return null;
	}

	/**
	 * @return
	 */
	@POST
	@UnitOfWork
	@Path("/listing/{id}/add_post")
	public Post addPost(@PathParam("id") long listingId,
						@Valid PostRequest postRequest) {
		final Listing listing = service.findById(listingId);
		return service.addPost(listing, postRequest);
	}
}
