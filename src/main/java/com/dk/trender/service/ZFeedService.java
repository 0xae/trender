package com.dk.trender.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrClient;

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZCollection;
import com.dk.trender.core.ZPost;

public class ZFeedService {
	private final ConcurrentUpdateSolrClient client;	
	public ZFeedService(ConcurrentUpdateSolrClient client) {
		this.client = client;
	}

	public List<ZCollection> forChannel(ZChannel chan, List<String> fq) {
		List<String> types = Arrays.asList(
			ZPost.BBC,
			ZPost.STEEMIT,
			ZPost.TWITTER,
			ZPost.YOUTUBE
		);

		QueryConf conf = chan.queryConf();
		
		List<ZCollection> cols = new ArrayList<>();
		return cols;
	}
}