package com.dk.trender.resources;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.dk.trender.auth.ZLogin;
import com.dk.trender.auth.ZToken;
import com.dk.trender.core.ZUser;
import com.dk.trender.service.ZUserService;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/api/v1/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AuthApi {
	private final ZUserService $user;
	public AuthApi(ZUserService $user) {
		this.$user = $user;
	}

	@POST
	@Path("/signup")
	@UnitOfWork
	public ZUser signup(@Valid ZUser req) {
		return $user.create(req);
	}

	@POST
	@Path("/login")
	@UnitOfWork
	public ZToken login(@Valid ZLogin req) {
		return $user.login(req);
	}

	@GET
	@Path("/me")
	@UnitOfWork
	@PermitAll
	public ZUser get(@Context final SecurityContext context) {
		ZUser user = (ZUser) context.getUserPrincipal();
		return $user.get(user.getId());
	}
}
