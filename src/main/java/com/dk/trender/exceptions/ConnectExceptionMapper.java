package com.dk.trender.exceptions;

import java.net.ConnectException;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class ConnectExceptionMapper 
	implements ExceptionMapper<java.net.ConnectException> {
	@Override
	public Response toResponse(ConnectException arg0) {
		// TODO Auto-generated method stub
		return Response
				   .status(503)
				   .entity("Service not available." + arg0.getMessage())
				   .build();
	}
}
