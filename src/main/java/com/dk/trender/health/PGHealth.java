package com.dk.trender.health;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;

import com.codahale.metrics.health.HealthCheck;

public class PGHealth extends HealthCheck {
	private final ConcurrentUpdateSolrClient client;

	public PGHealth(ConcurrentUpdateSolrClient client) {
		this.client = client;
	}

	@Override
	protected Result check() throws Exception {
		return null;
	}
}
