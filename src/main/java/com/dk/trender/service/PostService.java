package com.dk.trender.service;

import java.util.LinkedList;
import java.util.List;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;

import com.dk.trender.core.Post;

/**
 * 
 * @author ayrton
 * TODO: move solr and solrUp to dw service
 *       and remove this horrible leak
 */
public class PostService {
	private static final String URL = "http://localhost:8983/solr/trender";
	private ConcurrentUpdateSolrClient solrUp;
	private SolrClient solr;
	
	public PostService() {
		solr = new HttpSolrClient
				.Builder(URL)
				.build();

		solrUp = new ConcurrentUpdateSolrClient
				.Builder(URL)
				.withQueueSize(10)
				.withThreadCount(4)
				.build();
	}

	public void create(List<Post> posts) {
		final List<SolrInputDocument> docs = new LinkedList<>();

		for (final Post p : posts) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("id", p.getId());
			doc.addField("type", p.getType());
			doc.addField("authorName", p.getAuthorName());
			doc.addField("source", p.getSource());
			doc.addField("link", p.getLink());
			doc.addField("description", p.getDescription());
			doc.addField("timestamp", p.formatTs());
			doc.addField("location", p.getLocation());
			
			// the optional fellas
			doc.addField("authorPicture", p.getAuthorPicture());
			doc.addField("picture", p.getPicture());
			doc.addField("data", p.getData());
			doc.addField("category", p.getCategory());		
			
			docs.add(doc);
		}

		try {
			solrUp.add(docs);
			solrUp.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
