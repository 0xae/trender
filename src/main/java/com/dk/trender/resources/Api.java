package com.dk.trender.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
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
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 * @date 2017-04-02 18:36:53
 */
@Path("/api/v1/")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Api {
	private static final Logger log = LoggerFactory.getLogger(Api.class);
	private final ZChannelService $channel;
	private final ZCollectionService $col;

	public Api(ZChannelService channel,
			   ZCollectionService $col) {
		this.$channel = channel;
		this.$col = $col;
	}

	@POST
	@Path("/collection/new")	
	@UnitOfWork
	@PermitAll
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
	@PermitAll
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
	@PermitAll
	public void deleteChannel(@PathParam("id") long id) {
		$channel.deleteById(id);
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
}

