package com.dk.trender.service;

import static com.dk.trender.core.ZPost.BBC;
import static com.dk.trender.core.ZPost.STEEMIT;
import static com.dk.trender.core.ZPost.TWITTER;
import static com.dk.trender.core.ZPost.YOUTUBE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZPost;
import com.dk.trender.exceptions.SolrExecutionException;

public class ZSearchService {
	private final ConcurrentUpdateSolrClient solr;
	private static final Logger log = LoggerFactory.getLogger(ZSearchService.class);
	public ZSearchService(ConcurrentUpdateSolrClient solr) {
		super();
		this.solr = solr;
	}
	
	/**
	 * XXX: Arrays.asList("type:"+type) ???
	 * @param conf
	 * @return
	 */
	public Map<String, List<ZPost>> groupByType(QueryConf conf) {		
		return Arrays
		.asList(BBC, STEEMIT, TWITTER, YOUTUBE)
		.parallelStream()
		.collect(Collectors.<String, String, List<ZPost>>toMap(
			type -> type,
			type -> {
				return search(conf, Arrays.asList("type:"+type))
				  .getResults()
				  .stream()
				  .map(ZPost::fromDoc)
				  .collect(Collectors.toList());
			})
		);
	}
	
	private QueryResponse search(QueryConf conf, List<String> pfq) {
		SolrQuery sq = new SolrQuery();
		sq.set("q", conf.getQ());
		sq.set("rows", conf.getLimit());
		sq.set("start", conf.getStart());
		sq.set("sort", conf.getSort());
		sq.set("facet", true);
		sq.set("facet.field", "category", "type");

		List<String> fq = new ArrayList<>(conf.getFq());
		fq.add("!cached:none");
		fq.addAll(pfq);
		sq.set("fq", fq.toArray(new String[]{}));

		try {
			return solr.query(sq);
		} catch (Exception e) {
			throw new SolrExecutionException(e);
		}
	}
}
