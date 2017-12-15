package com.dk.trender.health;

import java.io.IOException;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;

import com.codahale.metrics.health.HealthCheck;

public class SolrHealth extends HealthCheck {
	private final ConcurrentUpdateSolrClient client;
	public SolrHealth(ConcurrentUpdateSolrClient client) {
		this.client = client;
	}

	@Override
	protected Result check() throws Exception {
		try {
			client.ping();
			return Result.healthy();
		} catch (IOException ioe) {
			return Result.unhealthy(ioe.getMessage());
		}
	}
}
