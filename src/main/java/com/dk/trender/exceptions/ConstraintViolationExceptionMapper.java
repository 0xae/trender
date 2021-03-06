package com.dk.trender.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.postgresql.util.PSQLException;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;


public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
	private static final String SQL_DUPLICATED_STATE = "23505";
	
	@Override
	public Response toResponse(ConstraintViolationException e) {
		
		if (e.getCause() instanceof PSQLException && 
			 ((PSQLException) e.getCause()).getSQLState().equals(SQL_DUPLICATED_STATE)) {
			String msg="Verify if this object has unique properties";
			return Response
					   .status(400)
					   .entity("{\"errors\": [\""+msg+"\"]}")
					   .build();
		}

		throw e;
	}
}
