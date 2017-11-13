package com.dk.trender.core.managed;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;

import io.dropwizard.lifecycle.Managed;

/**
 * 
 * @author ayrton
 */
public class ManagedSolr implements Managed {
	private static final String URL = "http://localhost:8983/solr/trender";
	private ConcurrentUpdateSolrClient client;

	public ManagedSolr() {
		client = new ConcurrentUpdateSolrClient
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
		if (client != null) {
			client.close();			
		}
	}

	public ConcurrentUpdateSolrClient getClient() {
		return client;
	}
}
