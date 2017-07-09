package com.dk.trender.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.dk.trender.core.IndexItem;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IndexService {
	private Queue<IndexItem> queue;

	public IndexService() {
		this.queue = new ConcurrentLinkedQueue<>();
	}

	public IndexService(Queue<IndexItem> queue) {
		this.queue = queue;
	}

	public void addToIndex(List<IndexItem> url) {		
		queue.addAll(url);
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
}
