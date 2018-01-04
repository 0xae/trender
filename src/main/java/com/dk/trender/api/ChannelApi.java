package com.dk.trender.api;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZSearch;
import com.dk.trender.service.ZChannelService;
import com.dk.trender.service.ZCollectionService;

import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.validation.OneOf;

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
	private final ZCollectionService $colls;

	public ChannelApi(ZChannelService channel,
					  ZCollectionService col) {
		this.$channel = channel;
		this.$colls = col;
	}

	@GET
	@UnitOfWork
	public List<ZChannel> all() {
		return $channel.all();
	}

	@GET
	@Path("/{id}/feed/{collName}")
	@UnitOfWork
	public ZCollection feed(@PathParam("id") long id,
							@PathParam("collName") String collName,
							@Context UriInfo uri) {
		ZCollection col = $colls.byName("t-newsfeed")
								.filter(new ZSearch(uri.getQueryParameters()));
		ZChannel chan = $channel.byId(id);
		return $colls.feed(chan, col);
	}

	@GET
	@Path("/sugestions/{name}")
	@UnitOfWork
	public List<ZChannel> sugestions(@PathParam("name") 
								     @OneOf({"recent", "top"}) 
								  	 String name) {
		List<ZChannel> data;
		if ("recent".equals(name)) {
			data = $channel.recent();
		} else {
			data = $channel.top();
		}

		ZCollection col = $colls.byName("t-newsfeed").copy();
		col.queryConf(col.queryConf().setLimit(4));

		for (ZChannel chan : data) {
			chan.getCollections()
			.add($colls.feed(chan, col));
		}

		return data;
	}

	@GET
	@Path("/{id}")
	@UnitOfWork
	public ZChannel get(@PathParam("id") long id) {
		ZChannel c = $channel.byId(id);
		List<ZCollection> cols = new ArrayList<>();
		cols.add($colls.byName("t-newsfeed"));
		cols.addAll($channel.collections(id, 0));
		c.setCollections(cols);
		return c;
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
	public List<ZCollection> getColls(@PathParam("id") long id,
								   @QueryParam("start") 
								   @DefaultValue("0") int start) {
		return $channel.collections(id, start);
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
		// $channel.delete(id);
	}
}
