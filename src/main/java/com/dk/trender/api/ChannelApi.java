package com.dk.trender.api;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
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

import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;
import com.dk.trender.service.ZChannelService;

import io.dropwizard.hibernate.UnitOfWork;

/**
 * 
 * @author ayrton
 */
@Path("/api/v1/channel")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ChannelApi {
	private static final Logger log = LoggerFactory.getLogger(ChannelApi.class);
	private final ZChannelService $channel;

	public ChannelApi(ZChannelService channel) {
		this.$channel = channel;
	}

	@GET
	@Path("/")
	@UnitOfWork
	public List<ZChannel> all() {
		return $channel.all();
	}

	@GET
	@Path("/{id}")	
	@UnitOfWork
	public ZChannel get(@PathParam("id") long id) {
		return $channel.byId(id);
	}

	@GET
	@Path("/find")	
	@UnitOfWork
	public List<ZChannel> find(@QueryParam("audience") 
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
	@Path("/find_by")
	@UnitOfWork
	public ZChannel findBy(@QueryParam("name") @NotEmpty String name,
						   @QueryParam("q") @NotEmpty String q) {
		return $channel.findByName(name, q);
	}

	@GET
	@Path("/{id}/collections")	
	@UnitOfWork
	public List<ZCollection> getCols(@PathParam("id") long id) {
		return $channel.collections(id);
	}

	@POST
	@Path("/new")
	@UnitOfWork
	public ZChannel create(@Valid ZChannel req) {		
		return $channel.create(req);
	}

	@POST
	@Path("/{id}")	
	@UnitOfWork
	public ZChannel update(@PathParam("id") long id,
						   @Valid ZChannel chan) {
		return $channel.update(chan);
	}

	@POST
	@Path("/{id}/delete")	
	@UnitOfWork
	public void delete(@PathParam("id") long id) {
		$channel.delete(id);
	}

	@GET
	@Path("/{id}/feed")	
	@UnitOfWork
	public  Map<String, ZCollection> feed(@PathParam("id") long id) {
		ZChannel chan = $channel.byId(id);
		return $channel.feed(chan); 
	}
}
