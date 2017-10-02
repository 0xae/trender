package com.dk.trender.service;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.Post;
import com.dk.trender.exceptions.SolrExecutionException;

/**
 * 
 * @author ayrton
 */
public class PostService {
	private ConcurrentUpdateSolrClient solr;
	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	public PostService(ConcurrentUpdateSolrClient service) {
		solr = service;
	}

	public void create(List<Post> posts) {
		List<SolrInputDocument> docs = new LinkedList<>();
		DateTime start = DateTime.now();

		for (final Post p : posts) {
			p.indexedAt(start);
			if (exists(p)) {
				log.info("skip doc: " + p.getLink());
				return;
			}

			log.info("index doc: " + p.getLink());
			docs.add(p.toDoc());
			start = start.plusMillis(60);
		}

		try {
			solr.add(docs);
			solr.commit();
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	public Post byId(String id) {
		try {
			SolrDocument doc = Optional
					.ofNullable(solr.getById(id))
					.orElseThrow(NoResultException::new);
			return Post.fromDoc(doc);
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}		
	}
	
	public void updateMedia(String id, String media) {
		try {
			SolrDocument post = solr.getById(id);
			if (post == null) {
				log.warn("post " + id + " not found!");
				return;
				// throw new NoResultException("post " + id + " not found!");
			}

			SolrInputDocument doc = new SolrInputDocument();
			post.setField("cached", media);

			for (String key : post.getFieldNames()) {
				doc.setField(key, post.get(key));
			}

			solr.add(doc);
			solr.commit();
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	private boolean exists(Post p) {
		try {
			return solr.getById(p.getId()) != null;
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
}
