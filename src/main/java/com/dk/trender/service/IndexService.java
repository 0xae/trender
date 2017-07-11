package com.dk.trender.service;

import static org.joda.time.format.DateTimeFormat.forPattern;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.LocalDateTime;

import com.dk.trender.core.IndexItem;
import com.dk.trender.core.PostMedia;

public class IndexService {
	private final Queue<IndexItem> queue;
	private final MediaService mediaService;
	public static int MAX_MEDIA_COUNT = 10;

	public IndexService(MediaService service) {
		this.queue = new ConcurrentLinkedQueue<>();
		this.mediaService = service;
	}

	public void addToIndex(List<IndexItem> items) {
		for (final IndexItem item : items) {
			if (canBeenIndexed(item)) {
				queue.offer(item);
			}
		}
	}

	public List<IndexItem> retrieveIndex() {
		List<IndexItem> buf = new LinkedList<>();
		IndexItem c;
		int t=0;
		while ((c=queue.poll()) != null && (++t < 10)) {
			buf.add(c);			
		}
		return buf;
	}

	public int indexSize() {
		return queue.size();
	}

	private boolean canBeenIndexed(IndexItem item) {
		final String fId = item.getfId();
		return !queue.contains(item) &&
				mediaService.getPostMediaCount(fId, "*") < MAX_MEDIA_COUNT;
	}
}
