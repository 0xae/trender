package com.dk.trender.exceptions;

import java.util.List;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

public class BadRequestExceptionMapper implements ExceptionMapper<BadRequest> {
	@Override
	public Response toResponse(BadRequest bad) {
		return Response
			   .status(Status.BAD_REQUEST)
			   .entity(new ErrorBean(bad.errors()))
			   .build();
	}
	
	public static class ErrorBean {
		private List<String> errors;
		public ErrorBean(List<String> errors) {
			super();
			this.errors = errors;
		}

		public List<String> getErrors() {
			return errors;
		}

		public void setErrors(List<String> errors) {
			this.errors = errors;
		}
	}
}
