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

import com.dk.trender.core.QueryConf;
import com.dk.trender.core.ZChannel;
import com.dk.trender.core.ZPost;
import com.dk.trender.core.feed.ZGroup;
import com.dk.trender.exceptions.SolrExecutionException;

public class ZFeedService {
	private static final int ROWS_PER_REQ=4;
	private final ConcurrentUpdateSolrClient solr;
	private final ZChannelService $channel;

	public ZFeedService(ConcurrentUpdateSolrClient client,
						ZChannelService chan) {
		this.solr = client;
		this.$channel = chan;
	}
}