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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZCollection;
import com.dk.trender.service.ZCollectionService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 */
@Path("/api/v1/collection")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ZCollectionApi {
	private static final Logger log = LoggerFactory.getLogger(ZCollectionApi.class);
	private final ZCollectionService $col;

	public ZCollectionApi(ZCollectionService $col) {
		this.$col = $col;
	}

	@GET
	@Path("/")
	@UnitOfWork
	public List<ZCollection> getAllCols(@QueryParam("audience")
									    @NotEmpty String audience) {
		return $col.all(audience);
	}

	@GET
	@Path("/{id}")	
	@UnitOfWork
	public ZCollection colById(@PathParam("id") long id) {
		return $col.byId(id);
	}

	@GET
	@Path("/{id}/feed")	
	@UnitOfWork
	public ZCollection feed(@PathParam("id") long id) {
		ZCollection col = $col.byId(id);
		return col;
	}

	@POST
	@Path("/new")	
	@UnitOfWork
	public ZCollection createCol(@Valid ZCollection obj) {		
		return $col.create(obj);
	}

	@PUT
	@Path("/{id}")	
	@UnitOfWork
	public ZCollection colUpdate(@PathParam("id") long id,
								 @Valid ZCollection col) {
		return $col.update(col);
	}
}