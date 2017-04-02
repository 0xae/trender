package com.dk.trender.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	@Override
	public Response toResponse(ConstraintViolationException exception) {
		return Response
			   .status(400)
			   .entity("Verify if this object has unique properties")
			   .build();
	}
}
