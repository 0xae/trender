package com.dk.trender.service;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import com.dk.trender.core.IndexItem;

public class IndexService {
	private final Queue<Set<IndexItem>> queue;
	private final MediaService mediaService;
	public static int MAX_MEDIA_COUNT = 10;

	public IndexService(MediaService service) {
		this.queue = new ConcurrentLinkedQueue<>();
		this.mediaService = service;
	}

	/*
	 * TODO: batch requests to hibernate
	 */
	public void addToIndex(List<IndexItem> items) {
		final Set<IndexItem> set = items.stream()
							   .filter(this::canBeIndexed)
							   .collect(Collectors.toSet());
		queue.offer(set);
	}

	public List<IndexItem> retrieveIndex() {
		if (queue.isEmpty()) {
			return Collections.emptyList();
		} 
		
		return queue.poll()
					.stream()
					.collect(Collectors.toList());
	}

	public int indexSize() {
		return queue.size();
	}

	private boolean canBeIndexed(IndexItem item) {
		final String fId = item.getfId();
		return mediaService.getPostMediaCount(fId, "*") < MAX_MEDIA_COUNT;
	}
}
