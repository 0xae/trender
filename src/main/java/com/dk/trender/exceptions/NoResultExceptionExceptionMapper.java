package com.dk.trender.exceptions;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.codahale.metrics.Meter;
import static com.codahale.metrics.MetricRegistry.name;

import com.codahale.metrics.MetricRegistry;

import javax.persistence.NoResultException;

/**
 * 
 * @author ayrton
 * @date 2017-04-01 07:00:30
 */
public class NoResultExceptionExceptionMapper implements ExceptionMapper<NoResultException> {
	private final Logger LOGGER = LoggerFactory.getLogger(NoResultExceptionExceptionMapper.class);
	private final Meter exceptions;

	public NoResultExceptionExceptionMapper(final MetricRegistry metrics) {
		exceptions = metrics.meter(name(getClass(), "exceptions"));
	}

	@Override
	public Response toResponse(final NoResultException exception) {
		exceptions.mark();
		LOGGER.info(exception.getMessage());
		return Response.status(404).build();
	}
}
