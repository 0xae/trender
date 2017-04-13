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

import com.dk.trender.core.Profile;
import com.dk.trender.service.ProfileService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-04-01 20:44:19
 */
@Path("/profile")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ProfileResource {
	private final ProfileService service;

	public ProfileResource(final ProfileService service) {
		this.service = service;
	}

	@GET
	@UnitOfWork
	public List<Profile> getAll() {
		return service.findAll();
	}

	@GET
	@UnitOfWork
	@Path("/nopicture")
	public List<Profile> getProfileWithNoPicture() {
		return service.fetchProfileWithoutPictures();
	}

	@GET
	@UnitOfWork
	@Path("/{id}")
	public Profile getById(@PathParam("id") final long id) {
		return service.findById(id);
	}

	@POST
	@UnitOfWork
	public Profile create(@Valid final Profile request) {
		return service.create(request);
	}

	@PUT
	@UnitOfWork
	@Path("/{id}/picture")
	public Profile updatePicture(@PathParam("id") final long id,
			                     @NotEmpty final String picture) {
		final Profile p = service.findById(id);
		p.setPicture(picture);
		return service.update(p);
	}

	@PUT
	@UnitOfWork
	@Path("/{id}/title")
	public Profile updateTitle(@PathParam("id") final long id,
			                   @NotEmpty final String title) {
		final Profile p = service.findById(id);
		p.setTitle(title);
		return service.update(p);
	}
}
