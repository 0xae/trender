package com.dk.trender.service;

import java.io.IOException;
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
import com.google.common.collect.ImmutableMap;

import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.SolrExecutionException;
import com.dk.trender.service.utils.DateUtils;


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

	public int create(List<ZPost> posts) {
		List<SolrInputDocument> docs = new LinkedList<>();
		DateTime start = DateTime.now();
		int indexed = 0;

		for (final ZPost post : posts) {
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

	public void update(ZPost p) {
		create(Arrays.asList(p));
	}

	public ZPost byId(String id) {
		try {
			SolrDocument doc = Optional
					.ofNullable(solr.getById(id))
					.orElseThrow(NoResultException::new);
			return ZPost.fromDoc(doc);
		} catch (NoResultException k) {
			throw k;
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
	
	public void addToCollection(String postId, String collectionName) {
		SolrInputDocument doc = new SolrInputDocument();
		doc.addField("id", postId);
		doc.addField("collections", ImmutableMap.of("add", collectionName));
		try {
			solr.add(Arrays.asList(doc));
			solr.commit();
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	public void updateMedia(String id, String media) {
		try {
			SolrDocument post = solr.getById(id);
			if (post == null) {
				String msg = "post " + id + " not found!";
				log.warn(msg);
				throw new NoResultException(msg);
			}

			SolrInputDocument doc = new SolrInputDocument();
			post.setField("cached", media);

			for (String key : post.getFieldNames()) {
				doc.setField(key, post.get(key));
			}

			if (post.get("indexedAt") == null) {
				doc.setField("indexedAt", DateUtils.format(DateTime.now()));
			}

			if (post.get("lang") == null) {
				doc.setField("lang", "en-us");
			}

			solr.add(doc);
			solr.commit();
		} catch (NoResultException k) {
			throw k;
		}  catch (Exception e) {
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
