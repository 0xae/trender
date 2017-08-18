package com.dk.trender.core.managed;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;

import io.dropwizard.lifecycle.Managed;

public class ManagedSolr implements Managed {
	private static final String URL = "http://localhost:8983/solr/trender";
	private ConcurrentUpdateSolrClient solrUp;

	public ManagedSolr() {
		solrUp = new ConcurrentUpdateSolrClient
				.Builder(URL)
				.withQueueSize(10)
				.withThreadCount(4)
				.build();		
	}

	@Override
	public void start() throws Exception {
	}

	@Override
	public void stop() throws Exception {
		solrUp.close();
	}

	public ConcurrentUpdateSolrClient getSolr() {
		return solrUp;
	}
}
