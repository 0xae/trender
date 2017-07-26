package com.dk.trender.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.dk.trender.core.IndexItem;

public class IndexService {
	public static int MAX_MEDIA_COUNT = 10;
	private final Map<String, Queue<Set<IndexItem>>> map;
	private final MediaService mediaService;

	public IndexService(MediaService service) {
		this.map = new ConcurrentHashMap<>();
		this.mediaService = service;
	}

	/*
	 * TODO: batch requests to hibernate
	 * XXX: only map needs to be sync
	 */
	public void addToIndex(String indexName, List<IndexItem> items) {
		Set<IndexItem> set = items.stream()
							   .filter(this::canBeIndexed)
							   .collect(Collectors.toSet());
		Queue<Set<IndexItem>> queue = Optional
									  .ofNullable(map.get(indexName))
									  .orElse(new ConcurrentLinkedQueue<>());
		queue.offer(set);		
		map.put(indexName, queue);
	}

	public List<IndexItem> retrieveIndex(String indexName) {
		return Optional
				.ofNullable(map.get(indexName))
				.map(Q -> Q.poll().stream().collect(Collectors.toList()))
				.orElse(Collections.emptyList());
	}

	public Map<String, Integer> stats() {
		Map<String, Integer> r = new HashMap<>();
		for (String k : map.keySet()) {
			r.put(k, map.get(k).size());
		}
		return r;
	}

	private boolean canBeIndexed(IndexItem item) {
		final String fId = item.getfId();
		return mediaService.getPostMediaCount(fId, "*") < MAX_MEDIA_COUNT;
	}
}
