package com.dk.trender.service;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.IndexItem;
import com.dk.trender.core.PostMedia;

public class IndexService {
	private final Queue<Set<IndexItem>> queue;
	private final MediaService mediaService;
	public static int MAX_MEDIA_COUNT = 10;

	public IndexService(MediaService service) {
		this.queue = new ConcurrentLinkedQueue<>();
		this.mediaService = service;
	}

	/*
	 * TODO: adopt a functional style
	 */
	public void addToIndex(List<IndexItem> items) {
		final Set<IndexItem> set = new HashSet<>();
		for (final IndexItem item : items) {
			if (canBeIndexed(item)) {
				set.add(item);
			}
		}
		
		queue.offer(set);
	}

	public List<IndexItem> retrieveIndex() {
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
