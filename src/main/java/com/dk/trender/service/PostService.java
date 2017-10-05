package com.dk.trender.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.NoResultException;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.Post;
import com.dk.trender.exceptions.SolrExecutionException;
import com.dk.trender.service.utils.DateUtils;

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

		for (final Post post : posts) {
			SolrDocument found = exists(post);
			if (found != null) {
				post.indexedAt(new DateTime(found.get("indexedAt")));
				log.info("doc exists: " + post.getLink());
			} else {
				post.indexedAt(start);
				log.info("index doc: " + post.getLink());
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
	}

	public Post byId(String id) {
		try {
			SolrDocument doc = Optional
					.ofNullable(solr.getById(id))
					.orElseThrow(NoResultException::new);
			return Post.fromDoc(doc);
		} catch (NoResultException k) {
			throw k;
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}

	public void updateMedia(String id, String media) {
		try {
			log.info("update media ({}) on post ({})", media, id);
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
			e.printStackTrace();
			throw new SolrExecutionException(e);
		}
	}

	private SolrDocument exists(Post p) {
		try {
			return solr.getById(p.getId());
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
}
