package com.dk.trender.resources;

import java.io.IOException;
import java.net.InetSocketAddress;

import javax.ws.rs.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import net.spy.memcached.MemcachedClient;

@Path("/api/v1/test")
public class TestApi {
	@GET
	@Path("/put/{key}")
	public void store(@PathParam("key") String key) 
						throws IOException {
		MemcachedClient client = null;
		client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		client.set(key, 0, 123);
	}
	
	@GET
	@Path("/get/{key}")
	public String get(@PathParam("key") String key) throws Exception {
		MemcachedClient client = null;
		client = new MemcachedClient(new InetSocketAddress("127.0.0.1", 11211));
		return client.get(key).toString();		
	}
}
