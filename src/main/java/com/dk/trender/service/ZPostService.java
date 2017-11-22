package com.dk.trender.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.persistence.NoResultException;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.SolrExecutionException;
import com.dk.trender.service.utils.Utils;
import com.google.common.collect.ImmutableMap;

/**
 * 
 * @author ayrton
 */
public class ZPostService {
	private ConcurrentUpdateSolrClient solr;
	private static final Logger log = LoggerFactory.getLogger(ZPostService.class);

	public ZPostService(ConcurrentUpdateSolrClient service) {
		solr = service;
	}
	
	public int save(ZPost obj) {
		return save(Arrays.asList(obj));
	}

	public int save(List<ZPost> posts) {
		List<SolrInputDocument> docs = new LinkedList<>();
		DateTime start = DateTime.now();
		int indexed = 0;

		for (final ZPost post : posts) {
			// XXX: cant we use an Optional<> here?
			SolrDocument found = exists(post);
			if (found != null) {
				post.indexedAt(new DateTime(found.get("indexedAt")));
			} else {
				indexed++;
				post.indexedAt(start);
				start = start.plusMillis(60);
			}
			docs.add(post.toDoc());
		}

		try {
			solr.add(docs);
			solr.commit();
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}

		return indexed;
	}

	public ZPost byId(String id) {
		SolrDocument doc;
		try {
			doc = Optional
					.ofNullable(solr.getById(id))
					.orElseThrow(NoResultException::new);
			return ZPost.fromDoc(doc);
		} catch (NoResultException | 
				 SolrServerException | IOException e) {
			throw new SolrExecutionException(e);
		}
	}

	public void updateCollection(String op, String postId, String collectionName) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", postId);
		doc.addField("collections", ImmutableMap.of(op, collectionName));

		try {
			solr.add(Arrays.asList(doc));
			solr.commit();
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	private SolrDocument exists(ZPost p) {
		try {
			return solr.getById(p.getId());
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
}
